package thePirate.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thePirate.cards.OnDig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DigAction extends AbstractGameAction {

    public static final String[] TEXT;
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

        List < AbstractCard > cardsSelected = new ArrayList<>();
        String text;
        AbstractPlayer player = this.player;

        if (numberOfCards > 1){
            text = TEXT[1] + this.numberOfCards + TEXT[2];
        }else {
            text = TEXT[0];
        }

        addToTop(new SelectCardsAction(player.discardPile.group, numberOfCards,text, optional, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                card.stopGlowing();
                return digCard == null || digCard.uuid.equals(card.uuid);
            }
        }, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> abstractCards) {
                for(AbstractCard c : abstractCards){
                    c.upgrade();
                    player.discardPile.moveToHand(c);
                    player.hand.refreshHandLayout();
                    cardsSelected.add(c);
                }

                //adding onBury logic to Consumer
                if (cardsSelected.size() > 0){
                    List<AbstractCard> combatCards = new ArrayList<>();
                    combatCards.addAll(player.hand.group);
                    combatCards.addAll(player.discardPile.group);
                    combatCards.addAll(player.drawPile.group);

                    for (AbstractCard c : combatCards) {
                        if (c instanceof OnDig){
                            ((OnDig) c).onDig(cardsSelected);
                        }
                    }
                }

            }
        }));

        this.isDone = true;

    }


    static {
        TEXT = CardCrawlGame.languagePack.getUIString("thePirate:DigAction").TEXT;
    }

}
