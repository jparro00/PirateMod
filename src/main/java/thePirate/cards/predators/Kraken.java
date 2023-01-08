package thePirate.cards.predators;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.DefaultMod;
import thePirate.cards.lures.AbstractLure;
import thePirate.cards.lures.BatheInBlood;
import thePirate.characters.TheDefault;
import thePirate.powers.InkPower;

import java.util.Iterator;

import static thePirate.DefaultMod.makeCardPath;

public class Kraken extends AbstractPredator{

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;
    private static final int MAGIC = 10;
    private static final int UPGRADED_MAGIC = 5;
    private static final int SECOND_MAGIC = 1;
    private static final int UPGRADED_SECOND_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = DefaultMod.makeID(Kraken.class.getSimpleName());
    public static final String IMG = makeCardPath("Kraken.png", TYPE);
    // /TEXT DECLARATION/

    public Kraken(){
        this(true);
    }
    public Kraken(boolean showPreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, showPreview);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
    }




    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StunMonsterAction(m,p, secondMagic));
        Iterator var3 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        AbstractMonster mo;
        while(var3.hasNext()) {
            mo = (AbstractMonster)var3.next();
            if (!mo.isDeadOrEscaped()) {
                this.addToBot(new ApplyPowerAction(m,p,new InkPower(m,p,magicNumber),magicNumber));
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
