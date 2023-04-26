package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thePirate.cards.attacks.ShotsInTheDark;

public class ShotsInTheDarkAction extends AbstractGameAction {


    private DamageInfo info;
    private ShotsInTheDark card;

    public ShotsInTheDarkAction(AbstractGameAction.AttackEffect effect, ShotsInTheDark card) {
        this.info = new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn);
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.card = card;
    }

    @Override
    public void update() {

        this.target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        card.calculateCardDamage((AbstractMonster)target);
        info = new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn);
        if (this.duration == 0.1F && this.target != null) {
            if(!this.target.isDying){
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
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
