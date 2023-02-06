package thePirate.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thePirate.PirateMod;
import thePirate.characters.ThePirate;
import thePirate.powers.RetainCannonballPower;
import thePirate.relics.AbstractCannonRelic;

import static thePirate.PirateMod.makeCardPath;

public class Cannon extends AbstractCannonCard{




    // STAT DECLARATION
    private static final CardTarget TARGET = CardTarget.SELF;  //   since they don't change much.
    //TODO: need to figure out how to prevent this from triggering gremlin knob
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;
    public static final int vulnerableCount = 1;
    public int cannonballsRetainCount;
    private static final int COST = 0;
    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(Cannon.class.getSimpleName()); // USE THIS ONE FOR THE TEMPLATE;
    public static final String IMG = makeCardPath(Cannon.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public Cannon() {
        this(TARGET);
    }

    public Cannon(CardTarget target){
        super(ID, IMG, COST, TYPE, COLOR, target);
        this.selfRetain = true;
        this.isInnate = true;
        this.exhaust = true;
        this.cannonballsRetainCount = 1;

    }


    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        AbstractPlayer p = AbstractDungeon.player;
        this.addToTop(new ApplyPowerAction(p, p,new RetainCannonballPower(AbstractDungeon.player, cannonballsRetainCount),cannonballsRetainCount,true));
    }



    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        for(AbstractRelic relic : p.relics){
            if(relic instanceof AbstractCannonRelic){
                AbstractCannonRelic cannonRelic = (AbstractCannonRelic)relic;
                for (AbstractGameAction action : cannonRelic.onUseCannon(p,m)){
                    addToBot(action);
                }
            }
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
    }
}
