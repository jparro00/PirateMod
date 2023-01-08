package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.DefaultMod;
import thePirate.actions.DigAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.TheDefault;

import static thePirate.DefaultMod.makeCardPath;

public class Dig extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Dig.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Dig.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;
    public static final int UPGRADE_DRAW = 1;
    public static final int BASE_DRAW = 0;
    public static final int BASE_DIG = 1;


    private static final int COST = 1;

    // /STAT DECLARATION/


    public Dig() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = BASE_DIG;
        this.secondMagic = baseSecondMagic = BASE_DRAW;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToTop(new DigAction(this.magicNumber));
        this.addToBot(new DrawCardAction(this.baseSecondMagic));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagic(UPGRADE_DRAW);
            upgradeDescription();
        }
    }
}
