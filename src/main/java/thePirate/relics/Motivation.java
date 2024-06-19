package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.powers.OnBury;
import thePirate.util.TextureLoader;

import java.util.List;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class Motivation extends AbstractPirateRelic implements OnBury {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(Motivation.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(Motivation.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(Motivation.class.getSimpleName() + ".png"));
    private boolean cardsSelected = true;
    public int cardsBurried;


    @Override
    public void atTurnStart() {
        cardsBurried = 0;
        pulse = true;
    }

    public Motivation(){
        super(ID, IMG,OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Motivation();
    }

    @Override
    public void onBury(AbstractCard card) {
    }

    @Override
    public void onVictory() {
        pulse = false;
    }

    @Override
    public void onBuryCards(List<AbstractCard> cards) {
        if (cardsBurried == 0 && cards.size() > 0){
            addToBot(new GainEnergyAction(1));
            pulse = false;
            flash();
        }
        cardsBurried += cards.size();
    }
}
