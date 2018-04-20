package resources.game.spell;

import resources.game.Alive;
import resources.game.GameObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthGainSpell extends Spell {
    private int healthGain;

    @JsonCreator
    public HealthGainSpell(@JsonProperty("healthGain") int healthGain,
                           @JsonProperty("manaCost") int manaCost) {
        super(manaCost);
        this.healthGain = healthGain;
    }

    public int getHealthGain() {
        return healthGain;
    }

    @Override
    public void apply(GameObject gameObject) {
        if (!(gameObject instanceof Alive)) {
            return;
        }

        final Alive aliveObject = (Alive) gameObject;

        aliveObject.setHealth(aliveObject.getHealth() + healthGain);
    }
}
