package thePirate.cards.skills;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.Malaise;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thePirate.DefaultMod;
import thePirate.actions.BuryAction;
import thePirate.cards.AbstractDynamicCard;
import thePirate.cards.attacks.InkBlast;
import thePirate.cards.attacks.InkShot;
import thePirate.characters.TheDefault;
import thePirate.powers.InkPower;

import static thePirate.DefaultMod.makeCardPath;

public class DarkOffering extends AbstractDynamicCard {

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE; //  Up to you, I like auto-complete on these
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  //   since they don't change much.
    private static final CardType TYPE = CardType.SKILL;       //
    public static final CardColor COLOR = TheDefault.Enums.COLOR_GRAY;

    private static final int COST = -1;
    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 1;

    // /STAT DECLARATION/

    // TEXT DECLARATION
    public static final String ID = DefaultMod.makeID(DarkOffering.class.getSimpleName());
    public static final String IMG = makeCardPath(DarkOffering.class.getSimpleName() + ".png", TYPE);
    // /TEXT DECLARATION/

    public DarkOffering() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        exhaust = true;
    }


    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        if (this.upgraded) {
            ++effect;
        }

        if (effect > 0) {
            addToBot(new BuryAction(effect, true));
            for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters){
                int inkEffect = effect * magicNumber;
                addToBot(new ApplyPowerAction(mo,p,new InkPower(mo,p,inkEffect), inkEffect));
            }
            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }
    }


    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (UPGRADED_MAGIC > 0)
                upgradeMagicNumber(UPGRADED_MAGIC);
            upgradeDescription();
        }
    }
}
