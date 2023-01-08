package thePirate.patches.ui.panels;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thePirate.DefaultMod;
import thePirate.powers.OnUseEnergy;
import thePirate.patches.characters.AbstractPlayerPatch;

import java.util.Iterator;

@SpirePatch(
        clz= EnergyPanel.class,
        method="useEnergy"
)
public class EnergyPanelPatch {

    public static void Prefix(int e){
        AbstractPlayer p = AbstractDungeon.player;

        //execute onUseEnergy
        for (Iterator<AbstractPower> iter = p.powers.iterator(); iter.hasNext(); ){
            AbstractPower power = iter.next();
            if (power instanceof OnUseEnergy){
                ((OnUseEnergy) power).onUseEnergy(e);
            }
        }

        //set energyUsed
        if(p != null){
            DefaultMod.logger.info("using " + e + " energy");
            int energyUsedThisTurn = AbstractPlayerPatch.energyUsedThisTurn.get(p);
            energyUsedThisTurn += e;
            AbstractPlayerPatch.energyUsedThisTurn.set(p, energyUsedThisTurn);
        }
    }

}
