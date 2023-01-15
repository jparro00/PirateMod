package thePirate.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.powers.InkPower;

public class InkPotion extends AbstractDynamicPotion{

    public static final String POTION_ID = PirateMod.makeID(InkPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final Color LIQUID = CardHelper.getColor(23,8,68);
    public static final Color HYBRID = CardHelper.getColor(34,12,99);
    public static final Color SPOTS = null;
    public static final int INK = 12;

    public InkPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SPHERE, PotionColor.EXPLOSIVE);
        
        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();
        
        // Initialize the Description
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        
       // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
        targetRequired = true;

        // Initialize the on-hover name + description
        tips.add(new PowerTip(name, description));
        
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
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(target,AbstractDungeon.player, new InkPower(target, AbstractDungeon.player,potency),potency));
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new InkPotion();
    }

    // This is your potency.
    @Override
    public int getPotency(final int potency) {
        return INK;
    }

    public void upgradePotion()
    {
      potency += 1;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
