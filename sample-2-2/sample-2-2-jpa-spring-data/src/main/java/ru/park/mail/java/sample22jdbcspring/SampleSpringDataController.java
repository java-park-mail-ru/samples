package ru.park.mail.java.sample22jdbcspring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/data")
@RestController
public class SampleSpringDataController extends SampleController {

	public SampleSpringDataController(SampleJpaDao dao) {
		super(dao);
	}

}
