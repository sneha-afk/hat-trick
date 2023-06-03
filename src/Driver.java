import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Driver extends Application {
	public static double USER_SCREEN_WIDTH;
	public static double USER_SCREEN_HEIGHT;
	public static double MIN_WINDOW_WIDTH;
	public static double MIN_WINDOW_HEIGHT;
	public static double PADDING;
	public static BackgroundFill DEFAULT_BUTTON_FILL = new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5),
			new Insets(0));

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage st) throws Exception {
		USER_SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
		USER_SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
		MIN_WINDOW_WIDTH = USER_SCREEN_WIDTH / 2;
		MIN_WINDOW_HEIGHT = USER_SCREEN_HEIGHT / 2;
		PADDING = MIN_WINDOW_WIDTH / 50;

		Game gameStage = new Game();
		gameStage.setTitle("Hat Trick");
		gameStage.setMinHeight(MIN_WINDOW_HEIGHT);
		gameStage.setMinSize(Math.min(MIN_WINDOW_WIDTH, Level.WIDTH), Math.min(MIN_WINDOW_HEIGHT, Level.HEIGHT));
		gameStage.setMaxSize(USER_SCREEN_WIDTH, USER_SCREEN_HEIGHT);

		gameStage.setResizable(true);
		gameStage.show();
	}
}
