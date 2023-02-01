package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thePirate.PirateMod;
import thePirate.cards.attacks.HeatedShot;

public class HeatedShotAction extends AbstractGameAction {

    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private HeatedShot card;

    public HeatedShotAction(AbstractMonster target, DamageInfo info, AbstractGameAction.AttackEffect effect, HeatedShot card) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.card = card;
    }

    @Override
    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            PirateMod.logger.info("target.isDying: " + this.target.isDying);
            PirateMod.logger.info("target.isDead: " + target.isDead);
            PirateMod.logger.info("target.currentHealth: " + target.currentHealth);
            if(!target.isDying){
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
                this.target.damage(this.info);
            }
            if ((((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) || target.halfDead) {
                card.use(AbstractDungeon.player, (AbstractMonster)this.target);
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
    }



}
