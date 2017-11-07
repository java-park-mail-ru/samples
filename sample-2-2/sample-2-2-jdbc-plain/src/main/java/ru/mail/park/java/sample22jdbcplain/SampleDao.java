package ru.mail.park.java.sample22jdbcplain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class SampleDao {

	private final DataSource ds;

	public SampleDao(DataSource ds) {
		this.ds = ds;
		createSchemaIfNotExists();
	}

	private void createSchemaIfNotExists() {
		try (Connection con = ds.getConnection(); Statement st = con.createStatement()) {
			st.executeUpdate("create table if not exists house(id serial primary key,"
					+ " name text not null unique, sigil text, words text,"
					+ " allegiance_id bigint references house(id))");
			st.executeUpdate("create table if not exists person( id serial primary key, name text not null,"
					+ "	house_id bigint references house(id), farther_id bigint references person(id),"
					+ "	mother_id bigint references person(id))");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public House createHouse(String name, String words, String sigil, Long allegianceId) {
		try (Connection con = ds.getConnection();
				PreparedStatement pst = con.prepareStatement(
						"insert into house(name, sigil, words, allegiance_id) values(?,?,?,?) returning id",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, name);
			pst.setString(2, words);
			pst.setString(3, sigil);
			pst.setObject(4, allegianceId);
			pst.executeUpdate();
			try (ResultSet genKey = pst.getGeneratedKeys()) {
				if (genKey.next()) {
					return new House(genKey.getLong(1), name, words, sigil, allegianceId);
				} else {
					throw new SQLException("Cannot get generated primary key");
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Person createPerson(String name, Long houseId, Long fartherId, Long motherId) {

		try (Connection con = ds.getConnection();
				PreparedStatement pst = con.prepareStatement(
						"insert into person(name, house_id, farther_id, mother_id) values(?,?,?,?) returning id",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, name);
			pst.setObject(2, houseId);
			pst.setObject(3, fartherId);
			pst.setObject(4, motherId);
			pst.executeUpdate();
			try (ResultSet genKey = pst.getGeneratedKeys()) {
				if (genKey.next()) {
					return new Person(genKey.getLong(1), name, houseId, fartherId, motherId);
				} else {
					throw new SQLException("Cannot get generated primary key");
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Person getPerson(long id) {
		try (Connection con = ds.getConnection();
				PreparedStatement pst = con.prepareStatement("select * from person where id=?")) {
			pst.setLong(1, id);
			try (ResultSet res = pst.executeQuery()) {
				if (res.next()) {
					return mapPerson(res);
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Person> getAllPeople() {

		try (Connection con = ds.getConnection();
				PreparedStatement pst = con.prepareStatement("select * from person");
				ResultSet res = pst.executeQuery()) {
			List<Person> result = new ArrayList<>();
			while (res.next()) {
				result.add(mapPerson(res));
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public List<Person> getPeople(long houseId) {
		try (Connection con = ds.getConnection();
				PreparedStatement pst = con.prepareStatement("select * from person where house_id=?")) {
			pst.setLong(1, houseId);
			try (ResultSet res = pst.executeQuery()) {
				List<Person> result = new ArrayList<>();
				while (res.next()) {
					result.add(mapPerson(res));
				}
				return result;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private Person mapPerson(ResultSet res) throws SQLException {
		long id = res.getLong("id");
		String name = res.getString("name");
		Long houseId = res.getLong("house_id");
		if (res.wasNull()) {
			houseId = null;
		}
		Long fartherId = res.getLong("farther_id");
		if (res.wasNull()) {
			fartherId = null;
		}
		Long motherId = res.getLong("mother_id");
		if (res.wasNull()) {
			motherId = null;
		}
		return new Person(id, name, houseId, fartherId, motherId);
	}
}
