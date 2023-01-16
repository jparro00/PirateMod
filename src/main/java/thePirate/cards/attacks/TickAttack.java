package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.MoveCardAction;
import thePirate.cards.AbstractDynamicCard;

import static thePirate.PirateMod.makeCardPath;

public class TickAttack extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.SPECIAL; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;

    private static final int DAMAGE = 0;
    private static final int UPGRADED_DMG = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(TickAttack.class.getSimpleName());
    public static final String IMG = makeCardPath(TickAttack.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public TickAttack() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new MoveCardAction(p.discardPile,p.hand,this));
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
