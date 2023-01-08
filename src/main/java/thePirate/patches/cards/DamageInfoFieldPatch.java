package thePirate.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;

@SpirePatch(
        clz= DamageInfo.class,
        method=SpirePatch.CLASS
)
public class DamageInfoFieldPatch {

    public static SpireField<Integer> originalOutput = new SpireField<Integer>(() -> -1);


}
