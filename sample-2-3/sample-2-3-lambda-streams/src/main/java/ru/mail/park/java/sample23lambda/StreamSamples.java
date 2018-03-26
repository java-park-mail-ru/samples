package ru.mail.park.java.sample23lambda;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

import one.util.streamex.StreamEx;

public class StreamSamples {
    public static void main(String[] args) throws IOException {
        distinctCommands();

        commandCounts();
        commandCountsEx();

        groupSizes();
        groupSizesEx();

        countUserGroups();
    }

    private static void distinctCommands() throws IOException {
        try (Stream<String> passwdLines = Files.lines(Paths.get("/etc/passwd"))) {
            distinctCommands(passwdLines, System.out::println);
        }
    }

    static void distinctCommands(Stream<String> lines, Consumer<String> consumer) {
        lines.map(User::new)
                .filter(User::isPasswordEncrypted)
                .map(User::getCommand)
                .distinct()
                .forEach(consumer);
    }


    private static void commandCounts() throws IOException {
        try (Stream<String> passwdLines = Files.lines(Paths.get("/etc/passwd"))) {
            commandCounts(
                    passwdLines,
                    (command, count) -> System.out.println(command + " " + count)
            );
        }
    }

    static void commandCounts(Stream<String> lines, BiConsumer<String, Long> consumer) {
        lines.map(User::new)
                .filter(User::isPasswordEncrypted)
                .map(User::getCommand)
                .collect(groupingBy(identity(), counting()))
                .forEach(consumer);
    }


    private static void commandCountsEx() throws IOException {
        try (StreamEx<String> passwdLines = StreamEx.ofLines(Paths.get("/etc/passwd"))) {
            commandCountsEx(
                    passwdLines,
                    (command, count) -> System.out.println(command + " " + count)
            );
        }
    }

    static void commandCountsEx(StreamEx<String> lines, BiConsumer<String, Long> consumer) {
        lines.map(User::new)
                .filter(User::isPasswordEncrypted)
                .map(User::getCommand)
                .groupingBy(identity(), counting())
                .forEach(consumer);
    }


    private static void groupSizes() throws IOException {
        try (Stream<String> groupLines = Files.lines(Paths.get("/etc/group"))) {
            groupSizes(
                    groupLines,
                    (group, count) -> System.out.println(group + " " + count)
            );
        }
    }

    static void groupSizes(Stream<String> lines, BiConsumer<String, Integer> consumer) {
        lines.map(Group::new)
                .filter(Group::isPasswordEncrypted)
                .forEach(group -> consumer.accept(group.getName(), group.getUsers().size()));
    }


    private static void groupSizesEx() throws IOException {
        try (StreamEx<String> groupLines = StreamEx.ofLines(Paths.get("/etc/group"))) {
            groupSizesEx(
                    groupLines,
                    (group, count) -> System.out.println(group + " " + count)
            );
        }
    }

    static void groupSizesEx(StreamEx<String> lines, BiConsumer<String, Integer> consumer) {
        lines.map(Group::new)
                .filter(Group::isPasswordEncrypted)
                .mapToEntry(Group::getUsers)
                .mapKeys(Group::getName)
                .mapValues(List::size)
                .forKeyValue(consumer);
    }


    private static void countUserGroups() throws IOException {
        try (Stream<String> groupLines = Files.lines(Paths.get("/etc/group"))) {
            countUserGroups(
                    groupLines,
                    (command, count) -> System.out.println(command + " " + count)
            );
        }
    }

    static void countUserGroups(Stream<String> lines, BiConsumer<String, Long> consumer) {
        lines.map(Group::new)
                .map(Group::getUsers)
                .flatMap(Collection::stream)
                .collect(groupingBy(identity(), counting()))
                .forEach(consumer);
    }

    static class User {
        private final String username;
        private final String password;
        private final int userId;
        private final int groupId;
        private final String info;
        private final String home;
        private final String command;

        User(String passwdLine) {
            String[] split = passwdLine.split(":");
            if (split.length != 7) {
                throw new IllegalArgumentException(
                        "Not recgnized passwd line - " + passwdLine);
            }

            this.username = split[0];
            this.password = split[1];
            this.userId = Integer.valueOf(split[2]);
            this.groupId = Integer.valueOf(split[3]);
            this.info = split[4];
            this.home = split[5];
            this.command = split[6];
        }

        boolean isPasswordEncrypted() {
            return "x".equals(password);
        }

        String getUsername() {
            return username;
        }

        String getPassword() {
            return password;
        }

        int getUserId() {
            return userId;
        }

        int getGroupId() {
            return groupId;
        }

        String getInfo() {
            return info;
        }

        String getHome() {
            return home;
        }

        String getCommand() {
            return command;
        }
    }

    static class Group {
        private final String name;
        private final String password;
        private final int groupId;
        private final List<String> users;

        Group(String groupLine) {
            String[] split = groupLine.split(":");
            this.name = split[0];
            this.password = split[1];
            this.groupId = Integer.valueOf(split[2]);
            if (split.length == 4) {
                this.users = asList(split[3].split(","));
            } else {
                this.users = emptyList();
            }
        }

        boolean isPasswordEncrypted() {
            return "x".equals(password);
        }

        String getName() {
            return name;
        }

        String getPassword() {
            return password;
        }

        int getGroupId() {
            return groupId;
        }

        List<String> getUsers() {
            return users;
        }
    }
}
