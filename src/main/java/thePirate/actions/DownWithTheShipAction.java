package thePirate.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DownWithTheShipAction extends AbstractGameAction {

    private DamageInfo info;
    private float startingDuration;

    public DownWithTheShipAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.WAIT;
        this.attackEffect = AttackEffect.FIRE;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update() {
        AbstractPlayer player = AbstractDungeon.player;
        int count = player.drawPile.size() - 1;

        int i;
        for(i = 0; i < count; ++i) {
            //TODO: let's find a better attack effect
            this.addToTop(new DamageAction(this.target, this.info, AttackEffect.FIRE));
        }

        if(player.drawPile.size() > 0){
            addToTop(new SelectCardsAction(player.drawPile.group, 1, "Select a card to put on top of you draw pile", false, new Predicate<AbstractCard>() {
                @Override
                public boolean test(AbstractCard card) {
                    return true;
                }
            }, new Consumer<List<AbstractCard>>() {
                @Override
                public void accept(List<AbstractCard> abstractCards) {
                    for (AbstractCard card : abstractCards){
                        List<AbstractCard> cardsToRemove = new ArrayList<>(player.drawPile.group);
                        cardsToRemove.remove(card);
                        AbstractDungeon.actionManager.addToBottom(new BuryAction(cardsToRemove) );

                    }
                }
            }));
        }

        this.isDone = true;
    }



}
