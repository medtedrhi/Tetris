import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PlayTetris extends JFrame {

    public PlayTetris() {
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(3, 1));
        JLabel nameLabel = new JLabel("Enter your name:");
        JTextField nameField = new JTextField();
        JButton startButton = new JButton("Start Game");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(startButton);
        add(panel);

        setLocationRelativeTo(null);
        setVisible(true);

        startButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!");
            } else {
                dispose();
                startGame(playerName);
            }
        });
    }

    private void startGame(String playerName) {
        JFrame gameFrame = new JFrame("Tetris Game - " + playerName);
        TetrisComponent game = new TetrisComponent(10, 20, playerName);
        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        game.setFocusable(true);
        game.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PlayTetris::new);
    }
}
