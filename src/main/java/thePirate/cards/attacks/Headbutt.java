package thePirate.cards.attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thePirate.PirateMod;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class Headbutt extends com.megacrit.cardcrawl.cards.red.Headbutt {

    // STAT DECLARATION

    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Headbutt.class.getSimpleName());
    // /TEXT DECLARATION/

    public Headbutt() {
        super();
        this.color = COLOR;
    }

/*
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }

    }
*/
    public AbstractCard makeCopy() {
        return new Headbutt();
    }
}
