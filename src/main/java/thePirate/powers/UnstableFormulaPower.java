package thePirate.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

public class UnstableFormulaPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = PirateMod.makeID(UnstableFormulaPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public UnstableFormulaPower(final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = AbstractDungeon.player;
        this.amount = amount;
        this.source = AbstractDungeon.player;

        type = PowerType.BUFF;
        isTurnBased = false;

        // We load those txtures here.
        Texture tex84 = TextureLoader.getPowerTexture(this, 84);
        Texture tex32 = TextureLoader.getPowerTexture(this, 32);

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean flash = false;
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            AbstractPower power = mo.getPower(InkPower.POWER_ID);
            if (power != null && power.amount > 0){
                int newAmount = power.amount * this.amount;
                addToBot(new ApplyPowerAction(mo,owner,new InkPower(mo, owner,newAmount)));
                flash = true;
            }
        }
        if (flash)
            flash();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + "#b" +amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new UnstableFormulaPower(amount);
    }
}
