package thePirate.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

public class CardCounterPatches {

    public static void incrementBury(int value){
        cardsBuriedThisTurn += value;
        cardsBuriedThisCombat += value;
    }
    public static int cardsBuriedThisTurn;
    public static int cardsBuriedThisCombat;
    public static int energyUsedThisTurn;

    @SpirePatch2(clz = GameActionManager.class, method = "clear")
    public static class ResetCounters {
        @SpirePrefixPatch
        public static void reset() {
            energyUsedThisTurn = 0;
            cardsBuriedThisTurn = 0;
            cardsBuriedThisCombat = 0;
        }
    }

    @SpirePatch2(clz = GameActionManager.class, method = "getNextAction")
    public static class NewTurnCounters {
        @SpireInsertPatch(locator = Locator.class)
        public static void reset() {
            cardsBuriedThisTurn = 0;
            energyUsedThisTurn = 0;
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                return LineFinder.findInOrder(ctBehavior, new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnRelics"));
            }
        }
    }

}