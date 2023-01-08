package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.DefaultMod;
import thePirate.actions.BuryAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.TheDefault;

import static thePirate.DefaultMod.makeCardPath;

public class Bury extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Bury.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    //TODO: JP: replace this at some point with actual image
    public static final String IMG = makeCardPath(Bury.class.getSimpleName()+".png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    public static final int BURY = 1;
    public static final int DRAW = 1;
    public static final int UPGRADED_DRAW = 1;

    // /STAT DECLARATION/


    public Bury() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURY;
        secondMagic = baseSecondMagic = DRAW;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new BuryAction(this.magicNumber, true));
        this.addToBot(new DrawCardAction(this.secondMagic));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagic(UPGRADED_DRAW);
            upgradeDescription();
            initializeDescription();
        }
    }
}
