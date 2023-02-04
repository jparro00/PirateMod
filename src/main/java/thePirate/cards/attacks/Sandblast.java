package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.patches.actions.CardCounterPatches;

import static thePirate.PirateMod.makeCardPath;

public class Sandblast extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 3;
    private static final int UPGRADED_COST = 3;

    private static final int DAMAGE = 12;
    private static final int UPGRADED_DMG = 0;

    public static final int MAGIC = 2;
    public static final int UPGRADED_MAGIC = 1;


    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Sandblast.class.getSimpleName());
    public static final String IMG = makeCardPath(Sandblast.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public Sandblast() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void applyPowers() {
        setCostForTurn(cost - CardCounterPatches.cardsBuriedThisTurn);
        super.applyPowers();
    }

    public void atTurnStart() {
        this.resetAttributes();
        this.applyPowers();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new ApplyPowerAction(m,p,new WeakPower(m,magicNumber,false),magicNumber));
        addToBot(new ApplyPowerAction(m,p,new VulnerablePower(m,magicNumber,false),magicNumber));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_DMG > 0)
                upgradeDamage(UPGRADED_DMG);
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            if (UPGRADED_MAGIC != 0){
                upgradeMagicNumber(UPGRADED_MAGIC);
            }
            upgradeDescription();
        }
    }

}
