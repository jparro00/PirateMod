package thePirate.patches.characters;

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

    //Note... this is a really terrible way to do this
    public static void Prefix(AbstractPlayer __instance, DamageInfo info){
        DefaultMod.logger.info("enter patch Prefix()");
        DefaultMod.logger.info("info.output: " + info.output);
        DefaultMod.logger.info("info.base: " + info.base);
        DefaultMod.logger.info("info.isModified: " + info.isModified);

        info.output = DamageInfoFieldPatch.originalOutput.get(info);
        int damageAmount = info.output;
        DefaultMod.logger.info("damageAmount: " + damageAmount);


        if (damageAmount < 0) {
            damageAmount = 0;
        }
        System.out.println("string");


        if (damageAmount > 1 && __instance.hasPower("IntangiblePlayer")) {
            damageAmount = 1;
        }
        if(info.owner != null){
            for (AbstractPower power : info.owner.powers){
                if(power instanceof OnAttackToChangeDamagePreBlock){
                    damageAmount = ((OnAttackToChangeDamagePreBlock) power).onAttackToChangeDamagePreBlock(info, info.output);
                    info.output = damageAmount;
                    DefaultMod.logger.info("damageAmount after modification: " + damageAmount);
                }
            }
        }

        DefaultMod.logger.info("Exit patch Prefix()");

    }
}
