package thePirate.cards.attacks;

import basemod.ReflectionHacks;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static thePirate.PirateMod.makeCardPath;

public class Retaliation extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 0;
    private static final int UPGRADED_DMG = 0;

    private AbstractMonster targetMonster;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Retaliation.class.getSimpleName());
    public static final String IMG = makeCardPath(Retaliation.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public Retaliation() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 1;
    }

/*
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = false;
        if (m == null){
            targetMonster = null;
            rawDescription = languagePack.getCardStrings(ID).DESCRIPTION;
            initializeDescription();
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                if(mo.getIntentBaseDmg() >= 0){
                    canUse = true;
                    break;
                }
            }
        }else {
            canUse = m.getIntentBaseDmg() >= 0;
        }
        return canUse;
    }
*/

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if(!m.isDeadOrEscaped() && m.getIntentBaseDmg() >= 0 ){
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }

    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> toolTips = new ArrayList<>();
        String title = languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[2];
        String desc = languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[3];
        toolTips.add(new TooltipInfo(title, desc));
        return toolTips;
    }

    @Override
    public void applyPowers() {
        calculateCardDamage(null);
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {

        //update description with damage amount based on enemy intent
        if (m != targetMonster){
            targetMonster = m;
            if (m != null && m.getIntentBaseDmg() >=0){
                int multiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                if (multiAmt > 0){
                    rawDescription = languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[0];
                }else {
                    rawDescription = languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[1];
                }
            }else {
                rawDescription = languagePack.getCardStrings(ID).DESCRIPTION;
            }
            initializeDescription();
        }
        if(m != null && m.getIntentBaseDmg() >= 0) {
            baseDamage = m.getIntentBaseDmg();

            int multiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
            if (multiAmt > 0) {
                baseMagicNumber = multiAmt;
                magicNumber = baseMagicNumber;
                upgradedMagicNumber = true;
            }else {
                baseMagicNumber = 1;
                magicNumber = baseMagicNumber;
                upgradedMagicNumber = false;
            }

            damage = m.getIntentDmg();
            //special case for intangible
            if (m.hasPower(IntangiblePower.POWER_ID)){
                damage = 1;
            }
            isDamageModified = baseDamage != damage;

        }else {
            baseDamage = 0;
            isDamageModified = false;
            baseMagicNumber = 0;
            magicNumber = baseMagicNumber;
            upgradedMagicNumber = false;
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(m != null){
            for (int i = 0; i < magicNumber; i++){
                addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            }

        }

        //null out targetMonster so description is correct in discard pile
        targetMonster = null;
        rawDescription = languagePack.getCardStrings(ID).DESCRIPTION;
        initializeDescription();
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_DMG > 0)
                upgradeDamage(UPGRADED_DMG);
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }
}
