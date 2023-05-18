package thePirate.cards.attacks;

import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.CutlassAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class Cutlass extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(Cutlass.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Cutlass.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int UPGRADED_COST = 0;

    private static final int DAMAGE = 8;

    private static final int UPGRADE_PLUS_DMG = 1;

    private static final int BASE_DAMAGE_DEBUF = 2;
    public static final int UPGRADE_DAMAGE_DEBUF = -1;
    public static final int EXAUSTIVE = 3;
    public static final int UPGRADE_EXHAUSTIVE = 0;

    // /STAT DECLARATION/


    public Cutlass() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = BASE_DAMAGE_DEBUF;
        ExhaustiveVariable.setBaseValue(this, 3);

    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        this.addToBot(new ModifyDamageAction(this.uuid, -this.magicNumber));
        this.addToBot(new CutlassAction(this));
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADE_PLUS_DMG > 0)
                upgradeDamage(UPGRADE_PLUS_DMG);
            if (UPGRADE_DAMAGE_DEBUF != BASE_DAMAGE_DEBUF)
                upgradeMagicNumber(UPGRADE_DAMAGE_DEBUF);
            if (UPGRADE_EXHAUSTIVE > 0){
                ExhaustiveVariable.upgrade(this, UPGRADE_EXHAUSTIVE);
            }
            initializeDescription();
        }
    }
}
