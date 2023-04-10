package thePirate.patches.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.powers.TimeWarpPower;
import thePirate.util.TextureLoader;

@SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
public class TimeWarpReminderPatch {

    //Since MintySpire doesn't use TextureLoader I'll instantiate the texture this way
    private static final Texture timeWarpIconTexture = TextureLoader.getTexture(PirateMod.makePowerPath(TimeWarpPower.class.getSimpleName()+"_" + 84 +".png"));
    private static  final TextureAtlas.AtlasRegion doubleRegion = new TextureAtlas.AtlasRegion(timeWarpIconTexture, 0, 0, timeWarpIconTexture.getWidth(), timeWarpIconTexture.getHeight());

    //TODO Work out an elegant way for the glow text to be slightly larger than the original, while maintaining the same origin
    @SpirePostfixPatch
    public static void patch(AbstractCard __instance, SpriteBatch sb) {
        if (echoFormValidChecker(__instance)) {
            sb.setColor(Color.WHITE);
            float x = __instance.current_x + 390f * __instance.drawScale / 3f * Settings.scale;
            float y = __instance.current_y + 380.0f * __instance.drawScale / 3.0f * Settings.scale;
            renderHelper(sb, doubleRegion, x, y, __instance);
            //glow effect implementation taken from GK's SpicyShops
/*
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, (MathUtils.cosDeg((float) (System.currentTimeMillis() / 5L % 360L)) + 1.25F) / 3.0F));
            renderHelper(sb, doubleRegion, x, y, __instance);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
*/
        }
    }

    //code to render an image on top of all relevant cards taken from Jedi's Ranger
    private static void renderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float drawX, float drawY, AbstractCard C) {
        sb.draw(img, drawX + img.offsetX - (float) img.originalWidth / 2.0F, drawY + img.offsetY - (float) img.originalHeight / 2.0F, (float) img.originalWidth / 2.0F - img.offsetX, (float) img.originalHeight / 2.0F - img.offsetY, (float) img.packedWidth, (float) img.packedHeight, C.drawScale * Settings.scale, C.drawScale * Settings.scale, C.angle);
    }

    //Overly complicated validation method to check when to draw the double card effect
    private static boolean echoFormValidChecker(AbstractCard __instance) {
        //TODO: add config to turn this on/off
        if (!PirateMod.disableTimeWarpReminder.toggle.enabled && AbstractDungeon.player != null &&AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            TimeWarpPower timeWarpPower = (TimeWarpPower)AbstractDungeon.player.getPower(TimeWarpPower.POWER_ID);
            if (timeWarpPower != null) {
                AbstractCard card = timeWarpPower.firstCard;
                if (__instance.equals(card)){
                    return true;
                }
            }
        }

        return false;
    }

}
