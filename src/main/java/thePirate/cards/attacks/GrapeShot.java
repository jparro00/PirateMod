package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.PirateSFXAction;
import thePirate.actions.StormDamageAction;
import thePirate.characters.ThePirate;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static thePirate.PirateMod.makeCardPath;

public class GrapeShot extends AbstractCannonBallCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DMG = 1;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(GrapeShot.class.getSimpleName());
    public static final String IMG = makeCardPath("GrapeShot.png", TYPE);
    // /TEXT DECLARATION/

    public GrapeShot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 0;
        storm = true;
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        calculateCardDamage(target);
        if (!PirateMod.disableCannonSFX.toggle.enabled){
            this.addToBot(new PirateSFXAction("CANNON_FIRE"));
        }
        this.addToBot(new StormDamageAction(target, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        storm(p,m);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() > 0){
            rawDescription = languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION[0];
        }else {
            rawDescription = languagePack.getCardStrings(ID).DESCRIPTION;
        }
        initializeDescription();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADE_PLUS_DMG != DAMAGE)
                upgradeDamage(UPGRADE_PLUS_DMG);
            if(UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }

}
