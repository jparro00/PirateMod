package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class GreedyChest extends AbstractPirateRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(GreedyChest.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(GreedyChest.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(GreedyChest.class.getSimpleName() + ".png"));

    public boolean isActive;

    public GreedyChest(){
        super(ID, IMG,OUTLINE,RelicTier.RARE, LandingSound.CLINK);
    }


    public void atBattleStart() {
        this.isActive = false;
        int gold = AbstractDungeon.player.gold;
        this.addToBot(new AbstractGameAction() {
            public void update() {
                if (!isActive && gold > 100) {
                    flash();
                    AbstractDungeon.player.addPower(new StrengthPower(AbstractDungeon.player, gold/100));
                    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, GreedyChest.this));
                    isActive = true;
                    AbstractDungeon.onModifyPower();
                }

                this.isDone = true;
            }
        });
    }

    public void onVictory() {
        this.isActive = false;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GreedyChest();
    }

}
