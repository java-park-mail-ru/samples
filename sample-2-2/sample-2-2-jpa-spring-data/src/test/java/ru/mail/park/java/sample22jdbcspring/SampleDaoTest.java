package ru.mail.park.java.sample22jdbcspring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.java.sample22jdbcspring.SampleDao.DuplicateHouseException;
import ru.mail.park.java.sample22jdbcspring.domain.House;
import ru.mail.park.java.sample22jdbcspring.domain.Person;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public abstract class SampleDaoTest {


    protected abstract SampleDao getDao();


    @Test
    void testCreateHouse() {
        House house = getDao().createHouse("testname", "testwords", "testsigil", null);
        assertNotNull(house.getId());
        assertEquals("testname", house.getName());
        assertEquals("testwords", house.getWords());
        assertEquals("testsigil", house.getSigil());
        assertNull(house.getAllegiance());
    }

    @Test
    void testCreatePerson() {
        Person person = getDao().createPerson("testname", null, null, null);
        assertNotNull(person.getId());
        assertEquals("testname", person.getName());
        assertNull(person.getHouse());
        assertNull(person.getFarther());
        assertNull(person.getMother());

        assertEquals(person, getDao().getPerson(person.getId()));
    }

    @Test
    void testSnow() {
        House starks = getDao().createHouse("Starks", "Winter is coming", "Direwolf", null);
        Person ned = getDao().createPerson("Eddard", starks.getId(), null, null);
        Person john = getDao().createPerson("John", null, ned.getId(), null);

        assertEquals(ned, getDao().getPerson(ned.getId()));
        assertEquals(john, getDao().getPerson(john.getId()));
    }

    @Test
    void testCreateDuplicateHouse() {
        getDao().createHouse("testname", "testwords", "testsigil", null);
        assertThrows(DuplicateHouseException.class, () -> {
            getDao().createHouse("testname", "testwords", "testsigil", null);

        }, "House with name testname already exists");
    }
}
