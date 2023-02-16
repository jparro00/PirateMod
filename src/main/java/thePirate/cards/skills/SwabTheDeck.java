package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class SwabTheDeck extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(SwabTheDeck.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("SwabTheDeck.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;
    public static final int BLOCK = 7;
    public static final int EXTRA_BLOCK = 3;
    public static final int EXTRA_BLOCK_UPGRADE = 1;

    private static final int COST = 1;
    public static final int UPGRADE_BLOCK = 3;

    // /STAT DECLARATION/


    public SwabTheDeck() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BLOCK;
        this.magicNumber = this.baseMagicNumber = EXTRA_BLOCK;
    }
    @Override
    public void applyPowers() {

        //put magic number in the block variable to modify with dex
        int tmpBaseBlock = baseBlock;
        baseBlock = baseMagicNumber;
        super.applyPowers();
        this.isMagicNumberModified = this.isBlockModified;
        magicNumber = block;

        //recalculate block
        baseBlock = tmpBaseBlock;
        super.applyPowers();

    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 0) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, this.block));
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.size() == 1) {
            this.addToBot(new GainBlockAction(p, this.magicNumber));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(EXTRA_BLOCK_UPGRADE);
            upgradeBlock(UPGRADE_BLOCK);
            initializeDescription();
        }
    }
}
