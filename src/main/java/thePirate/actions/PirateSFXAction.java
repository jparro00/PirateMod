package thePirate.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import thePirate.PirateMod;

public class PirateSFXAction extends AbstractGameAction {
    private String key;
    private float pitchVar;
    private boolean adjust;

    public PirateSFXAction(String key) {
        this(key, 0.0F, false);
    }

    public PirateSFXAction(String key, float pitchVar) {
        this(key, pitchVar, false);
    }

    public PirateSFXAction(String key, float pitchVar, boolean pitchAdjust) {
        this.pitchVar = 0.0F;
        this.adjust = false;
        this.key = key;
        this.pitchVar = pitchVar;
        this.adjust = pitchAdjust;
        this.actionType = ActionType.WAIT;
    }

    public void update() {
        if (!this.adjust) {
            PirateMod.sound.play(this.key, this.pitchVar);
        } else {
            PirateMod.sound.playA(this.key, this.pitchVar);
        }

        this.isDone = true;
    }
}
