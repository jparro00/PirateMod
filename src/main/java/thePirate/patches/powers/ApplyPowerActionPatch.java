package thePirate.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = "update"
        )
public class ApplyPowerActionPatch {

/*
    //Patching ApplyPowerAction to account for CorruptArtifact
    public SpireReturn<Void> Prefix(ApplyPowerAction _instance, float ___duration, float ___startingDuration, AbstractPower ___powerToApply){

        if (_instance.target != null && !_instance.target.isDeadOrEscaped()) {
            if (___duration == ___startingDuration) {
                if (_instance.target.hasPower(CorruptArtifactPower.POWER_ID) && ___powerToApply.type == AbstractPower.PowerType.BUFF) {
                    AbstractDungeon.actionManager.addToTop(new TextAboveCreatureAction(_instance.target, _instance.TEXT[0]));
                    ___duration -= Gdx.graphics.getDeltaTime();
                    CardCrawlGame.sound.play("NULLIFY_SFX");
                    _instance.target.getPower(CorruptArtifactPower.POWER_ID).flashWithoutSound();
                    _instance.target.getPower(CorruptArtifactPower.POWER_ID).onSpecificTrigger();
                    return SpireReturn.Return();
                }
            }
        }
        return SpireReturn.Continue();
    }
*/

}
