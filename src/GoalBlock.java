import javafx.scene.image.Image;

public class GoalBlock extends Actor implements BallCollider {

	public GoalBlock(Image image) {
		setImage(image);
	}

	@Override
	public void act(long now) {
		if (isTouchingBall())
			onBallCollision();
	}

	@Override
	public void onBallCollision() {
		Level world = (Level) getWorld();
		world.onComplete();
	}

}
