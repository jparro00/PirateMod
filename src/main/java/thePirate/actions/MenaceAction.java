package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

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
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, power, power.amount));
            }
        }
        isDone = true;

    }
}
