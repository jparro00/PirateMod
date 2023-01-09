package thePirate.cards.attacks;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.Mysterious;
import thePirate.cards.Purgable;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class StrangeBlade extends AbstractDynamicCard implements Mysterious, Purgable {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 10;
    private static final int UPGRADED_DMG = 0;
    public static final int MAGIC = 3;

    public boolean purge;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(StrangeBlade.class.getSimpleName());
    public static final String IMG = makeCardPath("StrangeBlade.png", TYPE);
    // /TEXT DECLARATION/

    public StrangeBlade() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if(upgraded)
            this.addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public void update() {
        super.update();
        AbstractPlayer p = AbstractDungeon.player;
        if(p != null && upgraded && p.masterDeck.contains(this)){
            upgraded = false;
            name = languagePack.getCardStrings(ID).NAME;

            initializeTitle();
            rawDescription = languagePack.getCardStrings(ID).DESCRIPTION;
            initializeDescription();
        }
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
    public void setPurge(boolean purge) {
        this.purge = purge;
    }

    @Override
    public boolean getPurge() {
        return purge;
    }
}
