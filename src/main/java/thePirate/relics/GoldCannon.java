package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import java.util.List;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class GoldCannon extends SilverCannon{

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(GoldCannon.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(GoldCannon.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(GoldCannon.class.getSimpleName() + ".png"));

    public static final int STRENGTH = 2;

    public GoldCannon() {
        this(ID, IMG, OUTLINE);
    }

    public GoldCannon(String id, Texture img, Texture outline){
        super(id,img,outline);

    }

    @Override
    public List<AbstractGameAction> onUseCannon(AbstractPlayer p, AbstractMonster m) {
        List<AbstractGameAction> actions = super.onUseCannon(p,m);
        actions.add(new ApplyPowerAction(p, p, new StrengthPower(p, STRENGTH), STRENGTH));

        return actions;
    }

    @Override
    public AbstractCard.CardTarget getTarget() {
        return AbstractCard.CardTarget.ENEMY;
    }
    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
