package thePirate.cards.attacks;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.powers.FreeCannonballPower;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCannonBallCard extends AbstractDynamicCard {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PirateMod.makeID("CardDescriptors"));

    public AbstractCannonBallCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        this(id, img, cost, type, color, rarity, target, false);
    }
    public AbstractCannonBallCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target, boolean hardcore) {
        super(id, img, cost, type, color, rarity, target, hardcore);
    }

    @Override
    public List<String> getCardDescriptors() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(uiStrings.TEXT[1]);
        return retVal;
    }

    @Override
    public boolean freeToPlay() {

        if (super.freeToPlay()) {
            return true;
        } else {
            return AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hasPower(FreeCannonballPower.POWER_ID);
        }
    }
}
