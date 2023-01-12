package thePirate.cards.attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.cards.AbstractDynamicCard;
import thePirate.powers.FreeCannonballPower;

public abstract class AbstractCannonBallCard extends AbstractDynamicCard {

    public AbstractCannonBallCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

    @Override
    public boolean freeToPlay() {

        if (this.freeToPlayOnce) {
            return true;
        } else {
            return AbstractDungeon.player != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hasPower(FreeCannonballPower.POWER_ID);
        }
    }
}
