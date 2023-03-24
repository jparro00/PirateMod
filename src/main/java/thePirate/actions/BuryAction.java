package thePirate.actions;


import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import thePirate.PirateMod;
import thePirate.cards.attacks.BeachBuddy;
import thePirate.patches.actions.CardCounterPatches;
import thePirate.powers.OnBury;
import thePirate.util.CardUtil;
import thePirate.util.TextureLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static thePirate.PirateMod.makeScreenPath;

public class BuryAction extends AbstractGameAction {
    public static final String[] TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean optional;
    public List<AbstractCard> preselectedCards;

    private static final Texture buryLabel= TextureLoader.getTexture(makeScreenPath("bury_screen_icon.png"));

    public List<AbstractCard> cardsSelected;

    public BuryAction(int numberOfCards, boolean optional) {
        this(numberOfCards,optional, new ArrayList<AbstractCard>());
    }
    public BuryAction(int numberOfCards, boolean optional, List<AbstractCard> preselectedCards) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
        this.optional = optional;
        this.cardsSelected = new ArrayList<>();
        this.preselectedCards = preselectedCards;

    }

    //sepcial constructor for removing specific cards without a selection
    public BuryAction(List<AbstractCard> cardsSelected){
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = 0;
        this.optional = false;
        this.cardsSelected = cardsSelected;
        this.preselectedCards = new ArrayList<>();
    }

    public BuryAction(AbstractCard card){
        this(new ArrayList<AbstractCard>(Arrays.asList(new AbstractCard[]{card})));
    }


    public BuryAction(int numberOfCards) {
        this(numberOfCards, false);
    }

    public void queueBuryIcon(){
        BuryIconPatch.isBury = true;
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                BuryIconPatch.isBury = false;
                isDone = true;
            }
        });
    }

    public void update() {
        queueBuryIcon();
        if (this.duration == this.startDuration) {
            //first check if cardsSelected was passed at creation
            if (!cardsSelected.isEmpty()){
                Iterator<AbstractCard> iter = cardsSelected.iterator();

                while(iter.hasNext()) {
                    AbstractCard c = iter.next();
                    this.player.drawPile.moveToDiscardPile(c);
                }

                this.isDone = true;

            }else if (!this.player.drawPile.isEmpty() && this.numberOfCards > 0) {
                AbstractCard c;
                Iterator var6;
                if (this.player.drawPile.size() <= this.numberOfCards && !this.optional) {
                    ArrayList<AbstractCard> cardsToMove = new ArrayList();
                    var6 = this.player.drawPile.group.iterator();

                    while(var6.hasNext()) {
                        c = (AbstractCard)var6.next();
                        cardsToMove.add(c);
                    }

                    var6 = cardsToMove.iterator();

                    while(var6.hasNext()) {
                        c = (AbstractCard)var6.next();
                        this.player.drawPile.moveToDiscardPile(c);
                        cardsSelected.add(c);
                    }

                    this.isDone = true;
                } else {
                    CardGroup temp = new CardGroup(CardGroupType.UNSPECIFIED);
                    var6 = this.player.drawPile.group.iterator();

                    while(var6.hasNext()) {
                        c = (AbstractCard)var6.next();
                        temp.addToBottom(c);
                    }

                    if (!player.hasRelic(FrozenEye.ID)){
                        temp.sortAlphabetically(true);
                    }
                    temp.sortByRarityPlusStatusCardType(false);

                    //set text (with case for Frozen Eye)
                    String text;
                    if (this.numberOfCards == 1){
                        text = TEXT[0];
                    }else {
                        text = TEXT[1] + numberOfCards + TEXT[2];
                    }
/*
                    if (player.hasRelic(FrozenEye.ID)){
                        text += " NL " + TEXT[3];
                    }
*/

                    if (this.optional) {
                        AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, text);

                        //only add cards that are in the players draw pile
                        List<AbstractCard> combatPreselectedCards = new ArrayList<>();
                        for (AbstractCard preselectedCard : preselectedCards){
                            for (AbstractCard drawCard : player.drawPile.group){
                                if (preselectedCard.uuid.equals(drawCard.uuid)){
                                    combatPreselectedCards.add(drawCard);
                                }
                            }

                        }
                        //add the cards and make them glow
                        for (AbstractCard card : combatPreselectedCards){
                            card.beginGlowing();
                            Color originalGlowColor = card.glowColor;
                            addToBot(new AbstractGameAction() {
                                @Override
                                public void update() {
                                    card.glowColor = originalGlowColor;
                                    isDone = true;
                                }
                            });
                            card.glowColor = Color.YELLOW;
                            AbstractDungeon.gridSelectScreen.selectedCards.add(card);
                        }
                        if (combatPreselectedCards.size() > 0){
                            ReflectionHacks.setPrivate(AbstractDungeon.gridSelectScreen, GridCardSelectScreen.class,"cardSelectAmount",combatPreselectedCards.size());
                        }

                        //TODO: not sure how this would work for nonOptional bury screens...
                    } else {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, text, false);
                    }

                    this.tickDuration();
                }
            } else {
                this.isDone = true;
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    this.player.drawPile.moveToDiscardPile(c);
                    cardsSelected.add(c);
                }

                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }

            this.tickDuration();
        }

        //exhaust cards if player has too much rum power
        if(isDone && cardsSelected != null && cardsSelected.size() > 0){


            //run onBury for each card buried
            for (AbstractCard card : cardsSelected){
                for (AbstractCard c : player.discardPile.group){
                    if (c instanceof OnBury){
                        ((OnBury)c).onBury(card);
                    }
                }
                for (AbstractPower playerPower : player.powers){
                    if(playerPower instanceof OnBury){
                        ((OnBury)playerPower).onBury(card);
                    }
                }
            }

            //run onBuryCards once for all cards
            for (AbstractCard c : CardUtil.getCombatCards(false, false)){
                if (c instanceof OnBury){
                    ((OnBury)c).onBuryCards(cardsSelected);
                }
            }
            //run for powers
            for (AbstractPower playerPower : player.powers){
                if(playerPower instanceof OnBury){
                    ((OnBury)playerPower).onBuryCards(cardsSelected);
                }
            }
            //run for relics
            for (AbstractRelic relic : player.relics){
                if(relic instanceof OnBury){
                    ((OnBury) relic).onBuryCards(cardsSelected);
                }
            }

            //Increment cards buried this turn/combat
            CardCounterPatches.incrementBury(cardsSelected.size());


        }

        //return beach buddy cards to hand
        if(cardsSelected != null && cardsSelected.size() > 0){
            for (AbstractCard card : player.discardPile.group){
                if (card instanceof BeachBuddy){
                    ((BeachBuddy) card).onBuryCards(cardsSelected);
                }
            }
        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString("thePirate:BuryAction").TEXT;
    }

    @SpirePatch2(clz = GridCardSelectScreen.class, method = "render", paramtypez = { SpriteBatch.class})
    public static class BuryIconPatch {

        public static boolean isBury;
        @SpirePrefixPatch
        public static void renderBuryIcon(GridCardSelectScreen __instance, SpriteBatch sb) {
            if (!PeekButton.isPeeking && isBury){
                Color color = sb.getColor();
                sb.setColor(Color.WHITE);
                float derp = 1;
                if (!PirateMod.disableDigBuryPulse.toggle.enabled){
                    derp = Interpolation.swingOut.apply(1.0F, 1.1F, MathUtils.cosDeg((float)(System.currentTimeMillis() / 4L % 360L)) / 12.0F);
                }
                sb.draw(buryLabel, (32 * Settings.scale), (float) Settings.HEIGHT / 2.0F + (64 * Settings.scale), 0F, 0F, 256.0F, 256.0F, Settings.scale * derp, Settings.scale * derp, 0.0F, 0, 0, 256, 256, false, false);

                if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(FrozenEye.ID)){
                    Texture texture = AbstractDungeon.player.getRelic(FrozenEye.ID).img;
                    sb.setColor(Color.WHITE);
                    sb.draw(texture, (128 * Settings.scale), (float) Settings.HEIGHT / 2.0F + (64 * Settings.scale), 0F, 0F, 256.0F, 256.0F, Settings.scale * derp, Settings.scale * derp, 0.0F, 0, 0, 256, 256, false, false);

                }



                sb.setColor(color);
            }
        }
    }
    @SpirePatch2(clz = GridCardSelectScreen.class, method = "update")
    public static class BuryPreSelectedCardsPatch {

        @SpireInsertPatch(
                locator=Locator.class
        )
        public static SpireReturn<Void> Insert(GridCardSelectScreen __instance){
            if(BuryIconPatch.isBury){
                int numCards = ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class,"numCards");
                int cardSelectAmount = ReflectionHacks.getPrivate(__instance,GridCardSelectScreen.class,"cardSelectAmount");
                if (cardSelectAmount > numCards){
                    ReflectionHacks.setPrivate(__instance,GridCardSelectScreen.class,"cardSelectAmount", cardSelectAmount - 1);
                    AbstractCard card = __instance.selectedCards.get(0);
                    card.stopGlowing();
                    __instance.selectedCards.remove(0);
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws PatchingException, CannotCompileException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "numCards");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }


    }
}
