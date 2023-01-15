package thePirate.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.CardHelper;

public abstract class AbstractDynamicPotion extends CustomPotion {

    public static final Color LIQUID = CardHelper.getColor(0,0,0);
    public static final Color HYBRID = null;
    public static final Color SPOTS = null;


    public AbstractDynamicPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
        super(name, id, rarity, size, color);
    }

    public Color getColor(COLOR color){
        switch (color){
            case SPOTS:
                return SPOTS;
            case HYBRID:
                return HYBRID;
            case LIQUID:
                return LIQUID;
            default:
                return LIQUID;
        }
    }

    public enum COLOR {
        LIQUID,
        HYBRID,
        SPOTS
    }
}
