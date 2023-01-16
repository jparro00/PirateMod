package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.StartTurnDigPower;

import static thePirate.PirateMod.makeCardPath;

public class HeadToShore extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(HeadToShore.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("HeadToShore.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;

    // /STAT DECLARATION/


    public HeadToShore() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DiscardAction(p, p, 1, false));
        this.addToBot(new ApplyPowerAction(p, p, new StartTurnDigPower(1),1));
        if (upgraded)
            addToBot(new DrawCardAction(1));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
            initializeDescription();
        }
    }
}
