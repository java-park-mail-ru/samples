package ru.park.mail.java.sample23lambda;

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
			passwdLines.map(User::new)
					.filter(User::isPasswordEncrypted)
					.map(User::getCommand)
					.distinct()
					.forEach(System.out::println);
		}
	}

	private static void commandCounts() throws IOException {
		try (Stream<String> passwdLines = Files.lines(Paths.get("/etc/passwd"))) {
			passwdLines.map(User::new)
					.filter(User::isPasswordEncrypted)
					.map(User::getCommand)
					.collect(groupingBy(identity(), counting()))
					.forEach((command, count) -> System.out.println(command + " " + count));
		}
	}

	private static void commandCountsEx() throws IOException {
		try (StreamEx<String> passwdLines = StreamEx.ofLines(Paths.get("/etc/passwd"))) {
			passwdLines.map(User::new)
					.filter(User::isPasswordEncrypted)
					.map(User::getCommand)
					.groupingBy(identity(), counting())
					.forEach((command, count) -> System.out.println(command + " " + count));
		}
	}

	private static void groupSizes() throws IOException {
		try (Stream<String> groupLines = Files.lines(Paths.get("/etc/group"))) {
			groupLines.map(Group::new)
					.filter(Group::isPasswordEncrypted)
					.forEach(group -> System.out.println(group.getName() + " " + group.getUsers().size()));
		}
	}

	private static void groupSizesEx() throws IOException {
		try (StreamEx<String> groupLines = StreamEx.ofLines(Paths.get("/etc/group"))) {
			groupLines.map(Group::new)
					.filter(Group::isPasswordEncrypted)
					.mapToEntry(Group::getUsers)
					.mapKeys(Group::getName)
					.mapValues(List::size)
					.forKeyValue((group, count) -> System.out.println(group + " " + count));
		}
	}

	private static void countUserGroups() throws IOException {
		try (Stream<String> groupLines = Files.lines(Paths.get("/etc/group"))) {
			groupLines.map(Group::new)
					.map(Group::getUsers)
					.flatMap(Collection::stream)
					.collect(groupingBy(identity(), counting()))
					.forEach((command, count) -> System.out.println(command + " " + count));
		}
	}

	public static class User {
		private final String username;
		private final String password;
		private final int userId;
		private final int groupId;
		private final String info;
		private final String home;
		private final String command;

		// root:x:0:0:root:/root:/bin/bash
		// daemon:x:1:1:daemon:/usr/sbin:/usr/sbin/nologin
		// bin:x:2:2:bin:/bin:/usr/sbin/nologin
		// sys:x:3:3:sys:/dev:/usr/sbin/nologin
		// sync:x:4:65534:sync:/bin:/bin/sync
		// games:x:5:60:games:/usr/games:/usr/sbin/nologin
		public User(String passwdLine) {
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

		public boolean isPasswordEncrypted() {
			return "x".equals(password);
		}

		public String getUsername() {
			return username;
		}

		public String getPassword() {
			return password;
		}

		public int getUserId() {
			return userId;
		}

		public int getGroupId() {
			return groupId;
		}

		public String getInfo() {
			return info;
		}

		public String getHome() {
			return home;
		}

		public String getCommand() {
			return command;
		}
	}

	// root:x:0:
	// daemon:x:1:
	// bin:x:2:
	// sys:x:3:
	// adm:x:4:syslog,isopov
	public static class Group {
		private final String name;
		private final String password;
		private final int groupId;
		private final List<String> users;

		public Group(String groupLine) {
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

		public boolean isPasswordEncrypted() {
			return "x".equals(password);
		}

		public String getName() {
			return name;
		}

		public String getPassword() {
			return password;
		}

		public int getGroupId() {
			return groupId;
		}

		public List<String> getUsers() {
			return users;
		}
	}
}
