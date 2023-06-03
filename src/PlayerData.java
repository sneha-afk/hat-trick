import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class PlayerData {
	private SaveFile[] saveFiles;

	/** Used to find out which save slot is currently selected. */
	private ToggleGroup toggleGroup;

	/** Access to the main stage is needed to change scenes. */
	private Game game;

	private int numSeasonsComplete;
	private int numLevelsComplete;
	private int numCoins;
	private String ballSpriteName;
	private Shop shop;
	private ArrayList<Item> userItems;

	public PlayerData(Game stage) {
		// Create all the SaveFile objects once so excess memory isn't used
		saveFiles = getSaveFiles();

		game = stage;

		numSeasonsComplete = 0;
		numLevelsComplete = 0;
		numCoins = 0;
		ballSpriteName = "default_ball.png";

		shop = new Shop(new File(System.getProperty("user.dir") + "/Shop Data/01 - Complete Inventory.txt"));
		shop.sortByName();

		userItems = shop.getItemsNotInCurrInventory();
	}

	public void incrementLevels() {
		numLevelsComplete++;
	}

	public void incrementSeasons() {
		numSeasonsComplete++;
	}

	public void incrementCoins(int incr) {
		numCoins += incr;
	}

	public void decrementCoins(int decr) {
		numCoins -= decr;
	}

	public void setBallSprite(String fileName) {
		ballSpriteName = fileName;
	}

	public int getNumLevels() {
		return numLevelsComplete;
	}

	public int getNumSeasons() {
		return numSeasonsComplete;
	}

	public int getNumCoins() {
		return numCoins;
	}

	public ArrayList<Item> getUserItems() {
		userItems = shop.getItemsNotInCurrInventory();
		return userItems;
	}

	public String getBallSprite() {
		return ballSpriteName;
	}

	public Shop getShop() {
		return shop;
	}

	public void loadDialog() {
		Button submit = showDialog("load");

		submit.setGraphic(new ImageView("/GUI/checkmark.png"));
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				SaveFile sf = getCurrentSelectedSave();

				if (sf.isFileEmpty()) {
					Stage erStage = new Stage();
					erStage.getIcons().add(new Image("/Images/icon.png"));
					erStage.setTitle("Error");
					erStage.setMinWidth(Driver.USER_SCREEN_WIDTH / 4);
					erStage.setMinHeight(Driver.USER_SCREEN_HEIGHT / 4);

					Text title = new Text("Error");
					title.setFont(new Font("Calibri Light", 36));
					title.setTextAlignment(TextAlignment.LEFT);

					Text dir = new Text("The selected save slot is empty.\n" + "Please choose another.");
					dir.setFont(new Font("Calibri Light", 18));
					dir.setTextAlignment(TextAlignment.LEFT);

					Button okButton = new Button("OK");
					okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							game.playButtonClick();
							erStage.close();
						}
					});

					HBox alignButton = new HBox(okButton);
					alignButton.setAlignment(Pos.BOTTOM_RIGHT);
					for (Node b : alignButton.getChildren()) {
						((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
					}

					VBox content = new VBox(title, dir, alignButton);
					content.setPadding(new Insets(Driver.PADDING));

					erStage.setScene(new Scene(content));

					erStage.show();
				} else {
					loadFile(sf);
					game.playButtonClick();
					game.previousScene();
				}
			}
		});
	}

	public void saveDialog() {
		Button submit = showDialog("save");

		submit.setGraphic(new ImageView("/GUI/checkmark.png"));
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				SaveFile sf = getCurrentSelectedSave();

				if (!sf.isFileEmpty() || sf == getAutosaveFile()) {
					Stage erStage = new Stage();
					erStage.getIcons().add(new Image("/Images/icon.png"));
					erStage.setTitle("Error");
					erStage.setMinWidth(Driver.USER_SCREEN_WIDTH / 4);
					erStage.setMinHeight(Driver.USER_SCREEN_HEIGHT / 4);

					Text title = new Text("Confirm");
					title.setFont(new Font("Calibri Light", 36));
					title.setTextAlignment(TextAlignment.LEFT);

					Text dir = new Text();
					dir.setFont(new Font("Calibri Light", 18));
					dir.setTextAlignment(TextAlignment.LEFT);

					HBox alignButton = new HBox(Driver.PADDING);
					alignButton.setPadding(new Insets(Driver.PADDING));
					alignButton.setAlignment(Pos.BOTTOM_RIGHT);

					// Player is trying to save into the autosave file
					if (sf == getAutosaveFile()) {
						title.setText("Error");
						dir.setText("The selected slot is the autosave.\n Please pick another slot to save to.");

						Button okButton = new Button("OK", new ImageView("/GUI/checkmark.png"));
						okButton.setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
						okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								game.playButtonClick();
								erStage.close();
							}
						});

						alignButton.getChildren().add(okButton);
					} else { // Player is trying to overwrite a save file
						title.setText("Confirm");
						dir.setText(
								"The selected save slot has data stored.\n" + "Do you want to overwrite this save?");

						Button yesButton = new Button("Yes", new ImageView("/GUI/checkmark.png"));
						yesButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								saveFile(sf);
								game.previousScene();
								game.playButtonClick();
								erStage.close();
							}
						});

						Button noButton = new Button("No", new ImageView("/GUI/cross.png"));
						noButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								game.playButtonClick();
								erStage.close();
							}
						});

						alignButton.getChildren().addAll(yesButton, noButton);
						for (Node b : alignButton.getChildren()) {
							((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
						}
					}

					VBox content = new VBox(title, dir, alignButton);
					content.setPadding(new Insets(Driver.PADDING));

					erStage.setScene(new Scene(content));
					erStage.show();
				} else {
					saveFile(sf);
					game.previousScene();
				}
			}
		});
	}

	public void clearDialog() {
		Button submit = showDialog("clear");

		submit.setGraphic(new ImageView("/GUI/checkmark.png"));
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				game.playButtonClick();
				clearFile(getCurrentSelectedSave());
				game.previousScene();
			}
		});
	}

	public SaveFile getCurrentSelectedSave() {
		return (SaveFile) toggleGroup.getSelectedToggle();
	}

	public void autosave() {
		saveFile(getAutosaveFile());
	}

	/**
	 * Returns the submit button of the data dialog to assign event handlers.
	 * command can be "save", "load", or "clear".
	 */
	private Button showDialog(String command) {
		toggleGroup.selectToggle(null);

		Text title = new Text();
		title.setFont(new Font("Calibri Light", 36));
		title.setTextAlignment(TextAlignment.LEFT);

		Text dir = new Text();
		dir.setFont(new Font("Calibri Light", 18));
		dir.setTextAlignment(TextAlignment.LEFT);

		Button submit = new Button();
		Button backToPrevious = new Button("Back", new ImageView("/GUI/return.png"));
		backToPrevious.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				game.playButtonClick();
				game.previousScene();
			}
		});

		if (command.equals("save")) {
			title.setText("Save Game");
			dir.setText("Select a save slot.");
			submit.setText("Save");
			submit.setGraphic(new ImageView("/GUI/save.png"));
		} else if (command.equals("load")) {
			title.setText("Load Game");
			dir.setText("Select a save to load.");
			submit.setText("Load");
			submit.setGraphic(new ImageView("/GUI/import.png"));
		} else if (command.equals("clear")) {
			title.setText("Clear Game");
			dir.setText("Select a save slot to clear.");
			submit.setText("Clear");
			submit.setGraphic(new ImageView("/GUI/trashcan.png"));
		}

		VBox slots = saveSlotVBox();
		slots.setAlignment(Pos.CENTER);
		slots.setPadding(new Insets(Driver.PADDING * 2, 0, Driver.PADDING * 3, 0));

		HBox buttons = new HBox(Driver.PADDING * 2);
		buttons.getChildren().addAll(submit, backToPrevious);
		buttons.setAlignment(Pos.BOTTOM_CENTER);
		for (Node b : buttons.getChildren()) {
			((Button) b).setBackground(new Background(Driver.DEFAULT_BUTTON_FILL));
		}

		VBox allContent = new VBox(title, dir, slots, buttons);
		allContent.setPadding(new Insets(Driver.PADDING));
		allContent.setMinSize(Driver.MIN_WINDOW_WIDTH, Driver.MIN_WINDOW_HEIGHT);
		allContent.setMaxSize(Driver.USER_SCREEN_WIDTH, Driver.USER_SCREEN_HEIGHT);

		game.changeScene(new Scene(allContent));

		return submit;
	}

	private void loadFile(SaveFile sf) {
		if (sf == null || sf.isFileEmpty())
			return;

		try {
			Scanner feedLine = new Scanner(sf.getFile());

			// Skip title and generation date
			feedLine.nextLine();
			feedLine.nextLine();

			feedLine.next(); // Seasons
			numSeasonsComplete = feedLine.nextInt();

			feedLine.next(); // Levels
			numLevelsComplete = feedLine.nextInt();

			feedLine.next(); // Coins
			numCoins = feedLine.nextInt();

			feedLine.next(); // Ball Sprite
			ballSpriteName = feedLine.next();

			feedLine.nextLine(); // User Items
			feedLine.nextLine();
			String fullUserItemsLine = feedLine.nextLine();
			String readItems = fullUserItemsLine.substring(fullUserItemsLine.indexOf("[") + 1,
					fullUserItemsLine.indexOf("]"));
			ArrayList<String> userItemsNames = new ArrayList<String>();

			String currName = "";
			for (int i = 0; i < readItems.length(); i++) {
				if (readItems.charAt(i) == ',') {
					userItemsNames.add(currName);
					currName = "";
					i++; // Skip whitespace after the comma
				} else
					currName += "" + readItems.charAt(i);

				// currName is now the last word: add before quitting the loop
				if (i + 1 >= readItems.length())
					userItemsNames.add(currName);
			}

			shop.loadUserItems(userItemsNames);
			userItems = shop.getItemsNotInCurrInventory();

			feedLine.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		game.updateCurrentlySelectedSprite();
		game.updateShopScreen();
	}

	private void saveFile(SaveFile sf) {
		if (sf == null)
			return;

		try {
			FileWriter writer = new FileWriter(sf.getFile());
			writer.write("HAT TRICK DATA SAVE FILE\n" + "GENERATED ON: " + (new Date()) + "\n\n");
			writer.write("#_SEASONS_COMPLETE: " + numSeasonsComplete + "\n\n");
			writer.write("#_LEVELS_COMPLETE: " + numLevelsComplete + "\n\n");
			writer.write("#_COINS: " + numCoins + "\n\n");
			writer.write("BALL_SPRITE_USED: " + ballSpriteName + "\n\n");

			String userItemsString = "[";
			userItems = shop.getItemsNotInCurrInventory();
			for (int i = 0; i < userItems.size(); i++) {
				userItemsString += userItems.get(i).getScreenName();

				if (i != userItems.size() - 1)
					userItemsString += ", ";
			}
			userItemsString += "]";

			writer.write("USER_ITEMS: " + userItemsString + "\n\n");

			String leftoverItems = "[";
			ArrayList<Item> readItems = shop.getCurrInventory();
			for (int i = 0; i < readItems.size(); i++) {
				leftoverItems += readItems.get(i).getScreenName();

				if (i != readItems.size() - 1)
					leftoverItems += ", ";
			}
			leftoverItems += "]";

			writer.write("SHOP_ITEMS_NOT_BOUGHT: " + leftoverItems + "\n\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void clearFile(SaveFile sf) {
		if (sf == null)
			return;

		try {
			FileWriter writer = new FileWriter(sf.getFile());
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private SaveFile getAutosaveFile() {
		return saveFiles[0];
	}

	private SaveFile[] getSaveFiles() {
		File saveFileDirectory = new File(System.getProperty("user.dir") + "/Save Files");
		File[] saveFiles = saveFileDirectory.listFiles();

		SaveFile[] toReturn = new SaveFile[saveFiles.length];

		toggleGroup = new ToggleGroup();

		BackgroundFill full = new BackgroundFill(Color.MEDIUMSEAGREEN, new CornerRadii(5), new Insets(0));
		BackgroundFill empty = new BackgroundFill(Color.GAINSBORO, new CornerRadii(5), new Insets(0));

		for (int i = 0; i < saveFiles.length; i++) {
			SaveFile sf = new SaveFile("Save Slot " + i, saveFiles[i]);

			// First save file is the autosave file
			if (i == 0)
				sf.setText("Autosaved");

			sf.setToggleGroup(toggleGroup);

			// If the button is selected, indicate it by highlighting it
			sf.selectedProperty().addListener(new ChangeListener<Boolean>() {
				BackgroundFill selected = new BackgroundFill(Color.CADETBLUE, new CornerRadii(5), new Insets(0));

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (newValue == true) {
						sf.setBackground(new Background(selected));
					} else {
						if (sf.isFileEmpty())
							sf.setBackground(new Background(empty));
						else
							sf.setBackground(new Background(full));
					}

				}
			});

			// Color the button depending on the length of the File
			if (sf.isFileEmpty())
				sf.setBackground(new Background(empty));
			else
				sf.setBackground(new Background(full));

			toReturn[i] = sf;
		}

		return toReturn;
	}

	private VBox saveSlotVBox() {
		VBox saves = new VBox(Driver.PADDING / 2);
		saves.setPadding(new Insets(Driver.PADDING));
		saves.setAlignment(Pos.CENTER);
		for (SaveFile sf : saveFiles) {
			saves.getChildren().add(sf);
		}

		return saves;
	}

}