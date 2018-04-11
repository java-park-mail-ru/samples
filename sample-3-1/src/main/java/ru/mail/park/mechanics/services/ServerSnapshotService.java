package ru.mail.park.mechanics.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.game.GameUser;
import ru.mail.park.mechanics.messages.outbox.ServerSnap;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServerSnapshotService {
    @NotNull
    private final RemotePointService remotePointService;

    public ServerSnapshotService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull GameSession gameSession, long frameTime) {
        final List<GameUser.ServerPlayerSnap> playersSnaps = new ArrayList<>();
        for (GameUser player : gameSession.getPlayers()) {
            playersSnaps.add(player.getSnap());
        }
        final ServerSnap snap = new ServerSnap();

        snap.setPlayers(playersSnaps);
        snap.setBoard(gameSession.getBoard().getSnap());
        snap.setServerFrameTime(frameTime);
        //noinspection OverlyBroadCatchBlock
        try {
            for (GameUser player : gameSession.getPlayers()) {
                remotePointService.sendMessageToUser(player.getUserId(), snap);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed  sending snapshot", ex);
        }

    }
}
