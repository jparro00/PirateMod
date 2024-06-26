package thePirate.audio;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundInfo;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import thePirate.PirateMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PirateSoundMaster extends SoundMaster {

    private HashMap<String, Sfx> map = new HashMap();
    private ArrayList<SoundInfo> fadeOutList = new ArrayList();
    private static final String SFX_DIR = PirateMod.getModID() + "Resources/audio/sound/";

    public PirateSoundMaster(){
        long startTime = System.currentTimeMillis();
        Settings.SOUND_VOLUME = Settings.soundPref.getFloat("Sound Volume", 0.5F);
        this.map.put("CANNON_HIT_SHIP", load("cannon_hit_ship_short.ogg"));
        this.map.put("CANNON_FIRE", load("cannon_fire.ogg"));
        this.map.put("CHAINS", load("chains_crashing.ogg"));
        this.map.put("INK_SPLAT_CANNON", load("ink_splat.ogg"));
        this.map.put("MONKEY_1", load("monkey_sound_1.ogg"));
        this.map.put("MONKEY_2", load("monkey_sound_2.ogg"));
        this.map.put("MONKEY_3", load("monkey_sound_3.ogg"));

        PirateMod.logger.info("Sound Effect Volume: " + Settings.SOUND_VOLUME);
        PirateMod.logger.info("Loaded " + this.map.size() + " Sound Effects");
        PirateMod.logger.info("SFX load time: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private Sfx load(String filename) {
        return this.load(filename, false);
    }

    private Sfx load(String filename, boolean preload) {
        return new Sfx(SFX_DIR + filename, preload);
    }

    public void update() {
        Iterator<SoundInfo> i = this.fadeOutList.iterator();

        while(i.hasNext()) {
            SoundInfo e = (SoundInfo)i.next();
            e.update();
            Sfx sfx = (Sfx)this.map.get(e.name);
            if (sfx != null) {
                if (e.isDone) {
                    sfx.stop(e.id);
                    i.remove();
                } else {
                    sfx.setVolume(e.id, Settings.SOUND_VOLUME * Settings.MASTER_VOLUME * e.volumeMultiplier);
                }
            }
        }

    }

    public void preload(String key) {
        if (this.map.containsKey(key)) {
            PirateMod.logger.info("Preloading: " + key);
            long id = ((Sfx)this.map.get(key)).play(0.0F);
            ((Sfx)this.map.get(key)).stop(id);
        } else {
            PirateMod.logger.info("Missing: " + key);
        }

    }

    public long play(String key, boolean useBgmVolume) {
        if (CardCrawlGame.MUTE_IF_BG && Settings.isBackgrounded) {
            return 0L;
        } else if (this.map.containsKey(key)) {
            return useBgmVolume ? ((Sfx)this.map.get(key)).play(Settings.MUSIC_VOLUME * Settings.MASTER_VOLUME) : ((Sfx)this.map.get(key)).play(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME);
        } else {
            PirateMod.logger.info("Missing: " + key);
            return 0L;
        }
    }

    public long play(String key) {
        return CardCrawlGame.MUTE_IF_BG && Settings.isBackgrounded ? 0L : this.play(key, false);
    }

    public long play(String key, float pitchVariation) {
        if (CardCrawlGame.MUTE_IF_BG && Settings.isBackgrounded) {
            return 0L;
        } else if (this.map.containsKey(key)) {
            return ((Sfx)this.map.get(key)).play(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME, 1.0F + MathUtils.random(-pitchVariation, pitchVariation), 0.0F);
        } else {
            PirateMod.logger.info("Missing: " + key);
            return 0L;
        }
    }

    public long playA(String key, float pitchAdjust) {
        if (CardCrawlGame.MUTE_IF_BG && Settings.isBackgrounded) {
            return 0L;
        } else if (this.map.containsKey(key)) {
            return ((Sfx)this.map.get(key)).play(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME, 1.0F + pitchAdjust, 0.0F);
        } else {
            PirateMod.logger.info("Missing: " + key);
            return 0L;
        }
    }

    public long playV(String key, float volumeMod) {
        if (CardCrawlGame.MUTE_IF_BG && Settings.isBackgrounded) {
            return 0L;
        } else if (this.map.containsKey(key)) {
            return ((Sfx)this.map.get(key)).play(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME * volumeMod, 1.0F, 0.0F);
        } else {
            PirateMod.logger.info("Missing: " + key);
            return 0L;
        }
    }

    public long playAV(String key, float pitchAdjust, float volumeMod) {
        if (CardCrawlGame.MUTE_IF_BG && Settings.isBackgrounded) {
            return 0L;
        } else if (this.map.containsKey(key)) {
            return ((Sfx)this.map.get(key)).play(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME * volumeMod, 1.0F + pitchAdjust, 0.0F);
        } else {
            PirateMod.logger.info("Missing: " + key);
            return 0L;
        }
    }

    public long playAndLoop(String key) {
        if (this.map.containsKey(key)) {
            return ((Sfx)this.map.get(key)).loop(Settings.SOUND_VOLUME * Settings.MASTER_VOLUME);
        } else {
            PirateMod.logger.info("Missing: " + key);
            return 0L;
        }
    }

    public long playAndLoop(String key, float volume) {
        if (this.map.containsKey(key)) {
            return ((Sfx)this.map.get(key)).loop(volume);
        } else {
            PirateMod.logger.info("Missing: " + key);
            return 0L;
        }
    }

    public void adjustVolume(String key, long id, float volume) {
        ((Sfx)this.map.get(key)).setVolume(id, volume);
    }

    public void adjustVolume(String key, long id) {
        ((Sfx)this.map.get(key)).setVolume(id, Settings.SOUND_VOLUME * Settings.MASTER_VOLUME);
    }

    public void fadeOut(String key, long id) {
        this.fadeOutList.add(new SoundInfo(key, id));
    }

    public void stop(String key, long id) {
        ((Sfx)this.map.get(key)).stop(id);
    }

    public void stop(String key) {
        if (this.map.get(key) != null) {
            ((Sfx)this.map.get(key)).stop();
        }

    }
}
