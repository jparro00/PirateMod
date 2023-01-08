package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.List;

public class GainEnergyFromBuryAction extends AbstractGameAction {


    public BuryAction buryAction;
    public int amount;
    public boolean recursive;

    public GainEnergyFromBuryAction(int amount){
        this(amount, false, null);
    }

    public GainEnergyFromBuryAction(int amount, boolean recursive, BuryAction buryAction){
        this.recursive = recursive;
        this.amount = amount;
        this.buryAction = buryAction;
    }

    @Override
    public void update() {

        if(!recursive){
            buryAction = new BuryAction(amount);
            this.addToBot(buryAction);
            this.addToBot(new GainEnergyFromBuryAction(amount, true, buryAction));
        }else {
            int energyToGain = 0;
            List<AbstractCard> cards = buryAction.cardsSelected;
            for (AbstractCard card : cards){
                energyToGain += card.cost;
            }

            this.addToBot(new GainEnergyAction(energyToGain));
        }

        isDone = true;

    }

}
