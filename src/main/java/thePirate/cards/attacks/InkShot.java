package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.PirateSFXAction;
import thePirate.characters.ThePirate;
import thePirate.powers.InkPower;

import static thePirate.PirateMod.makeCardPath;

public class InkShot extends AbstractCannonBallCard {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(InkShot.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("InkShot.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;

    private static final int DAMAGE = 8;
    private static final int UPGRADE_PLUS_DMG = 3;
    public static final int BASE_INK = 2;
    public static final int UPGRADED_INK = 1;

    // /STAT DECLARATION/


    public InkShot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BASE_INK;
        this.isMultiDamage = true;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        if (!PirateMod.disableCannonSFX.toggle.enabled){
            this.addToBot(new PirateSFXAction("CANNON_FIRE"));
            this.addToBot(new PirateSFXAction("INK_SPLAT_CANNON"));
        }
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
            this.addToBot(new ApplyPowerAction(mo, p, new InkPower(mo, p, magicNumber), magicNumber));
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADED_INK);
            initializeDescription();
        }
    }
}
