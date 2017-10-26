package ru.mail.park.mechanics.requests;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.avatar.Board;
import ru.mail.park.mechanics.avatar.GameUser;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;
import ru.mail.park.websocket.Message;
import java.util.Map;

public class InitGame{
    @SuppressWarnings("unused")
    public static final class Request extends Message {
        private Id<UserProfile> self;
        private Id<UserProfile> enemy;
        private Board.BoardSnap board;
        private Map<Id<UserProfile>, GameUser.ServerPlayerSnap> players;
        private Map<Id<UserProfile>, String> names;
        private Map<Id<UserProfile>, String> colors;

        public Board.BoardSnap getBoard() {
            return board;
        }

        public void setBoard(Board.BoardSnap board) {
            this.board = board;
        }

        @NotNull
        public Map<Id<UserProfile>, String> getNames() {
            return names;
        }

        public void setNames(@NotNull Map<Id<UserProfile>, String> names) {
            this.names = names;
        }

        @NotNull
        public Id<UserProfile> getSelf() {
            return self;
        }

        public Id<UserProfile> getEnemy() {
            return enemy;
        }

        public void setEnemy(Id<UserProfile> enemy) {
            this.enemy = enemy;
        }

        @NotNull
        public Map<Id<UserProfile>, String> getColors() {
            return colors;
        }

        public void setColors(@NotNull Map<Id<UserProfile>, String> colors) {
            this.colors = colors;
        }

        public void setSelf(@NotNull Id<UserProfile> self) {
            this.self = self;
        }
        @NotNull
        public Map<Id<UserProfile>, GameUser.ServerPlayerSnap> getPlayers() {
            return players;
        }

        public void setPlayers(@NotNull Map<Id<UserProfile>, GameUser.ServerPlayerSnap> players) {
            this.players = players;
        }
    }

}
