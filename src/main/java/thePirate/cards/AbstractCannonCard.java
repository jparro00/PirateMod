package thePirate.cards;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import thePirate.PirateMod;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCannonCard extends AbstractDynamicCard{

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(PirateMod.makeID("CardDescriptors"));

    @Override
    public boolean canUpgrade() {
        return false;
    }

    public AbstractCannonCard(String id, String img, int cost, CardType type, CardColor color, CardTarget target) {
        super(id, img, cost, type, color, RARITY, target);
    }

    @Override
    public List<String> getCardDescriptors() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(uiStrings.TEXT[0]);
        return retVal;
    }

}
