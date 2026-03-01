package com.aqeel.tomatoguess.game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This class interacts with the Tomato API to retrieve random images for the
 * game.
 * It decodes the received data and creates a Game object with the image and
 * solution.
 *
 * @author Aqeel Jabir
 */
public class TomatoServer {

    /**
     * Reads data from the specified URL and returns it as a string.
     *
     * @param URLString the URL to read data from.
     * @return data read from the URL as a string.
     */
    private static String readUrl(String URLString) {
        try {

            URL url = new URI(URLString).toURL();
            InputStream inputStream = url.openStream();

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.print("An Error Occurred! \n" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a random game from the Tomato API
     *
     * @return a Game object containing the image and solution.
     */
    public Game getRandomGame() {
        String tomatoAPI = "https://marcconrad.com/uob/tomato/api.php?out=csv&base64=yes";
        String dataraw = readUrl(tomatoAPI);
        String[] data = dataraw.split(",");

        byte[] decodeImg = Base64.getDecoder().decode(data[0]);
        ByteArrayInputStream quest = new ByteArrayInputStream(decodeImg);

        int solution = Integer.parseInt(data[1]);

        BufferedImage img = null;

        try {
            img = ImageIO.read(quest);
            return new Game(img, solution);
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
