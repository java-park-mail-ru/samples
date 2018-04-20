package resources;

import resources.base.Resource;
import resources.base.ResourceFactory;
import resources.game.Mage;
import resources.game.spell.HealthGainSpell;
import resources.game.spell.Spell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SerializationTest {

    @Autowired
    private ResourceFactory resourceFactory;

    private Mage.MageResource mageResource;
    private Spell hit;
    private Spell gainHealth;


    @BeforeEach
    public void setUp() {
        this.mageResource = resourceFactory.get("MageResourceLvl1.json", Mage.MageResource.class);
        this.hit = resourceFactory.get("spells/UnhealSpell_Lvl1.json", Spell.class);
        this.gainHealth = resourceFactory.get("spells/HealSpell_Lvl1.json", Spell.class);
    }

    @Test
    public void mageCreateTest() {
        assertNotNull(mageResource);
        final Mage mage = new Mage(mageResource);
        assertEquals(mage.getHealth(), mageResource.getMaxHealth());
        assertEquals(mage.getMana(), mageResource.getMaxMana());
    }

    @Test
    public void selfHurtTest() {
        assertNotNull(mageResource);
        final Mage mage = new Mage(mageResource);
        assertNotNull(mage);
        hit.apply(mage);
        hit.apply(mage);
        assertEquals(mageResource.getMaxHealth() - 30, mage.getHealth());
        assertEquals(mageResource.getMaxMana(), mage.getMana());
        gainHealth.apply(mage);
        assertEquals(mageResource.getMaxHealth() - 5, mage.getHealth());
        gainHealth.apply(mage);
        assertEquals(mageResource.getMaxHealth(), mage.getHealth());
    }

    @Test
    public void lvlUpTest() {
        final Mage.MageResource mageResourceLvl2 =
                resourceFactory.get("MageResourceLvl2.json", Mage.MageResource.class);
        final Mage mage = new Mage(mageResource);
        mage.lvlUp(mageResourceLvl2);
        assertEquals(mageResourceLvl2.getMaxHealth(), mage.getHealth());
        assertEquals(mageResourceLvl2.getMaxMana(), mage.getMana());
    }

    @Test
    public void RawMageResourceTest() {
        final Resource rawResource = resourceFactory.get("MageResourceLvl1.json");
        assertTrue(rawResource instanceof Mage.MageResource);

    }

    @Test
    public void RawSpellResourceTest() {
        final Resource rawResource = resourceFactory.get("spells/HealSpell_Lvl1.json");
        assertTrue(rawResource instanceof HealthGainSpell);
    }
}
