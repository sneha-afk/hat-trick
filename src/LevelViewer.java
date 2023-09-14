import javafx.scene.Parent;
import javafx.scene.Scene;

public class LevelViewer extends Scene {
	private Game gameStage;

	public LevelViewer(Parent level, Game stage) {
		super(level);
		gameStage = stage;
	}
	
	public Game getStage() {
		return gameStage;
	}
	
	public void triggerNextLevel() {
		gameStage.nextLevel();
	}
}
