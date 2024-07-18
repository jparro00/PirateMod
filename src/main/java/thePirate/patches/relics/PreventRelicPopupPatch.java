package thePirate.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch2(
        clz = AbstractRelic.class,
        method = "updateRelicPopupClick"
        )
public class PreventRelicPopupPatch {

    //Patching relics so they don't show info popup when clicked with Death Knell on a controller
    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(AbstractRelic __instance){

        if (ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "isHoveringCard")){
            return SpireReturn.Return();
        }
        return SpireReturn.Continue();
    }

}
