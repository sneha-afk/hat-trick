import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

public abstract class World extends javafx.scene.layout.Pane {
	private javafx.animation.AnimationTimer timer;
	private HashSet<KeyCode> keycodes = new HashSet<KeyCode>();

	public World() {
		timer = new AnimationTimer() {
			public void handle(long now) {
				act(now);
				List<Actor> actors = getObjects(Actor.class);

				for (Actor a : actors)
					a.act(now);

			}
		};
	}

	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}

	public AnimationTimer getTimer() {
		return timer;
	}

	public void setTimer(AnimationTimer timer) {
		this.timer = timer;
	}

	public void add(Actor actor) {
		getChildren().add(actor);
	}

	public void remove(Actor actor) {
		getChildren().remove(actor);
	}

	@SuppressWarnings("unchecked")
	public <A extends Actor> java.util.List<A> getObjects(java.lang.Class<A> cls) {
		List<Node> nodes = getChildren();
		List<A> toReturn = new ArrayList<A>();

		for (Node n : nodes) {
			if (n.getClass().equals(cls) || n.getClass().getSuperclass().equals(cls))
				toReturn.add((A) n);
		}

		return toReturn;
	}

	public abstract void act(long now);

	public boolean isKeyDown(KeyCode code) {
		return keycodes.contains(code);
	}

	public void addKeyCode(KeyCode code) {
		keycodes.add(code);
	}

	public void removeKeyCode(KeyCode code) {
		keycodes.remove(code);
	}
}
