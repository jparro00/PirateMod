package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PurgeRemovablesAction extends AbstractGameAction {

    public AbstractCard card;
    //removes the card from combat after played
    public boolean purgeCardOnUse;
    public boolean removeFromCombat;

    public PurgeRemovablesAction(AbstractCard card, boolean purgeCardOnUse){
        this(card, purgeCardOnUse, false);
    }

    public PurgeRemovablesAction(AbstractCard card, boolean purgeCardOnUse, boolean removeFromCombat){
        this.removeFromCombat = removeFromCombat;
        this.purgeCardOnUse = purgeCardOnUse;
        this.card = card;
    }

    public PurgeRemovablesAction(AbstractCard card){
        this(card, false);
    }

    @Override
    public void update(){

        //TODO: reevaluate at some point to decide if this code makes sense
        AbstractPlayer p = AbstractDungeon.player;
        Set<AbstractCard> combatCards = new HashSet<>();
        combatCards.addAll(p.hand.group);
        combatCards.addAll(p.discardPile.group);
        combatCards.addAll(p.exhaustPile.group);
        combatCards.addAll(p.drawPile.group);


        CardGroup masterDeck = AbstractDungeon.player.masterDeck;
        Iterator<AbstractCard> iterator = masterDeck.group.iterator();
        AbstractCard cardToRemove = null;
        while (iterator.hasNext()){
            AbstractCard card = iterator.next();
            if (card.uuid.equals(this.card.uuid)){
                cardToRemove = card;
            }
        }

        card.purgeOnUse = this.purgeCardOnUse;

        if(cardToRemove != null){
            masterDeck.removeCard(cardToRemove);
        }

        if(removeFromCombat){
            p.hand.removeCard(card);
            p.discardPile.removeCard(card);
            p.exhaustPile.removeCard(card);
            p.drawPile.removeCard(card);
        }

        this.isDone = true;

    }

}
