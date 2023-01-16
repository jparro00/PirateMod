package thePirate.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makePowerPath;


public class InkPower extends AbstractPower implements CloneablePowerInterface, OnAttackToChangeDamagePreBlock {
    public AbstractCreature source;

    public static final String POWER_ID = PirateMod.makeID("InkPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath(InkPower.class.getSimpleName()+"_84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath(InkPower.class.getSimpleName()+"_32.png"));

    public InkPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }


/*    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount){

        AbstractPlayer player = AbstractDungeon.player;
        int output = info.output;
        damageAmount = output;
        int playerBlock = AbstractDungeon.player.currentBlock;
        int damageBack = 0;
        int originalAmount = amount;


        if (damageAmount < 0)
            damageAmount = 0;
        if (damageAmount > 1 && player.hasPower("IntangiblePlayer"))
            damageAmount = 1;

        if(amount - damageAmount <= 0){
            damageBack = amount;
            damageAmount -= amount;
            amount = 0;
        } else {
            amount -= damageAmount;
            damageBack = originalAmount - amount;
            damageAmount = 0;
        }

        if (damageAmount > 0){
            damageAmount -= playerBlock;
        }

        if (owner.hasPower(ChemicalWarfarePower.POWER_ID)){
            damageBack *= ChemicalWarfarePower.DAMAGE_MODIFIER;
        }

        if (damageBack > 0)
            this.addToBot(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, damageBack, DamageInfo.DamageType.THORNS)));

        if(amount != originalAmount && !AbstractDungeon.player.hasPower(VolatileInkPower.POWER_ID)){
            this.addToTop(new ReducePowerAction(info.owner, info.owner, this.ID, originalAmount - amount));
        }

        return damageAmount;

*//* old code
        DefaultMod.logger.info("enter onAttackToChangeDamage()");
        DefaultMod.logger.info("damageAmount: " + damageAmount);
        DefaultMod.logger.info("player block: " + AbstractDungeon.player.currentBlock);

        DefaultMod.logger.info("base: " + info.base);
        DefaultMod.logger.info("output: " + info.output);
        DefaultMod.logger.info("isModified: " + info.isModified);
        int damageBack;
        int damageBlocked;
        int amountLeft;
        int baseDamage = damageAmount;
        if(amount - baseDamage <= 0){
            damageBack = damageBlocked = amount;
            amountLeft = 0;
        }else{
            damageBack = damageBlocked = baseDamage;
            amountLeft = amount - baseDamage;
        }
        DefaultMod.logger.info("before check damageBack = " + damageBack);
        if (owner.hasPower(ChemicalWarfarePower.POWER_ID)){
            DefaultMod.logger.info("damageBack = " + damageBack);
            damageBack *= ChemicalWarfarePower.DAMAGE_MODIFIER;
            DefaultMod.logger.info("after chemical warfare damageBack = " + damageBack);
        }


        this.addToBot(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, damageBack, DamageInfo.DamageType.THORNS)));
        if(!AbstractDungeon.player.hasPower(VolatileInkPower.POWER_ID)){
            this.addToTop(new ReducePowerAction(info.owner, info.owner, this.ID, amount - amountLeft));
        }
        return damageAmount - damageBlocked;
*//*


    }*/

    @Override
    public int onAttackToChangeDamagePreBlock(DamageInfo info, int damageAmount) {
        PirateMod.logger.info("enter onAttackToChangeDamagePreBlock()");
        PirateMod.logger.info("info.name: " + info.name);
        PirateMod.logger.info("info.type: " + info.type);
        PirateMod.logger.info("info.owner: " + info.owner);
        info.name = "test";
        if(info.type.equals(DamageInfo.DamageType.NORMAL)){
            AbstractPlayer player = AbstractDungeon.player;

            int output = info.output;
            //damageAmount = output;

            int playerBlock = AbstractDungeon.player.currentBlock;
            int damageBack = 0;
            int tmpAmount = amount;
            int originalAmount = amount;


            if (damageAmount < 0)
                damageAmount = 0;
            if (damageAmount > 1 && player.hasPower("IntangiblePlayer"))
                damageAmount = 1;

            PirateMod.logger.info("damageAmount: " + damageAmount);
            PirateMod.logger.info("player block: " + playerBlock);
            PirateMod.logger.info("amount: " + tmpAmount);
            PirateMod.logger.info("originalAmount: " + originalAmount );



            if(tmpAmount - damageAmount <= 0){
                PirateMod.logger.info("enter FIRST case");
                damageBack = tmpAmount;
                damageAmount -= tmpAmount;
                tmpAmount = 0;
                PirateMod.logger.info("damageAmount: " + damageAmount);
                PirateMod.logger.info("amount: " + tmpAmount);
            } else {
                PirateMod.logger.info("enter SECOND case");
                tmpAmount -= damageAmount;
                damageBack = originalAmount - tmpAmount;
                damageAmount = 0;
                PirateMod.logger.info("damageAmount: " + damageAmount);
                PirateMod.logger.info("amount: " + tmpAmount);
            }

            if (owner.hasPower(ChemicalWarfarePower.POWER_ID)){
                damageBack *= ChemicalWarfarePower.DAMAGE_MODIFIER;
            }

            if (damageBack > 0)
                this.addToTop(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, damageBack, DamageInfo.DamageType.THORNS)));

            if(tmpAmount != originalAmount && !AbstractDungeon.player.hasPower(VolatileInkPower.POWER_ID)){
                this.addToTop(new ReducePowerAction(info.owner, info.owner, this.ID, originalAmount - tmpAmount));
            }

        }

        PirateMod.logger.info("returning damageAmount: " + damageAmount);
        return damageAmount;
    }

    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {

    }

    // Note: If you want to apply an effect when a power is being applied you have 3 options:
    //onInitialApplication is "When THIS power is first applied for the very first time only."
    //onApplyPower is "When the owner applies a power to something else (only used by Sadistic Nature)."
    //onReceivePowerPower from StSlib is "When any (including this) power is applied to the owner."


    @Override
    public void atEndOfRound() {
        if(AbstractDungeon.player.hasPower(VolatileInkPower.POWER_ID)){
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }

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
        return new InkPower(owner, source, amount);
    }

}
