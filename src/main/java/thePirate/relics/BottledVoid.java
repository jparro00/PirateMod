package thePirate.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.patches.relics.BottledPlaceholderField;
import thePirate.util.TextureLoader;

import java.util.Iterator;
import java.util.function.Predicate;

import static thePirate.PirateMod.makeRelicOutlinePath;
import static thePirate.PirateMod.makeRelicPath;

public class BottledVoid extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {
    // This file will show you how to use 2 things - (Mostly) The Custom Bottle Relic and the Custom Savable - they go hand in hand.

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Savable
     *
     * Choose a card. Whenever you play any card, draw the chosen card.
     */

    // BasemodWiki Says: "When you need to store a value on a card or relic between runs that isn't a relic's counter value
    // or a card's misc value, you use a custom savable to save and load it between runs."

    private static AbstractCard card;  // The field value we wish to save in this case is the card that's going to be in our bottle.
    private boolean cardSelected = true; // A boolean to indicate whether or not we selected a card for bottling.
    // (It's set to false on Equip)


    // ID, images, text.
    public static final String ID = PirateMod.makeID(BottledVoid.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath(BottledVoid.class.getSimpleName() + ".png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath(BottledVoid.class.getSimpleName() + ".png"));

    public BottledVoid() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.CLINK);
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    // Now, for making Bottled cards we need a small patch - our own custom SpireField
    // I've included that already in patches.relics.BottledPlaceholderField
    // The basemod wiki I linked above has comments about onSave and onLoad

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return BottledPlaceholderField.inBottledPlaceholderField::get;
    }

    @Override
    public Integer onSave() {
        if (card != null) {
            return AbstractDungeon.player.masterDeck.group.indexOf(card);
        } else {
            return -1;
        }
    }

    @Override
    public void onLoad(Integer cardIndex) {
        if (cardIndex == null) {
            return;
        }
        if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
            card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
            if (card != null) {
                BottledPlaceholderField.inBottledPlaceholderField.set(card, true);
                setDescriptionAfterLoading();
            }
        }
    }


    @Override
    public void onEquip() { // 1. When we acquire the relic
        cardSelected = false; // 2. Tell the relic that we haven't bottled the card yet
        if (AbstractDungeon.isScreenUp) { // 3. If the map is open - hide it.
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        // 4. Set the room to INCOMPLETE - don't allow us to use the map, etc.
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck); // 5. Get a card group of all currently unbottled cards
        AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[3] + name + DESCRIPTIONS[2], false, false, false, false);
        // 6. Open the grid selection screen with the cards from the CardGroup we specified above. The description reads "Select a card to bottle for" + (relic name) + "."
    }


    @Override
    public void onUnequip() { // 1. On unequip
        if (card != null) { // If the bottled card exists (prevents the game from crashing if we removed the bottled card from our deck for example.)
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(card); // 2. Get the card
            if (cardInDeck != null) {
                BottledPlaceholderField.inBottledPlaceholderField.set(cardInDeck, false); // In our SpireField - set the card to no longer be bottled. (Unbottle it)
            }
        }
    }

    @Override
    public void update() {
        super.update(); //Do all of the original update() method in AbstractRelic

        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            // If the card hasn't been bottled yet and we have cards selected in the gridSelectScreen (from onEquip)
            cardSelected = true; //Set the cardSelected boolean to be true - we're about to bottle the card.
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0); // The custom Savable "card" is going to equal
            // The card from the selection screen (it's only 1, so it's at index 0)
            BottledPlaceholderField.inBottledPlaceholderField.set(card, true); // Use our custom spire field to set that card to be bottled.
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.INCOMPLETE) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE; // The room phase can now be set to complete (From INCOMPLETE in onEquip)
            AbstractDungeon.gridSelectScreen.selectedCards.clear(); // Always clear your grid screen after using it.
            setDescriptionAfterLoading(); // Set the description to reflect the bottled card (the method is at the bottom of this file)
        }
    }


    @Override
    public void atBattleStartPreDraw() {
        PirateMod.logger.info("enter atBattleStartPreDraw()");
        Iterator<AbstractCard> iterator = AbstractDungeon.player.drawPile.group.iterator();
        while (iterator.hasNext()){
            AbstractCard c = iterator.next();
            PirateMod.logger.info("c.cardID: " + c.cardID);
            if (c.uuid.equals(card.uuid)){
                AbstractDungeon.player.limbo.addToTop(c);
                iterator.remove();
            }
        }

    }

    @Override
    public void atTurnStartPostDraw() {
        super.atTurnStartPostDraw();
    }

    @Override
    public void atBattleStart() {
        PirateMod.logger.info("enter atBattleStart()");

            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            PirateMod.logger.info("enter update()");
                            Iterator<AbstractCard> iterator = AbstractDungeon.player.limbo.group.iterator();
                            while (iterator.hasNext()){
                                AbstractCard c = iterator.next();
                                PirateMod.logger.info("c.cardID: " + c.cardID);
                                if (c.uuid.equals(card.uuid)){
                                    AbstractDungeon.player.drawPile.addToRandomSpot(c);
                                    iterator.remove();
                                }
                            }

                            isDone = true;
                        }
                    });
                    isDone = true;
                }
            });

    }

    // Change description after relic is already loaded to reflect the bottled card.
    public void setDescriptionAfterLoading() {
        this.description = DESCRIPTIONS[1] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    // Standard description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
