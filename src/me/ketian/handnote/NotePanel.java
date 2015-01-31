package me.ketian.handnote;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Ketian on 11/11/14.
 */
public class NotePanel extends JPanel {
    static final int LETTERS_PER_LINE = 25; // for 13' inch
    static final int LINES_PER_PAGE = 17; // for 13' inch
    //static final int LETTERS_PER_LINE = 20; // for 1080p
    //static final int LINES_PER_PAGE = 23; // for 1080p
    static final int MARGIN = 2;
    static final int HEADER = 1;
    static final int FOOTER = 2;

    static final int MAX_H = 1280;
    static final int MAX_W = 1280;

    private int height, width;
    private int cursorX = 1, cursorY = HEADER;
    private int letterWidth, letterHeight;
    private int[][] data = new int[MAX_W][MAX_H];
    private int[][] dataTemp = new int[MAX_W][MAX_H];

    public void init() {
        height = (LINES_PER_PAGE + HEADER + FOOTER) *
                (MARGIN + InputPanel.LETTER_HEIGHT);
        width = (LETTERS_PER_LINE + 2) * (InputPanel.LETTER_WIDTH);

        // System.out.println(height);
        // System.out.println(width);

        letterHeight = InputPanel.LETTER_HEIGHT;
        letterWidth = InputPanel.LETTER_WIDTH;

        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        setPreferredSize(new Dimension(width, height));

    }

    public void clearData() {
        int x = cursorX * letterWidth, y = cursorY * (letterHeight + MARGIN) + MARGIN;

        for (int dx = 0; dx < letterWidth; ++dx)
            for (int dy = 0; dy < letterHeight; ++ dy)
                data[x + dx][y + dy] = 0;
    }

    public void setData(int[][] letter) {
        int x = cursorX * letterWidth, y = cursorY * (letterHeight + MARGIN) + MARGIN;

        for (int dx = 0; dx < letterWidth; ++dx) {
            if (x + dx >= width) break;
            for (int dy = 0; dy < letterHeight; ++dy) {
                if (y + dy >= height) break;
                data[x + dx][y + dy] |= letter[dx][dy];
            }
        }
        cursorRight();
    }

    public void setData(float[][] letter) {
        int x = cursorX * letterWidth, y = cursorY * (letterHeight + MARGIN) + MARGIN;

        for (int dx = 0; dx < letterWidth; ++dx) {
            if (x + dx >= width) break;
            for (int dy = 0; dy < letterHeight; ++dy) {
                if (y + dy >= height) break;
                data[x + dx][y + dy] |= (int) letter[dx][dy];
            }
        }
        cursorRight();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j) {
                if (data[i][j] == 0) continue;
                g.setColor(new Color(0, 0, 0, data[i][j]));
                g.drawLine(i, j, i, j);
            }

        g.setColor(Color.BLUE);
        g.drawRect(cursorX * letterWidth, cursorY * (letterHeight + MARGIN) + MARGIN - 1, letterWidth, letterHeight);

        g.setColor(Color.GRAY);
        for (int i = 2; i <= HEADER + LINES_PER_PAGE; ++i)
            g.drawLine(letterWidth, i * (MARGIN + letterHeight),
                    letterWidth * (LETTERS_PER_LINE), i * (MARGIN + letterHeight));
    }

    public void backspace() {
        cursorLeft();

        clearData();
        repaint();
    }

    public void save(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            int flag = JOptionPane.showConfirmDialog(this,
                    "File already exists! Are you sure to overwrite it?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            // System.out.println(flag);
            if (flag == 1) return;
        }
        try {
            PrintWriter output = new PrintWriter(file);

            for (int i = 0; i < width; ++i) {
                for (int j = 0; j < height; ++j) {
                    output.print(data[i][j]);
                    output.print(' ');
                }
                output.println();
            }

            output.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failure: cannot write to file!",
                    "Failure",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Success: file " + filename + " has been written!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

    }

    public void open(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Failure: file does not exists!",
                    "Failure",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Scanner input = new Scanner(file);

            for (int i = 0; i < width; ++i)
                for (int j = 0; j < height; ++j)
                    dataTemp[i][j] = input.nextInt();

            input.close();
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this,
                    "Failure: cannot read file!",
                    "Failure",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this,
                "Success: file " + filename + " has been opened!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        for (int i = 0; i < width; ++i)
            for (int j = 0; j < height; ++j)
                data[i][j] = dataTemp[i][j];
        repaint();
        cursorX = 1; cursorY = HEADER;
    }

    public void paint(String filename) {
        BufferedImage theImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int value = (255-data[x][y]) << 16 | (255-data[x][y]) << 8 | (255-data[x][y]);
                theImage.setRGB(x, y, value);
            }
        }
        File file = new File(filename);
        if (file.exists()) {
            int flag = JOptionPane.showConfirmDialog(this,
                    "File already exists! Are you sure to overwrite it?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (flag == 1) return;
        }
        try {
            ImageIO.write(theImage, "png", file);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failure: file cannot be written!",
                    "Failure",
                    JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(this,
                "Success: note has been painted at " + filename + " !",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void cursorUp() {
        if (cursorY > HEADER) --cursorY;
        repaint();
    }

    public void cursorDown() {
        if (cursorY < HEADER+LINES_PER_PAGE) ++cursorY;
        repaint();
    }

    public void cursorRight() {
        ++cursorX;
        if (cursorX == LETTERS_PER_LINE) {
            if (cursorY < HEADER+LINES_PER_PAGE) {
                cursorX = 1;
                ++cursorY;
            } else --cursorX;
        }
        repaint();
    }

    public void cursorLeft() {
        --cursorX;
        if (cursorX < 1) {
            cursorX = LETTERS_PER_LINE - 1;
            --cursorY;
            if (cursorY < HEADER) {
                cursorY = HEADER;
                cursorX = 1;
            }
        }
        repaint();
    }

}
