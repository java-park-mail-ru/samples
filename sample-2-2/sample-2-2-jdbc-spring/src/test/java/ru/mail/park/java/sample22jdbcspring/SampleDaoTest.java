package ru.mail.park.java.sample22jdbcspring;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.java.sample22jdbcspring.domain.House;
import ru.mail.park.java.sample22jdbcspring.domain.Person;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
class SampleDaoTest {

    @Autowired
    private SampleDao dao;

    @Test
    void testCreateHouse() {
        House house = dao.createHouse("testname", "testwords", "testsigil", null);
        assertTrue(house.getId() > 0);
        assertEquals("testname", house.getName());
        assertEquals("testwords", house.getWords());
        assertEquals("testsigil", house.getSigil());
        assertNull(house.getAllegianceId());
    }

    @Test
    void testCreatePerson() {
        Person person = dao.createPerson("testname", null, null, null);
        assertTrue(person.getId() > 0);
        assertEquals("testname", person.getName());
        assertNull(person.getHouseId());
        assertNull(person.getFartherId());
        assertNull(person.getMotherId());

        assertEquals(person, dao.getPerson(person.getId()));
    }

    @Test
    void testSnow() {
        House starks = dao.createHouse("Starks", "Winter is coming", "Direwolf", null);
        Person ned = dao.createPerson("Eddard", starks.getId(), null, null);
        Person john = dao.createPerson("John", null, ned.getId(), null);

        assertEquals(ned, dao.getPerson(ned.getId()));
        assertEquals(john, dao.getPerson(john.getId()));
    }

    @Test
    void testCreateDuplicateHouse() {
        dao.createHouseNamed("testname", "testwords", "testsigil", null);
        assertThrows(DuplicateKeyException.class, () -> {
            dao.createHouseNamed("testname", "testwords", "testsigil", null);

        });
    }

    @Test
    void testCreateHouseNamed() {
        House house = dao.createHouseNamed("testname", "testwords", "testsigil", null);
        assertTrue(house.getId() > 0);
        assertEquals("testname", house.getName());
        assertEquals("testwords", house.getWords());
        assertEquals("testsigil", house.getSigil());
        assertNull(house.getAllegianceId());
    }

}
