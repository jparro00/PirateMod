package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.city.Addict;
import com.megacrit.cardcrawl.events.city.Beggar;
import com.megacrit.cardcrawl.events.city.TheJoust;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.patches.vfx.PirateGainPennyEffect;
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
        super(ID, IMG,OUTLINE,RelicTier.COMMON, LandingSound.CLINK);
        this.counter = 0;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.actNum <= 4;
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
        if (gold > 0 && counter > 0){
            flash();
        }
        counter -= gold;
        if (counter < 0)
            counter = 0;

    }


    @Override
    public void onSpendGold(int gold) {
        counter += gold;
        Hitbox goldHb = AbstractDungeon.topPanel.goldHb;
        for(int i = 0; i < gold; ++i) {
            AbstractDungeon.effectList.add(new PirateGainPennyEffect(null, goldHb.cX, goldHb.cY, hb.cX, hb.cY, false));
        }
        flash();

    }

    public boolean isSpendGoldEvent(AbstractImageEvent event){
        boolean spend = false;
        if (
                event instanceof Cleric ||
                event instanceof Beggar ||
                event instanceof TheJoust ||
                event instanceof Addict
        ){
            spend = true;
        }

        return spend;

    }
}
