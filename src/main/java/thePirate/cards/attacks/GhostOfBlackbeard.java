package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.OnBury;

import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class GhostOfBlackbeard extends AbstractDynamicCard implements OnBury {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;

    private static final int DAMAGE = 10;
    private static final int UPGRADED_DMG = 3;

    // /STAT DECLARATION/
    private int burriedCards;

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(GhostOfBlackbeard.class.getSimpleName());
    public static final String IMG = makeCardPath(GhostOfBlackbeard.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public GhostOfBlackbeard() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
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
    @Override
    public void applyPowers() {
//        setCostForTurn(cost + burriedCards);
        super.applyPowers();
    }

    @Override
    public void onBury(AbstractCard card) {

    }

    @Override
    public void onBuryCards(List<AbstractCard> cards) {
        int costIncrease = cards.size();
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                updateCost(cards.size());
                isDone = true;
            }
        });
    }
}
