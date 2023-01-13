package thePirate.cards.predators;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.lures.AbstractLure;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractPredator extends AbstractDynamicCard implements SpawnModificationCard, OnObtainCard {

    public boolean showPreview;
    public AbstractPredator(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, boolean showPreview) {
        super(id, img, cost, type, color, rarity, target);
        this.showPreview = showPreview;
        if(showPreview){
            cardsToPreview = getLure();
        }
        tags.add(CardTags.HEALING);
    }

    @Override
    public void onRewardListCreated(ArrayList<AbstractCard> rewardCards) {
        PirateMod.logger.info("enter AbstractPredator.onRewardListCreated()");
        if (canSpawn(rewardCards)){
            rewardCards.add(new Kraken());
        }
    }

    @Override
    public void onObtainCard() {
        AbstractPlayer player = AbstractDungeon.player;
        AbstractLure lure = getLure();

        //remove all copies of the associated lure
        for (Iterator<AbstractCard> iter = player.masterDeck.group.iterator(); iter.hasNext(); ) {
            AbstractCard card = iter.next();
            if(lure.cardID.equals(card.cardID) && card instanceof AbstractLure){
                //iter.remove();
//                AbstractDungeon.effectsQueue.add(new PurgeCardEffect(card));
                ((AbstractLure) card).setPurge(true);

            }

        }


    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {


    }

    @Override
    public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards){
        PirateMod.logger.info("enter canSpawn()");
        boolean retVal = false;
        AbstractPlayer player = AbstractDungeon.player;
        for(AbstractCard card : player.hand.group){

            if (getLure().cardID.equals(card.cardID)){
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    @Override
    public boolean canSpawnShop(ArrayList<AbstractCard> currentShopCards) {
        return false;
    }

    public abstract AbstractLure getLure();
}
