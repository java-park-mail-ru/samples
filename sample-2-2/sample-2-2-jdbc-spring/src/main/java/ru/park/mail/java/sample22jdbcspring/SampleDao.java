package ru.park.mail.java.sample22jdbcspring;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.park.mail.java.sample22jdbcspring.domain.House;
import ru.park.mail.java.sample22jdbcspring.domain.Person;

@Service
@Transactional
public class SampleDao {

	private final JdbcTemplate template;
	private final NamedParameterJdbcTemplate namedTemplate;

	public SampleDao(JdbcTemplate template, NamedParameterJdbcTemplate namedTemplate) {
		this.template = template;
		this.namedTemplate = namedTemplate;
	}

	public House createHouse(String name, String words, String sigil, Long allegianceId) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(con -> {
			PreparedStatement pst = con.prepareStatement(
					"insert into house(name, sigil, words, allegiance_id)" + " values(?,?,?,?)" + " returning id",
					PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, name);
			pst.setString(2, words);
			pst.setString(3, sigil);
			pst.setObject(4, allegianceId);
			return pst;
		}, keyHolder);
		return new House(keyHolder.getKey().longValue(), name, words, sigil, allegianceId);
	}

	private static final RowMapper<House> HOUSE_MAPPER = (res, num) -> {

		Long allegianceId = res.getLong("allegiance_id");
		if (res.wasNull()) {
			allegianceId = null;
		}

		return new House(res.getLong("id"), res.getString("name"), res.getString("words"), res.getString("sigil"),
				allegianceId);
	};

	public House getHouse(long id) {
		List<House> result = template.query("select * from house where id=?", ps -> ps.setLong(1, id), HOUSE_MAPPER);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	public Person createPerson(String name, Long houseId, Long fartherId, Long motherId) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(con -> {
			PreparedStatement pst = con.prepareStatement(
					"insert into person(name, house_id, farther_id, mother_id)" + " values(?,?,?,?)" + " returning id",
					PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, name);
			pst.setObject(2, houseId);
			pst.setObject(3, fartherId);
			pst.setObject(4, motherId);
			return pst;
		}, keyHolder);
		return new Person(keyHolder.getKey().longValue(), name, houseId, fartherId, motherId);
	}

	private static final RowMapper<Person> PERSON_MAPPER = (res, num) -> {
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
	};

	public Person getPerson(long id) {
		List<Person> result = template.query("select * from person where id=?", ps -> ps.setLong(1, id), PERSON_MAPPER);
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	public List<Person> getAllPeople() {
		return template.query("select * from person", PERSON_MAPPER);
	}

	public List<Person> getPeople(long houseId) {
		return template.query("select * from person where house_id=?", ps -> ps.setLong(1, houseId), PERSON_MAPPER);
	}

	public House createHouseNamed(String name, String words, String sigil, Long allegianceId) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);
		params.addValue("words", words);
		params.addValue("sigil", sigil);
		params.addValue("allegianceId", allegianceId);
		namedTemplate.update("insert into house(name, sigil, words, allegiance_id)"
				+ " values(:name,:words,:sigil,:allegianceId)" + " returning id", params, keyHolder);
		return new House(keyHolder.getKey().longValue(), name, words, sigil, allegianceId);
	}

}
