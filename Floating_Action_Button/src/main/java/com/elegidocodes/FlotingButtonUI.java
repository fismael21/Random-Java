package com.elegidocodes;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.plaf.LayerUI;

/**
 *
 * @author Fernando Ismael Canul Caballero
 */

public class FlotingButtonUI extends LayerUI<Component> {

    private Shape shape;
    private boolean mousePressed;
    private boolean mouseHovered;
    private final Image image;
    private BufferedImage imageShadow;

    public FlotingButtonUI() {
        image = new ImageIcon("plus.png").getImage(); 
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        if (c instanceof JLayer) {
            ((JLayer) c).setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        if (c instanceof JLayer) {
            ((JLayer) c).setLayerEventMask(0);
        }
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int buttonSize = 40;
        int xCoordinate = c.getWidth() - buttonSize - 14;
        int yCoordinate = c.getHeight() - buttonSize - 28;

        shape = new Ellipse2D.Double(xCoordinate, yCoordinate, buttonSize, buttonSize);

        if (mousePressed) {
            g2.setColor(new Color(65, 45, 203));
        } else if (mouseHovered) {
            g2.setColor(new Color(82, 60, 222));
        } else {
            g2.setColor(new Color(72, 52, 212));
        }

        if (imageShadow == null) {
            generateImageShadow(buttonSize);
        }

        g2.drawImage(imageShadow, xCoordinate - 5, yCoordinate - 5, null);

        g2.fill(shape);

        int iconSize = 24;
        int xIcon = (buttonSize - iconSize) / 2;
        int yIcon = (buttonSize - iconSize) / 2;
        g2.drawImage(image, xCoordinate + xIcon, yCoordinate + yIcon, null);

        g2.dispose();
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends Component> jLayer) {
        super.processMouseEvent(e, jLayer);

        if (mouseHovered) {
            e.consume();
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                if (mouseHovered) {
                    mousePressed = true;
                    jLayer.repaint(shape.getBounds());
                }
            } else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                mousePressed = false;
                jLayer.repaint(shape.getBounds());
                if (mouseHovered) {
                    MainWindow mainWindow = (MainWindow) jLayer.getView();
                    mainWindow.actionButton();
                }
            }
        }
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends Component> jLayer) {
        super.processMouseMotionEvent(e, jLayer);
        Point point = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), jLayer.getView());
        boolean hover = shape.contains(point);
        if (mouseHovered != hover) {
            mouseHovered = hover;
            jLayer.repaint(shape.getBounds());
        }
        if (mousePressed) {
            e.consume();
        }
    }

    private void generateImageShadow(int size) {
        int shadowSize = 6;
        int width = size + shadowSize * 2;
        int height = size + shadowSize * 2;
        imageShadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imageShadow.createGraphics();

        g2.drawImage(new ShadowRenderer(shadowSize, 0.3f, Color.BLACK).createShadow(createShape(size)), 0, 0, null);
        g2.dispose();
    }

    private BufferedImage createShape(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.fill(new Ellipse2D.Double(0, 0, size, size));
        g2.dispose();

        return img;
    }

}
