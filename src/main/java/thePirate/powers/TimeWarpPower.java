package thePirate.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thePirate.PirateMod;
import thePirate.actions.MoveCardAction;
import thePirate.util.TextureLoader;

public class TimeWarpPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = PirateMod.makeID(TimeWarpPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public AbstractCard firstCard;
    public int lastCardXCost;


    public TimeWarpPower(final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = AbstractDungeon.player;
//        this.amount = amount;
        this.amount = -1;
        this.source = AbstractDungeon.player;

        type = PowerType.BUFF;
        isTurnBased = false;
        Texture tex84 = TextureLoader.getPowerTexture(this, 84);
        Texture tex32 = TextureLoader.getPowerTexture(this, 32);

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        if (firstCard == null && !card.exhaust && !AbstractCard.CardType.POWER.equals(card.type)){
            firstCard = card;
            if (card.costForTurn == -1) {
                lastCardXCost = EnergyPanel.getCurrentEnergy();
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        if(firstCard != null){
            AbstractPlayer p = AbstractDungeon.player;
            boolean foundCard = false;
            for (AbstractCard card : p.discardPile.group){
                if (card.uuid.equals(firstCard.uuid)){
                    foundCard = true;
                    firstCard = card;
                    addToTop(new DiscardToHandAction(firstCard));
                    break;
                }
            }
            if (!foundCard){
                for (AbstractCard card : p.drawPile.group){
                    if (card.uuid.equals(firstCard.uuid)){
                        foundCard = true;
                        firstCard = card;
                        addToTop(new MoveCardAction(p.drawPile, p.hand, firstCard));
                        break;
                    }
                }
            }
            if (foundCard){
                if (lastCardXCost > 0){
                    addToBot(new GainEnergyAction(lastCardXCost));
                }else {
                    addToBot(new GainEnergyAction(firstCard.costForTurn));
                }
            }
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    firstCard = null;
                    lastCardXCost = 0;
                    isDone = true;
                }
            });
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new TimeWarpPower(amount);
    }
}
