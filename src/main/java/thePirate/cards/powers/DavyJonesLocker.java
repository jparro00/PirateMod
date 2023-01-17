package thePirate.cards.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.Purgable;
import thePirate.characters.ThePirate;
import thePirate.powers.DavyJonesLockerPower;

import static thePirate.PirateMod.makeCardPath;

public class DavyJonesLocker extends AbstractDynamicCard implements Purgable {


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.POWER;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 1;
    private static final int UPGRADED_MAGIC = 1;
    private boolean purge;
    public boolean queuedForPurge;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(DavyJonesLocker.class.getSimpleName());
    public static final String IMG = makeCardPath("DavyJonesLocker.png", TYPE);
    // /TEXT DECLARATION/

    public DavyJonesLocker() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        tags.add(CardTags.HEALING);
    }



    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(upgraded){
            this.addToTop(new AddCardToDeckAction(new DavyJonesLocker()));
        }
        this.setPurge(true);
        this.addToBot(new ApplyPowerAction(p,p,new DavyJonesLockerPower(p,p,magicNumber),magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_MAGIC != MAGIC)
                upgradeMagicNumber(UPGRADED_MAGIC);
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            this.cardsToPreview = new DavyJonesLocker();
            upgradeDescription();
        }
    }
    @Override
    public void setPurge(boolean purge) {
        this.purge = purge;

    }

    @Override
    public boolean getPurge() {
        return purge;
    }

    @Override
    public boolean queuedForPurge() {
        return queuedForPurge;
    }

    @Override
    public void setQueuedForPurge(boolean queuedForPurge) {
        this.queuedForPurge = queuedForPurge;
    }
}
