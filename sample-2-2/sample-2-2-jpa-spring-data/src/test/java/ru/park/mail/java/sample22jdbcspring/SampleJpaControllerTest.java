package ru.park.mail.java.sample22jdbcspring;

import org.springframework.beans.factory.annotation.Autowired;

public class SampleJpaControllerTest extends SampleControllerTest{

	@Autowired
	private SampleJpaDao dao;
	
	@Override
	protected SampleDao getDao() {
		return dao;
	}

	@Override
	protected String getBasePath() {
		return "/jpa";
	}
}
