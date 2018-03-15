package ru.mail.park.java.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    private User winner, loser;

    @BeforeEach
    void setUp() {
        winner = new User("winner");
        loser = new User("loser");
    }

    @Nested
    @DisplayName("When winner`s power increase")
    class WhenIncreasePower {
        @BeforeEach
        void increasePower() {
            winner.increasePower();
        }

        @Test
        @DisplayName("power increased")
        void powerIncreased() {
            assertEquals(1, winner.getPower());
        }

        @Nested
        @DisplayName("When fight with loser")
        class WhenFightLoser {
            @BeforeEach
            void fight() {
                winner.fight(loser);
            }

            @Test
            @DisplayName("wins against loser")
            void winsAgainstLoser() {
                assertEquals(1, winner.getWins());

            }

            @Test
            @DisplayName("loser has no win")
            void loserHasNoWin() {
                assertEquals(0, loser.getWins());
            }

            @Test
            @DisplayName("loser loses power")
            void loserLosesPower() {
                assertEquals(0, loser.getPower());
            }
        }

        @Nested
        @DisplayName("When fight with himself")
        class WhenShadowFight {
            @BeforeEach
            void fight() {
                winner.fight(winner);
            }


            @Test
            @DisplayName("no win")
            void loserHasNoWin() {
                assertEquals(0, winner.getWins());
            }

            @Test
            @DisplayName("power remains")
            void loserLosesPower() {
                assertEquals(1, winner.getPower());
            }
        }
    }
}