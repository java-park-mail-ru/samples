package resources.game;

import resources.base.Resource;
import resources.game.spell.Spell;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Mage extends GameObject implements Alive, Magician {
    private int mana;
    private int health;
    private MageResource mageResource;

    public Mage(MageResource mageResource) {
        mana = mageResource.maxMana;
        health = mageResource.maxHealth;
        this.mageResource = mageResource;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = Math.min(mageResource.maxHealth, health);
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public void setMana(int mana) {
        this.mana = Math.min(mana, mageResource.maxMana);
    }

    public void lvlUp(MageResource nextLvlResource) {
        this.mageResource = nextLvlResource;
        setHealth(mageResource.maxHealth);
        setMana(mageResource.maxMana);
    }

    public static class MageResource extends Resource {
        private int maxMana;
        private int maxHealth;

        private List<Spell> spells;

        @JsonCreator
        public MageResource(@JsonProperty("maxMana") int maxMana,
                            @JsonProperty("maxHealth") int maxHealth,
                            @JsonProperty("spells") List<Spell> spells) {
            this.maxMana = maxMana;
            this.maxHealth = maxHealth;
            this.spells = spells;
        }

        public List<Spell> getSpells() {
            return spells;
        }

        public int getMaxHealth() {
            return maxHealth;
        }

        public int getMaxMana() {
            return maxMana;
        }
    }
}
