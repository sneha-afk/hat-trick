import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Game extends Stage {
	public static final int NUM_LEVELS_IN_SEASON = 7;
	public static final int BASE_NUM_COINS_INCREMENT = 100;
	public static final double DEFAULT_AUDIO_VOLUME = 0.5;

	private Scene previous;
	private Scene menu;
	private Scene help;
	private Scene settings;
	private Scene shop;
	private Scene customize;

	private PlayerData data;

	private MediaPlayer musicPlayer;
	private MediaPlayer soundPlayer;
	private Media menuMusic;
	private Media levelMusic;
	private Media buttonClickSFX;

	private Slider musicVolumeSlider;
	private Slider soundsVolumeSlider;

	public Game() {
		mainMenu();
		getIcons().add(new Image("/Images/icon.png"));
		initializeMusic();
	}

	public Game(StageStyle style) {
		super(style);
		mainMenu();
		getIcons().add(new Image("/Images/icon.png"));
		initializeMusic();
	}

	public void changeScene(Scene toChange) {
		previous = getScene();
		setScene(toChange);
		sizeToScene();
	}

	public void previousScene() {
		Scene temp = getScene(); // Current scene

		setScene(previous);
		sizeToScene();

		previous = temp;
	}

	public void nextLevel() {
		setMusicToLevel();

		Level level = new Level();
		level.start();
		LevelViewer viewer = new LevelViewer(level, this);

		String ballSpriteToLoad = "/Images/Ball Sprites/" + getData().getBallSprite();
		level.getBall().setImage(new Image(ballSpriteToLoad, Level.BALL_SIZE, Level.BALL_SIZE, true, true));

		changeScene(viewer);
		setY(level.getHeight() / 20);
	}

	public PlayerData getData() {
		return data;
	}

	public void updateData() {
		data.incrementLevels();
		if (data.getNumLevels() % NUM_LEVELS_IN_SEASON == 0) {
			data.incrementSeasons();
			data.incrementCoins(BASE_NUM_COINS_INCREMENT);
		}

		data.autosave();
	}

	private void initializeMusic() {
		File menuMusicFile = new File(System.getProperty("user.dir") + "/src/Sounds/menu_music.wav");
		File levelMusicFile = new File(System.getProperty("user.dir") + "/src/Sounds/level_music.wav");
		File buttonClickFile = new File(System.getProperty("user.dir") + "/src/Sounds/button_click.wav");

		menuMusic = new Media(menuMusicFile.toURI().toString());
		levelMusic = new Media(levelMusicFile.toURI().toString());
		buttonClickSFX = new Media(buttonClickFile.toURI().toString());

		musicPlayer = new MediaPlayer(menuMusic);
		// Keep looping the audio
		musicPlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				musicPlayer.seek(Duration.ZERO);
			}
		});
		musicPlayer.setVolume(DEFAULT_AUDIO_VOLUME);
		musicPlayer.play();

		soundPlayer = new MediaPlayer(buttonClickSFX);
		soundPlayer.setVolume(DEFAULT_AUDIO_VOLUME);
	}

	private void setMusicToMenu() {
		if (musicPlayer != null && musicPlayer.getMedia() != menuMusic) {
			musicPlayer.stop();
			musicPlayer = new MediaPlayer(menuMusic);

			if (musicVolumeSlider != null)
				musicPlayer.setVolume(musicVolumeSlider.getValue() / 100);
			else
				musicPlayer.setVolume(DEFAULT_AUDIO_VOLUME);

			// Keep looping the audio
			musicPlayer.setOnEndOfMedia(new Runnable() {
				public void run() {
					musicPlayer.seek(Duration.ZERO);
				}
			});

			musicPlayer.play();
		}
	}

	private void setMusicToLevel() {
		if (musicPlayer != null && musicPlayer.getMedia() != levelMusic) {
			musicPlayer.stop();
			musicPlayer = new MediaPlayer(levelMusic);

			if (musicVolumeSlider != null)
				musicPlayer.setVolume(musicVolumeSlider.getValue() / 100);
			else
				musicPlayer.setVolume(DEFAULT_AUDIO_VOLUME);

			// Keep looping the audio
			musicPlayer.setOnEndOfMedia(new Runnable() {
				public void run() {
					musicPlayer.seek(Duration.ZERO);
				}
			});
			musicPlayer.play();
		}
	}

	public void playButtonClick() {
		soundPlayer.seek(Duration.ZERO);
		soundPlayer.play();
	}

	public void mainMenu() {
		if (menu == null) {
			BorderPane pane = new BorderPane();

			Text title = new Text("Hat Trick");
			title.setFont(Font.font("Calibri", FontWeight.BOLD, 54));
			title.setTextAlignment(TextAlignment.CENTER);

			DropShadow dropShadow = new DropShadow();
			dropShadow.setOffsetX(3);
			dropShadow.setOffsetY(4);
			title.setEffect(dropShadow);

			VBox titleContainer = new VBox(title);
			titleContainer.setPadding(new Insets(Driver.PADDING));
			titleContainer.setAlignment(Pos.CENTER);

			VBox buttons = new VBox(Driver.PADDING);
			Button startGame = new Button("Start New Game", new ImageView("/GUI/gamepad.png"));
			Button saveButton = new Button("Save Game", new ImageView("/GUI/save.png"));
			Button loadSave = new Button("Load Game", new ImageView("/GUI/import.png"));
			Button customizeButton = new Button("Customize", new ImageView("/GUI/wrench.png"));
			Button shop = new Button("Shop", new ImageView("/GUI/cart.png"));
			Button goToHelp = new Button("Help", new ImageView("/GUI/information.png"));
			Button goToSettings = new Button("Settings", new ImageView("/GUI/gear.png"));
			Button quit = new Button("Quit", new ImageView("/GUI/power.png"));
			buttons.getChildren().addAll(startGame, saveButton, loadSave, new Separator(), customizeButton, shop,
					new Separator(), goToHelp, goToSettings, quit);
			buttons.setAlignment(Pos.CENTER_LEFT);

			pane.setRight(titleContainer);
			pane.setLeft(buttons);
			pane.setPadding(new Insets(Driver.PADDING));
			pane.setMinSize(Driver.MIN_WINDOW_WIDTH, Driver.MIN_WINDOW_HEIGHT);

			data = new PlayerData(this);

			startGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					nextLevel();
					playButtonClick();
				}
			});

			loadSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					data.loadDialog();
					playButtonClick();
				}
			});

			saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					data.saveDialog();
					playButtonClick();
				}
			});

			customizeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					customize();
					playButtonClick();
				}
			});

			shop.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					shop();
					playButtonClick();
				}
			});

			goToHelp.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					help();
					playButtonClick();
				}
			});

			goToSettings.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					settings();
					playButtonClick();
				}
			});

			quit.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					playButtonClick();
					System.exit(0);
				}
			});

			for (Node b : buttons.getChildren()) {
				if (b instanceof Button)
					((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
			}

			menu = new Scene(pane);
		}

		setMusicToMenu();
		changeScene(menu);
	}

	public void help() {
		if (help == null) {
			BorderPane pane = new BorderPane();

			Text title = new Text("Help");
			title.setFont(new Font("Calibri Light", 36));
			title.setTextAlignment(TextAlignment.LEFT);

			Text dir = new Text("Learn how to play.");
			dir.setFont(new Font("Calibri Light", 18));
			dir.setTextAlignment(TextAlignment.LEFT);

			VBox text = new VBox(title, dir);
			text.setAlignment(Pos.CENTER_LEFT);
			text.setPadding(new Insets(0, 0, Driver.PADDING * 2, 0));

			VBox allInstructions = new VBox(Driver.PADDING * 2);
			allInstructions.setPadding(new Insets(Driver.PADDING));
			Font boldTitleFont = Font.font("Calibri", FontWeight.BOLD, 18);
			Font regularFont = new Font("Calibri Light", 16);
			Color warningColor = Color.RED;

			// Movement Section
			Text moveInstructTitle = new Text("Movement	");
			moveInstructTitle.setFont(boldTitleFont);
			moveInstructTitle.setTextAlignment(TextAlignment.LEFT);
			Text moveExplain = new Text("Use the A-D keys or LEFT-RIGHT arrow keys to move the ball horizontally.\n"
					+ "Mouse clicks toggle the ball rolling forward.");
			moveExplain.setFont(regularFont);
			moveExplain.setTextAlignment(TextAlignment.LEFT);
			Text moveWarning = new Text("You cannot roll backwards, think before moving forward!");
			moveWarning.setFont(regularFont);
			moveWarning.setFill(warningColor);
			moveWarning.setTextAlignment(TextAlignment.LEFT);
			allInstructions.getChildren()
					.add(new HBox(Driver.PADDING * 2, moveInstructTitle, new VBox(moveExplain, moveWarning)));
			// Saves Section
			Text savesInstructTitle = new Text("Saves		");
			savesInstructTitle.setFont(boldTitleFont);
			savesInstructTitle.setTextAlignment(TextAlignment.LEFT);
			Text savesExplain = new Text("You can load and save files to keep your progress. One file is autosaved\n"
					+ "which is overwritten after you open the game again and clear a level.");
			savesExplain.setFont(regularFont);
			savesExplain.setTextAlignment(TextAlignment.LEFT);
			Text savesWarning = new Text("To ensure you won't lose data, save your progress before exiting the game!");
			savesWarning.setFont(regularFont);
			savesWarning.setFill(warningColor);
			savesWarning.setTextAlignment(TextAlignment.LEFT);
			allInstructions.getChildren()
					.add(new HBox(Driver.PADDING * 2, savesInstructTitle, new VBox(savesExplain, savesWarning)));
			// Shop Section
			Text shopInstructTitle = new Text("Shop		");
			shopInstructTitle.setFont(boldTitleFont);
			shopInstructTitle.setTextAlignment(TextAlignment.LEFT);
			Text shopExplain = new Text("After one season, or seven levels, you are awarded 100 coins which can be\n"
					+ "used to purchase ball colors in the shop!");
			shopExplain.setFont(regularFont);
			shopExplain.setTextAlignment(TextAlignment.LEFT);
			allInstructions.getChildren().add(new HBox(Driver.PADDING * 2, shopInstructTitle, shopExplain));
			// Customize Section
			Text customizeInstructTitle = new Text("Customize	");
			customizeInstructTitle.setFont(boldTitleFont);
			customizeInstructTitle.setTextAlignment(TextAlignment.LEFT);
			Text customizeExplain = new Text("After you purchase ball colors, you can change the color of the ball\n"
					+ "in the Customize menu.");
			customizeExplain.setFont(regularFont);
			customizeExplain.setTextAlignment(TextAlignment.LEFT);
			allInstructions.getChildren().add(new HBox(Driver.PADDING * 2, customizeInstructTitle, customizeExplain));

			Button backToPrevious = new Button("Back", new ImageView("/GUI/return.png"));
			backToPrevious.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					playButtonClick();
					previousScene();
				}
			});

			VBox buttons = new VBox(Driver.PADDING, backToPrevious);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			for (Node b : buttons.getChildren()) {
				((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
			}

			pane.setTop(text);
			pane.setCenter(allInstructions);
			pane.setRight(buttons);
			pane.setPadding(new Insets(Driver.PADDING));
			pane.setMinSize(Driver.MIN_WINDOW_WIDTH, Driver.MIN_WINDOW_HEIGHT);

			help = new Scene(pane);
		}

		setMusicToMenu();
		changeScene(help);
	}

	public void settings() {
		if (settings == null) {
			BorderPane pane = new BorderPane();

			Text title = new Text("Settings");
			title.setFont(new Font("Calibri Light", 36));
			title.setTextAlignment(TextAlignment.LEFT);

			Text dir = new Text("Adjust your settings.");
			dir.setFont(new Font("Calibri Light", 18));
			dir.setTextAlignment(TextAlignment.LEFT);

			VBox text = new VBox(title, dir);
			text.setAlignment(Pos.CENTER_LEFT);

			Button clearSave = new Button("Clear Save", new ImageView("/GUI/trashcan.png"));
			Button audioButton = new Button("Audio", new ImageView("/GUI/audio.png"));
			Button backToMenu = new Button("Back", new ImageView("/GUI/return.png"));

			clearSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					playButtonClick();
					data.clearDialog();
				}
			});

			audioButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					playButtonClick();
					audioSettings();
				}
			});

			backToMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					playButtonClick();
					mainMenu();
				}
			});

			VBox buttons = new VBox(Driver.PADDING, clearSave, audioButton, new Separator(), backToMenu);
			buttons.setAlignment(Pos.CENTER_RIGHT);
			for (Node b : buttons.getChildren()) {
				if (b instanceof Button)
					((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
			}

			pane.setLeft(text);
			pane.setRight(buttons);
			pane.setPadding(new Insets(Driver.PADDING));
			pane.setMinSize(Driver.MIN_WINDOW_WIDTH, Driver.MIN_WINDOW_HEIGHT);

			settings = new Scene(pane);
		}

		setMusicToMenu();
		changeScene(settings);
	}

	public void audioSettings() {
		Stage audioSettings = new Stage();
		audioSettings.getIcons().add(new Image("/Images/icon.png"));
		audioSettings.setTitle("Audio Settings");
		audioSettings.setMinWidth(Driver.USER_SCREEN_WIDTH / 4);
		audioSettings.setMinHeight(Driver.USER_SCREEN_HEIGHT / 4);
		audioSettings.setResizable(false);

		BorderPane audioSettingsPane = new BorderPane();

		Text title = new Text("Audio");
		title.setFont(new Font("Calibri Light", 24));
		title.setTextAlignment(TextAlignment.LEFT);

		Text dir = new Text("Adjust the volume of the game.");
		dir.setFont(new Font("Calibri Light", 16));
		dir.setTextAlignment(TextAlignment.LEFT);

		VBox text = new VBox(Driver.PADDING, title, dir);
		text.setPadding(new Insets(Driver.PADDING));

		Text musicVolumeLabel = new Text("Music:	");
		musicVolumeLabel.setFont(new Font("Calibri Light", 14));
		musicVolumeSlider = new Slider(0, 100, musicPlayer.getVolume() * 100);
		musicVolumeSlider.setShowTickLabels(true);
		musicVolumeSlider.setShowTickMarks(true);
		musicVolumeSlider.setMinWidth(audioSettings.getMinWidth() * 0.7);
		HBox musicVolumeContent = new HBox(Driver.PADDING, musicVolumeLabel, musicVolumeSlider);

		Text soundsVolumeLabel = new Text("Sounds:	");
		soundsVolumeLabel.setFont(new Font("Calibri Light", 14));
		soundsVolumeSlider = new Slider(0, 100, soundPlayer.getVolume() * 100);
		soundsVolumeSlider.setShowTickLabels(true);
		soundsVolumeSlider.setShowTickMarks(true);
		soundsVolumeSlider.setMinWidth(audioSettings.getMinWidth() * 0.7);
		HBox soundVolumeContent = new HBox(Driver.PADDING, soundsVolumeLabel, soundsVolumeSlider);

		VBox volumeSliders = new VBox(Driver.PADDING, musicVolumeContent, soundVolumeContent);
		volumeSliders.setPadding(new Insets(Driver.PADDING));
		volumeSliders.setAlignment(Pos.CENTER);

		Button defaultButton = new Button("Default", new ImageView("/GUI/barsHorizontal.png"));
		Button confirmButton = new Button("Confirm", new ImageView("/GUI/checkmark.png"));

		musicVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				musicPlayer.setVolume(newValue.doubleValue() / 100);
			}
		});

		soundsVolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				playButtonClick(); // Player can test the volume
				soundPlayer.setVolume(newValue.doubleValue() / 100);
			}
		});

		defaultButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				playButtonClick();
				musicVolumeSlider.setValue(DEFAULT_AUDIO_VOLUME * 100);
				soundsVolumeSlider.setValue(DEFAULT_AUDIO_VOLUME * 100);

				musicPlayer.setVolume(musicVolumeSlider.getValue() / 100);
				soundPlayer.setVolume(soundsVolumeSlider.getValue() / 100);
			}
		});

		confirmButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				playButtonClick();
				audioSettings.close();
			}
		});

		HBox alignButton = new HBox(Driver.PADDING, defaultButton, confirmButton);
		alignButton.setPadding(new Insets(Driver.PADDING));
		alignButton.setAlignment(Pos.BOTTOM_RIGHT);
		for (Node b : alignButton.getChildren()) {
			((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
		}

		audioSettingsPane.setTop(text);
		audioSettingsPane.setCenter(volumeSliders);
		audioSettingsPane.setBottom(alignButton);

		audioSettings.setScene(new Scene(audioSettingsPane));
		audioSettings.show();
	}

	public void shop() {
		if (shop == null) {
			BorderPane pane = new BorderPane();

			Text title = new Text("Shop");
			title.setFont(new Font("Calibri Light", 36));
			title.setTextAlignment(TextAlignment.LEFT);

			Text dir = new Text("Buy ball colors.");
			dir.setFont(new Font("Calibri Light", 18));
			dir.setTextAlignment(TextAlignment.LEFT);

			VBox text = new VBox(title, dir);
			text.setAlignment(Pos.CENTER_LEFT);
			text.setPadding(new Insets(0, 0, Driver.PADDING, 0));

			updateShopScreen();

			Label numCoinsLabel = new Label("" + getData().getNumCoins(),
					new ImageView(new Image("/Images/coin.png", 25, 25, true, true)));

			Button backToPrevious = new Button("Back", new ImageView("/GUI/return.png"));
			backToPrevious.setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
			backToPrevious.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					playButtonClick();
					previousScene();
				}
			});

			VBox rightSide = new VBox(Driver.PADDING * 7, numCoinsLabel, backToPrevious);
			rightSide.setPadding(new Insets(Driver.PADDING));
			rightSide.setAlignment(Pos.CENTER_RIGHT);

			pane.setTop(text);
			pane.setRight(rightSide);
			pane.setPadding(new Insets(Driver.PADDING));
			pane.setMinSize(Driver.MIN_WINDOW_WIDTH, Driver.MIN_WINDOW_HEIGHT);

			shop = new Scene(pane);
		}

		updateShopScreen();
		setMusicToMenu();
		changeScene(shop);
	}

	public void customize() {
		if (customize == null) {
			BorderPane pane = new BorderPane();

			Text title = new Text("Customize");
			title.setFont(new Font("Calibri Light", 36));
			title.setTextAlignment(TextAlignment.LEFT);

			Text dir = new Text("Cuztomize your game experience.");
			dir.setFont(new Font("Calibri Light", 18));
			dir.setTextAlignment(TextAlignment.LEFT);

			VBox text = new VBox(title, dir);
			text.setAlignment(Pos.CENTER_LEFT);
			text.setPadding(new Insets(0, 0, Driver.PADDING, 0));

			Background selected = new Background(
					new BackgroundFill(Color.MEDIUMSEAGREEN, new CornerRadii(Driver.PADDING), new Insets(0)));

			TilePane tilePane = new TilePane(Driver.PADDING, Driver.PADDING);
			tilePane.setPrefColumns(6);

			ItemViewer defaultBallSpriteViewer = new ItemViewer(
					new Item("Default", new File("/Images/Ball Sprites/default_ball.png"), "ball_sprite", 0), false);
			defaultBallSpriteViewer.setBackground(selected);
			tilePane.getChildren().add(defaultBallSpriteViewer);

			for (Item i : getData().getUserItems()) {
				ItemViewer itemView = new ItemViewer(i, false);
				itemView.setBackground(null);
				tilePane.getChildren().add(itemView);
			}

			for (Node n : tilePane.getChildren()) {
				ItemViewer itemView = (ItemViewer) n;

				itemView.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// Find the currently selected item and deselect it
						for (Node n : tilePane.getChildren()) {
							ItemViewer checkItemView = (ItemViewer) n;

							if (checkItemView.getBackground() != null) {
								checkItemView.setBackground(null);
							}
						}

						itemView.setBackground(selected);
					}
				});
			}

			ScrollPane scrollPane = new ScrollPane(tilePane);

			Button saveButton = new Button("Save", new ImageView("/GUI/save.png"));
			Button backToPrevious = new Button("Back", new ImageView("/GUI/return.png"));

			saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					for (Node n : tilePane.getChildren()) {
						ItemViewer checkItemView = (ItemViewer) n;
						// Find the currently selected item and save it to the data
						if (checkItemView.getBackground() != null) {
							getData().setBallSprite(checkItemView.getItem().getFileName());
							getData().autosave();
						}
					}

					playButtonClick();
					previousScene();
				}
			});

			backToPrevious.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					playButtonClick();
					previousScene();
				}
			});

			VBox buttons = new VBox(Driver.PADDING, saveButton, backToPrevious);
			buttons.setPadding(new Insets(Driver.PADDING));
			buttons.setAlignment(Pos.CENTER_RIGHT);
			for (Node b : buttons.getChildren()) {
				((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
			}

			pane.setTop(text);
			pane.setCenter(scrollPane);
			pane.setLeft(buttons);
			pane.setPadding(new Insets(Driver.PADDING));
			pane.setMinSize(Driver.MIN_WINDOW_WIDTH, Driver.MIN_WINDOW_HEIGHT);

			customize = new Scene(pane);
		}

		setMusicToMenu();
		changeScene(customize);
	}

	public void updateCurrentlySelectedSprite() {
		if (customize != null) {
			String itemNameToSelect = getData().getBallSprite();

			ScrollPane scrollPane = (ScrollPane) ((BorderPane) customize.getRoot()).getCenter();

			Background selected = new Background(
					new BackgroundFill(Color.MEDIUMSEAGREEN, new CornerRadii(Driver.PADDING), new Insets(0)));

			TilePane tilePane = (TilePane) scrollPane.getContent();
			tilePane.getChildren().removeAll(tilePane.getChildren()); // Reset the tile pane
			tilePane.setPrefColumns(6);
			tilePane.setPadding(new Insets(Driver.PADDING));

			ItemViewer defaultBallSpriteViewer = new ItemViewer(
					new Item("Default", new File("/Images/Ball Sprites/default_ball.png"), "ball_sprite", 0), false);
			tilePane.getChildren().add(defaultBallSpriteViewer);

			for (Item i : getData().getUserItems()) {
				ItemViewer itemView = new ItemViewer(i, false);
				tilePane.getChildren().add(itemView);
			}

			for (Node n : tilePane.getChildren()) {
				ItemViewer itemView = (ItemViewer) n;
				if (itemView.getItem().getFileName().equals(itemNameToSelect))
					itemView.setBackground(selected);

				itemView.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// Find the currently selected item and deselect it
						for (Node n : tilePane.getChildren()) {
							ItemViewer checkItemView = (ItemViewer) n;
							if (checkItemView.getBackground() != null) {
								checkItemView.setBackground(null);
							}
						}
						itemView.setBackground(selected);
					}
				});
			}

			scrollPane.setContent(tilePane);
			scrollPane.setPadding(new Insets(Driver.PADDING));
		}
	}

	public void updateShopScreen() {
		if (shop != null) {
			// Update the coin label
			VBox coinCotnainer = (VBox) ((BorderPane) shop.getRoot()).getRight();
			Label coinLabel = (Label) coinCotnainer.getChildren().get(0);
			coinLabel.setText("" + getData().getNumCoins());

			TilePane tilePane = new TilePane(Driver.PADDING, Driver.PADDING);
			tilePane.setPrefColumns(6);
			tilePane.setPadding(new Insets(Driver.PADDING));

			for (Item i : getData().getShop().getCurrInventory()) {
				ItemViewer itemView = new ItemViewer(i);

				itemView.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						Stage confirmBuy = new Stage();
						confirmBuy.getIcons().add(new Image("/Images/icon.png"));
						confirmBuy.setTitle("View Item");
						confirmBuy.setMinWidth(Driver.USER_SCREEN_WIDTH / 3);
						confirmBuy.setMinHeight(Driver.USER_SCREEN_HEIGHT / 3);

						Text title = new Text("Purchase?");
						title.setFont(new Font("Calibri Light", 24));
						title.setTextAlignment(TextAlignment.LEFT);

						Text dir = new Text("You have " + getData().getNumCoins() + " coins.\n"
								+ "Would you like to buy: " + itemView.getItemName() + "?");
						dir.setFont(new Font("Calibri Light", 16));
						dir.setTextAlignment(TextAlignment.LEFT);

						Text response = new Text("");
						response.setFont(new Font("Calibri Light", 18));
						response.setFill(Color.RED);

						Button noButton = new Button("No", new ImageView("/GUI/cross.png"));
						noButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								confirmBuy.close();
							}
						});

						Button yesButton = new Button("Yes",
								new ImageView(new Image("/Images/coin.png", 25, 25, true, true)));
						yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								int price = i.getPrice();
								int userCoins = getData().getNumCoins();

								if (price > userCoins) {
									response.setText("You do not have enough coins. ");
								} else {
									tilePane.getChildren().remove(itemView);
									getData().getShop().remove(i.getScreenName());
									getData().decrementCoins(price);
									getData().autosave();

									coinLabel.setText("" + getData().getNumCoins());

									response.setText("Success!");
									response.setFill(Color.DARKGREEN);
									noButton.setText("Close");

									// Do not process more Yes responses
									yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
										@Override
										public void handle(MouseEvent event) {
										}
									});
								}
							}
						});

						HBox alignButton = new HBox(Driver.PADDING / 2, response, yesButton, noButton);
						alignButton.setAlignment(Pos.BOTTOM_RIGHT);
						alignButton.setPadding(new Insets(Driver.PADDING));
						for (Node b : alignButton.getChildren()) {
							if (b instanceof Button)
								((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
						}

						VBox content = new VBox(title, dir);
						content.setPadding(new Insets(Driver.PADDING));

						BorderPane bp = new BorderPane(new ItemViewer(itemView));
						bp.setTop(content);
						bp.setBottom(alignButton);

						confirmBuy.setScene(new Scene(bp));
						confirmBuy.show();
					}
				});

				tilePane.getChildren().add(itemView);
			}

			ScrollPane scrollPane = new ScrollPane(tilePane);

			((BorderPane) shop.getRoot()).setCenter(scrollPane);
		}
	}

	public void setMinSize(double minWidth, double minHeight) {
		setMinWidth(minWidth);
		setMinHeight(minHeight);
	}

	public void setMaxSize(double maxWidth, double maxHeight) {
		setMaxWidth(maxWidth);
		setMaxHeight(maxHeight);
	}

}