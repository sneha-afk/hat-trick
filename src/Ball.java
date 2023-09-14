import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Ball extends Actor {
	private int dx;
	private int dy;

	public Ball(int dx, int dy) {
		setImage(new Image("/Images/Ball Sprites/default_ball.png", Level.BALL_SIZE, Level.BALL_SIZE, true, true));
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void act(long now) {
		move(dx, dy);
		edgeLoop();

		int moveAmount = 3;

		if (getWorld().isKeyDown(KeyCode.LEFT) || getWorld().isKeyDown(KeyCode.A))
			move(-moveAmount, 0);
		else if (getWorld().isKeyDown(KeyCode.RIGHT) || getWorld().isKeyDown(KeyCode.D))
			move(moveAmount, 0);
	}

	@Override
	public void move(double dx, double dy) {
		setX(getX() + dx);
		setY(getY() + dy);
		edgeLoop();
	}

	public void edgeLoop() {
		double leftEdge = getX();
		double rightEdge = leftEdge + getWidth();
		double topEdge = getY();
		double bottomEdge = topEdge + getHeight();

		if (leftEdge <= 0)
			setX(0);
		if (rightEdge >= getWorld().getWidth())
			setX(getWorld().getWidth() - getWidth());
		if (topEdge <= 0)
			getLevelWorld().onGameOver();
		if (bottomEdge >= getWorld().getHeight())
			setY(getWorld().getHeight() - getHeight());

	}

	public Level getLevelWorld() {
		return (Level) getWorld();
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}
}
