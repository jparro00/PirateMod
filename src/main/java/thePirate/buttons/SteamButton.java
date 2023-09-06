package thePirate.buttons;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import thePirate.PirateMod;

public class SteamButton {
    private static final UIStrings uiStrings;
    private static final String[] TEXT;
    private static final float SHOW_X;
    private static final float DRAW_Y;
    private static final float ANIMATION_Y;
    private static final float ANIMATION_X;
    private static final float HIDE_X;
    private static final Color HOVER_BLEND_COLOR;
    private static final Color TEXT_DISABLED_COLOR;
    private static final float TEXT_OFFSET_X;
    private static final float TEXT_OFFSET_Y;
    private static final float HITBOX_W;
    private static final float HITBOX_H;
    private static final float W = 512.0F;
    private static final float H = 256.0F;
    private Color glowColor;
    private float current_x;
    private float target_x;
    public boolean isHidden;
    private float glowAlpha;
    public Hitbox hb;
    public boolean isDisabled;
    public boolean isHovered;
    private String buttonText;
    private long startTime = 0;
    boolean clicked = false;

    private static final Texture img1 = PirateMod.STEAM_ANIMATION_1_REGION.getTexture();
    private static final Texture img2 = PirateMod.STEAM_ANIMATION_2_REGION.getTexture();
    private static final Texture img3 = PirateMod.STEAM_ANIMATION_3_REGION.getTexture();

    public SteamButton() {
        this.glowColor = Color.WHITE.cpy();
        this.hb = new Hitbox(HITBOX_W, HITBOX_H);
        this.buttonText = TEXT[0];
        this.current_x = HIDE_X;
        this.target_x = this.current_x;
        this.isHidden = true;
        this.isDisabled = true;
        this.isHovered = false;
        this.glowAlpha = 0.0F;
        this.hb.move(SHOW_X + 106.0F * Settings.scale, DRAW_Y + 60.0F * Settings.scale);
    }

    public void update() {
        if (!this.isHidden) {
            this.updateGlow();
            this.hb.update();
            if (InputHelper.justClickedLeft && this.hb.hovered && !this.isDisabled) {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }

            if (this.hb.justHovered && !this.isDisabled) {
                CardCrawlGame.sound.play("UI_HOVER");
            }

            this.isHovered = this.hb.hovered;
            if (CInputActionSet.topPanel.isJustPressed()) {
                CInputActionSet.topPanel.unpress();
                this.hb.clicked = true;
            }
        }

        if (this.current_x != this.target_x) {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD) {
                this.current_x = this.target_x;
            }
        }

    }

    private void updateGlow() {
        this.glowAlpha += Gdx.graphics.getDeltaTime() * 3.0F;
        if (this.glowAlpha < 0.0F) {
            this.glowAlpha *= -1.0F;
        }

        float tmp = MathUtils.cos(this.glowAlpha);
        if (tmp < 0.0F) {
            this.glowColor.a = -tmp / 2.0F + 0.3F;
        } else {
            this.glowColor.a = tmp / 2.0F + 0.3F;
        }

    }

    public void show() {
        if (this.isHidden) {
            this.glowAlpha = 0.0F;
            this.target_x = SHOW_X;
            this.isHidden = false;
        }

    }

    public void hide() {
        if (!this.isHidden) {
            this.glowAlpha = 0.0F;
            this.target_x = HIDE_X;
            this.isHidden = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        this.renderShadow(sb);
        sb.setColor(this.glowColor);
        this.renderOutline(sb);
        sb.setColor(Color.SLATE);
//        sb.setColor(new Color(16f, 28f, 37f, 100f));
//        sb.setColor(Color.PURPLE);
        this.renderButton(sb);
        sb.setColor(Color.WHITE);
        if (this.hb.hovered && !this.isDisabled && !this.hb.clickStarted) {
            sb.setBlendFunction(770, 1);
            sb.setColor(HOVER_BLEND_COLOR);
            this.renderButton(sb);
            sb.setBlendFunction(770, 771);
        }

        if (this.isDisabled) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.buttonText, this.current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, TEXT_DISABLED_COLOR);
        } else if (this.hb.clickStarted) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.buttonText, this.current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, Color.LIGHT_GRAY);
        } else if (this.hb.hovered) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.buttonText, this.current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, Settings.LIGHT_YELLOW_COLOR);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, this.buttonText, this.current_x + TEXT_OFFSET_X, DRAW_Y + TEXT_OFFSET_Y, Settings.LIGHT_YELLOW_COLOR);
        }

        this.renderControllerUi(sb);
        if (!this.isHidden) {
            this.hb.render(sb);
            if (!isHovered){
                startTime = 0;
            }
            if (isHovered){
                renderSteamThumb(sb);
            }
        }

    }

    public void renderSteamThumb(SpriteBatch sb){
        if (startTime == 0){
            startTime = System.currentTimeMillis();
            clicked = false;
        }
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - startTime;

        sb.setColor(Color.WHITE);

        if (timeElapsed >= 0 && timeElapsed < 1000) {
            // Show img1 for 0 to 1 seconds
//            sb.draw(img1, ANIMATION_X * Settings.scale, ANIMATION_Y * Settings.scale);
            sb.draw(img1, ANIMATION_X, ANIMATION_Y, 0, 0, 127.0F, 65.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 127, 65, false, false);
        } else if (timeElapsed >= 1000 && timeElapsed < 1200) {
            if (clicked == false){
                CardCrawlGame.sound.play("UI_CLICK_1");
            }
            clicked = true;
            sb.draw(img2, ANIMATION_X, ANIMATION_Y, 0, 0, 127.0F, 65.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 127, 65, false, false);
            // Show img2 for 1 to 1.2 seconds
        } else if (timeElapsed >= 1200) {
            // Show img3 for 1.2 to 2 seconds
            sb.draw(img3, ANIMATION_X, ANIMATION_Y, 0, 0, 127.0F, 65.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 127, 65, false, false);
        }
    }

    private void renderShadow(SpriteBatch sb) {
        sb.draw(ImageMaster.CONFIRM_BUTTON_SHADOW, this.current_x - 256.0F, DRAW_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
    }

    private void renderOutline(SpriteBatch sb) {
        sb.draw(ImageMaster.CONFIRM_BUTTON_OUTLINE, this.current_x - 256.0F, DRAW_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
    }

    private void renderButton(SpriteBatch sb) {
        sb.draw(ImageMaster.CONFIRM_BUTTON, this.current_x - 256.0F, DRAW_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
    }

    private void renderControllerUi(SpriteBatch sb) {
        if (Settings.isControllerMode) {
            sb.setColor(Color.WHITE);
            sb.draw(CInputActionSet.topPanel.getKeyImg(), this.current_x - 32.0F + 47.0F * Settings.scale, DRAW_Y - 32.0F + 57.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }

    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("thePirate:SteamButton");
        TEXT = uiStrings.TEXT;
        SHOW_X = (float)Settings.WIDTH - 256.0F * Settings.scale;
        DRAW_Y = (1080.0F * 4f/5f) * Settings.scale;
        ANIMATION_X = (1650.0F) * Settings.scale;
        ANIMATION_Y = (970.0F) * Settings.scale;
        HIDE_X = SHOW_X + 400.0F * Settings.scale;
        HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.3F);
        TEXT_DISABLED_COLOR = new Color(0.6F, 0.6F, 0.6F, 1.0F);
        TEXT_OFFSET_X = 136.0F * Settings.scale;
        TEXT_OFFSET_Y = 57.0F * Settings.scale;
        HITBOX_W = 300.0F * Settings.scale;
        HITBOX_H = 100.0F * Settings.scale;
    }
}
