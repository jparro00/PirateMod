package thePirate.actions;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class PositionDamageAction extends DamageAction {


    public int monsterPosition;


    public PositionDamageAction(int monsterPosition, DamageInfo info, AttackEffect effect){
        super(null, info, effect);
        this.monsterPosition = monsterPosition;
        List<AbstractMonster> monsterList = AbstractDungeon.getCurrRoom().monsters.monsters;
        for(AbstractMonster monster : monsterList){
            if(!monster.isDead){
                this.target = monster;
                break;
            }
        }
        if(target == null){
            target = AbstractDungeon.getCurrRoom().monsters.monsters.get(0);
        }

    }


}
