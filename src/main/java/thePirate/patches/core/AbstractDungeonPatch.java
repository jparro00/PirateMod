package thePirate.patches.core;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thePirate.cards.Purgable;

import java.util.Iterator;

@SpirePatch(
        clz= AbstractDungeon.class,
        method="update"
)
public class AbstractDungeonPatch {



    public static void Postfix(AbstractDungeon _instance){
        if(AbstractDungeon.CurrentScreen.NONE.equals(_instance.screen)){
            Iterator<AbstractCard> iter = _instance.player.masterDeck.group.iterator();
            while (iter.hasNext()){
                AbstractCard card = iter.next();
                if (card instanceof Purgable && ((Purgable) card).getPurge()){
                    _instance.player.masterDeck.removeCard(card);
                }
            }

        }
    }


}
