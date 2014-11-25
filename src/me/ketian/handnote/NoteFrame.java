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
            public void focusGained(FocusEvent e) {
                inputPanelRight.setBorder(new LineBorder(Color.GRAY, 2));
                inputPanelLeft.setBorder(new LineBorder(Color.BLUE, 2));
                leftPanelFocused = true;
                if (inputPanelRight.isModified()) {
                    notePanel.setData(inputPanelRight.getConvData());
                    inputPanelRight.resetData();
                    inputPanelRight.resetModified();
                }
            }
        });

        inputPanelRight.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                inputPanelLeft.setBorder(new LineBorder(Color.GRAY, 2));
                inputPanelRight.setBorder(new LineBorder(Color.BLUE, 2));
                leftPanelFocused = false;
                if (inputPanelLeft.isModified()) {
                    notePanel.setData(inputPanelLeft.getConvData());
                    inputPanelLeft.resetData();
                    inputPanelLeft.resetModified();
                }
            }
        });

        inputPanels.add(inputPanelLeft);
        inputPanels.add(inputPanelRight);

        /// Space
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

                /*
                for (int i = 0; i < InputPanel.LETTER_WIDTH; ++i) {
                    for (int j = 0; j < InputPanel.LETTER_HEIGHT; ++j)
                        System.out.print(Integer.toString((int) data[j][i]) + "\t");
                    System.out.println();
                }
                */

                inputPanel.resetModified();
                inputPanel.resetData();
            }
        });

        /// Delete a character
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
                "backspace");
        notePanel.getActionMap().put("backspace", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notePanel.backspace();
            }
        });

        /// Cursor up
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                "up");
        notePanel.getActionMap().put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notePanel.cursorUp();
            }
        });

        /// Cursor down
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                "down");
        notePanel.getActionMap().put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notePanel.cursorDown();
            }
        });

        /// Cursor left
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                "left");
        notePanel.getActionMap().put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notePanel.cursorLeft();
            }
        });

        /// Cursor right
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                "right");
        notePanel.getActionMap().put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notePanel.cursorRight();
            }
        });

        /// Save note
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"),
                "save");
        notePanel.getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(notePanel,
                        "You want to save the note as:",
                        "Save Note",
                        JOptionPane.QUESTION_MESSAGE
                );
                if ((s != null) && (s.length() > 0)) {
                    notePanel.save(s);
                }
            }
        });

        /// Open note
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("O"),
                "open");
        notePanel.getActionMap().put("open", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(notePanel,
                        "You want to open the note at:",
                        "Open Note",
                        JOptionPane.QUESTION_MESSAGE
                );
                if ((s != null) && (s.length() > 0)) {
                    notePanel.open(s);
                }
            }
        });

        /// Print note
        notePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"),
                "paint");
        notePanel.getActionMap().put("paint", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(notePanel,
                        "You want to paint the note at:",
                        "Print Note",
                        JOptionPane.QUESTION_MESSAGE
                );
                if ((s != null) && (s.length() > 0)) {
                    notePanel.paint(s);
                }
            }
        });

        setLayout(new FlowLayout());
        add(notePanel);
        add(inputPanels);


        JOptionPane.showMessageDialog(notePanel,
                "Welcome to HandNote: a simple hand-written notebook\n" +
                        "Here are some hot keys that might be helpful:\n" +
                        "\t[O]pen: open a note\n" +
                        "\t[S]ave: save the note\n" +
                        "\t[P]aint: paint the note\n" +
                        "\t[BackSpace]: delete a letter\n" +
                        "\t[Space]: move forward\n" +
                        "\t[Up], [Down], [Left] and [Right] to move the cursor\n",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);
    }

    static public void main(String[] args) {
        NoteFrame noteFrame = new NoteFrame();
        noteFrame.setTitle("HandNote: a simple handwritten notebook");
        // noteFrame.setSize(850, 800);
        noteFrame.setSize(680, 980);
        noteFrame.setLocationRelativeTo(null);
        noteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        noteFrame.setResizable(false);
        noteFrame.setVisible(true);

    }
}
