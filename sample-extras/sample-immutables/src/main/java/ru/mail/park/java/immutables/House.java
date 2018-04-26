package ru.mail.park.java.immutables;

import org.immutables.value.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Value.Immutable
public interface House {
    long id();
    String name();
    String words();
    String emblem();
    @Nullable
    Long allegianceId();
    List<String> men();
}
