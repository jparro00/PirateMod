package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thePirate.powers.InkPower;

public class ConvertBlockToInkAction extends AbstractGameAction {


    public ConvertBlockToInkAction(AbstractCreature target){
        this.target = target;
    }


    @Override
    public void update() {

        if (target.currentBlock > 0){
            int block = target.currentBlock;
            this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new InkPower(target, AbstractDungeon.player, block), block));
            this.addToBot(new LoseBlockAction(target, AbstractDungeon.player, block));
        }

        this.isDone = true;

    }


}
