package me.ketian.handnote;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by Ketian on 11/11/14.
 */
public class NotePanel extends JPanel {
    static final int LETTERS_PER_LINE = 20;
    static final int MARGIN = 2;
    static final int LINES_PER_PAGE = 23;
    static final int HEADER = 2;
    static final int FOOTER = 2;

    static final int MAX_H = 1024;
    static final int MAX_W = 720;

    private int height, width;
    private int cursorX = 1, cursorY = HEADER;
    private int letterWidth, letterHeight;
    private int[][] data = new int[MAX_W][MAX_H];

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
        ++cursorX;
        if (cursorX == LETTERS_PER_LINE) {
            cursorX = 1;
            ++cursorY;
        }

        repaint();
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
        ++cursorX;
        if (cursorX == LETTERS_PER_LINE) {
            cursorX = 1;
            ++cursorY;
        }

        repaint();
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
        --cursorX;
        if (cursorX < 1) {
            cursorX = LETTERS_PER_LINE - 1;
            --cursorY;
            if (cursorY < HEADER) {
                cursorY = HEADER;
                cursorX = 1;
            }
        }
        clearData();
        repaint();
    }

}
