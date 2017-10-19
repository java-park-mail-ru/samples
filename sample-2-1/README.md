- **JUnit** - Java-библиотека для тестирования отдельных модулей (Unit'ов) ПО
- **Mock** - Аннотация для создания заглушки (будь то класса или интерфейса)
- **Mockito** - Популярный фреймворк для Mock-тестирования
- **MockMvc** - Спринговый класс, который может имитировать Http-запросы контроллеру (вместе с хедерами, куки, телом запроса и т.д.) и анализировать результаты этого запроса (e.g., статус и тело ответа)
- **SpringBootTest** - Аннотация, используемая для выполнения интеграционного тестирования (противоположность Unit-тестов). При тестировании запускает всё приложение. В свойстве *webEnvironment* можно указать, какой окружение будет использоваться при запуске, например:
  * *WebEnvironment.MOCK* - грузит ``EmbeddedWebApplicationContext``, но использует Mock'овое сервлет-окружение. Установлено по дефолту. Часто используется совместно с ``@AutoConfigureMockMvc`` для тестирования с использованием MockMvc. Если в *classpath* нет никакого Servlet API, то подгрузится ``ApplicationContext`` и веб-окружение использоваться не будет.
  * *WebEnvironment.RANDOM_PORT* - подгрузится ``EmbeddedWebApplicationContext`` и запустятся реальные сервлет-контейнеры (*Embedded servlet containers*), которые будут слушать случайный порт (веб окружение используется).
  * *WebEnvironment.DEFINED_PORT* - аналогично предыдущему, за исключением того, что приложение будет слушать либо порт, указанный в application.properties, либо дефолтный 8080.
  * *WebEnvironment.NONE* - загрузится ``ApplicationContext``, не будет использоваться веб-окружение (*webEnvironment*).
- **TestRestTemplate** - класс, с помощью которого можно отправлять реальные http-запросы к нашему серверу. Необходимо указание либо *WebEnvironment.RANDOM_PORT*, либо *WebEnvironment.DEFINED_PORT*.
- **Before** - метод, помеченный данной аннотацией, будет вызван перед выполнением каждого теста.
- **BeforeClass** - метод с этой аннотацией выполнится один раз перед началом выполнения всех тестов.
- **Test** - метод, помеченный этой аннотацией, является тестом
  * Аннотация может принимать два параметра:
    *expected* - класс исключения, которое должен выбросить тест для успешного завершения. Если исключение не выброшено или выброшено другое, то тест завершился неудачно.
    *timeout* - максимальное время в миллисекундах, за которое должен выполниться тест.
- **After** - метод, помеченный этой аннотацией, выполнится после каждого теста. Гарантируется, что он выполнится, даже если метод с ``@Before`` или ``@Test`` выбросит исключение.
- **MockBean** - (почти как @Mock) "спрингбутовая" аннотация. Если Bean типа, помеченного этой аннотацией, уже определён в ``ApplicationContext``, то он будет заменён Mock'ом (заглушкой). Если Bean такого типа не определен в контексте, то будет добавлен новый (как Mock).
  * __Пример:__
  ```java
   @SpringBootTest(webEnvironment = RANDOM_PORT)
   @RunWith(SpringRunner.class)
   public class MockBeanUserControllerTest {
       @MockBean
       private UserDao userDao;

       @Autowired
       private TestRestTemplate restTemplate;

       @Test
       public void loginWithMockBean() {
           User user = new User();
           user.setId(1L);
           user.setUsername("foo");

           when(userDao.findUserByUsername(anyString())).thenReturn(user);
           when(userDao.checkUserPassword(any(User.class), anyString())).thenReturn(true);

           ResponseEntity<UserDTO> loginResp = restTemplate.postForEntity(
                   "/api/auth/login",
                   new UserSigninInfo("foo", "bar"),
                   UserDTO.class
           );
           assertEquals(HttpStatus.OK, loginResp.getStatusCode());
           List<String> cookies = loginResp.getHeaders().get("Set-Cookie");
           assertNotNull(cookies);
           assertFalse(cookies.isEmpty());

           UserDTO userResp = loginResp.getBody();
           assertNotNull(user);

           assertEquals("foo", userResp.getUsername());
           verify(userDao).findUserByUsername(anyString());
           verify(userDao).checkUserPassword(any(User.class), anyString());
       }
   }
  ```
    * В данном примере используется Mock для всего класса UserDao, поэтому в тесте имитировали поведение всех методов данного класса, которые будут вызываться в ходе выполнения теста, с помощью конструкции ``when().thenReturn()``. В данном случае это методы ``findUserByUsername(String)`` для нахождения юзера по введенному юзернейму (при любом параметре вернется заранее определенный юзер) и ``checkUserPassword(User, password)`` для сверки полученного юзера и его пароля (при любых параметрах этого метода вернется ``true``). Последние две строки теста проверяют, были ли вызваны эти два метода, в противном случае бросает исключение.
  
- **SpyBean** - также "спрингбутовая" аннотация. Отличие от ``@MockBean`` заключается в том, что она позволяет сделать Mock'ом не весь класс, а отдельные его методы. ``ApplicationContext`` при этом не перезаписывается
  * __Пример:__
  ```java
   @SpringBootTest(webEnvironment = RANDOM_PORT)
   @RunWith(SpringRunner.class)
   public class SpyBeanUserControllerTest {
       @SpyBean
       private UserDao userDao;

       @Autowired
       private PasswordEncoder passwordEncoder;

       @Autowired
       private TestRestTemplate restTemplate;

       @Test
       public void loginWithSpyBean() {
           User user = new User();
           user.setId(1L);
           user.setUsername("foo");
           user.setPassword(passwordEncoder.encode("bar"));

           doReturn(user).when(userDao).findUserByUsername(anyString());

           ResponseEntity<UserDTO> loginResp = restTemplate.postForEntity(
                   "/api/auth/login",
                   new UserSigninInfo("foo", "bar"),
                   UserDTO.class
           );
           assertEquals(HttpStatus.OK, loginResp.getStatusCode());
           List<String> cookies = loginResp.getHeaders().get("Set-Cookie");
           assertNotNull(cookies);
           assertFalse(cookies.isEmpty());

           UserDTO userResp = loginResp.getBody();
           assertNotNull(user);

           assertEquals("foo", userResp.getUsername());
           verify(userDao).findUserByUsername(anyString());
       }
   }
  ```
    * В этом примере уже не используется Mock класса UserDao, он - реальный Bean. Но в тесте мы говорим, что хотим Mock'нуть только метод ``findUserByUsername(String)``, а метод ``checkUserPassword(User, password)`` будет вызываться "в реальных условиях" (обоснование - не хотим мокать этот метод, потому что он не лезет в базу, а только проверяет пароли на соответствие). Необходимо заметить, что уже используется конструкция ``doReturn().when(mock).mockMethod()``, так как в случае конструкции ``when().thenReturn()`` из предыдущего примера вызовется метод реального Bean'а, а мы хотим заглушку.
