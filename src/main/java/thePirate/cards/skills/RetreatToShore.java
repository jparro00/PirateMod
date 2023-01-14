package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.BuryAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class RetreatToShore extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 0;
    private static final int BLOCK = 6;
    private static final int UPGRADED_BLOCK= 3;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(RetreatToShore.class.getSimpleName());
    public static final String IMG = makeCardPath("RetreatToShore.png", TYPE);
    // /TEXT DECLARATION/

    public RetreatToShore() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        block = baseBlock = BLOCK;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p,block));
        addToBot(new BuryAction(magicNumber, false));
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
            upgradeBlock(UPGRADED_BLOCK);
            upgradeDescription();
        }
    }
}
