package thePirate.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import thePirate.util.TextureLoader;

import java.util.Map;

public abstract class AbstractPirateRelic extends CustomRelic {

    public static final String HARDCORE_TEXTURE_PATH = "thePirateResources/images/ui/hardcore.png";
    private static final Texture hardcoreIconTexture = TextureLoader.getTexture(HARDCORE_TEXTURE_PATH);
    public static final TextureAtlas.AtlasRegion HARDCORE_ICON = new TextureAtlas.AtlasRegion(hardcoreIconTexture, 0,0, hardcoreIconTexture.getWidth(), hardcoreIconTexture.getHeight());
    public boolean hardcore;

    private RelicStrings relicStrings;

    public AbstractPirateRelic(String id, Texture img, Texture outline , RelicTier tier, LandingSound landingSound ) {
        this(id, img,outline,tier,landingSound, false);
    }

    public AbstractPirateRelic(String id, Texture img, Texture outline , RelicTier tier, LandingSound landingSound, boolean hardcore) {
        super(id, img,outline,tier,landingSound);
        this.hardcore = hardcore;
        if (this.hardcore && hasHardcoreRelicStrings(id)){
            if (getUpdatedDescription() != ""){
                this.description = getUpdatedDescription();
            }else{
                this.description = getDefaultHardcoreDescription();
            }
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }

    }

    private boolean hasHardcoreRelicStrings(String ID) {
        Map<String, RelicStrings> relics = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "relics");
        return relics.containsKey(ID + "_HC");
    }

    @Override
    public void render(SpriteBatch sb, boolean renderAmount, Color outlineColor) {
        super.render(sb, renderAmount, outlineColor);
        if (hardcore){
            renderHardcore(sb);
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        super.renderInTopPanel(sb);
        if (hardcore){
            renderHardcore(sb);
        }
    }

    @Override
    public void renderWithoutAmount(SpriteBatch sb, Color c) {
        super.renderWithoutAmount(sb, c);
        if (hardcore){
            renderHardcore(sb);
        }
    }

    public void renderHelper(SpriteBatch sb, TextureAtlas.AtlasRegion img, float drawX, float drawY) {
        float hardcoreIconScale = 0.5F;
        sb.draw(img, drawX + img.offsetX - (float)img.originalWidth / 2.0F, drawY + img.offsetY - (float)img.originalHeight / 2.0F, (float)img.originalWidth / 2.0F - img.offsetX, (float)img.originalHeight / 2.0F - img.offsetY, (float)img.packedWidth, (float)img.packedHeight, this.scale * hardcoreIconScale, this.scale * hardcoreIconScale ,0);
    }

    public void renderHardcore(SpriteBatch sb){

        sb.setColor(Color.WHITE);
        renderHelper(sb, HARDCORE_ICON, currentX, currentY);

        renderHelper(sb, HARDCORE_ICON, currentX, currentY);
        sb.setColor(Color.WHITE);

    }


    public String getDefaultHardcoreDescription() {
        String description = "";
        if (hardcore && hasHardcoreRelicStrings(this.relicId)){
            description = CardCrawlGame.languagePack.getRelicStrings(relicId + "_HC").DESCRIPTIONS[0];
        }else {
            description = CardCrawlGame.languagePack.getRelicStrings(relicId).DESCRIPTIONS[0];
        }
        return description;
    }

    static {
        HARDCORE_ICON.originalHeight = 64;
        HARDCORE_ICON.originalWidth = 64;
        HARDCORE_ICON.offsetX = -48;
        HARDCORE_ICON.offsetY = -32;
    }
}
