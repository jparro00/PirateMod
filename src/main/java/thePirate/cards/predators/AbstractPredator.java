package thePirate.cards.predators;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.lures.AbstractLure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractPredator extends AbstractDynamicCard implements OnObtainCard {

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PirateMod.makeID("CardDescriptors"));
    public boolean showPreview;
    public AbstractPredator(String id, String img, int cost, CardType type, CardColor color, CardTarget target, boolean showPreview) {
        super(id, img, cost, type, color, CardRarity.SPECIAL, target);
        this.showPreview = showPreview;
        if(showPreview){
            cardsToPreview = getLure();
        }
        tags.add(CardTags.HEALING);
//        setDisplayRarity(CardRarity.RARE);
    }
    @Override
    public List<String> getCardDescriptors() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(uiStrings.TEXT[3]);
        return retVal;
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

    public abstract AbstractLure getLure();
}
