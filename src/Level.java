import java.io.File;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Level extends World {
	public static final double BLOCK_SIZE = Driver.USER_SCREEN_HEIGHT / 18;
	public static final double BALL_SIZE = BLOCK_SIZE * 0.7;
	public static final int NUM_ROWS = 16;
	public static final int NUM_COLS = 9;
	public static final double WIDTH = NUM_COLS * BLOCK_SIZE;
	public static final double HEIGHT = NUM_ROWS * BLOCK_SIZE;

	private static File previousBlueprint;

	private Ball ball;

	public Level() {
		setBackground(new Background(new BackgroundImage(new Image("/Images/grass.png"), BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

		loadBlueprint(getBlueprint());

		ImageView pause = new ImageView("/GUI/pause.png");
		pause.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pauseMenu();
			}
		});
		getChildren().add(pause);

		ball = new Ball(0, 0);
		ball.setX((WIDTH / 2) - (ball.getWidth() / 2));
		ball.setY(HEIGHT - ball.getHeight());
		add(ball);

		// Ball movements event handlers: keyboard and mouse
		this.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				addKeyCode(event.getCode());
			}
		});

		this.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				removeKeyCode(event.getCode());
			}
		});

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			private boolean hasBeenClickedOnce;
			private int moveValue = 2;

			@Override
			public void handle(MouseEvent event) {
				if (!clickedPause(event.getX(), event.getY()))
					if (!hasBeenClickedOnce) {
						ball.setDy(-moveValue);
						hasBeenClickedOnce = true;
					} else {
						if (ball.getDy() == 0) // If still, start moving
							ball.setDy(-moveValue);
						else // If moving, stop moving
							ball.setDy(0);
					}
			}

			public boolean clickedPause(double x, double y) {
				double leftEdge = pause.getX();
				double rightEdge = leftEdge + pause.getImage().getWidth();
				double topEdge = pause.getY();
				double bottomEdge = topEdge + pause.getImage().getHeight();

				return leftEdge <= x && x <= rightEdge && topEdge <= y && y <= bottomEdge;
			}
		});
	}

	@Override
	public void act(long now) {
		this.requestFocus();
	}

	private void loadBlueprint(File blueprint) {
		new LevelBuilder(this, blueprint);
	}

	public Ball getBall() {
		return ball;
	}

	public void onGameOver() {
		levelOverMenu(false);
	}

	public void onComplete() {
		levelOverMenu(true);
	}

	private void levelOverMenu(boolean passed) {
		stop();

		VBox content = new VBox(Driver.PADDING);
		Text title = new Text("");
		title.setFont(new Font("Calibri Light", 36));
		title.setTextAlignment(TextAlignment.LEFT);

		Text dir = new Text("");
		dir.setFont(new Font("Calibri Light", 18));
		dir.setTextAlignment(TextAlignment.LEFT);

		Button decision = new Button("");
		Button menu = new Button("Main Menu", new ImageView("/GUI/home.png"));
		Button shop = new Button("Shop", new ImageView("/GUI/cart.png"));

		decision.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				getStage().playButtonClick();
				getStage().nextLevel();
			}
		});

		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				getStage().playButtonClick();
				getStage().mainMenu();
			}
		});

		shop.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				getStage().playButtonClick();
				getStage().shop();
			}
		});

		VBox buttons = new VBox(Driver.PADDING, decision, menu, shop);
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(Driver.PADDING, 0, 0, 0));
		
		for (Node b : buttons.getChildren()) {
			((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
		}

		if (passed) {
			title.setText("Goal!");
			dir.setText("Great job!");

			getStage().updateData();

			decision.setText("Next Level");
			decision.setGraphic(new ImageView("/GUI/play.png"));
		} else {
			title.setText("Fail!");
			dir.setText("Better luck next time.");

			decision.setText("Try Again");
			decision.setGraphic(new ImageView("/GUI/return.png"));
		}

		content.getChildren().addAll(title, dir, buttons);
		content.setMinSize(getWidth() / 2, getHeight() / 2);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(Driver.PADDING));
		content.setBackground(new Background(
				new BackgroundFill(Color.ALICEBLUE, new CornerRadii(Driver.PADDING), new Insets(0))));

		content.setLayoutX((getWidth() / 4));
		content.setLayoutY((getHeight() / 4));
		this.getChildren().add(content);
	}

	private void pauseMenu() {
		stop();

		VBox content = new VBox(Driver.PADDING);

		Text title = new Text("Paused");
		title.setFont(new Font("Calibri Light", 36));
		title.setTextAlignment(TextAlignment.LEFT);

		Text dir = new Text("Taking a break?");
		dir.setFont(new Font("Calibri Light", 18));
		dir.setTextAlignment(TextAlignment.LEFT);

		Button returnButton = new Button("Return", new ImageView("/GUI/play.png"));
		Button menu = new Button("Main Menu", new ImageView("/GUI/return.png"));

		returnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				getChildren().remove(content);
				getStage().playButtonClick();
				start();
			}
		});

		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				getStage().playButtonClick();
				getStage().mainMenu();
			}
		});

		VBox buttons = new VBox(Driver.PADDING, returnButton, menu);
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(Driver.PADDING, 0, 0, 0));
		
		for (Node b : buttons.getChildren()) {
			((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
		}

		content.getChildren().addAll(title, dir, buttons);
		content.setMinSize(getWidth() / 2, getHeight() / 2);
		content.setAlignment(Pos.CENTER);
		content.setPadding(new Insets(Driver.PADDING));
		content.setBackground(
				new Background(new BackgroundFill(Color.ALICEBLUE, new CornerRadii(Driver.PADDING), new Insets(0))));

		content.setLayoutX((getWidth() / 4));
		content.setLayoutY((getHeight() / 4));
		this.getChildren().add(content);
	}

	public static File getBlueprint() {
		File blueprintDirectory = new File(System.getProperty("user.dir") + "/Level Blueprints/");
		File[] allBlueprints = blueprintDirectory.listFiles();

		File blueprint = allBlueprints[(int) (Math.random() * allBlueprints.length)];
		while (previousBlueprint != null && blueprint.getName().equals(previousBlueprint.getName()))
			blueprint = allBlueprints[(int) (Math.random() * allBlueprints.length)];

		previousBlueprint = blueprint;

		return blueprint;
	}

	/** Similar to getScene() but casts to LevelViewer. */
	public LevelViewer getViewer() {
		return (LevelViewer) getScene();
	}

	public Game getStage() {
		return (Game) getViewer().getStage();
	}
}
