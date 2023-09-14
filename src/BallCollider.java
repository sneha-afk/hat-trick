
public interface BallCollider {
	public default boolean isTouchingBall() {
		if (this.getClass().getSuperclass() == Actor.class)
			return ((Actor) this).getOneIntersectingObject(Ball.class) != null;
		return false;
	}

	public void onBallCollision();
}
