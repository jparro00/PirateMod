package thePirate.cards.predators;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.PlayCardAction;
import thePirate.cards.lures.AbstractLure;
import thePirate.cards.lures.LowerDefenses;
import thePirate.characters.ThePirate;
import thePirate.powers.OnBury;

import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class ElectricEel extends AbstractPredator implements OnBury {


    // STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADED_MAGIC = 1;
    public static final int SECOND_MAGIC = 1;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(ElectricEel.class.getSimpleName());
    public static final String IMG = makeCardPath("ElectricEel.png", TYPE);
    // /TEXT DECLARATION/

    public ElectricEel(){
        this(true);
    }
    public ElectricEel(boolean showPreview) {
        super(ID, IMG, COST, TYPE, COLOR, TARGET, showPreview);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(magicNumber));
        addToBot(new DrawCardAction(secondMagic));
    }

    @Override
    public AbstractLure getLure() {
        return new LowerDefenses(false);
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

    @Override
    public void onBury(AbstractCard card) {
        if(this.equals(card)){
            addToTop(new PlayCardAction(AbstractDungeon.player.discardPile,this,null,false));
        }
    }

    @Override
    public void onBuryCards(List<AbstractCard> cards) {

    }
}
