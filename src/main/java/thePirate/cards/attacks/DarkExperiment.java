package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.DarkExperimentAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.InkPower;

import static thePirate.PirateMod.makeCardPath;

public class DarkExperiment extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 10;
    private static final int UPGRADED_DMG = 0;
    public static final int  MAGIC = 4;
    public static final int UPGRADED_MAGIC = 0;
    public static final int MAGIC_INCREMENT_AMOUNT = 4;
    public static final int UPGRADED_INCREMENT_AMOUNT = 1;
    public boolean modified;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(DarkExperiment.class.getSimpleName());
    public static final String IMG = makeCardPath("DarkExperiment.png", TYPE);
    // /TEXT DECLARATION/

    public DarkExperiment() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.misc = baseMagicNumber = magicNumber = MAGIC;
        secondMagic = baseSecondMagic = MAGIC_INCREMENT_AMOUNT;
        this.baseDamage = this.damage = DAMAGE;
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m,p,new InkPower(m,p,magicNumber),magicNumber));
        addToBot(new DarkExperimentAction(m,new DamageInfo(p,damage,damageTypeForTurn),secondMagic, this.uuid));

    }

    public void applyPowers() {
        baseMagicNumber = magicNumber = misc;
        super.applyPowers();
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        DarkExperiment darkExperiment = new DarkExperiment();
        darkExperiment.baseMagicNumber = darkExperiment.magicNumber = darkExperiment.misc = this.magicNumber;
        return darkExperiment;
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
            if(UPGRADED_MAGIC > 0)
                upgradeMagicNumber(UPGRADED_MAGIC);
            upgradeSecondMagic(UPGRADED_INCREMENT_AMOUNT);
            upgradeDescription();
        }
    }
}
