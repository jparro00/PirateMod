package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thePirate.PirateMod;
import thePirate.powers.FreeCannonballPower;
import thePirate.util.TextureLoader;

import java.util.List;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class SilverCannon extends BronzeCannon {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(SilverCannon.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(SilverCannon.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(SilverCannon.class.getSimpleName() + ".png"));

    public static final int VULNERABLE = 1;
    public static final int FREE_CANNONBALL = 1;

    public SilverCannon() {
        this(ID, IMG, OUTLINE);
    }

    public SilverCannon(String id, Texture img, Texture outline){
        super(id, img, outline);
    }

    @Override
    public List<AbstractGameAction> onUseCannon(AbstractPlayer p, AbstractMonster m) {
        List<AbstractGameAction> actions = super.onUseCannon(p,m);
        actions.add(new ApplyPowerAction(p, p, new FreeCannonballPower(p, FREE_CANNONBALL), 1));

        //Don't add vulnerable if this is coming from PlatinumCannon, since that changes target to ALL_ENEMY
        if(m != null){
            actions.add(new ApplyPowerAction(m,p,new VulnerablePower(m,VULNERABLE, false),VULNERABLE));
        }
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
