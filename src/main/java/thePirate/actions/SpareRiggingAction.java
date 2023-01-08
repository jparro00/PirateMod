package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class SpareRiggingAction extends AbstractGameAction {

    private AbstractCard card;

    public SpareRiggingAction(AbstractCard card){
        this.card = card;

    }


    @Override
    public void update() {

        AbstractDungeon.effectList.add(new PurgeCardEffect(this.card));
        AbstractDungeon.player.masterDeck.removeCard(this.card);
        this.isDone = true;
    }



}
