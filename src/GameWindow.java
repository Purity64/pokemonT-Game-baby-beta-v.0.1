import javax.swing.*;

public class GameWindow {
    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setTitle("PokamonT Game");

        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);

        GamePanel gamePanel = new GamePanel();
        jf.add(gamePanel);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(true);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);

        gamePanel.startGameThread();
    }
}