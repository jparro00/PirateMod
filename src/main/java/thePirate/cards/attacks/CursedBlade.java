package thePirate.cards.attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thePirate.PirateMod;
import thePirate.cards.AbstractDynamicCard;
import thePirate.characters.ThePirate;
import thePirate.powers.CorruptArtifactPower;
import thePirate.util.TextureLoader;

import static thePirate.PirateMod.makeCardPath;

public class CursedBlade extends AbstractDynamicCard {

    // STAT DECLARATION
    private static final Texture corruptArtifactTexture = TextureLoader.getTexture(PirateMod.makePowerPath(CorruptArtifactPower.class.getSimpleName()+"_" + 32 +".png"));
    public static final TextureAtlas.AtlasRegion CORRUPT_ARTIFACT_REMINDER = new TextureAtlas.AtlasRegion(corruptArtifactTexture, 0,0,corruptArtifactTexture.getWidth(), corruptArtifactTexture.getHeight());

    private static final CardRarity RARITY = CardRarity.COMMON; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.ATTACK;       //
    public static final CardColor COLOR = ThePirate.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 1;

    private static final int DAMAGE = 12;
    private static final int UPGRADED_DMG = 5;
    public static final int MAGIC = 1;

    public boolean usedThisCombat;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = PirateMod.makeID(CursedBlade.class.getSimpleName());
    public static final String IMG = makeCardPath("CursedBlade.png", TYPE);
    // /TEXT DECLARATION/

    public CursedBlade() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
    }



    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (!usedThisCombat){
            this.addToBot(new ApplyPowerAction(p,p,new CorruptArtifactPower(p,p,magicNumber),magicNumber));
        }
        usedThisCombat = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (!usedThisCombat && !PirateMod.disableCursedBladeReminder.toggle.enabled ){
            renderReminder(sb);
        }
    }

    public void renderReminder(SpriteBatch sb){

        if (AbstractDungeon.player != null &&AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !(AbstractDungeon.isScreenUp && (!AbstractDungeon.screen.equals(AbstractDungeon.CurrentScreen.GAME_DECK_VIEW) && !AbstractDungeon.screen.equals(AbstractDungeon.CurrentScreen.DISCARD_VIEW) && !AbstractDungeon.screen.equals(AbstractDungeon.CurrentScreen.HAND_SELECT) && !AbstractDungeon.screen.equals(AbstractDungeon.CurrentScreen.GRID)))) {
            sb.setColor(Color.WHITE);
            renderHelper(sb, CORRUPT_ARTIFACT_REMINDER, current_x, current_y);

            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, ((MathUtils.cosDeg((float) (System.currentTimeMillis() / 5L % 360L)) + 1.25F) / 2F) / 3.0F));
            renderHelper(sb, CORRUPT_ARTIFACT_REMINDER, current_x, current_y);
            sb.setBlendFunction(770, 771);
            sb.setColor(Color.WHITE);
        }

    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_DMG > 0)
                upgradeDamage(UPGRADED_DMG);
            if (UPGRADED_COST != COST)
                upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        CursedBlade cursedBlade = (CursedBlade) super.makeStatEquivalentCopy();
        cursedBlade.usedThisCombat = this.usedThisCombat;
        return cursedBlade;
    }

    static {
        CORRUPT_ARTIFACT_REMINDER.originalHeight = 512;
        CORRUPT_ARTIFACT_REMINDER.originalWidth = 512;
        CORRUPT_ARTIFACT_REMINDER.offsetX = 102;
        CORRUPT_ARTIFACT_REMINDER.offsetY = 352;
    }
}
