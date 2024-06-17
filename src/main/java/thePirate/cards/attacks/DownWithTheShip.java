package thePirate.cards.attacks;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.PirateMod;
import thePirate.actions.DownWithTheShipAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class DownWithTheShip extends AbstractDynamicCard {

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 2;
    private static final int UPGRADED_DMG = 1;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(DownWithTheShip.class.getSimpleName());
    public static final String IMG = makeCardPath("DownWithTheShip.png", TYPE);
    // /TEXT DECLARATION/

    public DownWithTheShip() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, PirateMod.isHardcore());
        baseDamage = DAMAGE;
        exhaust = true;
    }

    public void applyPowers() {
        if (!hardcore){
            super.applyPowers();
        }else{
            AbstractPower strength = AbstractDungeon.player.getPower("Strength");
            int originalStrength = 0;
            if (strength != null) {
                originalStrength = strength.amount;
                strength.amount = 0;
            }
            super.applyPowers();
            if (strength != null) {
                strength.amount = originalStrength;
            }
        }

    }

    public void calculateCardDamage(AbstractMonster mo) {
        if (!hardcore){
            super.calculateCardDamage(mo);
        }else{
            AbstractPower strength = AbstractDungeon.player.getPower("Strength");
            int originalStrength = 0;
            if (strength != null) {
                originalStrength = strength.amount;
                strength.amount = 0;
            }

            super.calculateCardDamage(mo);
            if (strength != null) {
                strength.amount = originalStrength;
            }
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DownWithTheShipAction(m, new DamageInfo(p,damage, damageTypeForTurn)));
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
            upgradeDescription();
        }
    }
}
