package thePirate.patches.characters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.characters.ThePirate;
import thePirate.relics.BetterOnUseGold;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "loseGold"
)
public class AbstractPlayerLoseGoldPatch {

    public static void Prefix(AbstractPlayer __instance, int goldAmount){


        //OnLoseGold is implemented directly in the pirate class, so adding here for support on other characters
        if (__instance.powers != null && !(__instance instanceof ThePirate) && goldAmount > 0) {
            for (AbstractPower power : __instance.powers) {
                if (power instanceof BetterOnUseGold) {
                    ((BetterOnUseGold) power).onLoseGold(goldAmount);
                }
            }
        }
    }

}
