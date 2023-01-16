package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import java.util.Iterator;
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
    public static final int DOUBLE_CANNONBALL = 1;

    public SilverCannon() {
        this(ID, IMG, OUTLINE);
    }

    public SilverCannon(String id, Texture img, Texture outline){
        super(id, img, outline);
    }

    @Override
    public List<AbstractGameAction> onUseCannon(AbstractPlayer p, AbstractMonster m) {
        List<AbstractGameAction> actions = super.onUseCannon(p,m);
        //trying out nerfing silver cannon's double cannonball effect
//        actions.add(new ApplyPowerAction(p,p,new PlayCannonballTwicePower(DOUBLE_CANNONBALL), DOUBLE_CANNONBALL));

        Iterator iterator = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(iterator.hasNext()) {
            AbstractMonster mo = (AbstractMonster)iterator.next();
            addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, VULNERABLE, false), VULNERABLE));
        }
        return actions;
    }

    @Override
    public AbstractCard.CardTarget getTarget() {
        return AbstractCard.CardTarget.ALL_ENEMY;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
