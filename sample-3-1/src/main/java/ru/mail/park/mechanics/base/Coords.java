package ru.mail.park.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Solovyev on 03/11/2016.
 */
@SuppressWarnings("PublicField")
public class Coords {

    public Coords(@JsonProperty("x") double x, @JsonProperty("y") double y) {
        this.x = x;
        this.y = y;
    }

    public final double x;
    public final double y;

    @Override
    public String toString() {
        return '{'
                + "x=" + x
                + ", y=" + y
                + '}';
    }

    @SuppressWarnings("NewMethodNamingConvention")
    @NotNull
    public static Coords of(double x, double y) {
        return new Coords(x, y);
    }
}
