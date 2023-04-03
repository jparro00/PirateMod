package thePirate.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import thePirate.PirateMod;
import thePirate.util.TextureLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class Coral extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = PirateMod.makeID(Coral.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(Coral.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(Coral.class.getSimpleName() + ".png"));
    private boolean cardsSelected = true;

    public boolean isActive;

    public Coral(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
    }

    public Coral(){
        super(ID, IMG,OUTLINE, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    public void update() {
        super.update();
        if (!this.cardsSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            this.giveCards(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
        }

    }

    public void giveCards(AbstractCard card) {
        this.cardsSelected = true;
        float displayCount = 0.0F;

        card.untip();
        card.unhover();
//        AbstractDungeon.player.masterDeck.group.add(card);
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(card, (float)Settings.WIDTH / 3.0F + displayCount, (float)Settings.HEIGHT / 2.0F, false));

        AbstractDungeon.gridSelectScreen.selectedCards.clear();
        AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.25F;
    }

    public static List<AbstractCard> getRandomCards(List<AbstractCard> cardList, AbstractCard.CardType type, int count){
        Collections.shuffle(cardList, AbstractDungeon.cardRng.random);
        List<AbstractCard> returnCards = new ArrayList<>();
        for (AbstractCard card : cardList){
            if (card.type.equals(type)){
                returnCards.add(card);
                if (returnCards.size() >= count){
                    break;
                }
            }
        }
        return returnCards;
    }

    public static List<AbstractCard> getCards(List<AbstractCard> cardPool){
        List<AbstractCard> returnCards = new ArrayList<>();
        returnCards.addAll(getRandomCards(cardPool, AbstractCard.CardType.ATTACK, 2));
        returnCards.addAll(getRandomCards(cardPool, AbstractCard.CardType.SKILL, 2));
        returnCards.addAll(getRandomCards(cardPool, AbstractCard.CardType.POWER, 1));
        return returnCards;
    }

    @Override
    public void onEquip() {
        this.cardsSelected = false;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        ArrayList<AbstractCard> cards = new ArrayList<>();
        CardLibrary.addRedCards(cards);
        tmp.group.addAll(getCards(cards));

        cards = new ArrayList<>();
        CardLibrary.addGreenCards(cards);
        tmp.group.addAll(getCards(cards));

        cards = new ArrayList<>();
        CardLibrary.addBlueCards(cards);
        tmp.group.addAll(getCards(cards));

        cards = new ArrayList<>();
        CardLibrary.addPurpleCards(cards);
        tmp.group.addAll(getCards(cards));
        for (AbstractCard card : tmp.group){
            UnlockTracker.markCardAsSeen(card.cardID);
        }

        if (tmp.group.isEmpty()) {
            this.cardsSelected = true;
        } else {
            if (!AbstractDungeon.isScreenUp) {
                AbstractDungeon.gridSelectScreen.open(tmp, 1, this.DESCRIPTIONS[1], false, false, false, false);
            } else {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
                AbstractDungeon.gridSelectScreen.open(tmp, 1, this.DESCRIPTIONS[1], false, false, false, false);
            }

        }
    }

    public void onVictory() {
        this.pulse = false;
        this.isActive = false;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Coral();
    }

}
