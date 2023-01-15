package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.actions.BuryAction;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class WoodenLeg extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(WoodenLeg.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(WoodenLeg.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(WoodenLeg.class.getSimpleName() + ".png"));
    private boolean cardsSelected = true;
    public static final int CARDS_TO_BURY = 3;


    public WoodenLeg(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
    }

    public WoodenLeg(){
        super(ID, IMG,OUTLINE, RelicTier.RARE, LandingSound.FLAT);
    }


    @Override
    public void atBattleStartPreDraw() {
        addToBot(new BuryAction(CARDS_TO_BURY, true));
        flash();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WoodenLeg();
    }

}
