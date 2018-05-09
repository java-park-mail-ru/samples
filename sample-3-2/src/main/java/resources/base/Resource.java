package resources.base;

import resources.game.Mage;
import resources.game.spell.Spell;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Spell.class),
        @JsonSubTypes.Type(Mage.MageResource.class),
        @JsonSubTypes.Type(ResourceRef.class),
})
public abstract class Resource {
}
