package thePirate.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface OnAttackToChangeDamagePreBlock {

    public int onAttackToChangeDamagePreBlock(DamageInfo info, int damageAmount);
}
