package ru.mail.park.java.immutables;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {

    @Test
    void testNulls() {
        assertEquals("name", assertThrows(NullPointerException.class, () -> ImmutableHouse.builder().name(null)).getMessage());
        assertEquals("words", assertThrows(NullPointerException.class, () -> ImmutableHouse.builder().words(null)).getMessage());
        assertEquals("emblem", assertThrows(NullPointerException.class, () -> ImmutableHouse.builder().emblem(null)).getMessage());
        ImmutableHouse.builder().allegianceId(null);
    }

    @Test
    void testRequired() {
        assertEquals("Cannot build House, some of required attributes are not set [id, name, words, emblem]",
                assertThrows(IllegalStateException.class, () -> ImmutableHouse.builder().build()).getMessage());

        assertEquals("Cannot build House, some of required attributes are not set [name, words, emblem]",
                assertThrows(IllegalStateException.class, () -> ImmutableHouse.builder().id(0L).build()).getMessage());

        assertEquals("Cannot build House, some of required attributes are not set [words]",
                assertThrows(IllegalStateException.class,
                        () -> ImmutableHouse.builder().id(0L).name("testname").emblem("testemblem").build()
                ).getMessage()
        );
    }

    @Test
    void testMen() {
        ImmutableHouse house = ImmutableHouse.builder()
                .id(0L)
                .name("testname")
                .emblem("testemblem")
                .words("testwords")
                .addMen("first")
                .addMen("second")
                .build();

        assertThrows(UnsupportedOperationException.class, () -> house.men().add("third"));

        assertNotEquals(house, house.withMen("third"));

    }
}
