package com.example.data;

import com.example.data.json.game.Mage;
import com.example.data.json.spell.Spell;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Spell.class),
        @JsonSubTypes.Type(Mage.MageResource.class),
})
public abstract class Resource {
}
