package thePirate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ImageMaster {
    public static TextureAtlas.AtlasRegion FRAME_SMALL_LURE_REGION;// = cardUiAtlas.findRegion("512_lure_frame");
    public static TextureAtlas.AtlasRegion FRAME_LARGE_LURE_REGION;// = cardUiAtlas.findRegion("1024_lure_frame");
    public static TextureAtlas.AtlasRegion FRAME_SMALL_ATTACK_REGION;// = cardUiAtlas.findRegion("512_predator_attack_frame");
    public static TextureAtlas.AtlasRegion FRAME_LARGE_ATTACK_REGION;// = cardUiAtlas.findRegion("1024_predator_attack_frame");
    public static TextureAtlas.AtlasRegion BANNER_SMALL_REGION;// = cardUiAtlas.findRegion("512_banner_predator");
    public static TextureAtlas.AtlasRegion BANNER_LARGE_REGION;//= cardUiAtlas.findRegion("1024_banner_predator");
    public static TextureAtlas.AtlasRegion FRAME_SMALL_SKILL_REGION;// = cardUiAtlas.findRegion("512_predator_skill_frame");
    public static TextureAtlas.AtlasRegion FRAME_LARGE_SKILL_REGION;// = cardUiAtlas.findRegion("1024_predator_skill_frame");
    public static TextureAtlas.AtlasRegion FRAME_SMALL_POWER_REGION;// = cardUiAtlas.findRegion("512_predator_power_frame");
    public static TextureAtlas.AtlasRegion FRAME_LARGE_POWER_REGION;// = cardUiAtlas.findRegion("1024_predator_power_frame");
    public static TextureAtlas cardUiAtlas;

    public static void initialize() {
        cardUiAtlas = new TextureAtlas(Gdx.files.internal(PirateMod.getModID() + "Resources/images/cardui/customCardUi.atlas"));
        FRAME_SMALL_LURE_REGION = cardUiAtlas.findRegion("512_lure_frame");
        FRAME_LARGE_LURE_REGION = cardUiAtlas.findRegion("1024_lure_frame");
        FRAME_SMALL_ATTACK_REGION = cardUiAtlas.findRegion("512_predator_attack_frame");
        FRAME_LARGE_ATTACK_REGION = cardUiAtlas.findRegion("1024_predator_attack_frame");
        BANNER_SMALL_REGION = cardUiAtlas.findRegion("512_banner_predator");
        BANNER_LARGE_REGION= cardUiAtlas.findRegion("1024_banner_predator");
        FRAME_SMALL_SKILL_REGION = cardUiAtlas.findRegion("512_predator_skill_frame");
        FRAME_LARGE_SKILL_REGION = cardUiAtlas.findRegion("1024_predator_skill_frame");
        FRAME_SMALL_POWER_REGION = cardUiAtlas.findRegion("512_predator_power_frame");
        FRAME_LARGE_POWER_REGION = cardUiAtlas.findRegion("1024_predator_power_frame");

    }

}
