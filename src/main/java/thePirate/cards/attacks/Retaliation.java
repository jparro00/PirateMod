package thePirate.cards.attacks;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

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

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (m == null){
            targetMonster = null;
            rawDescription = languagePack.getCardStrings(ID).DESCRIPTION;
            initializeDescription();
        }
        return m != null && m.getIntentBaseDmg() >= 0;
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        PirateMod.logger.info("enter calculateCardDamage()");;

        //update description with damage amount based on enemy intent
        if (m != targetMonster){
            targetMonster = m;
            if (m.getIntentBaseDmg() >=0){
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
            isDamageModified = true;

            if(m != null && m.getIntentBaseDmg() >= 0){
                int multiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                if (multiAmt > 0) {
                    baseMagicNumber = multiAmt;
                    magicNumber = baseMagicNumber;
                    upgradedMagicNumber = true;
//                    initializeDescription();
                }else {
                    baseMagicNumber = 1;
                    magicNumber = baseMagicNumber;
                    upgradedMagicNumber = false;
//                    initializeDescription();
                }

            }
            PirateMod.logger.info("damage: " + damage);
            PirateMod.logger.info("isDamageModified: " + isDamageModified);
            super.calculateCardDamage(m);
            PirateMod.logger.info("after calculateCardDamage()");
            PirateMod.logger.info("damage: " + damage);
            PirateMod.logger.info("isDamageModified: " + isDamageModified);
            PirateMod.logger.info("exit calculateCardDamage()");;
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
        PirateMod.logger.info("m.getIntentBaseDmg(): " + m.getIntentBaseDmg());
        PirateMod.logger.info("m.getIntentDmg(): " + m.getIntentDmg());
        PirateMod.logger.info("damage: " + damage);
        PirateMod.logger.info("isDamageModified: " + isDamageModified);
        PirateMod.logger.info("baseDamage: " + baseDamage);
/*
        damage = m.getIntentBaseDmg();
        isDamageModified = true;
*/
//        applyPowers();
        if(m != null && m.getIntentBaseDmg() >= 0){
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
