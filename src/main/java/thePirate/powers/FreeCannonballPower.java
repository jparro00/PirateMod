package thePirate.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.cards.attacks.AbstractCannonBallCard;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makePowerPath;

public class FreeCannonballPower extends AbstractPower {
    public static final String POWER_ID = FreeCannonballPower.class.getSimpleName();
    private static final PowerStrings powerStrings;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public FreeCannonballPower(AbstractCreature owner, int amount) {
        this.name = powerStrings.NAME;
        this.ID = "FreeCannonballPower";
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
    }



    public void updateDescription() {
        if (this.amount == 1) {
            this.description = powerStrings.DESCRIPTIONS[0];
        } else {
            this.description = powerStrings.DESCRIPTIONS[1] + this.amount + powerStrings.DESCRIPTIONS[2];
        }

    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AbstractCannonBallCard && this.amount > 0) {
            this.flash();
            --this.amount;
            if (this.amount == 0) {
                this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, FreeCannonballPower.POWER_ID));
            }
        }
    }

    @Override
    public void atEndOfRound() {
        this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, FreeCannonballPower.POWER_ID));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(FreeCannonballPower.POWER_ID);
    }
}
