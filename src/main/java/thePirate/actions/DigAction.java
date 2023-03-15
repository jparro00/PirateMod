package thePirate.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import thePirate.cards.OnDig;
import thePirate.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static thePirate.PirateMod.makeScreenPath;

public class DigAction extends AbstractGameAction {

    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;
    public AbstractCard digCard;
    private static final Texture digLabel= TextureLoader.getTexture(makeScreenPath("dig_screen_icon.png"));

    public DigAction(AbstractCard digCard,int numberOfCards, boolean optional) {
        this.digCard = digCard;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
    }
    public DigAction(int numberOfCards, boolean optional) {
        this(null, numberOfCards, optional);
    }

    public DigAction(int numberOfCards){
        this(numberOfCards, false);
    }

    public void queueDigIcon(){
        DigIconPatch.isDig= true;
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                DigIconPatch.isDig = false;
                isDone = true;
            }
        });
    }
    @Override
    public void update() {
        queueDigIcon();

        List < AbstractCard > cardsSelected = new ArrayList<>();
        String text;
        AbstractPlayer player = this.player;

        if (numberOfCards + player.hand.size() > 10){
            numberOfCards = 10 - player.hand.size();
        }
        if (numberOfCards > 1){
            text = TEXT[1] + this.numberOfCards + TEXT[2];
        }else {
            text = TEXT[0];
        }

        addToTop(new SelectCardsAction(player.discardPile.group, numberOfCards,text, optional, new Predicate<AbstractCard>() {
            @Override
            public boolean test(AbstractCard card) {
                card.stopGlowing();
                return digCard == null || digCard.uuid.equals(card.uuid);
            }
        }, new Consumer<List<AbstractCard>>() {
            @Override
            public void accept(List<AbstractCard> abstractCards) {
                for(AbstractCard c : abstractCards){
                    c.upgrade();
                    player.discardPile.moveToHand(c);
                    player.hand.refreshHandLayout();
                    cardsSelected.add(c);
                }

                //adding onBury logic to Consumer
                if (cardsSelected.size() > 0){
                    List<AbstractCard> combatCards = new ArrayList<>();
                    combatCards.addAll(player.hand.group);
                    combatCards.addAll(player.discardPile.group);
                    combatCards.addAll(player.drawPile.group);

                    for (AbstractCard c : combatCards) {
                        if (c instanceof OnDig){
                            ((OnDig) c).onDig(cardsSelected);
                        }
                    }
                }

            }
        }));

        this.isDone = true;

    }


    static {
        TEXT = CardCrawlGame.languagePack.getUIString("thePirate:DigAction").TEXT;
    }

    @SpirePatch2(clz = GridCardSelectScreen.class, method = "render", paramtypez = { SpriteBatch.class})
    public static class DigIconPatch {

        public static boolean isDig;
        @SpirePrefixPatch
        public static void renderDigIcon(GridCardSelectScreen __instance, SpriteBatch sb) {
            if (!PeekButton.isPeeking && isDig){
                Color color = sb.getColor();
                sb.setColor(Color.WHITE);
                float derp = Interpolation.swingOut.apply(1.0F, 1.1F, MathUtils.cosDeg((float)(System.currentTimeMillis() / 4L % 360L)) / 12.0F);
                sb.draw(digLabel, (float)Settings.WIDTH - 64 - 512, (float) Settings.HEIGHT / 2.0F + 64.0F, 0F, 0F, 256.0F, 256.0F, Settings.scale * derp, Settings.scale * derp, 0.0F, 0, 0, 256, 256, false, false);
                sb.setColor(color);
            }
        }
    }

}
