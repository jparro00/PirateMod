package thePirate.cards.attacks;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.PerfectedStrike;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.DefaultMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.TheDefault;
import thePirate.patches.characters.AbstractPlayerPatch;

import static thePirate.DefaultMod.makeCardPath;

public class MomentousAttack extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;
    private static final int COST = 2;
    private static final int DAMAGE = 9;
    public static final int ADDITIONAL_DAMAGE = 5;
    public static final int UPGRADE_ADDITIONAL_DAMAGE = 2;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = DefaultMod.makeID(MomentousAttack.class.getSimpleName());
    public static final String IMG = makeCardPath("MomentousAttack.png", TYPE);
    // /TEXT DECLARATION/




    public MomentousAttack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = ADDITIONAL_DAMAGE;
    }

    @Override
    public void applyPowers() {
        DefaultMod.logger.info("enter applyPowers()");
        int realBaseDamage = this.baseDamage;
        AbstractPlayer p = AbstractDungeon.player;
        int additionalDamage = 0;
        if(p != null){
            additionalDamage = AbstractPlayerPatch.energyUsedThisTurn.get(p) * magicNumber;
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
            additionalDamage = AbstractPlayerPatch.energyUsedThisTurn.get(p) * magicNumber;
        }

        this.baseDamage += additionalDamage;
        super.calculateCardDamage(mo);
        this.baseDamage = realBaseDamage;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void atTurnStart() {
        DefaultMod.logger.info("enter atTurnStart");
        AbstractPlayer p = AbstractDungeon.player;
        if(p != null){
            DefaultMod.logger.info("resetting energyUsed this turn to 0");
            AbstractPlayerPatch.energyUsedThisTurn.set(p, 0);
        }
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
            upgradeMagicNumber(UPGRADE_ADDITIONAL_DAMAGE);
            upgradeDescription();
        }
    }
}
