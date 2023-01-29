package thePirate.cards.lures;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.SpawnModificationCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thePirate.ImageMaster;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.Purgable;
import thePirate.cards.predators.AbstractPredator;
import thePirate.characters.ThePirate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class AbstractLure extends AbstractDynamicCard implements Purgable, SpawnModificationCard {

    // STAT DECLARATION

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PirateMod.makeID("CardDescriptors"));
    private static final CardTarget TARGET = CardTarget.NONE;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;
    public static final CardRarity RARITY = CardRarity.SPECIAL;

    public boolean purge;
    public boolean queuedForPurge;

    private static final int COST = -2;
    public boolean showPreview;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    // /TEXT DECLARATION/

    public AbstractLure(String id, String img, int magicNumber, boolean showPreview) {
        super(id, img, COST, TYPE, COLOR, RARITY, TARGET);
        this.showPreview = showPreview;
        this.magicNumber = baseMagicNumber = magicNumber;
        isInnate = true;
        tags.add(CardTags.HEALING);
        setDisplayRarity(CardRarity.UNCOMMON);
        this.frameSmallRegion = ImageMaster.FRAME_SMALL_LURE_REGION;
        this.frameLargeRegion = ImageMaster.FRAME_LARGE_LURE_REGION;
    }

    @Override
    public List<String> getCardDescriptors() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(uiStrings.TEXT[2]);
        return retVal;
    }

    public static List<AbstractLure> getSpawnableLures(){
        List<AbstractLure> lures = new ArrayList<>();
        lures.add(new BatheInBlood());
        lures.add(new WithdrawWeapons());
        lures.add(new LowerDefenses());

        Iterator<AbstractLure> iterator = lures.iterator();
        while (iterator.hasNext()){
            AbstractLure lure = iterator.next();
            //Note: we are passing null here, assuming the variable is never used in AbstractLure.canSpawn
            if (!lure.canSpawn(null)){
                iterator.remove();
            }
        }
        return lures;
    }

    @Override
    public boolean canSpawn(ArrayList<AbstractCard> currentRewardCards) {
        boolean canSpawn = true;
        AbstractPlayer player = AbstractDungeon.player;
        if(player != null){
            for(AbstractCard card : player.masterDeck.group){
                if(card instanceof AbstractLure){
                    canSpawn = false;
                    break;
                }
            }
        }
        if (canSpawn && currentRewardCards != null){
            for (AbstractCard card : currentRewardCards){
                if (!card.uuid.equals(this.uuid) && card instanceof AbstractLure){
                    canSpawn = false;
                    break;
                }
            }
        }
        return canSpawn;
    }

    @Override
    public boolean canSpawnShop(ArrayList<AbstractCard> currentShopCards) {
        return canSpawn(currentShopCards);
    }

    public boolean canUpgrade(){
        return false;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        this.cantUseMessage = CardCrawlGame.languagePack.getUIString(PirateMod.CANT_PLAY).TEXT[0];
        return false;
    }


    @Override
    public void triggerWhenDrawn() {
        AbstractPlayer player = AbstractDungeon.player;
        List<AbstractPower> powers = applyDebuffs(AbstractDungeon.player);
        for (AbstractPower power : powers){
            addToBot(new ApplyPowerAction(player,player,power, power.amount));
        }
        addToBot(new DrawCardAction(1));
        addToTop(new DiscardSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }
    public abstract List<AbstractPower> applyDebuffs(AbstractPlayer p);

    @Override
    public void setPurge(boolean purge) {
        this.purge = purge;

    }

    @Override
    public boolean getPurge() {
        return purge;
    }

    @Override
    public boolean queuedForPurge() {
        return queuedForPurge;
    }

    @Override
    public void setQueuedForPurge(boolean queuedForPurge) {
        this.queuedForPurge = queuedForPurge;
    }

    public abstract AbstractPredator getPredator();
}
