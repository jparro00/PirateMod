package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.Cannon;
import thePirate.powers.FreeCannonballPower;
import thePirate.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class BronzeCannon extends AbstractCannonRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(BronzeCannon.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(BronzeCannon.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(BronzeCannon.class.getSimpleName() + ".png"));
    public static final int FREE_CANNONBALL = 1;

    public BronzeCannon() {
        this(ID, IMG, OUTLINE);
    }

    public BronzeCannon(String id, Texture img, Texture outline){
        super(id, img, outline);

    }


    @Override
    public List<AbstractGameAction> onUseCannon(AbstractPlayer p, AbstractMonster m) {
        List<AbstractGameAction> actions = new ArrayList<>();
        actions.add(new ApplyPowerAction(p, p, new FreeCannonballPower(p, FREE_CANNONBALL), FREE_CANNONBALL));
        flash();
        return actions;
    }

    @Override
    public AbstractCard.CardTarget getTarget() {
        return AbstractCard.CardTarget.SELF;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
