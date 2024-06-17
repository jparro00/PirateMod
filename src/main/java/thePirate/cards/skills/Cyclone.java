package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static thePirate.PirateMod.isHardcore;
import static thePirate.PirateMod.makeCardPath;

public class Cyclone extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;
    public static final int BLOCK = 2;
    public static final int UPGRADED_BLOCK = 1;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Cyclone.class.getSimpleName());
    public static final String IMG = makeCardPath(Cyclone.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public Cyclone() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, isHardcore());
        block = baseBlock = BLOCK;
        exhaust = true;
        storm = true;
    }


    @Override
    public void applyPowers() {
        super.applyPowers();
        String id = ID;
        if (hardcore)
            id = ID + "_HC";
        if(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0){
            rawDescription = languagePack.getCardStrings(id).EXTENDED_DESCRIPTION[0];
        }else {
            rawDescription = languagePack.getCardStrings(id).DESCRIPTION;
        }
        initializeDescription();
    }

    @Override
    public void applyPowersToBlock() {
        if (hardcore){
            AbstractPower dexterity = AbstractDungeon.player.getPower(DexterityPower.POWER_ID);
            int originalDexterity = 0;
            if (dexterity != null) {
                originalDexterity = dexterity.amount;
                dexterity.amount = 0;
            }
            super.applyPowersToBlock();
            if (dexterity != null) {
                dexterity.amount = originalDexterity;
            }
        }else {
            super.applyPowersToBlock();
        }

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p,block));
//        addToBot(new StormGainBlockAction(p,block));
        storm(p,m);
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeBlock(UPGRADED_BLOCK);
            upgradeDescription();
        }
    }

}
