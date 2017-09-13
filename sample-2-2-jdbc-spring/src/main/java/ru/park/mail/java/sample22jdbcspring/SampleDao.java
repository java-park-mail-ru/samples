package ru.park.mail.java.sample22jdbcspring;

import java.sql.PreparedStatement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.park.mail.java.sample22jdbcspring.domain.House;

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
			PreparedStatement pst = con
					.prepareStatement("insert into house(name, sigil, words, allegiance_id)"
							+ " values(?,?,?,?)"
							+ " returning id",
							PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, name);
			pst.setString(2, words);
			pst.setString(3, sigil);
			pst.setObject(4, allegianceId);
			return pst;
		}, keyHolder);
		return new House(keyHolder.getKey().longValue(), name, words, sigil, allegianceId);
	}
	
	
	public House createHouseNamed(String name, String words, String sigil, Long allegianceId) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name);
		params.addValue("words", words);
		params.addValue("sigil", sigil);
		params.addValue("allegianceId", allegianceId);
		namedTemplate.update("insert into house(name, sigil, words, allegiance_id)"
				+ " values(:name,:words,:sigil,:allegianceId)"
				+ " returning id", params, keyHolder);
		return new House(keyHolder.getKey().longValue(), name, words, sigil, allegianceId);
	}
	

}
