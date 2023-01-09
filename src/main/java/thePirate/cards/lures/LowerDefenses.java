package thePirate.cards.lures;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import thePirate.PirateMod;
import thePirate.cards.predators.ElectricEel;

import java.util.ArrayList;
import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class LowerDefenses extends AbstractLure {

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    public static final int MAGIC = 1;

    public static final String SIMPLE_NAME = LowerDefenses.class.getSimpleName();
    public static final String ID = PirateMod.makeID(SIMPLE_NAME);
    public static final String IMG = makeCardPath(SIMPLE_NAME + ".png", CardType.SKILL);

    public LowerDefenses(){
        this(true);
    }
    public LowerDefenses(boolean showPreview) {
        super(ID, IMG, RARITY, MAGIC, showPreview);
        magicNumber = baseMagicNumber = MAGIC;
        if(showPreview)
            cardsToPreview = new ElectricEel(false);
    }

    @Override
    public List<AbstractPower> applyDebuffs(AbstractPlayer p) {
        List<AbstractPower> powers = new ArrayList<>();
        powers.add(new FrailPower(AbstractDungeon.player,magicNumber,false));
        return powers;
    }

}
