package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Disarm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thePirate.PirateMod;
import thePirate.cards.Cannon;
import thePirate.util.TextureLoader;

import java.util.List;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class PlatinumCannon extends GoldCannon{

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(PlatinumCannon.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(PlatinumCannon.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(PlatinumCannon.class.getSimpleName() + ".png"));

    public static final int VULNERABLE = 1;
    public static final int WEAK = 1;
    public static final int STRENGTH_DOWN = -2;

    public PlatinumCannon() {
        this(ID, IMG, OUTLINE);
    }

    public PlatinumCannon(String id, Texture img, Texture outline){
        super(id,img,outline);
    }

    @Override
    public List<AbstractGameAction> onUseCannon(AbstractPlayer p, AbstractMonster m) {
        List<AbstractGameAction> actions = super.onUseCannon(p,m);
        //TODO: I think I have to remove the targetted vulnerable
        PirateMod.logger.info("onUseCannon.m: " + m);
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            actions.add(new ApplyPowerAction(mo, p, new VulnerablePower(mo, VULNERABLE, false), VULNERABLE));
        }
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            actions.add(new ApplyPowerAction(mo, p, new WeakPower(mo, WEAK, false), WEAK));
        }
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            actions.add(new ApplyPowerAction(mo, p, new StrengthPower(mo, STRENGTH_DOWN), STRENGTH_DOWN));
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
