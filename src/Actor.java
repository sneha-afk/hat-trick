import java.util.ArrayList;
import java.util.List;

public abstract class Actor extends javafx.scene.image.ImageView {
	public void move(double dx, double dy) {
		setX(getX() + dx);
		setY(getY() + dy);
	}

	public World getWorld() {
		return (World) getParent();
	}

	public double getWidth() {
		return getBoundsInLocal().getWidth();
	}

	public double getHeight() {
		return getBoundsInLocal().getHeight();
	}

	public <A extends Actor> java.util.List<A> getIntersectingObjects(java.lang.Class<A> cls) {
		ArrayList<A> objs = new ArrayList<A>();
		List<A> allOfThisType = getWorld().getObjects(cls);

		if (!allOfThisType.isEmpty()) {
			for (A a : allOfThisType) {
				if (a != this && intersects(a.getBoundsInLocal()))
					objs.add(a);
			}
		}

		return objs;
	}

	public <A extends Actor> A getOneIntersectingObject(java.lang.Class<A> cls) {
		List<A> inter = getIntersectingObjects(cls);

		if (inter.isEmpty())
			return null;

		return inter.get(0);
	}

	public abstract void act(long now);
}
