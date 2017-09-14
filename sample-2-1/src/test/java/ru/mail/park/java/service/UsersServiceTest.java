package ru.mail.park.java.service;

import org.junit.Before;
import org.junit.Test;
import ru.mail.park.java.domain.User;

import static org.junit.Assert.*;

/**
 * Created by isopov on 29.09.16.
 */
public class UsersServiceTest {

    private UsersService usersService;

    @Before
    public void setup(){
        usersService = new UsersService();
    }


    @Test
    public void ensureExists(){
        User foo = usersService.ensureUserExists("foo");
        assertNotNull(foo);

        assertSame(foo, usersService.ensureUserExists("foo"));
    }

}