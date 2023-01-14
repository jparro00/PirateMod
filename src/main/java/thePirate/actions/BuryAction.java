package thePirate.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.cards.attacks.BeachBuddy;
import thePirate.powers.OnBury;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BuryAction extends AbstractGameAction {
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;

    public List<AbstractCard> cardsSelected;

    public BuryAction(int numberOfCards, boolean optional) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.cardsSelected = new ArrayList<>();
    }

    //sepcial constructor for removing specific cards without a selection
    public BuryAction(List<AbstractCard> cardsSelected){
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = 0;
        this.optional = false;
        this.cardsSelected = cardsSelected;

    }

    public BuryAction(AbstractCard card){
        this(new ArrayList<AbstractCard>(Arrays.asList(new AbstractCard[]{card})));
    }


    public BuryAction(int numberOfCards) {
        this(numberOfCards, false);
    }

    public void update() {
        if (this.duration == this.startDuration) {
            //first check if cardsSelected was passed at creation
            if (!cardsSelected.isEmpty()){
                Iterator<AbstractCard> iter = cardsSelected.iterator();

                while(iter.hasNext()) {
                    AbstractCard c = iter.next();
                    this.player.drawPile.moveToDiscardPile(c);
                }

                this.isDone = true;

            }else if (!this.player.drawPile.isEmpty() && this.numberOfCards > 0) {
                AbstractCard c;
                Iterator var6;
                if (this.player.drawPile.size() <= this.numberOfCards && !this.optional) {
                    ArrayList<AbstractCard> cardsToMove = new ArrayList();
                    var6 = this.player.drawPile.group.iterator();

                    while(var6.hasNext()) {
                        c = (AbstractCard)var6.next();
                        cardsToMove.add(c);
                    }

                    var6 = cardsToMove.iterator();

                    while(var6.hasNext()) {
                        c = (AbstractCard)var6.next();
                        this.player.drawPile.moveToDiscardPile(c);
                        cardsSelected.add(c);
                    }

                    this.isDone = true;
                } else {
                    CardGroup temp = new CardGroup(CardGroupType.UNSPECIFIED);
                    var6 = this.player.drawPile.group.iterator();

                    while(var6.hasNext()) {
                        c = (AbstractCard)var6.next();
                        temp.addToTop(c);
                    }

                    temp.sortAlphabetically(true);
                    temp.sortByRarityPlusStatusCardType(false);
                    if (this.numberOfCards == 1) {
                        if (this.optional) {
                            AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[0]);
                        } else {
                            AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[0], false);
                        }
                    } else if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
                    } else {
                        AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, TEXT[1] + this.numberOfCards + TEXT[2], false);
                    }

                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    this.player.drawPile.moveToDiscardPile(c);
                    cardsSelected.add(c);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }

        //exhaust cards if player has too much rum power
        if(isDone && cardsSelected != null && cardsSelected.size() > 0){


            //run onBury for each card buried
            for (AbstractCard card : cardsSelected){
                for (AbstractCard c : player.discardPile.group){
                    if (c instanceof OnBury){
                        ((OnBury)c).onBury(card);
                    }
                }
                for (AbstractPower playerPower : player.powers){
                    if(playerPower instanceof OnBury){
                        ((OnBury)playerPower).onBury(card);
                    }
                }
            }

            //run onBuryCards once for all cards
            for (AbstractCard c : player.discardPile.group){
                if (c instanceof OnBury){
                    ((OnBury)c).onBuryCards(cardsSelected);
                }
            }
            for (AbstractPower playerPower : player.powers){
                if(playerPower instanceof OnBury){
                    ((OnBury)playerPower).onBuryCards(cardsSelected);
                }
            }


        }

        //return beach buddy cards to hand
        if(cardsSelected != null && cardsSelected.size() > 0){
            for (AbstractCard card : player.discardPile.group){
                if (card instanceof BeachBuddy){
                    ((BeachBuddy) card).onBuryCards(cardsSelected);
                }
            }
        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("thePirate:BuryAction").TEXT;
    }
}
