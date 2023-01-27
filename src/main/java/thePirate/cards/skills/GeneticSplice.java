package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thePirate.PirateMod;
import thePirate.actions.GeneticSpliceAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class GeneticSplice extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADED_MAGIC = -1;
    public static final int BLOCK = 6;
    private static final int UPGRADED_BLOCK = 0;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(GeneticSplice.class.getSimpleName());
    public static final String IMG = makeCardPath(GeneticSplice.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public GeneticSplice() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        block = baseBlock = BLOCK;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GeneticSpliceAction(m,magicNumber));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                applyPowers();
                if (AbstractDungeon.player.getPower(DexterityPower.POWER_ID) != null)
                    PirateMod.logger.info("AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount: " + AbstractDungeon.player.getPower(DexterityPower.POWER_ID).amount);
                addToBot(new GainBlockAction(p,block));
                isDone = true;
            }
        });
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeMagicNumber(UPGRADED_MAGIC);
            if (UPGRADED_BLOCK > 0)
                upgradeBlock(UPGRADED_BLOCK);

            upgradeDescription();
        }
    }
}
