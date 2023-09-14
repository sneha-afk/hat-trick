import javafx.scene.image.Image;

/** A transparent block that has no actions. The ball can pass through this. */
public class BlankBlock extends Actor {
	public BlankBlock() {
		setImage(new Image("/Images/transparent.png", Level.BLOCK_SIZE, Level.BLOCK_SIZE, true, true));
	}

	@Override
	public void act(long now) {
		// Will not do anything.
	}

}
