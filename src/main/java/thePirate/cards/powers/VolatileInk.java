package thePirate.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.InkPower;
import thePirate.powers.VolatileInkPower;

import static thePirate.PirateMod.makeCardPath;

public class VolatileInk extends AbstractDynamicCard {


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;
    private static final int MAGIC = 5;
    private static final int UPGRADED_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(VolatileInk.class.getSimpleName());
    public static final String IMG = makeCardPath("VolatileInk.png", TYPE);
    // /TEXT DECLARATION/

    public VolatileInk() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for( AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            this.addToBot(new ApplyPowerAction(mo, p, new InkPower(mo, p, magicNumber), magicNumber));
        }
        addToBot(new ApplyPowerAction(p,p,new VolatileInkPower(1),0));
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
