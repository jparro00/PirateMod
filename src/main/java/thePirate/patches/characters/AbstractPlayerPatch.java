package thePirate.patches.characters;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(
        clz= AbstractPlayer.class,
        method=SpirePatch.CLASS
)
public class AbstractPlayerPatch {
    public static SpireField<Integer> energyUsedThisTurn = new SpireField<Integer>(() -> 0);
}
