package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.*;

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

    public static final int HC_MAX_STRENGTH = 3;
    public boolean isActive;

    public GreedyChest(){
        super(ID, IMG,OUTLINE,RelicTier.RARE, LandingSound.CLINK, isHardcore());
        calculateCounter();
    }


    public void atBattleStart() {
        calculateCounter();
        this.isActive = false;
        int gold = AbstractDungeon.player.gold;
        this.addToBot(new AbstractGameAction() {
            public void update() {
                if (!isActive && gold >= 100) {
                    int amount = gold/100;
                    //limit to max strength if hardcore mode enabled
                    amount = hardcore && amount > HC_MAX_STRENGTH ? HC_MAX_STRENGTH : amount;
                    flash();
                    AbstractDungeon.player.addPower(new StrengthPower(AbstractDungeon.player, amount));
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
        return getDefaultHardcoreDescription();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new GreedyChest();
    }

    public void calculateCounter(){
        if (AbstractDungeon.player != null){
            int gold = AbstractDungeon.player.gold;
            int amount = gold >= 100 ? gold/100 : -1;
            //limit to max strength if hardcore mode enabled
            amount = hardcore && amount > HC_MAX_STRENGTH ? HC_MAX_STRENGTH : amount;
            setCounter(amount);
        }
    }
    @Override
    public void onGainGold() {
        calculateCounter();
    }

    @Override
    public void onLoseGold() {
        calculateCounter();
    }

    @Override
    public void onSpendGold() {
        calculateCounter();
    }
}
