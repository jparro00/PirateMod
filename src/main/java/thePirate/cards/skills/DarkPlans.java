package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.Mysterious;
import thePirate.characters.ThePirate;
import thePirate.powers.InkPower;
import thePirate.powers.OnBury;

import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class DarkPlans extends AbstractDynamicCard implements Mysterious, OnBury {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;
    private static final int MAGIC = 10;
    private static final int UPGRADED_MAGIC = 0;
    private static final int SECOND_MAGIC = 0;
    private static final int UPGRADED_SECOND_MAGIC = 1;
    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(DarkPlans.class.getSimpleName());
    public static final String IMG = makeCardPath("DarkPlans.png", TYPE);
    // /TEXT DECLARATION/

    public DarkPlans() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (canUse(AbstractDungeon.player, null)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }

    }

    public boolean canUpgrade() {
        return Mysterious.canUpgrade();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {

        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
//        } else if(!(p.drawPile.size() == p.discardPile.size())) {
        } else if (!upgraded){
            this.cantUseMessage = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[0];
            return false;
        } else {
            return canUse;
        }
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for(int i = 0; i < secondMagic; i++){
            for( AbstractMonster mo :AbstractDungeon.getCurrRoom().monsters.monsters){
                this.addToBot(new ApplyPowerAction(mo, p, new InkPower(mo, p, magicNumber), magicNumber));
            }
        }

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            if (UPGRADED_MAGIC > 0)
                upgradeMagicNumber(UPGRADED_MAGIC);
            upgradeDescription();
        }
    }

    @Override
    public void onBury(AbstractCard card) {
        PirateMod.logger.info("enter onBury()");
        if (card.equals(this)){
            PirateMod.logger.info("upgrading from single card");
            upgradeSecondMagic(1);
        }

    }

    @Override
    public void onBuryCards(List<AbstractCard> cards) {
        PirateMod.logger.info("enter onBuryCards()");

    }
}
