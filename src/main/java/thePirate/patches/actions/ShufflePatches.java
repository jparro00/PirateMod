package thePirate.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import thePirate.cards.OnShuffle;
import thePirate.util.CardUtil;

import java.util.List;

public class ShufflePatches {

    @SpirePatch2(clz = EmptyDeckShuffleAction.class, method = SpirePatch.CONSTRUCTOR)
    public static class EmptyDeckShuffle {
        @SpirePostfixPatch
        public static void EmptyDeckShuffleAction() {
            List<AbstractCard> cards = CardUtil.getCombatCards(false, false);
            for (AbstractCard card : cards){
                if (card instanceof OnShuffle){
                    ((OnShuffle) card).onShuffle();
                }
            }
        }
    }

}
