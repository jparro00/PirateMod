package thePirate.cards.attacks;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static thePirate.PirateMod.makeCardPath;

public class GiantBeak extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(GiantBeak.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("GiantBeak.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 11;
    private static final int UPGRADE_PLUS_DMG = 3;
    public static final int AMOUNT = 1;

    // /STAT DECLARATION/


    public GiantBeak() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = AMOUNT;
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        String title = rawDescription = languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[0];
        String desc = rawDescription = languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[1];
        List<TooltipInfo> toolTips = new ArrayList<>();
        toolTips.add(new TooltipInfo(title, desc));
        return toolTips;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseDamage = this.baseDamage;
        int artifactCount = 0;
        int vulnerableCount = 0;

        Iterator<AbstractPower> iter = mo.powers.iterator();
        while(iter.hasNext()){
            AbstractPower power = iter.next();
            if("Vulnerable".equals(power.ID)){
                vulnerableCount += power.amount;
            }
            if("Artifact".equals(power.ID)){
                artifactCount += power.amount;
            }

        }

        if (vulnerableCount <= 0 && artifactCount < 1){
            AbstractPower vulnerable = new VulnerablePower(mo, 1, false);
            mo.powers.add(vulnerable);
            super.calculateCardDamage(mo);
            mo.powers.remove(vulnerable);
            this.baseDamage = realBaseDamage;
            this.isDamageModified = this.damage != this.baseDamage;
        }else{
            super.calculateCardDamage(mo);
            this.baseDamage = realBaseDamage;
            this.isDamageModified = this.damage != this.baseDamage;
        }

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
        int tmpDmg = this.damage;
//        if (!m.hasPower("Vulnerable")){
//            tmpDmg *= 1.5;
//        }
        this.addToBot(new DamageAction(m, new DamageInfo(p, tmpDmg, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
