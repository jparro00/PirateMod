package thePirate.patches.characters;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.DefaultMod;
import thePirate.patches.cards.DamageInfoFieldPatch;
import thePirate.powers.OnAttackToChangeDamagePreBlock;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class AbstractPlayerDamagePatch {

    @SpireInsertPatch(
            loc=1741,
            localvars={"damageAmount"}
    )
    public static void Insert(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount){

        DefaultMod.logger.info("damageAmount before modification: " + damageAmount[0]);
        if(info.owner != null){
            for (AbstractPower power : info.owner.powers){
                if(power instanceof OnAttackToChangeDamagePreBlock){
                    damageAmount[0] = ((OnAttackToChangeDamagePreBlock) power).onAttackToChangeDamagePreBlock(info, damageAmount[0]);
                    DefaultMod.logger.info("damageAmount after modification: " + damageAmount[0]);
                }
            }
        }

    }
}
