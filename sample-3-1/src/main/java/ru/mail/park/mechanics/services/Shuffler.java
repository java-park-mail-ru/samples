package ru.mail.park.mechanics.services;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class Shuffler {

    public void shuffle(List<?> list) {
        Collections.shuffle(list, ThreadLocalRandom.current());
    }
}
