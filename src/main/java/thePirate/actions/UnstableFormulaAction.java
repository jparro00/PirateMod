package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.powers.InkPower;

public class UnstableFormulaAction extends AbstractGameAction {


    public AbstractCreature owner;
    public AbstractPower power;
    public int multiplier;

    public UnstableFormulaAction(AbstractCreature owner, AbstractPower power, int multiplier) {
        this.owner = owner;
        this.power = power;
        this.multiplier = multiplier;
    }

    @Override
    public void update() {

        boolean flash = false;
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            AbstractPower power = mo.getPower(InkPower.POWER_ID);
            if (power != null)
            if (power != null && power.amount > 0){
                int newAmount = power.amount * multiplier;

                addToBot(new ApplyPowerAction(mo,owner,new InkPower(mo, owner,newAmount)));
                flash = true;
            }
        }
        if (flash)
            power.flash();
        isDone = true;
    }
}
