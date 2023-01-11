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
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;


    public FreeCannonballPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        // We load those txtures here.
        Texture tex84 = TextureLoader.getPowerTexture(this, 84);
        Texture tex32 = TextureLoader.getPowerTexture(this, 32);
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
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

}
