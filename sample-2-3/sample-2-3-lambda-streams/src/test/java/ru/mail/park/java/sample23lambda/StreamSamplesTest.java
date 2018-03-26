package ru.mail.park.java.sample23lambda;

import one.util.streamex.StreamEx;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

class StreamSamplesTest {

    private StreamEx<String> passwd() {
        return StreamEx.of(
                "root:x:0:0:root:/root:/bin/bash",
                "daemon:x:1:1:daemon:/usr/sbin:/usr/sbin/nologin",
                "bin:x:2:2:bin:/bin:/usr/sbin/nologin",
                "sys:x:3:3:sys:/dev:/usr/sbin/nologin",
                "sync:x:4:65534:sync:/bin:/bin/sync",
                "games:x:5:60:games:/usr/games:/usr/sbin/nologin"
        );
    }

    @Test
    void testDistinctCommands() {
        List<String> result = new ArrayList<>();
        StreamSamples.distinctCommands(Stream.empty(), result::add);
        assertEquals(emptyList(), result);

        StreamSamples.distinctCommands(passwd(), result::add);
        assertEquals(List.of("/bin/bash", "/usr/sbin/nologin", "/bin/sync"), result);
    }


    private static Map<String, Long> EXPECTED_COUNTS = Map.of(
            "/bin/bash", 1L,
            "/usr/sbin/nologin", 4L,
            "/bin/sync", 1L
    );

    @Test
    void testCommandCounts() {
        Map<String, Long> result = new HashMap<>();
        StreamSamples.commandCounts(Stream.empty(), result::put);
        assertEquals(emptyMap(), result);

        StreamSamples.commandCounts(passwd(), result::put);
        assertEquals(EXPECTED_COUNTS, result);
    }

    @Test
    void testCommandCountsEx() {
        Map<String, Long> result = new HashMap<>();
        StreamSamples.commandCountsEx(StreamEx.empty(), result::put);
        assertEquals(emptyMap(), result);

        StreamSamples.commandCountsEx(passwd(), result::put);
        assertEquals(EXPECTED_COUNTS, result);
    }

    private StreamEx<String> group() {
        return StreamEx.of(
                "root:x:0:",
                "daemon:x:1:",
                "bin:x:2:",
                "sys:x:3:",
                "adm:x:4:syslog,isopov",
                "docker:x:5:isopov"
        );
    }


    private static Map<String, Integer> EXPECTED_GROUP_SIZES = Map.of(
            "root", 0,
            "daemon", 0,
            "bin", 0,
            "sys", 0,
            "adm", 2,
            "docker", 1
    );

    @Test
    void testGroupSizes(){
        Map<String, Integer> result = new HashMap<>();
        StreamSamples.groupSizes(StreamEx.empty(), result::put);
        assertEquals(emptyMap(), result);

        StreamSamples.groupSizes(group(), result::put);
        assertEquals(EXPECTED_GROUP_SIZES, result);
    }

    @Test
    void testGroupSizesEx(){
        Map<String, Integer> result = new HashMap<>();
        StreamSamples.groupSizesEx(StreamEx.empty(), result::put);
        assertEquals(emptyMap(), result);

        StreamSamples.groupSizesEx(group(), result::put);
        assertEquals(EXPECTED_GROUP_SIZES, result);
    }

    @Test
    void testCoundUserGroups(){
        Map<String, Long> result = new HashMap<>();
        StreamSamples.countUserGroups(StreamEx.empty(), result::put);
        assertEquals(emptyMap(), result);

        StreamSamples.countUserGroups(group(), result::put);
        assertEquals(Map.of("syslog", 1L, "isopov", 2L), result);
    }
}