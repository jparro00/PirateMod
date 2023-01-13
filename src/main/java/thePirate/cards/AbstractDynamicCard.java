package thePirate.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import thePirate.PirateMod;
import thePirate.actions.PurgeRemovablesAction;
import thePirate.cards.skills.Makeshift;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractDynamicCard extends AbstractDefaultCard {

    // "How come DefaultCommonAttack extends CustomCard and not DynamicCard like all the rest?"

    // Well every card, at the end of the day, extends CustomCard.
    // Abstract Default Card extends CustomCard and builds up on it, adding a second magic number. Your card can extend it and
    // bam - you can have a second magic number in that card (Learn Java inheritance if you want to know how that works).
    // Abstract Dynamic Card builds up on Abstract Default Card even more and makes it so that you don't need to add
    // the NAME and the DESCRIPTION into your card - it'll get it automatically. Of course, this functionality could have easily
    // Been added to the default card rather than creating a new Dynamic one, but was done so to deliberately.
    private CardStrings cardStrings;
    public String upgradedDescription;

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
}