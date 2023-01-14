package thePirate.cards;

public abstract class AbstractCannonCard extends AbstractDynamicCard{

    private static final CardRarity RARITY = CardRarity.SPECIAL;

    @Override
    public boolean canUpgrade() {
        return false;
    }

    public AbstractCannonCard(String id, String img, int cost, CardType type, CardColor color, CardTarget target) {
        super(id, img, cost, type, color, RARITY, target);
    }

}
