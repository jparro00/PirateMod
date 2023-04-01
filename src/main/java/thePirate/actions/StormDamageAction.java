package thePirate.actions;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class StormDamageAction extends DamageAction {
    public StormDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        super(target, info, effect);
    }

    public StormDamageAction(AbstractCreature target, DamageInfo info, int stealGoldAmount) {
        super(target, info, stealGoldAmount);
    }

    public StormDamageAction(AbstractCreature target, DamageInfo info) {
        super(target, info);
    }

    public StormDamageAction(AbstractCreature target, DamageInfo info, boolean superFast) {
        super(target, info, superFast);
    }

    public StormDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, boolean superFast) {
        super(target, info, effect, superFast);
    }

    public StormDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, boolean superFast, boolean muteSfx) {
        super(target, info, effect, superFast, muteSfx);
    }

    @Override
    public void update() {
        isDone = true;
        super.update();
    }
}
