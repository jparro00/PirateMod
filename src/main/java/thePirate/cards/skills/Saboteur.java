package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.PirateSFXAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.SaboteurPower;

import java.util.Iterator;

import static thePirate.PirateMod.makeCardPath;

public class Saboteur extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;
    private static final int MAGIC = 0;
    private static final int UPGRADED_MAGIC = 0;
    private static final int SECOND_MAGIC = 0;
    private static final int UPGRADED_SECOND_MAGIC = 0;
    public static final int BLOCK = 12;
    public static final int UPGRADED_BLOCK = 3;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Saboteur.class.getSimpleName());
    public static final String IMG = makeCardPath("Saboteur.png", TYPE);
    // /TEXT DECLARATION/

    public Saboteur() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
        block = baseBlock = BLOCK;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!PirateMod.disableMonkeySFX.toggle.enabled){
            addToBot(new PirateSFXAction("MONKEY_2"));
        }
        addToBot(new GainBlockAction(p, block));
        if(target == CardTarget.ALL_ENEMY){
            Iterator iter = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            while(iter.hasNext()) {
                AbstractMonster mo = (AbstractMonster)iter.next();
                if (!mo.isDeadOrEscaped()) {
                    this.addToBot(new ApplyPowerAction(mo, p, new SaboteurPower(mo, p, 1),1));
                }
            }
        }else {
            this.addToBot(new ApplyPowerAction(m, p, new SaboteurPower(m, p, 1),1));
        }

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADED_BLOCK);
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            if (UPGRADED_MAGIC > 0)
                upgradeMagicNumber(UPGRADED_MAGIC);
            if (UPGRADED_SECOND_MAGIC > 0)
                upgradeSecondMagic(UPGRADED_SECOND_MAGIC);
            this.target = CardTarget.ALL_ENEMY;
            upgradeDescription();
        }
    }
}
