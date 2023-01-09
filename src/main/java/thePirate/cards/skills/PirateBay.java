package thePirate.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.PirateBayAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class PirateBay extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADED_MAGIC = 0;
    private static final int SECOND_MAGIC = 0;
    private static final int UPGRADED_SECOND_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(PirateBay.class.getSimpleName());
    public static final String IMG = makeCardPath("PirateBay.png", TYPE);
    // /TEXT DECLARATION/

    public PirateBay() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PirateBayAction(magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            if (UPGRADED_MAGIC > 0)
                upgradeMagicNumber(UPGRADED_MAGIC);
            if (UPGRADED_SECOND_MAGIC > 0)
                upgradeSecondMagic(UPGRADED_SECOND_MAGIC);
            upgradeDescription();
        }
    }
}
