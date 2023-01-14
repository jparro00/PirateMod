package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.Purgable;
import thePirate.cards.curses.Retreat;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class Overexert extends AbstractDynamicCard implements Purgable {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(Overexert.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Overexert.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.
    private boolean purge;
    public boolean queuedForPurge;
    private boolean previewCards;


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;

    private static final int DAMAGE = 24;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /STAT DECLARATION/

    public Overexert(boolean previewCards){
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.purgeOnUse = true;
        this.previewCards = previewCards;
        if(previewCards){
            this.cardsToPreview = new Retreat(false);
        }

    }

    public Overexert() {
        this(true);
    }

    @Override
    public void update(){
        super.update();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));
        AbstractCard retreat = new Retreat();
        if(this.upgraded){
            retreat.upgrade();
        }
        this.addToTop(new AddCardToDeckAction(retreat));
        this.setPurge(true);
        //TODO: card not being removed if lethal.  Testing below
//        this.addToBot(new PurgeRemovablesAction(this));
        //test:
//        this.addToTop(new PurgeRemovablesAction(this, true));



    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            if(previewCards){
                AbstractCard retreat = new Retreat(false);
                retreat.upgrade();
                this.cardsToPreview = retreat;
            }
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }

    @Override
    public void setPurge(boolean purge) {
        this.purge = purge;
    }

    @Override
    public boolean getPurge() {
        return this.purge;
    }

    @Override
    public boolean queuedForPurge() {
        return queuedForPurge;
    }

    @Override
    public void setQueuedForPurge(boolean queuedForPurge) {
        this.queuedForPurge = queuedForPurge;
    }
}
