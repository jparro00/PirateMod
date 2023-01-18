package thePirate.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import thePirate.patches.vfx.PirateGainPennyEffect;
import thePirate.relics.MoneyBag;

public class PayGoldAction extends AbstractGameAction {

    public int goldToLose;
    public Hitbox targetHb;
    public AbstractPlayer p;
    float targetX, targetY;

    public PayGoldAction(int goldToLose, Hitbox targetHb){
        this.goldToLose = goldToLose;
        this.targetHb = targetHb;
        p = AbstractDungeon.player;
        targetX = targetHb.cX;
        targetY = targetHb.cY;
    }


    @Override
    public void update() {

        MoneyBag moneyBag = null;
        Hitbox goldHb = AbstractDungeon.topPanel.goldHb;
        int moneyBagAmount = 0;
        if (p.hasRelic(MoneyBag.ID)){
            moneyBag = (MoneyBag)p.getRelic(MoneyBag.ID);
            int moneyBagCounter = moneyBag.counter;
            if (goldToLose <= moneyBagCounter){
                moneyBagAmount = goldToLose;
                goldToLose = 0;
            }else{
                moneyBagAmount = goldToLose - moneyBagCounter;
                goldToLose -= moneyBagCounter;
            }
        }
        for(int i = 0; i < goldToLose; ++i) {
            AbstractDungeon.effectList.add(new PirateGainPennyEffect(null, goldHb.cX, goldHb.cY, targetX, targetY, false));
        }
        for(int i = 0; i < moneyBagAmount; ++i) {
            if (moneyBag != null)
                AbstractDungeon.effectList.add(new PirateGainPennyEffect(null, moneyBag.hb.cX, moneyBag.hb.cY, targetX, targetY, false));
        }

        isDone = true;
    }
}
