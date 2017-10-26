package ru.mail.park.mechanics.avatar;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.mechanics.Config;
import ru.mail.park.mechanics.GameSession;
import ru.mail.park.mechanics.base.Coords;
import ru.mail.park.mechanics.game.GameObject;
import ru.mail.park.mechanics.game.GamePart;
import ru.mail.park.mechanics.game.Snap;
import ru.mail.park.mechanics.internal.Shuffler;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.mail.park.mechanics.Config.SQUARES_COUNT;

public class Board extends GameObject {

    @NotNull
    private final List<Square> squares;

    @NotNull
    private final GameSession gameSession;
    private final Shuffler shuffler;


    public Board(@NotNull GameSession gameSession, @NotNull Shuffler shuffler) {
        this.gameSession = gameSession;
        this.shuffler = shuffler;
        squares = new ArrayList<>();
        for (int i = 0; i < SQUARES_COUNT; i++) {
            squares.add(new Square());
        }
        squares.get(0).setOccupant(gameSession.getFirst().getUserId());
        squares.get(1).setOccupant(gameSession.getSecond().getUserId());
    }

    public void fireAt(@NotNull Coords coords) {
        final int i = (int) (coords.x / Config.SQUARE_SIZE) + ((int)(coords.y / Config.SQUARE_SIZE)) * Config.SQUARES_IN_A_ROW;
        if (i < 0 || i > 8) {
            return;
        }

        final Id<UserProfile> occupant = squares.get(i).getOccupant();
        if (occupant != null) {
            gameSession.getEnemy(occupant).claimPart(MechanicPart.class).incrementScore();
        }
    }

    public void randomSwap() {
        shuffler.shuffle(squares);
    }

    @Override
    @NotNull
    public BoardSnap getSnap() {
        return new BoardSnap(this);
    }

    @SuppressWarnings("unused")
    public static final class BoardSnap implements Snap<Board> {

        @NotNull
        private final List<Snap<? extends GamePart>> partSnaps;

        @NotNull
        private final List<Snap<Square>> squares;

        @NotNull
        private final Id<GameObject> id;

        public BoardSnap(@NotNull Board board) {
            this.partSnaps = board.getPartSnaps();
            this.id = board.getId();
            this.squares = board.squares.stream()
                    .map(Square::getSnap)
                    .collect(Collectors.toList());
        }

        @NotNull
        public Id<GameObject> getId() {
            return id;
        }

        @NotNull
        public List<Snap<Square>> getSquares() {
            return squares;
        }

        @NotNull
        public List<Snap<? extends GamePart>> getPartSnaps() {
            return partSnaps;
        }
    }


}
