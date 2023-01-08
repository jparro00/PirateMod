package thePirate.cards.attacks;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePirate.cards.AbstractDynamicCard;

public abstract class AbstractCannonBallCard extends AbstractDynamicCard {

    public AbstractCannonBallCard(String id, String img, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, color, rarity, target);
    }

}
