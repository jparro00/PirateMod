package thePirate.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.PirateMod;
import thePirate.actions.PirateSFXAction;
import thePirate.patches.vfx.PirateGainPennyEffect;
import thePirate.relics.BetterOnUseGold;
import thePirate.util.TextureLoader;

public class ThiefsAccomplicePower extends AbstractPower implements CloneablePowerInterface, BetterOnUseGold{
    public AbstractCreature source;

    public static final String POWER_ID = PirateMod.makeID(ThiefsAccomplicePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    public ThiefsAccomplicePower(final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.source = AbstractDungeon.player;

        type = PowerType.BUFF;
        isTurnBased = false;
        Texture tex84 = TextureLoader.getPowerTexture(this, 84);
        Texture tex32 = TextureLoader.getPowerTexture(this, 32);

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


    @Override
    public void onLoseGold(int gold) {
        if(gold > owner.gold){
            gold = owner.gold;
        }
        if (gold > 0){
            int thisGold = gold;
            addToTop(new GainGoldAction(gold));
            addToTop(new ReducePowerAction(owner, owner, POWER_ID,1));
            if (!PirateMod.disableMonkeySFX.toggle.enabled){
                addToTop(new PirateSFXAction("MONKEY_1"));
            }

            Hitbox goldHb = AbstractDungeon.topPanel.goldHb;
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    for(int i = 0; i < thisGold; ++i) {
                        AbstractDungeon.effectList.add(new PirateGainPennyEffect(null, owner.hb.cX, owner.hb.cY, goldHb.cX, goldHb.cY, false));
                    }
                    isDone = true;

                }
            });
        }
    }

    @Override
    public void onSpendGold(int gold) {
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ThiefsAccomplicePower(amount);
    }

}
