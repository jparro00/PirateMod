package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.powers.InkPower;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class ExoticDish extends AbstractPirateRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(ExoticDish.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(ExoticDish.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(ExoticDish.class.getSimpleName() + ".png"));
    public static final int INK = 5;

    public boolean isActive;

    public ExoticDish(){
        super(ID, IMG,OUTLINE,RelicTier.UNCOMMON, LandingSound.FLAT);
    }


    public void atBattleStart() {
        AbstractPlayer p = AbstractDungeon.player;
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            this.addToBot(new ApplyPowerAction(mo, p, new InkPower(mo, p, INK), INK));
        }
        flash();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ExoticDish();
    }

}
