package ru.mail.park.java.sample22jdbcspring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/jpa")
@RestController
public class SampleJpaController extends SampleController {

	public SampleJpaController(SampleJpaDao dao) {
		super(dao);
	}

}
