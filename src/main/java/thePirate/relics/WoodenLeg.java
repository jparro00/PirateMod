package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
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
        if (preselectedCards.size() < 0){
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

        if(PirateMod.isInCombat()){
            preselectedCards = new ArrayList<>();
            WoodenLeg thisRelic = this;
            addToTop(new SelectCardsAction(AbstractDungeon.player.masterDeck.group, CARDS_TO_BURY, DESCRIPTIONS[1], true, new Predicate<AbstractCard>() {

                @Override
                public boolean test(AbstractCard abstractCard) {
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
