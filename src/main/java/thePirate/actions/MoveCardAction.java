package thePirate.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import thePirate.PirateMod;

import java.util.function.Predicate;

public class MoveCardAction extends AbstractGameAction {

    public CardGroup source;
    public CardGroup destination;
    public AbstractCard card;

    public MoveCardAction(CardGroup source, CardGroup destination, AbstractCard card) {
        this.source = source;
        this.destination = destination;
        this.card = card;
    }

    @Override
    public void update() {

        AbstractCard cardToMove = this.card;
        addToTop(new MoveCardsAction(destination, source, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                PirateMod.logger.info("card is equal: " + card.equals(cardToMove));
                return card.equals(cardToMove);
            }
        }));

        this.isDone = true;

    }
}
