package thePirate.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.CephalopodFormPower;

import static thePirate.PirateMod.makeCardPath;

public class CephalopodForm extends AbstractDynamicCard {


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 3;
    private static final int UPGRADED_COST = 3;
    private static final int MAGIC = 1;
    private static final int UPGRADED_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(CephalopodForm.class.getSimpleName());
    public static final String IMG = makeCardPath("CephalopodForm.png", TYPE);
    // /TEXT DECLARATION/

    public CephalopodForm() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        isEthereal = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p,p,new CephalopodFormPower(magicNumber), magicNumber));
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
            isEthereal = false;
            upgradeDescription();
        }
    }
}
