package thePirate.patches.characters;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.cards.powers.OnLoseGold;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "loseGold"
)
public class AbstractPlayerLoseGoldPatch {

    public static void Prefix(AbstractPlayer __instance, int goldAmount){


        AbstractPlayer player = AbstractDungeon.player;
        if (player != null && goldAmount > 0) {
            for (AbstractPower power : player.powers) {
                if (power instanceof OnLoseGold) {
                    ((OnLoseGold) power).onLoseGold(goldAmount);
                }
            }
        }
    }

}
