package thePirate.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import thePirate.cards.Purgable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpirePatch(
        clz= CardCrawlGame.class,
        method="update"
)
public class CardCrawlGamePatch {


    @SpirePostfixPatch
    public static void Postfix(CardCrawlGame _instance){
        if (AbstractDungeon.player != null && AbstractDungeon.player.masterDeck != null){
            List<AbstractCard> cardsToRemove = new ArrayList<>();
            Iterator<AbstractCard> iter = AbstractDungeon.player.masterDeck.group.iterator();
            while (iter.hasNext()){
                AbstractCard card = iter.next();
                if (card instanceof Purgable && ((Purgable) card).getPurge()){
                    cardsToRemove.add(card);
                }
            }
            for (AbstractCard c: cardsToRemove){
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, (float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2))); // Create the card removal effect
                AbstractDungeon.player.masterDeck.removeCard(c); // Remove it from the deck
            }
        }
    }


}
