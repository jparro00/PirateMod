package thePirate.cards;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import thePirate.actions.PurgeRemovablesAction;
import thePirate.cards.lures.AbstractLure;
import thePirate.cards.predators.AbstractPredator;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractDynamicCard extends AbstractDefaultCard implements SpawnModificationCard {

    // "How come DefaultCommonAttack extends CustomCard and not DynamicCard like all the rest?"

    // Well every card, at the end of the day, extends CustomCard.
    // Abstract Default Card extends CustomCard and builds up on it, adding a second magic number. Your card can extend it and
    // bam - you can have a second magic number in that card (Learn Java inheritance if you want to know how that works).
    // Abstract Dynamic Card builds up on Abstract Default Card even more and makes it so that you don't need to add
    // the NAME and the DESCRIPTION into your card - it'll get it automatically. Of course, this functionality could have easily
    // Been added to the default card rather than creating a new Dynamic one, but was done so to deliberately.
    private CardStrings cardStrings;
    public String upgradedDescription;
    public boolean stormPending;
    public boolean storm;

    public AbstractDynamicCard(final String id,
                               final String img,
                               final int cost,
                               final CardType type,
                               final CardColor color,
                               final CardRarity rarity,
                               final CardTarget target) {

        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(id);
        upgradedDescription = cardStrings.UPGRADE_DESCRIPTION;


    }

    public boolean inPlayerCardGroup(){
        AbstractPlayer player = AbstractDungeon.player;
        return player != null &&
            (
                player.hand.contains(this) ||
                player.discardPile.contains(this) ||
                player.exhaustPile.contains(this) ||
                player.limbo.contains(this)
            );

    }

    public void storm(AbstractPlayer p, AbstractMonster m){
        if (storm && !stormPending){
            int cardsPlayedThisTurn = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
            cardsPlayedThisTurn += AbstractDungeon.actionManager.cardQueue.size() - 1;
            for (int i = 0; i < cardsPlayedThisTurn - 1; i++){
                AbstractDynamicCard tmp = (AbstractDynamicCard)this.makeSameInstanceOf();
                tmp.exhaust = false;
                tmp.stormPending = true;
                tmp.current_x = ((AbstractCard)this).current_x;
                tmp.current_y = ((AbstractCard)this).current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                tmp.applyPowers();
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, ((AbstractCard)this).energyOnUse, true, true), false);
            }
        }
    }

    @Override
    public AbstractCard replaceWith(ArrayList<AbstractCard> rewardCards) {

        AbstractLure lure = null;
        boolean hasRare = false;
        boolean hasPredator = false;
        boolean hasNonRare = false;

        for (AbstractCard card : rewardCards){
            if (card.rarity.equals(CardRarity.RARE)){
                hasRare = true;
            }else {
                hasNonRare = true;
            }
            if (card instanceof AbstractPredator){
                hasPredator = true;
            }
        }

        if (!hasPredator && hasRare && (hasNonRare || this.rarity != CardRarity.RARE)){
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group){
                if (card instanceof AbstractLure){
                    lure = (AbstractLure) card;
                    break;
                }
            }

            if(lure != null && !rarity.equals(CardRarity.RARE)){
                return lure.getPredator();
            }
        }
        return this;
    }
    @Override
    public void onRewardListCreated(ArrayList<AbstractCard> rewardCards) {
        boolean hasRare = false;
        boolean lastRare = false;
        boolean hasPredator = false;
        boolean hasNonRare = false;
        AbstractLure lure = null;
        for (AbstractCard card : rewardCards){
            if (card.rarity.equals(CardRarity.RARE)){
                hasRare = true;
            } else {
                hasNonRare = true;
            }
            if (card instanceof AbstractPredator){
                hasPredator = true;
            }
        }
        if (rewardCards.get(rewardCards.size()-1).rarity.equals(CardRarity.RARE)){
            lastRare = true;
        }
        if (!hasPredator && hasRare && lastRare && hasNonRare){

            //check if player has a lure in their deck
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group){
                if (card instanceof AbstractLure){
                    lure = (AbstractLure) card;
                    break;
                }
            }

            if(lure != null){
                rewardCards.set(0, lure.getPredator());
            }
        }
    }

    @Override
    public void update(){
        if(this instanceof Purgable && ((Purgable) this).getPurge() && !((Purgable) this).queuedForPurge()){
            AbstractPlayer player = AbstractDungeon.player;

            //purge non-makeshift cards, and makeshift cards that are in the player deck or combat
            if(!(this instanceof Makeshift) || (player != null && (player.masterDeck.contains(this) || inPlayerCardGroup()))) {
                addToTop(new PurgeRemovablesAction(this, true, true));
                ((Purgable) this).setQueuedForPurge(true);
                AbstractDungeon.effectsQueue.add(new PurgeCardEffect(this));
            }
        }
        super.update();
    }

    public void upgradeDescription(){
        if(upgradedDescription != null){
            rawDescription = upgradedDescription;
        }
        initializeDescription();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractDynamicCard card = (AbstractDynamicCard)super.makeStatEquivalentCopy();
        card.secondMagic = secondMagic;
        card.upgradedSecondMagic = upgradedSecondMagic;
        return card;
    }

}