package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.powers.InkPower;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class WritingReed extends AbstractPirateRelic implements OnApplyPowerRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(WritingReed.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(WritingReed.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(WritingReed.class.getSimpleName() + ".png"));
    public static final int EXTRA_INK = 1;


    public WritingReed(){
        super(ID, IMG,OUTLINE, RelicTier.COMMON, LandingSound.FLAT);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WritingReed();
    }

    @Override
    public boolean onApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return true;
    }



    @Override
    public int onApplyPowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        PirateMod.logger.info("enter onApplyPowerStacks()");
        if (power instanceof InkPower){
            power.amount += EXTRA_INK;
            stackAmount += EXTRA_INK;
            flash();
        }
        return stackAmount;
    }
}
