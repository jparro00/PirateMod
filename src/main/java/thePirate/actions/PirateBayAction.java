package thePirate.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PirateBayAction extends AbstractGameAction {

    public static final String[] TEXT;
    public int numberOfCards;

    public PirateBayAction(int numberOfCards){
        this.numberOfCards = numberOfCards;
    }

    public PirateBayAction(){
        this(1);
    }

    @Override
    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        if(player != null){

            String text = TEXT[0];
            if(numberOfCards > 1){
                text = TEXT[1] + numberOfCards + TEXT[2];
            }else {
                text = TEXT[0];
            }
            addToBot(new SelectCardsAction(player.drawPile.group, 1, text, false, new Predicate<AbstractCard>() {
                @Override
                public boolean test(AbstractCard card) {
                    return true;
                }
            }, new Consumer<List<AbstractCard>>() {
                @Override
                public void accept(List<AbstractCard> abstractCards) {
                    for (AbstractCard card : abstractCards){
                        AbstractCard newCard = card.makeStatEquivalentCopy();
                        if(card.upgraded)
                            newCard.upgrade();
                        player.drawPile.addToRandomSpot(newCard);
                        addToBot(new BuryAction(newCard));
                    }
                }
            }));
        }

        isDone = true;

    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("thePirate:PirateBayAction").TEXT;
    }

}
