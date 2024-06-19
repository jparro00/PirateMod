package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.powers.OnBury;
import thePirate.util.TextureLoader;

import java.util.List;

import static thePirate.PirateMod.*;

public class WoodenCompass extends AbstractPirateRelic implements OnBury {

    // ID, images, text.
    public static final String ID = PirateMod.makeID(WoodenCompass.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(WoodenCompass.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(WoodenCompass.class.getSimpleName() + ".png"));
    public static final int DAMAGE = 5;
    public static final int HC_DAMAGE = 3;
    public int damage;


    public WoodenCompass(){
        super(ID, IMG,OUTLINE, RelicTier.RARE, LandingSound.FLAT, isHardcore());
        if (hardcore)
            this.damage = HC_DAMAGE;
        else
            this.damage = DAMAGE;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return getDefaultHardcoreDescription();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WoodenCompass();
    }

    @Override
    public void onBury(AbstractCard card) {
    }


    @Override
    public void onBuryCards(List<AbstractCard> cards) {
        flash();
//        CardCrawlGame.sound.play("TINGSHA");
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        for (int i = 0; i < cards.size(); i++){
            addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }
}
