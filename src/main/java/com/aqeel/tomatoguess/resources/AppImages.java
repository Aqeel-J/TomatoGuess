package com.aqeel.tomatoguess.resources;

import com.aqeel.tomatoguess.App;

import javax.swing.ImageIcon;
import java.util.Objects;

/**
 * Resources class containing ImageIcon constants for the application images and
 * gifs.
 * 
 * @author Aqeel Jabir
 */
public class AppImages {

    // Images
    public static ImageIcon appLogo = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "logo.png")));
    public static ImageIcon wavingHandImg = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "waving_hand.png")));
    public static ImageIcon timesUpImg = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "times_up.png")));
    public static ImageIcon trophyImg = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "trophy_img.png")));
    public static ImageIcon hintImg = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "hint_img.png")));
    public static ImageIcon skipImg = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "skip_img.png")));
    public static ImageIcon extraTimerImg = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "10_timer_img.png")));
    public static ImageIcon heartPlusImg = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.IMAGE_FILE_PATH + "heart_plus_img.png")));

    // Gifs
    public static ImageIcon loaderGif = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.GIF_FILE_PATH + "loader.gif")));
    public static ImageIcon btnLoaderGif = new ImageIcon(Objects.requireNonNull(App.class.getResource(AppStrings.GIF_FILE_PATH + "button_loader.gif")));
}
