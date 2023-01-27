package thePirate.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.commons.lang3.StringUtils;
import thePirate.PirateMod;

import java.util.ArrayList;
import java.util.List;

public class CardUtil {

    public static List<AbstractCard> getCombatCards(boolean includeExhaust, boolean includeLimbo){
        List<AbstractCard> pool = new ArrayList<>();
        if (AbstractDungeon.player != null){
            AbstractPlayer player = AbstractDungeon.player;
            pool.addAll(player.hand.group);
            pool.addAll(player.discardPile.group);
            pool.addAll(player.drawPile.group);
            if (includeExhaust)
                pool.addAll(player.exhaustPile.group);
            if (includeLimbo)
                pool.addAll(player.limbo.group);

        }
        return pool;
    }

    public static void printCards(){

        CardGroup pool = new CardGroup(CardGroup.CardGroupType.CARD_POOL);
        pool.group.addAll(AbstractDungeon.commonCardPool.group);
        pool.group.addAll(AbstractDungeon.uncommonCardPool.group);
        pool.group.addAll(AbstractDungeon.rareCardPool.group);

        for (AbstractCard card : pool.group){
            List output = new ArrayList<>();
            output.add(card.name);
            output.add(toProperCase(card.type.name()));
            output.add(card.rawDescription);
            output.add(card.cost);
            output.add(toProperCase(card.rarity.name()));
            PirateMod.logger.info(StringUtils.join(output,'~'));
        }
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }

}
