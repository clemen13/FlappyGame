package game;

import javax.swing.*;

public class GameFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new GamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}