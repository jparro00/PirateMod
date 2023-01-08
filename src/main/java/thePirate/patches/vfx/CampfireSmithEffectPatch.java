package thePirate.patches.vfx;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.vfx.campfire.CampfireSmithEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import thePirate.cards.attacks.SpareRigging;

@SpirePatch2(clz = CampfireSmithEffect.class, method = "update")
public class CampfireSmithEffectPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
    public static void onSmithPatch(AbstractCard c){
        if(c instanceof SpareRigging){
            ((SpareRigging) c).onSmith();
        }
    }



    public static class Locator extends SpireInsertLocator{
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "upgrade");
            return LineFinder.findInOrder(ctMethodToPatch, (Matcher)methodCallMatcher);
        }

    }
}
