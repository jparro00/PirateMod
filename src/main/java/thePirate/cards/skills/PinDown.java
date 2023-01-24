package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class PinDown extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 2;
    private static final int SECOND_MAGIC = 0;
    private static final int UPGRADED_SECOND_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(PinDown.class.getSimpleName());
    public static final String IMG = makeCardPath(PinDown.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public PinDown() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
        this.cardsToPreview = new ShovelBlade();
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m,p,new WeakPower(m,magicNumber,false),magicNumber));
        addToBot(new MakeTempCardInHandAction(this.cardsToPreview.makeStatEquivalentCopy(), 1));

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
