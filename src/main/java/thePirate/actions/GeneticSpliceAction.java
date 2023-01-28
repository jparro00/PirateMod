package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thePirate.powers.InkPower;

public class GeneticSpliceAction extends AbstractGameAction {

    int divisor;

    public GeneticSpliceAction(AbstractCreature target, int divisor){
        this.target = target;
        this.divisor = divisor;
    }


    @Override
    public void update() {
        AbstractPower power = target.getPower(InkPower.POWER_ID);

        if (power != null){
            int targetInk = power.amount;
            int dexToGain = targetInk / divisor;
            AbstractPlayer p = AbstractDungeon.player;

            addToTop(new ApplyPowerAction(p,p,new DexterityPower(p,dexToGain),dexToGain));
            addToTop(new RemoveSpecificPowerAction(target,p,InkPower.POWER_ID));

        }

        isDone = true;

    }


}
