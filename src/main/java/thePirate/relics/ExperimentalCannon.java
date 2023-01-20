package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.powers.FreeCannonballPower;
import thePirate.powers.PlayCannonballTwicePower;
import thePirate.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class ExperimentalCannon extends AbstractCannonRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(ExperimentalCannon.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(ExperimentalCannon.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(ExperimentalCannon.class.getSimpleName() + ".png"));
    public static final int FREE_CANNONBALL = 1;

    public ExperimentalCannon() {
        super(ID, IMG, OUTLINE, RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public void obtain() {
        for (int i = 0; i< AbstractDungeon.player.relics.size(); ++i) {
            if (AbstractDungeon.player.relics.get(i) instanceof AbstractCannonRelic || AbstractDungeon.player.relics.get(i) instanceof GunsmithsBible) {
                this.instantObtain(AbstractDungeon.player, i, true);
                break;
            }
        }
        super.onEquip();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(GunsmithsBible.ID) || AbstractDungeon.player.hasRelic(BronzeCannon.ID);
    }


    @Override
    public List<AbstractGameAction> onUseCannon(AbstractPlayer p, AbstractMonster m) {
        List<AbstractGameAction> actions = new ArrayList<>();
        actions.add(new ApplyPowerAction(p,p, new PlayCannonballTwicePower(1),1));
        actions.add(new ApplyPowerAction(p,p, new FreeCannonballPower(FREE_CANNONBALL),FREE_CANNONBALL));
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
