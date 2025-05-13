import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class TetrisComponent extends JComponent implements KeyListener, Runnable {
    public static final int DROP_DELAY_DECREASE_SPEED = 500;
    public static final int MIN_DROP_DELAY = 3;

    private TetrisGrid grid;
    private int dropDelay = 25;
    private int delay = 0;
    private int delayCounter = 0;
    private boolean isGameOver = false;

    private String playerName;

    public TetrisComponent(int width, int height, String playerName) {
        super();
        this.playerName = playerName;
        grid = new TetrisGrid(width, height);
        setPreferredSize(new Dimension(grid.getGraphicsWidth(), grid.getGraphicsHeight()));
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        Thread run = new Thread(this);
        run.start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (!isGameOver) {
                update();
                repaint();
            } else {
                showGameOverDialog();
                break;
            }
        }
    }

    public void paint(Graphics g) {
        synchronized (g) {
            grid.draw(g);
        }
    }

    public void update() {
        delayCounter = (delayCounter + 1) % DROP_DELAY_DECREASE_SPEED;
        if (delayCounter == 0) {
            dropDelay = Math.max(MIN_DROP_DELAY, dropDelay - 1);
        }

        delay = (delay + 1) % dropDelay;
        if (delay == 0)
            grid.moveDown();

        grid.update();

        if (grid.isFilled()) {
            isGameOver = true;
        }
    }

    private void showGameOverDialog() {
        Database.insertScore(playerName, grid.getPoints());
        List<String> topScores = Database.getTopScores();

        StringBuilder sb = new StringBuilder("Game Over!\nYour Score: " + grid.getPoints() + "\n\nTop 10 Scores:\n");
        for (String score : topScores) {
            sb.append(score).append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_LEFT: grid.moveLeft(); break;
            case KeyEvent.VK_RIGHT: grid.moveRight(); break;
            case KeyEvent.VK_DOWN: grid.moveDown(); break;
            case KeyEvent.VK_UP: grid.turnRight(); break;
            case KeyEvent.VK_COMMA: grid.turnLeft(); break;
            case KeyEvent.VK_PERIOD: grid.turnRight(); break;
        }
    }

    public void keyReleased(KeyEvent ke) {}
    public void keyTyped(KeyEvent ke) {}
}
