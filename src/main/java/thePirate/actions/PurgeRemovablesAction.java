package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thePirate.DefaultMod;

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
        DefaultMod.logger.info("enter PurgeRemovablesAction.update()");

        //TODO: reevaluate at some point to decide if this cod makes sense
        AbstractPlayer p = AbstractDungeon.player;
        Set<AbstractCard> combatCards = new HashSet<>();
        combatCards.addAll(p.hand.group);
        combatCards.addAll(p.discardPile.group);
        combatCards.addAll(p.exhaustPile.group);
        combatCards.addAll(p.drawPile.group);

        int combatCount = 0;
        int combatUpgradeCount = 0;
        int masterDeckCount = 0;
        int masterDeckUpgradeCount = 0;
        boolean remove = true;
        boolean removeUpgrade = card.upgraded;
        //remove all cards that don't match ID
        Iterator<AbstractCard> iter = combatCards.iterator();
        while (iter.hasNext()){
            AbstractCard c = iter.next();
            if (!this.card.cardID.equals(c.cardID)){
                iter.remove();
            }
        }

        //count upgrades
        for (AbstractCard c : combatCards){
            combatCount++;
            if (c.upgraded){
                combatUpgradeCount++;
            }
        }

        //count deck upgrades
        for (AbstractCard c : p.masterDeck.group){
            masterDeckCount++;
            if (c.upgraded){
                masterDeckUpgradeCount++;
            }
        }

        if(this.card.upgraded && combatUpgradeCount > masterDeckUpgradeCount){
            removeUpgrade = false;
        }
        // /

        CardGroup masterDeck = AbstractDungeon.player.masterDeck;
        Iterator<AbstractCard> iterator = masterDeck.group.iterator();
        AbstractCard cardToRemove = null;
        while (iterator.hasNext()){
            AbstractCard card = iterator.next();
            if (card.cardID.equals(this.card.cardID) && card.upgraded == removeUpgrade){
                cardToRemove = card;
                break;
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
