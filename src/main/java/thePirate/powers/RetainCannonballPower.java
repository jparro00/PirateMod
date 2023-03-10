package thePirate.powers;


import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.actions.InvisibleRemovePowerAction;
import thePirate.cards.attacks.AbstractCannonBallCard;
import thePirate.util.TextureLoader;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RetainCannonballPower extends AbstractPower implements CloneablePowerInterface, InvisiblePower {
    public AbstractCreature source;
    public static final String POWER_ID = "RetainCannonballPower";
    private static final PowerStrings powerStrings;
    public static final String NAME = "RetainCannonballPower";


    public RetainCannonballPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }

        Texture tex84 = TextureLoader.getPowerTexture(this, 84);
        Texture tex32 = TextureLoader.getPowerTexture(this, 32);

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        this.updateDescription();
    }

    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        String text = "";
        if (amount == 1){
            text = powerStrings.DESCRIPTIONS[0]+ powerStrings.DESCRIPTIONS[1];
        }else {
            text = powerStrings.DESCRIPTIONS[0]+ amount + powerStrings.DESCRIPTIONS[2];
        }
        if (isPlayer && !AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.player.hasRelic("Runic Pyramid") && !AbstractDungeon.player.hasPower("Equilibrium")) {
            addToBot(new SelectCardsInHandAction(amount,text,false,false, new Predicate<AbstractCard>() {
                @Override
                public boolean test(AbstractCard abstractCard) {
//                    abstractCard.stopGlowing();
                    return abstractCard instanceof AbstractCannonBallCard;
                }
            }, new Consumer<List<AbstractCard>>() {
                @Override
                public void accept(List<AbstractCard> abstractCards) {
                    for (AbstractCard card : abstractCards){
                        card.retain = true;
                    }

                }
            }));
        }

        if (AbstractDungeon.player.hasPower(POWER_ID)) {
            addToBot(new InvisibleRemovePowerAction(owner, owner, POWER_ID));
        }

    }

    public void updateDescription() {
    }

    public AbstractPower makeCopy() {
        return new RetainCannonballPower(this.owner, this.amount);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("thePirate:RetainCannonballPower");
    }
}
