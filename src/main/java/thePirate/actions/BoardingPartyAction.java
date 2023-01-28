package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public class BoardingPartyAction extends AbstractGameAction {
    @Override
    public void update() {
        List<AbstractCard> nonAttackCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.drawPile.group){
            if (!card.type.equals(AbstractCard.CardType.ATTACK)){
                nonAttackCards.add(card);
            }
        }
        addToTop(new BuryAction(nonAttackCards));
        isDone = true;
    }
}
