package com.aqeel.tomatoguess.utilities;

import com.aqeel.tomatoguess.App;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

public class SVGIcon {

    public static ImageIcon loadSvgIcon(String path, int width, int height) {
            try {
                URI url = Objects.requireNonNull(SVGIcon.class.getResource(path)).toURI();
                SVGUniverse universe = new SVGUniverse();
                SVGDiagram diagram = universe.getDiagram(url);

                // Render the SVG to a BufferedImage
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                diagram.setIgnoringClipHeuristic(true);
                diagram.render(g);
                g.dispose();

                return new ImageIcon(image);
            } catch (Exception e) {
                e.fillInStackTrace();
                return null;
            }
        }
    }