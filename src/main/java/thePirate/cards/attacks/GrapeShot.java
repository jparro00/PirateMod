package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.Necronomicon;
import thePirate.DefaultMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.TheDefault;

import static thePirate.DefaultMod.makeCardPath;

public class GrapeShot extends AbstractCannonBallCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 2;
    private static final int UPGRADE_PLUS_DMG = 1;

    private boolean usePending;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = DefaultMod.makeID(GrapeShot.class.getSimpleName());
    public static final String IMG = makeCardPath("GrapeShot.png", TYPE);
    // /TEXT DECLARATION/

    public GrapeShot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cardsPlayedThisTurn = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        DefaultMod.logger.info("cardsPlayedThisTurn: " + cardsPlayedThisTurn);
        //use(p,m,cardsPlayedThisTurn);

        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        this.addToBot(new DamageAction(target, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        if(!usePending){
            for (int i = 0; i < cardsPlayedThisTurn - 1; i++){
                GrapeShot tmp = (GrapeShot)this.makeSameInstanceOf();
                tmp.exhaust = false;
                tmp.usePending = true;
                tmp.current_x = current_x;
                tmp.current_y = current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                tmp.applyPowers();
                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, this.energyOnUse, true, true), true);
            }
        }
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m, int numberOfUses){
        numberOfUses--;
        DefaultMod.logger.info("numberOfUses: " + numberOfUses);

        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        this.addToBot(new DamageAction(target, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));

        if(numberOfUses > 0){
            use(p, m, numberOfUses);
        }

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
