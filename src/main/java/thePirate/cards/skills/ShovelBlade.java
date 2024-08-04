package thePirate.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.DigAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.isHardcore;
import static thePirate.PirateMod.makeCardPath;

public class ShovelBlade extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADED_MAGIC = 1;
    private static final int SECOND_MAGIC = 0;
    private static final int UPGRADED_SECOND_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(ShovelBlade.class.getSimpleName());
    public static final String IMG = makeCardPath(ShovelBlade.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public ShovelBlade() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, isHardcore());
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
        if (!hardcore)
            selfRetain = true;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DigAction(magicNumber, true));
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
