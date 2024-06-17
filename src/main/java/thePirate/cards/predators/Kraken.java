package thePirate.cards.predators;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.lures.AbstractLure;
import thePirate.cards.lures.BatheInBlood;
import thePirate.characters.ThePirate;
import thePirate.powers.InkPower;

import static thePirate.PirateMod.isHardcore;
import static thePirate.PirateMod.makeCardPath;

public class Kraken extends AbstractPredator{

    // STAT DECLARATION

    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardTarget HC_TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;
    private static final int MAGIC = 10;
    private static final int UPGRADED_MAGIC = 5;
    private static final int SECOND_MAGIC = 1;
    private static final int UPGRADED_SECOND_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Kraken.class.getSimpleName());
    public static final String IMG = makeCardPath("Kraken.png", TYPE);
    // /TEXT DECLARATION/

    public Kraken(){
        this(true);
    }
    public Kraken(boolean showPreview) {
        super(ID, IMG, COST, TYPE, COLOR, TARGET, showPreview, isHardcore());
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
        if (hardcore)
            this.target = HC_TARGET;
        exhaust = true;
    }




    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!hardcore){
            addToBot(new StunMonsterAction(m,p, secondMagic));
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                if (!mo.isDeadOrEscaped() && !mo.equals(m)) {
                    this.addToBot(new ApplyPowerAction(mo,p,new InkPower(mo,p,magicNumber),magicNumber));
                }
            }
        }else {
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                if (!mo.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(mo,p,new InkPower(mo,p,magicNumber),magicNumber));
                }
            }
        }
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

    //@Override
    public AbstractLure getLure() {
        return new BatheInBlood(false);
    }
}
