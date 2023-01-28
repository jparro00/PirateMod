package thePirate.cards.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CaptainsWheel;
import com.megacrit.cardcrawl.relics.HornCleat;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.targeting.RelicTargeting;
import thePirate.characters.ThePirate;

import static thePirate.PirateMod.makeCardPath;

public class DeathKnell extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = RelicTargeting.RELIC;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(DeathKnell.class.getSimpleName());
    public static final String IMG = makeCardPath(DeathKnell.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public DeathKnell() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractRelic target = RelicTargeting.getTarget(this);

        if (target != null && !specialCase(target)){
            target.grayscale = false;
            target.atPreBattle();
            target.atBattleStartPreDraw();
            target.atBattleStart();
            target.atTurnStart();
            target.atTurnStartPostDraw();
            target.onVictory();
            target.flash();
        }

    }

    public boolean specialCase(AbstractRelic relic){
        boolean specialCase = false;
        if (relic instanceof HornCleat){
            relic.grayscale = false;
            relic.atTurnStart();
            relic.atBattleStart();
            relic.atTurnStart();
            specialCase = true;
        }
        else if (relic instanceof CaptainsWheel){
            relic.grayscale = false;
            relic.atTurnStart();
            relic.atBattleStart();
            relic.atTurnStart();
            specialCase = true;
        }
        return specialCase;

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }
}
