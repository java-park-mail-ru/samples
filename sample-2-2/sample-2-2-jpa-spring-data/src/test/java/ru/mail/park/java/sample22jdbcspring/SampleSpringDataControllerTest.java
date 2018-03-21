package ru.mail.park.java.sample22jdbcspring;

import org.springframework.beans.factory.annotation.Autowired;

class SampleSpringDataControllerTest extends SampleControllerTest{

	@Autowired
	private SampleSpringDataDao dao;
	
	@Override
	protected SampleDao getDao() {
		return dao;
	}
	@Override
	protected String getBasePath() {
		return "/data";
	}
}
