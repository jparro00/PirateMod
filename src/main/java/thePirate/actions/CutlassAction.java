package thePirate.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class CutlassAction extends AbstractGameAction {
    private AbstractPlayer p;
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final AbstractCard cutlass;

    public CutlassAction(AbstractCard cutlassCard) {
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.cutlass = cutlassCard;
    }

    public void update() {
        //this.p.hand.moveToDeck(this.cutlass, false);
        for( AbstractGameAction action : AbstractDungeon.actionManager.actions){
            if (action instanceof UseCardAction){
                UseCardAction useCardAction = (UseCardAction) action;
                useCardAction.reboundCard = true;
            }
        }
        this.isDone = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("SetupAction");
        TEXT = uiStrings.TEXT;
    }
}
