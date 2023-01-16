package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.InkPower;

import static thePirate.PirateMod.makeCardPath;

public class Tropomyosin extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    public static final int STRENGTH = -1;
    public static final int UPGRADE_STRENGTH = -1;
    public static final int INK = 2;
    public static final int UPGRADE_INK = 2;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Tropomyosin.class.getSimpleName());
    public static final String IMG = makeCardPath("Tropomyosin.png", TYPE);
    // /TEXT DECLARATION/

    public Tropomyosin() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = STRENGTH;
        baseSecondMagic = secondMagic = INK;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, magicNumber),magicNumber));
        this.addToBot(new ApplyPowerAction(m, p, new InkPower(m, p, secondMagic), secondMagic));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if(UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeMagicNumber(UPGRADE_STRENGTH);
            upgradeSecondMagic(UPGRADE_INK);
            upgradeDescription();
        }
    }
}
