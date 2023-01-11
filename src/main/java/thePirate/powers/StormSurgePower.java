package thePirate.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thePirate.PirateMod;
import thePirate.patches.characters.AbstractPlayerPatch;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makePowerPath;

public class StormSurgePower extends AbstractPower implements CloneablePowerInterface, OnUseEnergy {
    public AbstractCreature source;

    public static final String POWER_ID = PirateMod.makeID("StormSurgePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath(StormSurgePower.class.getSimpleName()+"_84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath(StormSurgePower.class.getSimpleName()+"_32.png"));

    public StormSurgePower(final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.source = AbstractDungeon.player;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {


    }


    @Override
    public void atStartOfTurn() {
        if(owner != null){
            AbstractPlayerPatch.energyUsedThisTurn.set(owner, 0);
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new StormSurgePower(amount);
    }

    @Override
    public void onUseEnergy(int e) {
        for(int i = 0; i < e; i++){
            this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1));
            this.addToBot(new ApplyPowerAction(owner, owner, new LoseStrengthPower(owner, 1), 1));
        }

    }
}
