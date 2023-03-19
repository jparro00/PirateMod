package thePirate.potions;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.actions.BuryAction;
import thePirate.actions.DigAction;

public class IslandPotion extends AbstractDynamicPotion{

    public static final String POTION_ID = PirateMod.makeID(IslandPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final Color LIQUID = CardHelper.getColor(78,48,0);
    public static final Color HYBRID = null;
    public static final Color SPOTS = null;

    public IslandPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.BOTTLE, PotionColor.EXPLOSIVE);
        
        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();
        
        // Initialize the Description
        description = DESCRIPTIONS[0];
        
       // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
        
        // Initialize the on-hover name + description
        tips.add(new PowerTip(name, description));
//        tips.add(new PowerTip(BaseMod.getKeywordProper(PirateMod.getModID().toLowerCase()+":ink"), BaseMod.getKeywordDescription(PirateMod.getModID().toLowerCase() + ":ink")));
        tips.add(new PowerTip(BaseMod.getKeywordProper(PirateMod.getModID().toLowerCase()+":bury") , BaseMod.getKeywordDescription(PirateMod.getModID().toLowerCase() + ":bury")));
        tips.add(new PowerTip(BaseMod.getKeywordProper(PirateMod.getModID().toLowerCase()+":dig") , BaseMod.getKeywordDescription(PirateMod.getModID().toLowerCase() + ":dig")));

    }

    @Override
    public Color getColor(COLOR color){
        switch (color){
            case SPOTS:
                return SPOTS;
            case HYBRID:
                return HYBRID;
            case LIQUID:
                return LIQUID;
            default:
                return LIQUID;
        }
    }

    @Override
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        // If you are in combat, gain strength and the "lose strength at the end of your turn" power, equal to the potency of this potion.
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new BuryAction(1, false));
            addToBot(new DigAction(1,false));
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new IslandPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return 1;
    }

    public void upgradePotion()
    {
      potency += 1;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
