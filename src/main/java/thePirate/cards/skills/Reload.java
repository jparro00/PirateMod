package thePirate.cards.skills;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.DefaultMod;
import thePirate.cards.AbstractCannonCard;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.attacks.AbstractCannonBallCard;
import thePirate.characters.TheDefault;

import java.util.function.Predicate;

import static thePirate.DefaultMod.makeCardPath;

public class Reload extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = DefaultMod.makeID(Reload.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Reload.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    // /STAT DECLARATION/


    public Reload() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //this.addToBot(new ReloadAction(false));
        this.addToBot(new MoveCardsAction(AbstractDungeon.player.hand, AbstractDungeon.player.exhaustPile, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                return card instanceof AbstractCannonBallCard || card instanceof AbstractCannonCard;
            }
        }));

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            this.retain = true;
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }
}
