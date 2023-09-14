import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.image.Image;

/** Takes in a Level and a File to build the level according to the File. */
public class LevelBuilder {
	public LevelBuilder(Level toBuild, File blueprint) {
		Actor[][] layout = new Actor[Level.NUM_ROWS][Level.NUM_COLS];

		try {
			Scanner in = new Scanner(blueprint);
			Image color = DeathBlock.getColor();

			// Skip title
			in.nextLine();
			in.nextLine();

			// Set up goal section
			for (int col = 0; col < Level.NUM_COLS; col++) {
				if (col < 3 || col > 5)
					layout[0][col] = new BlankBlock();
				else
					layout[0][col] = getGoalBlocks()[col - 3];
			}

			int currRow = 1;
			int currCol = 0;
			while (in.hasNext() && currRow < Level.NUM_ROWS) {
				if (currCol < Level.NUM_COLS) {
					String sym = in.next();

					if (sym.equals("END"))
						break;

					if (sym.charAt(0) == 'X') {
						if (sym.length() > 1) { // Moving
							// X:dx,dy:#ActsBeforeReversing
							int indexOfComma = sym.indexOf(",");
							int indexOfFirstColon = sym.indexOf(":");
							int indexOfSecondColon = sym.lastIndexOf(":");

							double dx = Double.parseDouble(sym.substring(indexOfFirstColon + 1, indexOfComma));
							double dy = Double.parseDouble(sym.substring(indexOfComma + 1, indexOfSecondColon));
							int abr = Integer.parseInt(sym.substring(indexOfSecondColon + 1));

							layout[currRow][currCol] = new DeathBlock(color, dx, dy, abr);
						} else // Non-moving
							layout[currRow][currCol] = new DeathBlock(color);
					}

					currCol++;
				} else {
					currCol = 0;
					currRow++;
				}
			}

			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (int row = 0; row < Level.NUM_ROWS; row++) {
			for (int col = 0; col < Level.NUM_COLS; col++) {
				Actor at = layout[row][col];

				if (at != null) {
					at.setX(Level.BLOCK_SIZE * col);
					at.setY(Level.BLOCK_SIZE * row);
					toBuild.add(at);
				}
			}
		}
	}

	public static GoalBlock[] getGoalBlocks() {
		File goalDirectory = new File(System.getProperty("user.dir") + "/src/Images/Goal Post Parts/");
		File[] allParts = goalDirectory.listFiles();

		GoalBlock[] blocks = new GoalBlock[allParts.length];

		for (int i = 0; i < blocks.length; i++) {
			File retrieve = allParts[i];
			Image image = new Image("/Images/Goal Post Parts/" + retrieve.getName(), Level.BLOCK_SIZE, Level.BLOCK_SIZE,
					false, true);

			blocks[i] = new GoalBlock(image);
		}

		return blocks;
	}
}
