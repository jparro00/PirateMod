package thePirate.patches.powers;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireTogether.actions.ApplyPowerWithoutModificationAction;
import thePirate.PirateMod;
import thePirate.powers.InkPower;

@SpirePatch2(
        clz = ApplyPowerWithoutModificationAction.class,
        method = "update",
        requiredModId = "spireTogether"
        )
public class ApplyPowerActionPatch {

    //Patching ApplyPowerAction for hardcore mode to prevent ink from applying for all players
    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(ApplyPowerWithoutModificationAction __instance){

        if (__instance.target != null && !__instance.target.isDeadOrEscaped()) {
                if (PirateMod.isHardcore()){
                    AbstractPower power = ReflectionHacks.getPrivate(__instance, ApplyPowerWithoutModificationAction.class, "powerToApply");
                        if (power.ID.equals(InkPower.POWER_ID) && !AbstractDungeon.player.equals(__instance.source)){
                                __instance.isDone = true;
                                return SpireReturn.Return();
                        }
                }
        }
        return SpireReturn.Continue();
    }

}
