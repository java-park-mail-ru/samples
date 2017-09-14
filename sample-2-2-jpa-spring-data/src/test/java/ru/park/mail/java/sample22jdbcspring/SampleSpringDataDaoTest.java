package ru.park.mail.java.sample22jdbcspring;

import org.springframework.beans.factory.annotation.Autowired;

public class SampleSpringDataDaoTest extends SampleDaoTest {

	@Autowired
	private SampleSpringDataDao dao;

	@Override
	protected SampleDao getDao() {
		return dao;
	}

}
