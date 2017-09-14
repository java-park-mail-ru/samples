package ru.park.mail.java.sample22jdbcspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.park.mail.java.sample22jdbcspring.domain.House;

public interface HouseRepository extends JpaRepository<House, Long> {

}
