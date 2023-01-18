package thePirate.patches.actions;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import thePirate.patches.vfx.PirateGainPennyEffect;
import thePirate.relics.MoneyBag;

import java.util.ArrayList;

@SpirePatch2(
        clz = DamageAction.class,
        method = "stealGold"
)
public class DamageActionStealGoldPatch {


    @SpireInsertPatch(
            locator=Locator.class
    )
    public static void Insert(DamageAction __instance, @ByRef int[] ___goldAmount){

        if (__instance.target.isPlayer){
            int moneyBagCounter = 0;
            int goldToLose = ___goldAmount[0];
            int gainPennyCount = 0;

            for (AbstractRelic r : ((AbstractPlayer)__instance.target).relics){

                if (r instanceof MoneyBag){
                    moneyBagCounter = r.counter;
                    ((MoneyBag) r).onLoseGold(goldToLose);
                    if (goldToLose <= moneyBagCounter){
                        gainPennyCount = goldToLose;
                        goldToLose = 0;
                    }else{
                        gainPennyCount = goldToLose - moneyBagCounter;
                        goldToLose -= moneyBagCounter;
                    }
                    ___goldAmount[0] = goldToLose;
                    for(int i = 0; i < gainPennyCount; ++i) {
                        AbstractDungeon.effectList.add(new PirateGainPennyEffect(__instance.source, r.hb.cX, r.hb.cY, __instance.source.hb.cX, __instance.source.hb.cY, false));
                    }
                    break;
                }

            }

        }

    }


    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws PatchingException, CannotCompileException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(DamageAction.class, "goldAmount");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }

}
