package thePirate.powers;

import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.RunicDome;
import com.megacrit.cardcrawl.vfx.BobEffect;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makePowerPath;


public class InkPower extends AbstractPower implements CloneablePowerInterface, OnAttackToChangeDamagePreBlock {
    public AbstractCreature source;

    public static final String POWER_ID = PirateMod.makeID("InkPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public BobEffect bobEffect;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    // There's a fallback "missing texture" image, so the game shouldn't crash if you accidentally put a non-existent file.
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath(InkPower.class.getSimpleName()+"_84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath(InkPower.class.getSimpleName()+"_32.png"));
    private static final Texture purpleArrow = TextureLoader.getTexture(makePowerPath("ink_intent.png"));

    public InkPower(final AbstractCreature owner, final AbstractCreature source, final int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;
        this.source = source;
        this.bobEffect = new BobEffect(5,2);

        type = PowerType.DEBUFF;
        isTurnBased = false;

        // We load those txtures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        if (this.amount >= 999) {
            this.amount = 999;
        }

        updateDescription();
    }

    @Override
    public int onAttackToChangeDamagePreBlock(DamageInfo info, int damageAmount) {
        if(info.type.equals(DamageInfo.DamageType.NORMAL)){
            AbstractPlayer player = AbstractDungeon.player;

            int output = info.output;

            int playerBlock = AbstractDungeon.player.currentBlock;
            int damageBack = 0;
            int tmpAmount = amount;
            int originalAmount = amount;


            if (damageAmount < 0)
                damageAmount = 0;
            if (damageAmount > 1 && player.hasPower("IntangiblePlayer"))
                damageAmount = 1;

            if(tmpAmount - damageAmount <= 0){
                damageBack = tmpAmount;
                damageAmount -= tmpAmount;
                tmpAmount = 0;
            } else {
                tmpAmount -= damageAmount;
                damageBack = originalAmount - tmpAmount;
                damageAmount = 0;
            }

            if (owner.hasPower(ChemicalWarfarePower.POWER_ID)){
                damageBack *= ChemicalWarfarePower.DAMAGE_MODIFIER;
            }

            if (damageBack > 0)
                this.addToTop(new DamageAction(info.owner, new DamageInfo(AbstractDungeon.player, damageBack, DamageInfo.DamageType.THORNS)));

            if(tmpAmount != originalAmount && !AbstractDungeon.player.hasPower(VolatileInkPower.POWER_ID) && !owner.hasPower(TropomyosinPower.POWER_ID)){
                this.addToTop(new ReducePowerAction(info.owner, info.owner, this.ID, originalAmount - tmpAmount));
            }

        }

        return damageAmount;
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        if (!AbstractDungeon.player.hasRelic(RunicDome.ID)){
            renderInkIntent(sb);
        }
    }

    public void renderInkIntent(SpriteBatch sb){
        sb.setColor(Color.WHITE);
        AbstractMonster m = (AbstractMonster)owner;
        int intentDmg = (Integer)ReflectionHacks.getPrivate(m,AbstractMonster.class,"intentDmg");
        Color color = ReflectionHacks.getPrivate(m,AbstractMonster.class,"intentColor");
        sb.setColor(color);
        if (m.getIntentBaseDmg() > 0 && !m.isDeadOrEscaped()){
            int blockedDmg;
            int passThroughDmg;
            int dmg = intentDmg;
            if (m.hasPower(TropomyosinPower.POWER_ID) || AbstractDungeon.player.hasPower(VolatileInkPower.POWER_ID)){
                if ((Boolean)ReflectionHacks.getPrivate(m,AbstractMonster.class,"isMultiDmg")){
                    int intentMultiDmg = (Integer)ReflectionHacks.getPrivate(m,AbstractMonster.class,"intentMultiAmt");
                    if (dmg >= amount){
                        blockedDmg = amount;
                    }
                    else {
                        blockedDmg = dmg;
                    }
                    blockedDmg *= intentMultiDmg;

                    passThroughDmg = (dmg - amount) * intentMultiDmg;
                    if (passThroughDmg < 0)
                        passThroughDmg = 0;
                }else {
                    passThroughDmg = dmg - amount;
                    if (passThroughDmg < 0)
                        passThroughDmg = 0;
                    if (dmg >= amount){
                        blockedDmg = amount;
                    }
                    else {
                        blockedDmg = dmg;
                    }
                }

            }else{
                if ((Boolean)ReflectionHacks.getPrivate(m,AbstractMonster.class,"isMultiDmg")){
                    int intentMultiDmg = (Integer)ReflectionHacks.getPrivate(m,AbstractMonster.class,"intentMultiAmt");
                    dmg = intentMultiDmg * intentDmg;
                }
                passThroughDmg = dmg - amount;
                if (passThroughDmg < 0)
                    passThroughDmg = 0;
                if (dmg >= amount){
                    blockedDmg = amount;
                }
                else {
                    blockedDmg = dmg;
                }
            }

            bobEffect.update();
            sb.draw(purpleArrow, m.intentHb.cX - (128 * Settings.scale), m.intentHb.cY - (64 * Settings.scale) + bobEffect.y, 0F, 0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0, 0, 0, 128, 128, false, false);
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(passThroughDmg),m.intentHb.cX - (30.0F+64) * Settings.scale, m.intentHb.cY + bobEffect.y - 12.0F * Settings.scale, color);
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(blockedDmg),m.intentHb.cX - (48) * Settings.scale, m.intentHb.cY + bobEffect.y + 30.0F * Settings.scale, color);
        }
    }


    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {

    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= 999) {
            this.amount = 999;
        }
    }
    // Note: If you want to apply an effect when a power is being applied you have 3 options:
    //onInitialApplication is "When THIS power is first applied for the very first time only."
    //onApplyPower is "When the owner applies a power to something else (only used by Sadistic Nature)."
    //onReceivePowerPower from StSlib is "When any (including this) power is applied to the owner."


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(AbstractDungeon.player.hasPower(VolatileInkPower.POWER_ID) || owner.hasPower(TropomyosinPower.POWER_ID)){
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }

    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new InkPower(owner, source, amount);
    }

}
