package me.ketian.handnote;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Ketian on 11/11/14.
 */
public class NoteFrame extends JFrame {

    private boolean leftPanelFocused = true;

    public NoteFrame() {

        final NotePanel notePanel = new NotePanel();
        JPanel inputPanels = new JPanel(new GridLayout(1, 2, 5, 5));
        final InputPanel inputPanelLeft = new InputPanel();
        final InputPanel inputPanelRight = new InputPanel();

        inputPanelRight.setBorder(new LineBorder(Color.GRAY, 2));

        notePanel.init();

        inputPanelLeft.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                inputPanelLeft.setBorder(new LineBorder(Color.GRAY, 2));
                inputPanelRight.setBorder(new LineBorder(Color.BLUE, 2));
                notePanel.setData(inputPanelLeft.getConvData());
                inputPanelLeft.resetData();
                leftPanelFocused = false;
            }
        });
        inputPanelRight.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                inputPanelRight.setBorder(new LineBorder(Color.GRAY, 2));
                inputPanelLeft.setBorder(new LineBorder(Color.BLUE, 2));
                notePanel.setData(inputPanelRight.getConvData());
                inputPanelRight.resetData();
                leftPanelFocused = true;
            }
        });

        inputPanels.add(inputPanelLeft);
        inputPanels.add(inputPanelRight);

        inputPanels.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"),
                "pressed");
        inputPanels.getActionMap().put("pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InputPanel inputPanel;
                if (leftPanelFocused) inputPanel = inputPanelLeft;
                else inputPanel = inputPanelRight;
                // System.out.println("Key S pressed!");
                float[][] data = inputPanel.getConvData();
                notePanel.setData(data);
                for (int i = 0; i < InputPanel.LETTER_WIDTH; ++i) {
                    for (int j = 0; j < InputPanel.LETTER_HEIGHT; ++j)
                        System.out.print(Integer.toString((int) data[j][i]) + "\t");
                    System.out.println();
                }

                inputPanel.resetData();
            }
        });

        setLayout(new FlowLayout());
        add(notePanel);
        add(inputPanels);

    }

    static public void main(String[] args) {
        NoteFrame noteFrame = new NoteFrame();
        noteFrame.setTitle("HandNote: a simple handwritten notebook");
        noteFrame.setSize(750, 1100);
        noteFrame.setLocationRelativeTo(null);
        noteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        noteFrame.setResizable(false);
        noteFrame.setVisible(true);
    }
}
