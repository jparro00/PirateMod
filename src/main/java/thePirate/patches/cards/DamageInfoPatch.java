package thePirate.patches.cards;


import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

@SpirePatch(
        clz= DamageInfo.class,
        method=SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCreature.class,
                int.class,
                DamageInfo.DamageType.class
        }
)
public class DamageInfoPatch {

    @SpirePostfixPatch
    public static void PostFix(DamageInfo __instance, AbstractCreature damageSource, int base, DamageInfo.DamageType type){
        DamageInfoFieldPatch.originalOutput.set(__instance, base);
    }
}
