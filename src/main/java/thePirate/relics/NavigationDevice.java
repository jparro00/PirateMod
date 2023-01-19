package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.actions.DigAction;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class NavigationDevice extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(NavigationDevice.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(NavigationDevice.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(NavigationDevice.class.getSimpleName() + ".png"));

    public boolean isActive;

    public NavigationDevice(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
    }

    public NavigationDevice(){
        super(ID, IMG,OUTLINE,RelicTier.UNCOMMON, LandingSound.CLINK);
    }


    @Override
    public void atTurnStart() {
        if(GameActionManager.turn == 1)
            addToBot(new DigAction(1, false));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NavigationDevice();
    }

}