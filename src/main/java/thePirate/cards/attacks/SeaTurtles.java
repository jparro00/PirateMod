package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.BuryAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.OnDig;
import thePirate.characters.ThePirate;
import thePirate.powers.OnBury;

import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class SeaTurtles extends AbstractDynamicCard implements OnBury, OnDig {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;

    private static final int DAMAGE = 8;
    private static final int UPGRADED_DMG = 3;
    boolean justBurried;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(SeaTurtles.class.getSimpleName());
    public static final String IMG = makeCardPath(SeaTurtles.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public SeaTurtles() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

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
    }

    @Override
    public void onBuryCards(List<AbstractCard> cards) {
        for (AbstractCard card : cards){
            if (card.uuid.equals(this.uuid)){
                if (!((SeaTurtles)card).justBurried){
                    SeaTurtles seaTurtles = (SeaTurtles) this.makeStatEquivalentCopy();
                    seaTurtles.justBurried = true;
                    AbstractDungeon.player.drawPile.addToRandomSpot(seaTurtles);
                    addToTop(new BuryAction(seaTurtles));
                }else {
                    ((SeaTurtles)card).justBurried = false;
                }
                break;
            }
        }

    }

    @Override
    public void onDig(List<AbstractCard> cardsSelected) {
        for (AbstractCard card : cardsSelected){
            if (card.uuid.equals(this.uuid)){
                SeaTurtles seaTurtles = (SeaTurtles) this.makeCopy();
                seaTurtles.upgrade();
                AbstractDungeon.player.drawPile.addToRandomSpot(seaTurtles);
                break;
            }
        }

    }
}
