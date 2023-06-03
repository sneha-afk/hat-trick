import java.io.File;

import javafx.scene.image.Image;

public class DeathBlock extends Actor implements BallCollider {
	private boolean movesOnAct;
	private double dx;
	private double dy;
	private int actsBeforeReversing;

	private int actsPassedSinceReverse;

	public DeathBlock(Image color) {
		setImage(color);
		movesOnAct = false;
	}

	public DeathBlock(Image color, double dx, double dy, int abr) {
		setImage(color);
		this.dx = dx;
		this.dy = dy;
		actsBeforeReversing = abr;
		movesOnAct = true;

		actsPassedSinceReverse = 0;
	}

	@Override
	public void act(long now) {
		if (movesOnAct) { // A little less laggy with this
			if (actsPassedSinceReverse == actsBeforeReversing) {
				dx = -dx;
				dy = -dy;
				actsPassedSinceReverse = 0;
			} else {
				move(dx, dy);
				actsPassedSinceReverse++;
			}
		}

		if (isTouchingBall())
			onBallCollision();
	}

	@Override
	public void onBallCollision() {
		Level world = (Level) getWorld();
		world.onGameOver();
	}

	public static Image getColor() {
		File colorDirectory = new File(System.getProperty("user.dir") + "/src/Images/Block Sprites");
		File[] allColors = colorDirectory.listFiles();

		String color = allColors[(int) (Math.random() * allColors.length)].getName();
		return new Image("/Images/Block Sprites/" + color, Level.BLOCK_SIZE, Level.BLOCK_SIZE, true, true);
	}

}
