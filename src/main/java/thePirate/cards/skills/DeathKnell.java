package thePirate.cards.skills;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.*;
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

    @Override
    public void render(SpriteBatch sb) {
        if (ReflectionHacks.getPrivate(this, AbstractCard.class,"hovered")){
            if(AbstractDungeon.player != null && !AbstractDungeon.player.inSingleTargetMode){
                for (AbstractRelic relic : AbstractDungeon.player.relics){
                    if (RelicTargeting.canTarget(relic)){
                        RelicTargeting.renderReticle(relic, sb);
                    }
                }
            }
        }
        super.render(sb);
    }

    public boolean specialCase(AbstractRelic relic){
        boolean specialCase = false;
        if (relic instanceof HornCleat){
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    relic.grayscale = false;
                    relic.atBattleStart();
                    relic.atTurnStart();
                    isDone = true;
                }
            });
            specialCase = true;
        }
        else if (relic instanceof CaptainsWheel){
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    relic.grayscale = false;
                    relic.atBattleStart();
                    relic.atTurnStart();
                    isDone = true;
                }
            });
            specialCase = true;
        }
        else if (relic instanceof AncientTeaSet){
            addToTop(new GainEnergyAction(2));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
            specialCase = true;
        }
        else if (relic instanceof CoffeeDripper ||
                    relic instanceof PhilosopherStone ||
                    relic instanceof CursedKey ||
                    relic instanceof Sozu ||
                    relic instanceof FusionHammer ||
                    relic instanceof BustedCrown ||
                    relic instanceof SlaversCollar ||
                    relic instanceof Ectoplasm ||
                    relic instanceof RunicDome
            ){
            addToTop(new GainEnergyAction(1));
            specialCase = true;
        }
        else if (relic instanceof VelvetChoker){
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {

                    addToTop(new GainEnergyAction(1));
                    relic.counter = 1;
                    relic.flash();
                    isDone = true;
                }
            });

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
