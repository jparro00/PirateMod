package thePirate.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
}
