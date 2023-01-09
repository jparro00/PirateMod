package thePirate.cards.predators;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.DigAction;
import thePirate.cards.lures.AbstractLure;
import thePirate.cards.lures.WithdrawWeapons;
import thePirate.characters.ThePirate;
import thePirate.powers.OnBury;

import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class AncientCrab extends AbstractPredator implements OnBury {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 20;
    private static final int UPGRADED_DMG = 5;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String SIMPLE_NAME = AncientCrab.class.getSimpleName();
    public static final String ID = PirateMod.makeID(SIMPLE_NAME);
    public static final String IMG = makeCardPath(SIMPLE_NAME + ".png", TYPE);
    // /TEXT DECLARATION/

    public AncientCrab(){
        this(true);
    }
    public AncientCrab(boolean showPreview) {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, showPreview);
        baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
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
    public void onBury(AbstractCard card) {
        if(this.equals(card)){
            addToBot(new DiscardToHandAction(this));
            addToBot(new DigAction(1,false));
        }

    }

    @Override
    public void onBuryCards(List<AbstractCard> cards) {

    }

    @Override
    public AbstractLure getLure() {
        return new WithdrawWeapons(false);
    }
}
