package thePirate.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makePowerPath;

public class SaboteurPower extends AbstractPower implements CloneablePowerInterface {
    public AbstractCreature source;

    public static final String POWER_ID = PirateMod.makeID("SabateurPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    private boolean appliedThisTurn;
    private int monsterInitialBlock;

    public SaboteurPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;
        Texture tex84 = TextureLoader.getPowerTexture(this, 84);
        Texture tex32 = TextureLoader.getPowerTexture(this, 32);

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        appliedThisTurn = true;
        monsterInitialBlock = owner.currentBlock;

        updateDescription();
    }

    @Override
    public void update(int slot){
        //TODO: this is removing block immediately sometimes
        if(appliedThisTurn && owner.currentBlock > monsterInitialBlock){
            this.addToBot(new LoseBlockAction(owner, AbstractDungeon.player, owner.currentBlock - monsterInitialBlock));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID ));
        }else if(owner.currentBlock >0) {
            this.addToBot(new LoseBlockAction(owner, AbstractDungeon.player, owner.currentBlock));
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID ));
        }

    }
    @Override
    public void onGainedBlock(float blockAmount) {
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        appliedThisTurn = false;
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
        return new SaboteurPower(owner, source, amount);
    }
}
