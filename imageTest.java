import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class imageTest extends JFrame {
    private JPanel drawingPanel;
    private JFileChooser fileChooser;
    private BufferedImage currentImage;
    private Color currentColor;
    private int currentThickness;
    private int prevX;
    private int prevY;
    
    public imageTest() {
        setTitle("Image Test");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawingPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (currentImage != null) {
                    g.drawImage(currentImage, 0, 0, this);
                }
            }

            @Override
            public void updateUI() {
                setDoubleBuffered(false);
            }
        };

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }
        });

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentImage != null) {
                    int x = e.getX();
                    int y = e.getY();

                    // Draw with the specified color and thickness
                    Graphics2D g2d = (Graphics2D) currentImage.getGraphics();
                    g2d.setColor(currentColor);
                    g2d.setStroke(new BasicStroke(currentThickness));
                    g2d.drawLine(prevX, prevY, x, y);
                    g2d.dispose();

                    // Update the drawing panel
                    drawingPanel.repaint();

                    prevX = x;
                    prevY = y;
                }
            }
        });

        add(drawingPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAndDisplayImage();
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });

        JButton colorButton = new JButton("Choose Color");
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseColor();
            }
        });

        JSlider thicknessSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 1);
        thicknessSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    currentThickness = source.getValue();
                }
            }
        });

        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(colorButton);
        buttonPanel.add(thicknessSlider);
        add(buttonPanel, BorderLayout.SOUTH);

        currentColor = Color.BLACK;
        currentThickness = 1;
    }

    private void loadAndDisplayImage() {
        if (fileChooser == null) {
            // use JFileChooser
            fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "gif", "png", "bmp"));
        }

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                currentImage = ImageIO.read(selectedFile);
                // set the size
                drawingPanel.setPreferredSize(new Dimension(currentImage.getWidth(), currentImage.getHeight()));
                pack();
                drawingPanel.repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveImage() {
        if (currentImage != null) {
            if (fileChooser == null) {
                fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
            }

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    ImageIO.write(currentImage, "jpg", selectedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void chooseColor() {
        // use JColorChooser
        Color newColor = JColorChooser.showDialog(this, "Choose Color", currentColor);
        if (newColor != null) {
            currentColor = newColor;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            imageTest editor = new imageTest();
            editor.setVisible(true);
        });
    }
}