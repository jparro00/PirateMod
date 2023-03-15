package thePirate.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GhostlyFireEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import thePirate.PirateMod;
import thePirate.actions.HeatedShotAction;
import thePirate.actions.PirateSFXAction;
import thePirate.characters.ThePirate;

import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class HeatedShot extends AbstractCannonBallCard {


    // TEXT DECLARATION

    public static final String ID = PirateMod.makeID(HeatedShot.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath("HeatedShot.png");
    // This does mean that you will need to have an image with the same NAME as the card in your image folder for it to run correctly.


    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 2;

    private static final int DAMAGE = 18;
    private static final int UPGRADE_PLUS_DMG = 6;

    // /STAT DECLARATION/


    public HeatedShot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractMonster target = null;
        List<AbstractMonster> monsterList = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(AbstractMonster monster : monsterList){
            if(!monster.isDead && !monster.isDying && !(monster.currentHealth <=0) && !monster.halfDead){
                target = monster;
                break;
            }
        }

        if (target != null && !target.isDeadOrEscaped()){
            this.addToBot(new PirateSFXAction("CANNON_HIT_SHIP"));
            this.addToBot(new VFXAction(p, new FireballEffect(AbstractDungeon.player.hb.x,AbstractDungeon.player.hb.y,target.hb.x,target.hb.y), 0.01F));
            AbstractDungeon.effectsQueue.add(new GhostlyFireEffect(target.hb_x, target.hb_y));
        }
        //this.addToBot(new DamageAction(target, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.addToBot(new HeatedShotAction(target, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, this));

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
