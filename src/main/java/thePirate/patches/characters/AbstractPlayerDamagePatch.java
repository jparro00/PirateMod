package thePirate.patches.characters;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import thePirate.PirateMod;
import thePirate.powers.OnAttackToChangeDamagePreBlock;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class AbstractPlayerDamagePatch {

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"damageAmount"}
    )
    public static void Insert(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount){

        PirateMod.logger.info("damageAmount before modification: " + damageAmount[0]);
        if(info.owner != null){
            for (AbstractPower power : info.owner.powers){
                if(power instanceof OnAttackToChangeDamagePreBlock){
                    damageAmount[0] = ((OnAttackToChangeDamagePreBlock) power).onAttackToChangeDamagePreBlock(info, damageAmount[0]);
                    PirateMod.logger.info("damageAmount after modification: " + damageAmount[0]);
                }
            }
        }

    }


    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}
