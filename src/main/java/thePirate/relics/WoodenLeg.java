package thePirate.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.screens.MasterDeckViewScreen;
import thePirate.PirateMod;
import thePirate.actions.BuryAction;
import thePirate.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class WoodenLeg extends CustomRelic implements ClickableRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(WoodenLeg.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(WoodenLeg.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(WoodenLeg.class.getSimpleName() + ".png"));
    private boolean cardsSelected = true;
    public static final int CARDS_TO_BURY = 3;
    public List<AbstractCard> preselectedCards;
    public static final Color BLUE_BORDER_GLOW_COLOR = ReflectionHacks.getPrivateStatic(AbstractCard.class,"BLUE_BORDER_GLOW_COLOR");


    public WoodenLeg(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
        this.preselectedCards = new ArrayList<>();
    }

    public WoodenLeg(){
        super(ID, IMG,OUTLINE, RelicTier.RARE, LandingSound.FLAT);
        this.preselectedCards = new ArrayList<>();
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardsSelected && (AbstractDungeon.gridSelectScreen.selectedCards.size() == CARDS_TO_BURY || (!AbstractDungeon.CurrentScreen.GRID.equals(AbstractDungeon.screen) && AbstractDungeon.gridSelectScreen.selectedCards.size() > 0 && AbstractDungeon.gridSelectScreen.selectedCards.size() < CARDS_TO_BURY))) {
            cardsSelected = true;
            this.preselectedCards = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            for (AbstractCard card : preselectedCards){
                card.stopGlowing();
            }
            if (AbstractDungeon.CurrentScreen.MAP.equals(AbstractDungeon.screen)){
                AbstractDungeon.overlayMenu.cancelButton.show(DungeonMapScreen.TEXT[1]);
            } else if (AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW.equals(AbstractDungeon.screen)) {
                AbstractDungeon.overlayMenu.cancelButton.show(MasterDeckViewScreen.TEXT[1]);
            } else if (AbstractDungeon.CurrentScreen.NONE.equals(AbstractDungeon.screen)){
                if (AbstractDungeon.dungeonMapScreen != null && AbstractDungeon.dungeonMapScreen.map != null){
                    AbstractDungeon.dungeonMapScreen.map.hideInstantly();
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards = new ArrayList<>();
        }
    }

    @Override
    public void atBattleStartPreDraw() {
        addToBot(new BuryAction(CARDS_TO_BURY, true, preselectedCards));
        flash();
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WoodenLeg();
    }

    @Override
    public String[] CLICKABLE_DESCRIPTIONS() {
        return ClickableRelic.super.CLICKABLE_DESCRIPTIONS();
    }

    @Override
    public void onRightClick() {
        PirateMod.logger.info(AbstractDungeon.previousScreen);

        if(PirateMod.isInCombat() && !AbstractDungeon.isScreenUp && AbstractDungeon.previousScreen == null){
            preselectedCards = new ArrayList<>();
            WoodenLeg thisRelic = this;
            addToTop(new SelectCardsAction(AbstractDungeon.player.masterDeck.group, CARDS_TO_BURY, DESCRIPTIONS[1], true, new Predicate<AbstractCard>() {

                @Override
                public boolean test(AbstractCard abstractCard) {
                    abstractCard.stopGlowing();
                    return true;
                }
            }, new Consumer<List<AbstractCard>>() {
                @Override
                public void accept(List<AbstractCard> cards) {
                    for (AbstractCard card : cards){
                        card.stopGlowing();
                    }
                    thisRelic.preselectedCards = new ArrayList<>(cards);
                }
            }));
        }else if (!AbstractDungeon.CurrentScreen.GRID.equals(AbstractDungeon.screen) && !AbstractDungeon.CurrentScreen.GRID.equals(AbstractDungeon.previousScreen)){

            this.cardsSelected = false;
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

            tmp.group.addAll(AbstractDungeon.player.masterDeck.group);
            for (AbstractCard card : tmp.group){
                card.stopGlowing();
                card.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
            }

            if (tmp.group.isEmpty()) {
                this.cardsSelected = true;
            } else {
                if (!AbstractDungeon.isScreenUp) {
                    AbstractDungeon.gridSelectScreen.open(tmp, CARDS_TO_BURY, true, DESCRIPTIONS[1]);
                } else if (AbstractDungeon.screen != null){// AbstractDungeon.CurrentScreen.MAP.equals(AbstractDungeon.screen) || AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW.equals(AbstractDungeon.screen)){
                    switch (AbstractDungeon.screen){
                        case MAP:
                        case MASTER_DECK_VIEW:
                        case SHOP:
                            AbstractDungeon.overlayMenu.cancelButton.hide();
                        case COMBAT_REWARD:
                        case CARD_REWARD:
                            AbstractDungeon.dynamicBanner.hide();
                            AbstractDungeon.previousScreen = AbstractDungeon.screen;
                            AbstractDungeon.gridSelectScreen.open(tmp, CARDS_TO_BURY, true, DESCRIPTIONS[1]);
                            break;
                        default:
                            break;

                    }
                }

            }
        }

    }

    @Override
    public void clickUpdate() {
        ClickableRelic.super.clickUpdate();
    }

    @Override
    public boolean hovered() {
        return ClickableRelic.super.hovered();
    }
}
