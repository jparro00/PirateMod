package thePirate.cards.predators;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.lures.AbstractLure;
import thePirate.cards.lures.LowerDefenses;
import thePirate.characters.ThePirate;
import thePirate.powers.ElectricEelPower;

import static thePirate.PirateMod.makeCardPath;

public class ElectricEel extends AbstractPredator {


    // STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADED_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(ElectricEel.class.getSimpleName());
    public static final String IMG = makeCardPath("ElectricEel.png", TYPE);
    // /TEXT DECLARATION/

    public ElectricEel(){
        this(true);
    }
    public ElectricEel(boolean showPreview) {
        super(ID, IMG, COST, TYPE, COLOR, TARGET, showPreview);
        magicNumber = baseMagicNumber = MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p,p,new ElectricEelPower(magicNumber),magicNumber));
    }

    @Override
    public AbstractLure getLure() {
        return new LowerDefenses(false);
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_MAGIC > 0)
                upgradeMagicNumber(UPGRADED_MAGIC);
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }
}
