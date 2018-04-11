package ru.mail.park.mechanics.services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.mail.park.mechanics.Config;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.game.GameUser;
import ru.mail.park.mechanics.messages.outbox.InitGame;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;
import ru.mail.park.websocket.RemotePointService;

import java.io.IOException;
import java.util.*;

@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapshotService.class);

    @NotNull
    private final RemotePointService remotePointService;

    public GameInitService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void initGameFor(@NotNull GameSession gameSession) {
         final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (GameUser player : players) {
            final InitGame.Request initMessage = createInitMessageFor(gameSession, player.getUserId());
            //noinspection OverlyBroadCatchBlock
            try {
                remotePointService.sendMessageToUser(player.getUserId(), initMessage);
            } catch (IOException e) {
                // TODO: Reentrance mechanism
                players.forEach(playerToCutOff -> remotePointService.cutDownConnection(playerToCutOff.getUserId(),
                        CloseStatus.SERVER_ERROR));
                LOGGER.error("Unnable to start a game", e);
            }
        }
    }

    @SuppressWarnings("TooBroadScope")
    private InitGame.Request createInitMessageFor(@NotNull GameSession gameSession, @NotNull Id<UserProfile> userId) {
        final InitGame.Request initGameMessage = new InitGame.Request();

        final Map<Id<UserProfile>, GameUser.ServerPlayerSnap> playerSnaps = new HashMap<>();
        final Map<Id<UserProfile>, String> names = new HashMap<>();
        final Map<Id<UserProfile>, String> colors = new HashMap<>();

        final Collection<GameUser> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (GameUser player : players) {
            playerSnaps.put(player.getUserId(), player.getSnap());
            names.put(player.getUserId(), player.getUserProfile().getLogin());
        }

        colors.put(userId, Config.SELF_COLOR);
        colors.put(gameSession.getEnemy(userId).getUserId(), Config.ENEMY_COLOR);

        initGameMessage.setSelf(userId);
        initGameMessage.setEnemy(gameSession.getEnemy(userId).getUserId());
        initGameMessage.setNames(names);
        initGameMessage.setColors(colors);
        initGameMessage.setPlayers(playerSnaps);

        initGameMessage.setBoard(gameSession.getBoard().getSnap());
        return initGameMessage;
    }
}
