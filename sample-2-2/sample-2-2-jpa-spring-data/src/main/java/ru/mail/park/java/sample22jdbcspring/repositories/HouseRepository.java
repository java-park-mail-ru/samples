package ru.mail.park.java.sample22jdbcspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mail.park.java.sample22jdbcspring.domain.House;

public interface HouseRepository extends JpaRepository<House, Long> {

}
