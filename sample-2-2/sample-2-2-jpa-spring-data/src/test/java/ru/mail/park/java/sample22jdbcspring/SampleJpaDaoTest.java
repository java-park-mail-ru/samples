package ru.mail.park.java.sample22jdbcspring;

import org.springframework.beans.factory.annotation.Autowired;

class SampleJpaDaoTest extends SampleDaoTest {

	@Autowired
	private SampleJpaDao dao;

	@Override
	protected SampleDao getDao() {
		return dao;
	}

}
