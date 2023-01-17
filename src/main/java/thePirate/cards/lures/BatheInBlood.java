package thePirate.cards.lures;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thePirate.PirateMod;
import thePirate.cards.predators.AbstractPredator;
import thePirate.cards.predators.Kraken;

import java.util.ArrayList;
import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class BatheInBlood extends AbstractLure {

    public static final int MAGIC = 1;

    public static final String SIMPLE_NAME = BatheInBlood.class.getSimpleName();
    public static final String ID = PirateMod.makeID(SIMPLE_NAME);
    public static final String IMG = makeCardPath(SIMPLE_NAME + ".png", CardType.SKILL);

    public BatheInBlood(){
        this(true);
    }
    public BatheInBlood(boolean showPreview) {
        super(ID, IMG, MAGIC, showPreview);
        magicNumber = baseMagicNumber = MAGIC;
        if(showPreview)
            cardsToPreview = new Kraken(false);
    }

    @Override
    public List<AbstractPower> applyDebuffs(AbstractPlayer p) {
        List<AbstractPower> powers = new ArrayList<>();
        powers.add(new VulnerablePower(AbstractDungeon.player,magicNumber,false));
        return powers;
    }

    @Override
    public AbstractPredator getPredator() {
        return new Kraken();
    }

}
