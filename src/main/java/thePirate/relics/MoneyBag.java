package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Girya;
import thePirate.PirateMod;
import thePirate.actions.DigAction;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class MoneyBag extends CustomRelic implements BetterOnUseGold{

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(MoneyBag.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(MoneyBag.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(MoneyBag.class.getSimpleName() + ".png"));

    public boolean isActive;


    public MoneyBag(){
        super(ID, IMG,OUTLINE,RelicTier.UNCOMMON, LandingSound.CLINK);
        this.counter = 0;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MoneyBag();
    }

    @Override
    public void onLoseGold(int gold) {
        PirateMod.logger.info("enter onLoseGold()");
        counter -= gold;
        if (counter < 0)
            counter = 0;

    }


    @Override
    public void onSpendGold(int gold) {
        PirateMod.logger.info("enter onSpendGold()");
        counter += gold;

    }
}
