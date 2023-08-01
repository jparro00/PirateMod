package thePirate.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnCreateBlockInstancePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import java.util.HashSet;

public class SaboteurPower extends AbstractPower implements CloneablePowerInterface, OnCreateBlockInstancePower {
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

    @Override
    public void onCreateBlockInstance(HashSet<AbstractBlockModifier> hashSet, Object o) {
        AbstractCreature owner = this.owner;
        String powerId = this.ID;
        boolean appliedThisTurn = this.appliedThisTurn;

        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        int blockToRemove = owner.currentBlock;
                        if (appliedThisTurn && owner.hasPower(BarricadePower.POWER_ID)){
                            blockToRemove -= monsterInitialBlock;
                        }
                        if(blockToRemove > 0){
                            this.addToBot(new LoseBlockAction(owner, AbstractDungeon.player, blockToRemove));
                            this.addToBot(new ReducePowerAction(owner, owner, powerId, 1));
                        }
                        isDone = true;
                    }
                });
                isDone = true;
            }
        });

    }
}
