package thePirate.cards.lures;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thePirate.PirateMod;
import thePirate.cards.predators.AbstractPredator;
import thePirate.cards.predators.AncientCrab;

import java.util.ArrayList;
import java.util.List;

import static thePirate.PirateMod.makeCardPath;

public class WithdrawWeapons extends AbstractLure {

    public static final int MAGIC = 1;

    public static final String SIMPLE_NAME = WithdrawWeapons.class.getSimpleName();
    public static final String ID = PirateMod.makeID(SIMPLE_NAME);
    public static final String IMG = makeCardPath(SIMPLE_NAME + ".png", CardType.SKILL);

    public WithdrawWeapons(){
        this(true);
    }
    public WithdrawWeapons(boolean showPreview) {
        super(ID, IMG, MAGIC, showPreview);
        magicNumber = baseMagicNumber = MAGIC;
        if(showPreview)
            cardsToPreview = new AncientCrab(false);
    }

    @Override
    public List<AbstractPower> applyDebuffs(AbstractPlayer p) {
        List<AbstractPower> powers = new ArrayList<>();
        powers.add(new WeakPower(AbstractDungeon.player,magicNumber,false));
        return powers;
    }

    @Override
    public AbstractPredator getPredator() {
        return new AncientCrab();
    }

}
