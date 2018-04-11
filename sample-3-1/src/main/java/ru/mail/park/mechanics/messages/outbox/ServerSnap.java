package ru.mail.park.mechanics.messages.outbox;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.game.Board;
import ru.mail.park.mechanics.game.GameUser;
import ru.mail.park.websocket.Message;

import java.util.List;

/**
 * Created by Solovyev on 03/11/2016.
 */
@SuppressWarnings({"NullableProblems"})
public class ServerSnap extends Message {

    @NotNull private List<GameUser.ServerPlayerSnap> players;
    @NotNull private Board.BoardSnap board;
    private long serverFrameTime;

    public Board.BoardSnap getBoard() {
        return board;
    }

    public void setBoard(Board.BoardSnap board) {
        this.board = board;
    }

    @NotNull
    public List<GameUser.ServerPlayerSnap> getPlayers() {
        return players;
    }

    public void setPlayers(@NotNull List<GameUser.ServerPlayerSnap> players) {
        this.players = players;
    }

    public long getServerFrameTime() {
        return serverFrameTime;
    }

    public void setServerFrameTime(long serverFrameTime) {
        this.serverFrameTime = serverFrameTime;
    }
}
