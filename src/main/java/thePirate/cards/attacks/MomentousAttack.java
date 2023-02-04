package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.patches.actions.CardCounterPatches;

import static thePirate.PirateMod.makeCardPath;

public class MomentousAttack extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;
    private static final int COST = 2;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 1;
    public static final int ADDITIONAL_DAMAGE = 7;
    public static final int UPGRADE_ADDITIONAL_DAMAGE = 2;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(MomentousAttack.class.getSimpleName());
    public static final String IMG = makeCardPath("MomentousAttack.png", TYPE);
    // /TEXT DECLARATION/




    public MomentousAttack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = ADDITIONAL_DAMAGE;
    }

    @Override
    public void applyPowers() {
        PirateMod.logger.info("enter applyPowers()");
        int realBaseDamage = this.baseDamage;
        AbstractPlayer p = AbstractDungeon.player;
        int additionalDamage = 0;
        if(p != null){
            additionalDamage = CardCounterPatches.energyUsedThisTurn * magicNumber;
        }
        this.baseDamage += additionalDamage;
        super.applyPowers();
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;

    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;

        AbstractPlayer p = AbstractDungeon.player;
        int additionalDamage = 0;
        if(p != null){
            additionalDamage = CardCounterPatches.energyUsedThisTurn * magicNumber;
        }

        this.baseDamage += additionalDamage;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_ADDITIONAL_DAMAGE);
            upgradeDescription();
        }
    }
}
