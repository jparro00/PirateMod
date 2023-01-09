package thePirate.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DigAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;
    public AbstractCard digCard;

    public DigAction(AbstractCard digCard,int numberOfCards, boolean optional) {
        this.digCard = digCard;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
    }
    public DigAction(int numberOfCards, boolean optional) {
        this(null, numberOfCards, optional);
    }

    public DigAction(int numberOfCards){
        this(numberOfCards, false);
    }

    @Override
    public void update() {


        AbstractDungeon.actionManager.addToTop(new MoveCardsAction(player.hand, player.discardPile, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                return digCard == null || digCard.uuid.equals(card.uuid);
            }
        }, this.numberOfCards, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> abstractCards) {
                for(AbstractCard c : abstractCards){
                    c.upgrade();
                }

            }
        }));
        this.isDone = true;

    }



}
