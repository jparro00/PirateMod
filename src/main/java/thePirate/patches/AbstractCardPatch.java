package thePirate.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.cards.attacks.AbstractCannonBallCard;

@SpirePatch(
        clz= AbstractCard.class,
        method="freeToPlay"
)
public class AbstractCardPatch {

    public static SpireReturn<Boolean> Prefix(AbstractCard _instance){

        if(AbstractDungeon.player != null &&
                AbstractDungeon.currMapNode != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                AbstractDungeon.player.hasPower("FreeCannonballPower") &&
                _instance instanceof AbstractCannonBallCard){
            return SpireReturn.Return(true);
        }
        return SpireReturn.Continue();
    }

}
