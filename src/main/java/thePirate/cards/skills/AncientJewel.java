package thePirate.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.OnBury;

import java.util.List;

import static thePirate.PirateMod.isHardcore;
import static thePirate.PirateMod.makeCardPath;

public class AncientJewel extends AbstractDynamicCard implements OnBury {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;


    private static final int COST = -2;
    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 1;
    public static final int HC_MAGIC = 1;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(AncientJewel.class.getSimpleName());
    public static final String IMG = makeCardPath("AncientJewel.png", TYPE);

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    // /TEXT DECLARATION/

    public AncientJewel() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET, isHardcore());
        magicNumber = baseMagicNumber = MAGIC;
        if(hardcore){
            magicNumber = baseMagicNumber = HC_MAGIC;
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_MAGIC > 0)
                upgradeMagicNumber(UPGRADED_MAGIC);
            upgradeDescription();
        }
    }

    @Override
    public void onBury(AbstractCard card) {
        if(this.equals(card)){
            addToBot(new GainEnergyAction(magicNumber));
        }

    }

    @Override
    public void onBuryCards(List<AbstractCard> cards) {

    }
}
