package thePirate;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thePirate.audio.PirateSoundMaster;
import thePirate.buttons.SteamButton;
import thePirate.cards.AbstractDefaultCard;
import thePirate.cards.lures.AbstractLure;
import thePirate.cards.predators.AbstractPredator;
import thePirate.cards.targeting.RelicTargeting;
import thePirate.characters.ThePirate;
import thePirate.potions.AbstractDynamicPotion;
import thePirate.potions.InkPotion;
import thePirate.potions.IslandPotion;
import thePirate.relics.*;
import thePirate.tutorials.PirateTutorial;
import thePirate.util.IDCheckDontTouchPls;
import thePirate.util.TextureLoader;
import thePirate.variables.DefaultSecondMagicNumber;
import thePirate.variables.StormVariable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theDefault" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 4 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theDefault:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theDefault", and change to "yourmodname" rather than "thedefault".
// You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories, and press alt+c to make the replace case sensitive (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class PirateMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber,
        OnStartBattleSubscriber{
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(PirateMod.class.getName());
    private static String modID;
    public static PirateSoundMaster sound;

    public static final String CANT_PLAY = "thePirate:CannotPlay";

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String SKIP_TUTORIALS_SETTING = "enablePlaceholder";
    public static final String HIDE_INK_INTENT_SETTING = "hideInkIntent";
    public static final String DISABLE_MONKEY_SFX_SETTING = "disableMonkeySFX";
    public static final String DISABLE_CANNON_SFX_SETTING = "disableCannonSFX";
    public static final String DISABLE_TIME_WARP_REMINDER_SETTING = "disableTimeWarpReminder";
    public static final String DISABLE_DIG_BURY_PULSE_SETTING = "disableDigBuryPulse";
    public static final String DISABLE_GOLD_SPEND_REMINDER_SETTING = "disableGoldSpendReminder";
    public static final String DISABLE_CURSED_BLADE_REMINDER_SETTING = "disableCursedBladeReminder";
    public static final String HARDCORE_MODE_SETTING= "hardCoreMode";

    public static Boolean skipTutorialsPlaceholder = true; // The boolean we'll be setting on/off (true/false)
    public static Boolean hideInkIntentPlaceholder = false;
    public static Boolean disableMonkeySFXPlaceholder = false;
    public static Boolean disableCannonSFXPlaceholder = false;
    public static Boolean disableTimeWarpReminderPlaceholder = false;
    public static Boolean disableDigBuryPulsePlaceholder = false;
    public static Boolean disableGoldSpendReminderPlaceholder = false;
    public static Boolean disableCursedBladeReminderPlaceholder = false;
    public static Boolean hardcoreModePlaceholder = false;
    public static ModLabeledToggleButton skipTutorials;
    public static ModLabeledToggleButton hideInkIntent;
    public static ModLabeledToggleButton disableMonkeySFX;
    public static ModLabeledToggleButton disableCannonSFX;
    public static ModLabeledToggleButton disableTimeWarpReminder;
    public static ModLabeledToggleButton disableDigBuryPulse;
    public static ModLabeledToggleButton disableGoldSpendReminder;
    public static ModLabeledToggleButton disableCursedBladeReminder;
    public static ModLabeledToggleButton hardcoreMode;


    public static List<AbstractDynamicPotion> customPotions;
    public static List<AbstractRelic> customRelics;
    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Pirate Mod";
    private static final String AUTHOR = "Ithilian"; // And pretty soon - You!
    private static final String DESCRIPTION = "The Pirate is a fully fleshed-out custom class with a primary focus on deck control and mechanic synergies rather than raw power.";

    public static Settings.GameLanguage[] SupportedLanguages;
    public static String[] SupportedLanguagesStrings;

    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color PIRATE_PURPLE = CardHelper.getColor(27.8f, 25.9f, 36.1f);
    
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
  
    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "thePirateResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "thePirateResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "thePirateResources/images/512/bg_power_default_gray.png";
    
    private static final String ENERGY_ORB_DEFAULT_GRAY = "thePirateResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "thePirateResources/images/512/card_small_orb.png";
    
    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "thePirateResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "thePirateResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "thePirateResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "thePirateResources/images/1024/card_default_gray_orb.png";

    //Custom card frames

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
    public static TextureAtlas.AtlasRegion GOLD_GREEN_REGION;
    public static TextureAtlas.AtlasRegion GOLD_RED_REGION;
    public static TextureAtlas.AtlasRegion STEAM_ANIMATION_1_REGION;
    public static TextureAtlas.AtlasRegion STEAM_ANIMATION_2_REGION;
    public static TextureAtlas.AtlasRegion STEAM_ANIMATION_3_REGION;
    public static TextureAtlas cardUiAtlas;

    // Character assets
    private static final String THE_DEFAULT_BUTTON = "thePirateResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "thePirateResources/images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "thePirateResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "thePirateResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "thePirateResources/images/char/defaultCharacter/corpse.png";


    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "thePirateResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "thePirateResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "thePirateResources/images/char/defaultCharacter/skeleton.json";
    
    // =============== MAKE IMAGE PATHS =================

    public static SteamButton steamButton;

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath, AbstractCard.CardType type) {
        String path = getModID() + "Resources/images/cards/" + resourcePath;
        if(ImageMaster.loadImage(path) == null){
            switch (type){
                case CURSE:
                    path = makeCardPath("Skill.png");
                    break;
                case POWER:
                    path = makeCardPath("Power.png");
                    break;
                case SKILL:
                    path = makeCardPath("Skill.png");
                    break;
                case ATTACK:
                    path = makeCardPath("Attack.png");
                    break;
                case STATUS:
                    path = makeCardPath("Skill.png");
                    break;
                default:
            }
        }

        return path;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    public static String makeScreenPath(String resourcePath) {
        return getModID() + "Resources/images/screens/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public PirateMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("thePirate");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename theDefaultResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project) and press alt+c (or mark the match case option)
        // replace all instances of theDefault with yourModID, and all instances of thedefault with yourmodid (the same but all lowercase).
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod id. Not mine.
        // It's important that the mod ID prefix for keywords used in the cards descriptions is lowercase!

        // 3. Scroll down (or search for "ADD CARDS") till you reach the ADD CARDS section, and follow the TODO instructions

        // 4. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theDefault. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
        logger.info("Creating the color " + ThePirate.Enums.COLOR_GRAY.toString());
        
        BaseMod.addColor(ThePirate.Enums.COLOR_GRAY, PIRATE_PURPLE, PIRATE_PURPLE, PIRATE_PURPLE,
                PIRATE_PURPLE, PIRATE_PURPLE, PIRATE_PURPLE, PIRATE_PURPLE,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(SKIP_TUTORIALS_SETTING, "FALSE"); // This is the default setting. It's actually set...
        theDefaultDefaultSettings.setProperty(HIDE_INK_INTENT_SETTING, "FALSE");
        theDefaultDefaultSettings.setProperty(DISABLE_MONKEY_SFX_SETTING, "FALSE");
        theDefaultDefaultSettings.setProperty(DISABLE_CANNON_SFX_SETTING, "FALSE");
        theDefaultDefaultSettings.setProperty(DISABLE_TIME_WARP_REMINDER_SETTING, "FALSE");
        theDefaultDefaultSettings.setProperty(DISABLE_DIG_BURY_PULSE_SETTING, "FALSE");
        theDefaultDefaultSettings.setProperty(DISABLE_GOLD_SPEND_REMINDER_SETTING, "FALSE");
        theDefaultDefaultSettings.setProperty(DISABLE_CURSED_BLADE_REMINDER_SETTING, "FALSE");
        theDefaultDefaultSettings.setProperty(HARDCORE_MODE_SETTING, "FALSE");
        try {
            SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            skipTutorialsPlaceholder = config.getBool(SKIP_TUTORIALS_SETTING);
            hideInkIntentPlaceholder = config.getBool(HIDE_INK_INTENT_SETTING);
            disableMonkeySFXPlaceholder = config.getBool(DISABLE_MONKEY_SFX_SETTING);
            disableCannonSFXPlaceholder = config.getBool(DISABLE_CANNON_SFX_SETTING);
            disableTimeWarpReminderPlaceholder = config.getBool(DISABLE_TIME_WARP_REMINDER_SETTING);
            disableDigBuryPulsePlaceholder = config.getBool(DISABLE_DIG_BURY_PULSE_SETTING);
            disableGoldSpendReminderPlaceholder = config.getBool(DISABLE_GOLD_SPEND_REMINDER_SETTING);
            disableCursedBladeReminderPlaceholder = config.getBool(DISABLE_CURSED_BLADE_REMINDER_SETTING);
            hardcoreModePlaceholder = config.getBool(HARDCORE_MODE_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = PirateMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = PirateMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = PirateMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======

    public static void initializeAtlas() {
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
        Texture goldGreen = TextureLoader.getTexture(
                PirateMod.getModID() + "Resources/images/cards/icons/" + "goldSpend_green.png"
        );
        GOLD_GREEN_REGION = new TextureAtlas.AtlasRegion(goldGreen, 0, 0, goldGreen.getWidth(), goldGreen.getHeight());
        GOLD_GREEN_REGION.originalHeight = 512;
        GOLD_GREEN_REGION.originalWidth = 512;
        GOLD_GREEN_REGION.offsetX = 87;
        GOLD_GREEN_REGION.offsetY = 332;

        Texture goldRed =  TextureLoader.getTexture(
                PirateMod.getModID() + "Resources/images/cards/icons/" + "goldSpend_red.png"
        );
        GOLD_RED_REGION = new TextureAtlas.AtlasRegion(goldRed, 0, 0, goldRed.getWidth(), goldRed.getHeight());
        GOLD_RED_REGION.originalHeight = 512;
        GOLD_RED_REGION.originalWidth = 512;
        GOLD_RED_REGION.offsetX = 87;
        GOLD_RED_REGION.offsetY = 332;


        Texture steamAnimation1 = ImageMaster.loadImage(PirateMod.getModID()+"Resources/images/charSelect/steamLike/like_default.png");
        STEAM_ANIMATION_1_REGION = new TextureAtlas.AtlasRegion(steamAnimation1, 0,0,steamAnimation1.getWidth(), steamAnimation1.getHeight());
        Texture steamAnimation2 = ImageMaster.loadImage(PirateMod.getModID()+"Resources/images/charSelect/steamLike/like_highlight.png");
        STEAM_ANIMATION_2_REGION = new TextureAtlas.AtlasRegion(steamAnimation2, 0,0,steamAnimation2.getWidth(), steamAnimation2.getHeight());
        Texture steamAnimation3 = ImageMaster.loadImage(PirateMod.getModID()+"Resources/images/charSelect/steamLike/like_green.png");
        STEAM_ANIMATION_3_REGION = new TextureAtlas.AtlasRegion(steamAnimation3, 0,0,steamAnimation3.getWidth(), steamAnimation3.getHeight());


    }

    public static void setCustomCardBorder(CustomCard card){
        if (cardUiAtlas == null){
            initializeAtlas();
        }

        if(card instanceof AbstractPredator){
            card.bannerLargeRegion = BANNER_LARGE_REGION;
            card.bannerSmallRegion = BANNER_SMALL_REGION;
            switch (card.type){
                case SKILL:
                    card.frameSmallRegion = FRAME_SMALL_SKILL_REGION;
                    card.frameLargeRegion = FRAME_LARGE_SKILL_REGION;
                    break;
                case ATTACK:
                    card.frameSmallRegion = FRAME_SMALL_ATTACK_REGION;
                    card.frameLargeRegion = FRAME_LARGE_ATTACK_REGION;
                    break;
                case POWER:
                    card.frameSmallRegion = FRAME_SMALL_POWER_REGION;
                    card.frameLargeRegion = FRAME_LARGE_POWER_REGION;
                    break;
                default:
            }
        } else if (card instanceof AbstractLure) {
            card.frameSmallRegion = FRAME_SMALL_LURE_REGION;
            card.frameLargeRegion = FRAME_LARGE_LURE_REGION;
        }
    }

    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        PirateMod defaultmod = new PirateMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + ThePirate.Enums.THE_PIRATE.toString());
        
        BaseMod.addCharacter(new ThePirate("the Pirate", ThePirate.Enums.THE_PIRATE),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, ThePirate.Enums.THE_PIRATE);
        
        receiveEditPotions();
        logger.info("Added " + ThePirate.Enums.THE_PIRATE.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================

    // =============== POST-INITIALIZE =================
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        skipTutorials = new ModLabeledToggleButton("Skip Tutorials",
            350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                skipTutorialsPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
                    skipTutorialsPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(SKIP_TUTORIALS_SETTING, skipTutorialsPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
        });

        hideInkIntent = new ModLabeledToggleButton("Hide Ink Intent",
                350.0f * (1), 750.0f - (2 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                hideInkIntentPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    hideInkIntentPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(HIDE_INK_INTENT_SETTING, hideInkIntentPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        disableMonkeySFX = new ModLabeledToggleButton("Disable Monkey SFX",
                350.0f * (1), 750.0f - (3 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableMonkeySFXPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    disableMonkeySFXPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(DISABLE_MONKEY_SFX_SETTING, disableMonkeySFXPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        disableCannonSFX = new ModLabeledToggleButton("Disable Cannonball SFX",
                350.0f * (1), 750.0f - (4 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableCannonSFXPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    disableCannonSFXPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(DISABLE_CANNON_SFX_SETTING, disableCannonSFXPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        disableTimeWarpReminder = new ModLabeledToggleButton("Disable Time Warp Reminder",
                350.0f * (1), 750.0f - (5 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableTimeWarpReminderPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    disableTimeWarpReminderPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(DISABLE_TIME_WARP_REMINDER_SETTING, disableTimeWarpReminderPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        disableDigBuryPulse = new ModLabeledToggleButton("Disable Dig/Bury Screen Icon Pulsing",
                350.0f * (1), 750.0f - (6 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableDigBuryPulsePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    disableDigBuryPulsePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(DISABLE_DIG_BURY_PULSE_SETTING, disableDigBuryPulsePlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        disableGoldSpendReminder = new ModLabeledToggleButton("Disable Gold Spend Reminder (e.g., Bolster Crew)",
                350.0f * (1), 750.0f - (7 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableGoldSpendReminderPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    disableGoldSpendReminderPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(DISABLE_GOLD_SPEND_REMINDER_SETTING, disableGoldSpendReminderPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        disableCursedBladeReminder = new ModLabeledToggleButton("Disable Cursed Blade Reminder",
                350.0f * (1), 750.0f - (8 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                disableCursedBladeReminderPlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    disableCursedBladeReminderPlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(DISABLE_CURSED_BLADE_REMINDER_SETTING, disableCursedBladeReminderPlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        hardcoreMode = new ModLabeledToggleButton("Hardcore Mode (requires restart)",
                350.0f * (1), 750.0f - (9 * 50), Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                hardcoreModePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
                    hardcoreModePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig(getModID(), getModID() + "Config", theDefaultDefaultSettings);
                        config.setBool(HARDCORE_MODE_SETTING, hardcoreModePlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(skipTutorials); // Add the button to the settings panel. Button is a go.
        settingsPanel.addUIElement(hideInkIntent);
        settingsPanel.addUIElement(disableMonkeySFX);
        settingsPanel.addUIElement(disableCannonSFX);
        settingsPanel.addUIElement(disableTimeWarpReminder);
        settingsPanel.addUIElement(disableDigBuryPulse);
        settingsPanel.addUIElement(disableGoldSpendReminder);
        settingsPanel.addUIElement(disableCursedBladeReminder);
        settingsPanel.addUIElement(hardcoreMode);
        updateCompendiumPostSettings();


        sound = new PirateSoundMaster();
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        
        // =============== EVENTS =================
        // https://github.com/daviscook477/BaseMod/wiki/Custom-Events

        // You can add the event like so:
        // BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);
        // Then, this event will be exclusive to the City (act 2), and will show up for all characters.
        // If you want an event that's present at any part of the game, simply don't include the dungeon ID

        // If you want to have more specific event spawning (e.g. character-specific or so)
        // deffo take a look at that basemod wiki link as well, as it explains things very in-depth
        // btw if you don't provide event type, normal is assumed by default

        // Create a new event builder
        // Since this is a builder these method calls (outside of create()) can be skipped/added as necessary
/*
        AddEventParams eventParams = new AddEventParams.Builder(IdentityCrisisEvent.ID, IdentityCrisisEvent.class) // for this specific event
            .dungeonID(TheCity.ID) // The dungeon (act) this event will appear in
            .playerClass(TheDefault.Enums.THE_DEFAULT) // Character specific event
            .create();

        // Add the event
        BaseMod.addEvent(eventParams);
*/

        CustomTargeting.registerCustomTargeting(RelicTargeting.RELIC, new RelicTargeting());

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    
    // ================ ADD POTIONS ===================

    public static void addPiratePotion(AbstractDynamicPotion potion){
        BaseMod.addPotion(potion.getClass(),potion.getColor(AbstractDynamicPotion.COLOR.LIQUID),potion.getColor(AbstractDynamicPotion.COLOR.HYBRID),potion.getColor(AbstractDynamicPotion.COLOR.SPOTS), potion.ID, ThePirate.Enums.THE_PIRATE);
    }
    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");
        
        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
//        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, ThePirate.Enums.THE_PIRATE);
        customPotions = new ArrayList<>();

        customPotions.add(new InkPotion());
        customPotions.add(new IslandPotion());

        for (AbstractDynamicPotion potion : customPotions) {
            addPiratePotion(potion);
        }
        logger.info("Done editing potions");
    }
    
    // ================ /ADD POTIONS/ ===================
    
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // Take a look at https://github.com/daviscook477/BaseMod/wiki/AutoAdd
        // as well as
        // https://github.com/kiooeht/Bard/blob/e023c4089cc347c60331c78c6415f489d19b6eb9/src/main/java/com/evacipated/cardcrawl/mod/bard/BardMod.java#L319
        // for reference as to how to turn this into an "Auto-Add" rather than having to list every relic individually.
        // Of note is that the bard mod uses it's own custom relic class (not dissimilar to our AbstractDefaultCard class for cards) that adds the 'color' field,
        // in order to automatically differentiate which pool to add the relic too.

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        customRelics = new ArrayList<>();

        customRelics.add(new BlackCannon());
        customRelics.add(new SilverCannon());
        customRelics.add(new GoldCannon());
        customRelics.add(new PlatinumCannon());
        customRelics.add(new GunsmithsBible());
        customRelics.add(new GreedyChest());
        customRelics.add(new Coral());
        customRelics.add(new ExoticDish());
        customRelics.add(new Motivation());
        customRelics.add(new WoodenLeg());
        customRelics.add(new WritingReed());
        customRelics.add(new BottledVoid());
        customRelics.add(new NavigationDevice());
        customRelics.add(new ExperimentalCannon());
        customRelics.add(new MoneyBag());
        customRelics.add(new WoodenCompass());

        for (AbstractRelic relic : customRelics) {
            BaseMod.addRelicToCustomPool(relic, ThePirate.Enums.COLOR_GRAY);
        }
        // This adds a relic to the Shared pool. Every character can find this relic.
//        BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);
        
        // Mark relics as seen - makes it visible in the compendium immediately
        // If you don't have this it won't be visible in the compendium until you see them in game
        // (the others are all starters so they're marked as seen in the character file)
        UnlockTracker.markRelicAsSeen(BottledVoid.ID);
        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());
        BaseMod.addDynamicVariable(new StormVariable());

        logger.info("Adding cards");
        // Add the cards
        // Don't delete these default cards yet. You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        // This method automatically adds any cards so you don't have to manually load them 1 by 1
        // For more specific info, including how to exclude cards from being added:
        // https://github.com/daviscook477/BaseMod/wiki/AutoAdd

        // The ID for this function isn't actually your modid as used for prefixes/by the getModID() method.
        // It's the mod id you give MTS in ModTheSpire.json - by default your artifact ID in your pom.xml

        //TODO: Rename the "DefaultMod" with the modid in your ModTheSpire.json file
        //TODO: The artifact mentioned in ModTheSpire.json is the artifactId in pom.xml you should've edited earlier
        new AutoAdd("PirateMod") // ${project.artifactId}
            .packageFilter(AbstractDefaultCard.class) // filters to any class in the same package as AbstractDefaultCard, nested packages included
            .setDefaultSeen(true)
            .cards();

        // .setDefaultSeen(true) unlocks the cards
        // This is so that they are all "seen" in the library,
        // for people who like to look at the card list before playing your mod

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================


    public static String assetPath(String path) {
        return getModID() + "Resources/" + path;
    }
    private String makeLocalizationPath(Settings.GameLanguage language, String filename) {
        String langPath = this.getLangString();
        return assetPath("localization/" + langPath + "/" + filename + ".json");
    }

    private String getLangString() {
        Settings.GameLanguage[] var1 = SupportedLanguages;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Settings.GameLanguage lang = var1[var3];
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }

        return "eng";
    }

    // ================ LOAD THE TEXT ===================

    private void loadLocalization(Settings.GameLanguage language, Class<?> stringType) {
        BaseMod.loadCustomStringsFile(stringType, this.makeLocalizationPath(language, stringType.getSimpleName()));
    }

    private void loadLocalization(Settings.GameLanguage language) {
        loadLocalization(language, CardStrings.class);
        loadLocalization(language, PowerStrings.class);
        loadLocalization(language, RelicStrings.class);
        loadLocalization(language, PotionStrings.class);
        loadLocalization(language, CharacterStrings.class);
        loadLocalization(language, UIStrings.class);
        loadLocalization(language, TutorialStrings.class);
        //Special case for DeathKell strings
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath(language, "DeathKnellStrings"));
    }
    @Override
    public void receiveEditStrings() {
        loadLocalization(Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadLocalization(Settings.language);
        }
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        String lang = this.getLangString();
        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath("localization/" + lang + "/KeywordStrings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        PirateKeyword[] keywords = gson.fromJson(json, PirateKeyword[].class);
        
        if (keywords != null) {
            for (PirateKeyword keyword : keywords) {
                if (isHardcore() && keyword.DESCRIPTION_HC != null){
                    BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION_HC);
                }else {
                    BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                }
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    public static boolean isInCombat(){

        return AbstractDungeon.player != null && CardCrawlGame.isInARun() && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;

    }
    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if (!skipTutorials.toggle.enabled && AbstractDungeon.player.chosenClass.equals(ThePirate.Enums.THE_PIRATE)){
            AbstractDungeon.ftue = new PirateTutorial(Settings.language);
            skipTutorials.toggle.toggle();

        }
    }
    public static boolean supportsLanguage(Settings.GameLanguage language){
        for(int i = 0; i < SupportedLanguages.length; i++) {
            Settings.GameLanguage lang = SupportedLanguages[i];
            if (lang.equals(language)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isHardcore(){
        if (hardcoreMode != null){
            return hardcoreMode.toggle.enabled;
        }else {
            return false;
        }
    }

    public void updateCompendiumPostSettings() {
        logger.info("entering updateCompendiumPostSettings");

        CardLibrary.getAllCards().iterator();
        for (AbstractCard card : CardLibrary.getAllCards()){
            if (card instanceof thePirate.cards.AbstractDynamicCard) {
                logger.info("reloading card: " + card.cardID);
                try {
                    Class<?> cardClass = card.getClass();
                    Constructor<?> constructor = cardClass.getConstructor();
                    CardLibrary.add((AbstractCard) constructor.newInstance());
                } catch (Exception e) {
                    PirateMod.logger.error("could not reload card: " + card.cardID);
                    PirateMod.logger.error(e);
                }
            }
        }

        receiveEditKeywords();
        RelicLibrary.resetForReload();
        RelicLibrary.initialize();
        logger.info("exiting updateCompendiumPostSettings");

    }

    public void receiveAddAudio() {
        String sfxDir = PirateMod.getModID() + "Resources/audio/sound/";
        BaseMod.addAudio(makeID("CANNON_HIT_SHIP"), sfxDir + "cannon_hit_ship_short.ogg");
    }

    static {
        SupportedLanguages = new Settings.GameLanguage[]{Settings.GameLanguage.ENG, Settings.GameLanguage.RUS, Settings.GameLanguage.SPA, Settings.GameLanguage.ZHS};
        SupportedLanguagesStrings = new String[]{"English", "Russian", "Spanish", "Simplified Chinese"};
    }
}
