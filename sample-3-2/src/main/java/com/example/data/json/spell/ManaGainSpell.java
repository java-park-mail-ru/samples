package com.example.data.json.spell;

import com.example.data.json.game.GameObject;
import com.example.data.json.game.Magician;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ManaGainSpell extends Spell {
    private int manaGain;

    @JsonCreator
    public ManaGainSpell(@JsonProperty("manaGain") int manaGain,
                         @JsonProperty("manaCost") int manaCost) {
        super(manaCost);
        this.manaGain = manaGain;
    }

    public int getManaGain() {
        return manaGain;
    }

    @Override
    public void apply(GameObject gameObject) {
        if (! (gameObject instanceof Magician)) {
            return;
        }

        final Magician aliveObject = (Magician) gameObject;

        aliveObject.setMana(aliveObject.getMana() + manaGain);
    }

}
