package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class GunsmithsBible extends CustomRelic implements ClickableRelic { // You must implement things you want to use from StSlib

    // ID, images, text.
    public static final String ID = PirateMod.makeID(GunsmithsBible.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(GunsmithsBible.class.getSimpleName() + ".png"));

    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(GunsmithsBible.class.getSimpleName()+".png"));

    private boolean usedThisTurn = false; // You can also have a relic be only usable once per combat. Check out Hubris for more examples, including other StSlib things.
    private boolean isPlayerTurn = false; // We should make sure the relic is only activateable during our turn, not the enemies'.

    public GunsmithsBible() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.CLINK);

        tips.clear();
        tips.add(new PowerTip(name, description));
    }


    @Override
    public void onRightClick() {// On right click

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) { // Only if you're in combat
            AbstractCannonRelic cannon;
            switch (AbstractDungeon.actNum){
                case 1:
                    cannon = new BronzeCannon();
                    break;
                case 2:
                    cannon = new SilverCannon();
                    break;
                case 3:
                    cannon = new GoldCannon();
                    break;
                case 4:
                    cannon = new PlatinumCannon();
                    break;
                default:
                    cannon = new BronzeCannon();
            }


            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(ID)) {
                    cannon.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        }
    }
    
    @Override
    public void atPreBattle() {
        usedThisTurn = false; // Make sure usedThisTurn is set to false at the start of each combat.
        beginLongPulse();     // Pulse while the player can click on it.
    }

    public void atTurnStart() {
        usedThisTurn = false;  // Resets the used this turn. You can remove this to use a relic only once per combat rather than per turn.
        isPlayerTurn = true; // It's our turn!
        beginLongPulse(); // Pulse while the player can click on it.
    }
    
    @Override
    public void onPlayerEndTurn() {
        isPlayerTurn = false; // Not our turn now.
        stopPulse();
    }
    

    @Override
    public void onVictory() {
        stopPulse(); // Don't keep pulsing past the victory screen/outside of combat.
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
