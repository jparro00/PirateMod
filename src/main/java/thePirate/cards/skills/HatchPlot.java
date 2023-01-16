package thePirate.cards.skills;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.PlayCardAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static thePirate.PirateMod.makeCardPath;

public class HatchPlot extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 1;
    private static final int UPGRADED_MAGIC = 0;
    private static final int SECOND_MAGIC = 0;
    private static final int UPGRADED_SECOND_MAGIC = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(HatchPlot.class.getSimpleName());
    public static final String IMG = makeCardPath("HatchPlot.png", TYPE);
    // /TEXT DECLARATION/

    public HatchPlot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        String text = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[0];
        addToBot(new SelectCardsAction(p.discardPile.group, magicNumber, text, false, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                return !cardID.equals(card.cardID);
//                return card.canUse(p, null) && !cardID.equals(card.cardID);
            }
        }, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> abstractCards) {
                //TODO: add logic next
                for(AbstractCard card : abstractCards){
                    addToBot(new PlayCardAction(p.discardPile, card, AbstractDungeon.getCurrRoom().monsters.getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng), false));
                }
            }
        }));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            exhaust = false;
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
