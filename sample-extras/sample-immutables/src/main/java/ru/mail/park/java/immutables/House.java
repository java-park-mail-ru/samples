package ru.mail.park.java.immutables;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Value.Immutable
@JsonDeserialize(builder = ImmutableHouse.Builder.class)
public interface House {
    @JsonProperty
    long id();
    @JsonProperty
    String name();
    @JsonProperty
    String words();
    @JsonProperty
    String emblem();
    @Nullable
    @JsonProperty
    Long allegianceId();
    @JsonProperty
    List<String> men();
}
