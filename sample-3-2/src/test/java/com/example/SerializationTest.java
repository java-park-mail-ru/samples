package com.example;

import com.example.data.Resource;
import com.example.data.ResourceFactory;
import com.example.data.json.game.Mage;
import com.example.data.json.spell.HealthGainSpell;
import com.example.data.json.spell.Spell;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SerializationTest {
    private ObjectMapper objectMapper;
    private ResourceFactory resourceFactory;
    private Mage.MageResource mageResource;
    private Spell hit;
    private Spell gainHealth;


    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.resourceFactory = new ResourceFactory(objectMapper);
        this.mageResource = resourceFactory.get("MageResourceLvl1.json", Mage.MageResource.class);
        this.hit = mageResource.getSpells().get(1);
        this.gainHealth = mageResource.getSpells().get(0);


    }

    @Test
    public void mageCreateTest() {
        Assert.assertNotNull(mageResource);
        final Mage mage = new Mage(mageResource);
        Assert.assertEquals(mage.getHealth(), mageResource.getMaxHealth());
        Assert.assertEquals(mage.getMana(), mageResource.getMaxMana());
    }

    @Test
    public void selfHurtTest() {
        Assert.assertNotNull(mageResource);
        final Mage mage = new Mage(mageResource);
        Assert.assertNotNull(mage);
        hit.apply(mage);
        hit.apply(mage);
        Assert.assertEquals(mageResource.getMaxHealth() - 30, mage.getHealth());
        Assert.assertEquals(mageResource.getMaxMana(), mage.getMana());
        gainHealth.apply(mage);
        Assert.assertEquals(mageResource.getMaxHealth() - 5, mage.getHealth());
        gainHealth.apply(mage);
        Assert.assertEquals(mageResource.getMaxHealth(), mage.getHealth());
    }

    @Test
    public void lvlUpTest() {
        final Mage.MageResource mageResourceLvl2 =
                resourceFactory.get("MageResourceLvl2.json", Mage.MageResource.class);
        final Mage mage = new Mage(mageResource);
        mage.lvlUp(mageResourceLvl2);
        Assert.assertEquals(mageResourceLvl2.getMaxHealth(), mage.getHealth());
        Assert.assertEquals(mageResourceLvl2.getMaxMana(), mage.getMana());
    }

    @Test
    public void RawMageResourceTest() {
        final Resource rawResource = resourceFactory.get("MageResourceLvl1.json");
        Assert.assertTrue(rawResource instanceof Mage.MageResource);

    }

    @Test
    public void RawSpellResourceTest() {
        final Resource rawResource = resourceFactory.get("HealthGainSpell.json");
        Assert.assertTrue(rawResource instanceof HealthGainSpell);
    }
}
