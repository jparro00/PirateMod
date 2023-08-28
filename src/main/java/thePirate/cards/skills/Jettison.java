package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thePirate.PirateMod;
import thePirate.actions.BuryAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class Jettison extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 1;
    private static final int SECOND_MAGIC = 1;
    private static final int UPGRADED_SECOND_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Jettison.class.getSimpleName());
    public static final String IMG = makeCardPath(Jettison.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public Jettison() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        BuryAction buryAction = new BuryAction(magicNumber,true);
        addToBot(buryAction);
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int tmpStrengthGain = 0;
                for (int cardCost : buryAction.costOfBuriedCards){
                    if (cardCost == -2){
                        addToBot(new ApplyPowerAction(p,p,new StrengthPower(p,secondMagic), secondMagic));
                    }else if (cardCost == -1){
                        tmpStrengthGain += EnergyPanel.totalCount;
                    }else {
                        tmpStrengthGain += cardCost;
                    }
                }
                if (tmpStrengthGain > 0){
                    this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, tmpStrengthGain), tmpStrengthGain));
                    this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, tmpStrengthGain), tmpStrengthGain));
                }
                isDone = true;
            }
        });

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
