package ru.park.mail.java.sample22jdbcspring;

import org.springframework.beans.factory.annotation.Autowired;

public class SampleJpaDaoTest extends SampleDaoTest {

	@Autowired
	private SampleJpaDao dao;

	@Override
	protected SampleDao getDao() {
		return dao;
	}

}
