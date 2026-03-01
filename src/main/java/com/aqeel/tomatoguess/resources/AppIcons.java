package com.aqeel.tomatoguess.resources;

import com.aqeel.tomatoguess.App;
import com.aqeel.tomatoguess.utilities.SVGIcon;

import javax.swing.ImageIcon;
import java.util.Objects;

/**
 * Resources class containing ImageIcon constants for the application icons.
 * 
 * @author Aqeel Jabir
 */
public class AppIcons {

    public static ImageIcon closeIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "close_icon.png")));
//    public static ImageIcon closeIcon = SVGIcon.loadSvgIcon(AppStrings.ICON_FILE_PATH + "close_icon.svg", 32, 32);
    public static ImageIcon exitIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "exit_icon.png")));
    public static ImageIcon filledBarIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "filled_bar_icon.png")));
    public static ImageIcon filledStarsIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "filled_stars_icon.png")));
    public static ImageIcon filledStarIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "filled_star_icon.png")));
    public static ImageIcon strokeStarIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "stroke_star_icon.png")));
    public static ImageIcon filledHeartIcon_96 = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "filled_heart_icon_96.png")));
    public static ImageIcon filledHeartIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "filled_heart_icon.png")));
    public static ImageIcon strokeHeartIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "stroke_heart_icon.png")));
    public static ImageIcon musicIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "musical_icon.png")));
    public static ImageIcon noMusicIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "no_music_icon.png")));
    public static ImageIcon soundOnIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "voice_on_icon.png")));
    public static ImageIcon muteIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "mute_icon.png")));
    public static ImageIcon penIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "filled_edit_icon.png")));
    public static ImageIcon checkIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "checkmark_icon.png")));
    public static ImageIcon globeIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "globe_icon.png")));
    public static ImageIcon flagIcon = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.ICON_FILE_PATH + "flag_icon.png")));
}
