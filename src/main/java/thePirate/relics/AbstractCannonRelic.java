package thePirate.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.cards.Cannon;

import java.util.List;

public abstract class AbstractCannonRelic extends AbstractPirateRelic {

    public AbstractCannonRelic(String id, Texture img, Texture outline ) {
        this(id, img,outline,RelicTier.SPECIAL,LandingSound.HEAVY);
    }
    public AbstractCannonRelic(String id, Texture img,Texture outline ,RelicTier tier, LandingSound landingSound ) {
        this(id, img,outline,tier,landingSound, false);
    }
    public AbstractCannonRelic(String id, Texture img,Texture outline ,RelicTier tier, LandingSound landingSound, boolean hardcore) {
        super(id, img,outline,tier,landingSound,hardcore);
    }


    @Override
    public void onEquip() {
        PirateMod.logger.info("AbstractDungeon.screen: " + AbstractDungeon.screen);
        if (CardCrawlGame.isInARun() && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
            atBattleStartPreDraw();
        }

    }

    // Flash at the start of Battle.
    @Override
    public void atBattleStartPreDraw(){
        PirateMod.logger.info("enter AbstractCannonRelic.atBattleStartPreDraw()");
        flash();
        AbstractDungeon.player.hand.addToHand(new Cannon(getTarget()));
        AbstractDungeon.player.hand.refreshHandLayout();
    }

    public abstract List<AbstractGameAction> onUseCannon(AbstractPlayer p, AbstractMonster m);


    public AbstractCard.CardTarget getTarget(){
        return AbstractCard.CardTarget.ENEMY;
    }

}
