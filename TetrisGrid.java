import java.util.*;
import java.util.List;
import java.awt.*;

public class TetrisGrid extends BlockGrid
{
	private Vector<TetrisBlock> lockedBlocks = new Vector();
	private TetrisBlock workingBlock;
	private int points = 0;

	public TetrisGrid(int width, int height)
	{
		super(width, height);
	}

	public void update()
	{
		if(workingBlock == null)
			setWorkingBlock(TetrisBlock.getRandomBlock().setLocation(getWidth()/2, 0));
	}

	

	/**
	* Sets the current working block.
	* @return True if the block is set, false if the block will not fit.
	*/
	public boolean setWorkingBlock(TetrisBlock block)
	{
		if(canFit(block))
		{
			workingBlock = block;
			return true;
		}
		return false;
	}

	public void lockWorkingBlock()
	{
		points++;
		if(workingBlock != null)
			lockedBlocks.addAll(workingBlock.getSingleBlocks());
		workingBlock = null;
		rowCheck();
	}

	// In TetrisGrid class
public void displayTopScores(Graphics g) {
    List<String> topScores = Database.getTopScores();
    int yOffset = 20; // Starting Y position for the first score

    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.BLACK);
    g2.drawString("Top 10 Scores:", getGraphicsWidth()/2 - 40, yOffset);
    yOffset += 20;

    // Display each score
    for (String score : topScores) {
        g2.drawString(score, getGraphicsWidth()/2 - 40, yOffset);
        yOffset += 20;
    }
}


	private boolean canFit(TetrisBlock block)
	{
		if((block.getX() < 0) || (block.getX()+block.getWidth() > getWidth()))
			return false;

		if(block.getY() >= getHeight())
			return false;

		for(int i = 0; i < lockedBlocks.size(); i++)
		{
			if(lockedBlocks.get(i).overlaps(block))
				return false;
		}

		return true;
	}

	public void moveDown()
	{
		if(workingBlock == null)
			return;

		TetrisBlock newBlock = workingBlock.moveDown();
		if(canFit(newBlock))
			workingBlock = newBlock;
		else
		{
			lockWorkingBlock();
		}
	}

	public void moveLeft()
	{
		if(workingBlock == null)
			return;

		TetrisBlock newBlock = workingBlock.moveLeft();
		if(canFit(newBlock))
			workingBlock = newBlock;
	}

	public void moveRight()
	{
		if(workingBlock == null)
			return;

		TetrisBlock newBlock = workingBlock.moveRight();
		if(canFit(newBlock))
			workingBlock = newBlock;
	}

	public void turnLeft()
	{
		if(workingBlock == null)
			return;

		TetrisBlock newBlock = workingBlock.turnLeft();
		if(canFit(newBlock))
			workingBlock = newBlock;
	}

	public void turnRight()
	{
		if(workingBlock == null)
			return;

		TetrisBlock newBlock = workingBlock.turnRight();
		if(canFit(newBlock))
			workingBlock = newBlock;
	}

	public boolean isFilled()
	{
		for(int i = 0; i < lockedBlocks.size(); i++)
		{
			if(lockedBlocks.get(i).getY() == 0)
				return true;
		}
		return false;
	}

	public boolean isRowFilled(int row)
	{
		int blocksInRow = 0;
		for(int i = 0; i < lockedBlocks.size(); i++)
		{
			if(lockedBlocks.get(i).getY() == row)
				blocksInRow++;
		}
		return blocksInRow == getWidth();
	}

	public void clearRow(int row)
	{
		for(int i = 0; i < lockedBlocks.size(); i++)
		{
			if(lockedBlocks.get(i).getY() == row)
			{
				lockedBlocks.remove(i);
				i--;
			}
			else if(lockedBlocks.get(i).getY() < row)
			{
				lockedBlocks.add(i,lockedBlocks.get(i).moveDown());
				lockedBlocks.remove(i+1);
			}
		}
	}

	public void rowCheck()
	{
		for(int row = 1; row < getHeight(); row++)
		{
			if(isRowFilled(row))
				clearRow(row);
		}
	}

	public int getPoints() {
		return points;
	}
	
	public void draw(Graphics g) {
		clear();
		if (workingBlock != null)
			workingBlock.draw(this);
		for (int i = 0; i < lockedBlocks.size(); i++) {
			lockedBlocks.get(i).draw(this);
		}
	
		if (g != null)
			super.draw(g);
	
		// Display score
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.drawString("Points: " + points, 5, 12 + 5);
	
		// If the game is over, display the top scores
		if (isFilled()) {
			g2.drawString("You Lose", getGraphicsWidth()/2 - 20, getGraphicsHeight()/2 - 6);
			// Call to display top scores when the game is over
			displayTopScores(g);
		}
	}
	
	

public static void main(String[] args) {
    TetrisGrid grid = new TetrisGrid(10, 20);

    // Prompt the player to enter their name
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter your name: ");
    String playerName = scanner.nextLine();

    // Optionally: Display a welcome message
    System.out.println("Welcome, " + playerName + "! Let's play Tetris.");

    // Set a working block and start the game
    System.out.println(grid.setWorkingBlock(TetrisBlock.RIGHT_L.setLocation(10, 10)));

    while (true) {
        grid.update();
        grid.draw(null);
        System.out.println(grid);

        // Game control logic
        String line = scanner.nextLine();
        if (line.equals("")) {
            grid.moveDown();
        } else if (line.equals("<")) {
            grid.moveLeft();
        } else if (line.equals(">")) {
            grid.moveRight();
        } else if (line.equals("(")) {
            grid.turnLeft();
        } else if (line.equals(")")) {
            grid.turnRight();
        }

        // If the game is over (when filled), save the score
        if (grid.isFilled()) {
            // Save the score with the player's name
            Database.insertScore(playerName, grid.getPoints());  // Assuming `getPoints()` retrieves the score
            System.out.println("Game Over! Your score has been saved.");
            
            // Retrieve and display the top scores
            System.out.println("\nTop Scores:");
            List<String> topScores = Database.getTopScores();
            for (String score : topScores) {
                System.out.println(score);
            }
            
            break;
        }
    }

    scanner.close();
}


	
}
