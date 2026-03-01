package com.aqeel.tomatoguess.utilities;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for scaling images.
 *
 * @author Aqeel Jabir
 */

public class ImageScaler {
    private int imageWidth, imageHeight;
    private ImageIcon resizedIcon;

    /**
     * Gets the width of the scaled image.
     *
     * @return the width of the scaled image.
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Sets the width of the scaled image.
     *
     * @param imageWidth to be set.
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * Gets the height of the scaled image.
     *
     * @return the height of the scaled image.
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Sets the height of the scaled image.
     *
     * @param imageHeight to be set.
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * Sets both width and height of the scaled image.
     *
     * @param imageHeight to be set.
     * @param imageWidth  to be set.
     */
    public void setSize(int imageHeight, int imageWidth) {
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
    }

    /**
     * Gets a scaled ImageIcon from the original ImageIcon.
     *
     * @param originalImage to scale.
     * @return the scaled ImageIcon
     */
    public ImageIcon getScaledImage(ImageIcon originalImage) {
        Image scaledImage;
        double widthScale, heightScale, scale;
        int newHeight, newWidth;

        try {
            Image orgImage = originalImage.getImage();

            // Calculate scaling factors for width and height.
            widthScale = (double) this.imageWidth / orgImage.getWidth(null);
            heightScale = (double) this.imageHeight / orgImage.getHeight(null);

            // Choose the minimum scale to maintain aspect ratio.
            scale = Math.min(widthScale, heightScale);

            // Calculate new dimensions based on the scale.
            newWidth = (int) (orgImage.getWidth(null) * scale);
            newHeight = (int) (orgImage.getHeight(null) * scale);

            // Scale the image.
            scaledImage = orgImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(scaledImage);

            return resizedIcon;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}