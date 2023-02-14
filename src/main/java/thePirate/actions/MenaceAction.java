package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.List;

public class MenaceAction extends AbstractGameAction {



    public MenaceAction(AbstractCreature source, AbstractCreature target){
        this.source = source;
        this.target = target;

    }


    @Override
    public void update() {
        List<AbstractPower> powers = target.powers;
        for (AbstractPower power : target.powers){
            if(power.type == AbstractPower.PowerType.DEBUFF){

                //check if the target has positive power and shackles
                AbstractPower strengthPower = target.getPower(StrengthPower.POWER_ID);
                if (strengthPower != null && !(strengthPower.amount >= 0) && power instanceof GainStrengthPower){
                    continue;
                }

                //double the power
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, power, power.amount));
            }
        }
        isDone = true;

    }
}
