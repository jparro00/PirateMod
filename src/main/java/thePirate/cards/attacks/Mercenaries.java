package thePirate.cards.attacks;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.PirateMod;
import thePirate.actions.LoseGoldAction;
import thePirate.actions.PayGoldAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class Mercenaries extends AbstractDynamicCard {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(Mercenaries.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("Mercenaries.png");

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;

    private static final int DAMAGE = 20;
    private static final int UPGRADE_PLUS_DMG = 6;
    public static final int GOLD_LOSS = 15;

    // /STAT DECLARATION/
    public boolean usedThisCombat;


    public Mercenaries() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = GOLD_LOSS;
        isEthereal = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (!usedThisCombat){
            addToTop(new LoseGoldAction(magicNumber));
            addToTop(new PayGoldAction(magicNumber, m.hb));
        }
        usedThisCombat = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        renderGoldIcon(sb);
    }

    public void renderGoldIcon(SpriteBatch sb){

        if (usedThisCombat){
            renderGreenGold(sb, current_x, current_y);
        }else {
            renderRedGold(sb, current_x, current_y);
        }

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
    @Override
    public AbstractCard makeStatEquivalentCopy() {
        Mercenaries mercenaries= (Mercenaries) super.makeStatEquivalentCopy();
        mercenaries.usedThisCombat = this.usedThisCombat;
        return mercenaries;
    }
}
