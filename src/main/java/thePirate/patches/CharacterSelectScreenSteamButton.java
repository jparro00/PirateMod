package thePirate.patches;


import basemod.CustomCharacterSelectScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import thePirate.PirateMod;
import thePirate.buttons.SteamButton;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class CharacterSelectScreenSteamButton {
    private static final String ABYSSAL = CardCrawlGame.languagePack.getCharacterString("thePirate:PirateCharacter").NAMES[0];

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "render"
    )
    public static class RenderButton {

        @SpirePostfixPatch
        public static void Postfix(CharacterSelectScreen __instance, SpriteBatch sb) {
            if (PirateMod.steamButton == null) {
                PirateMod.steamButton = new SteamButton();
                PirateMod.steamButton.show();
                PirateMod.steamButton.isDisabled = false;
                PirateMod.logger.info("Random Button successfully initialized!");
            }

            PirateMod.steamButton.render(sb);
        }
    }

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "updateButtons"
    )
    public static class UpdateRandomButton {
        public UpdateRandomButton() {
        }

        @SpirePrefixPatch
        public static void Prefix(CharacterSelectScreen __instance) {
            if (PirateMod.steamButton == null) {
                PirateMod.steamButton = new SteamButton();
                PirateMod.steamButton.isDisabled = false;
                PirateMod.logger.info("Random Button successfully initialized!");
            }

            if (__instance instanceof CustomCharacterSelectScreen){
                for (CharacterOption option : __instance.options){
                    PirateMod.steamButton.hide();
                    if (option.selected && ABYSSAL.equals(option.name)){
                        PirateMod.steamButton.show();
                        break;
                    }
                }
            }

            SteamButton button = PirateMod.steamButton;
            button.update();
            if (button.hb.clicked) {
                String os = System.getProperty("os.name").toLowerCase();

                try{
                    if (os.contains("win")) {
                        // Windows
                        new ProcessBuilder("cmd", "/c", "start", "steam://url/CommunityFilePage/2926169201").start();
                    } else if (os.contains("nix") || os.contains("nux")) {
                        // Linux
                        new ProcessBuilder("xdg-open", "steam://url/CommunityFilePage/2926169201").start();
                    } else if (os.contains("mac")) {
                        // macOS
                        new ProcessBuilder("open", "steam://url/CommunityFilePage/2926169201").start();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    PirateMod.logger.warn("Could not open Steam, attempting to open web browser");
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://steamcommunity.com/sharedfiles/filedetails/?id=2926169201"));
                        } catch (IOException | URISyntaxException exception) {
                            ex.printStackTrace();
                            PirateMod.logger.warn("Could not open web browser");
                        }
                    } else {
                        System.out.println("Unable to open web browser because Desktop is not supported");
                    }
                }


                button.hb.clicked = false;
            }

        }
    }
}
