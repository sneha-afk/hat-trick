import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ItemViewer extends BorderPane {
	private Item item;

	public ItemViewer(Item item) {
		this.item = item;

		File imageFile = item.getFile();
		String pathToImage = imageFile.getParent() + "\\" + imageFile.getName();
		Image image = new Image(pathToImage, Shop.VIEW_WIDTH, Shop.VIEW_HEIGHT, true, true);
		ImageView imgView = new ImageView(image);

		Text name = new Text(item.getScreenName());
		name.setFont(new Font("Calibri Light", 14));
		name.setTextAlignment(TextAlignment.CENTER);

		Text cost = new Text("" + item.getPrice());
		cost.setFont(new Font("Calibri Light", 12));
		cost.setTextAlignment(TextAlignment.CENTER);

		setTop(name);
		setCenter(imgView);
		setBottom(cost);
		setPadding(new Insets(Driver.PADDING / 2));

		setAlignment(name, Pos.CENTER);
		setAlignment(cost, Pos.CENTER);
	}

	public ItemViewer(ItemViewer copy) {
		this.item = copy.getItem();

		File imageFile = item.getFile();
		String pathToImage = imageFile.getParent() + "\\" + imageFile.getName();
		Image image = new Image(pathToImage, Shop.VIEW_WIDTH, Shop.VIEW_HEIGHT, true, true);
		ImageView imgView = new ImageView(image);

		Text name = new Text(item.getScreenName());
		name.setFont(new Font("Calibri Light", 14));
		name.setTextAlignment(TextAlignment.CENTER);

		Text cost = new Text("" + item.getPrice());
		cost.setFont(new Font("Calibri Light", 12));
		cost.setTextAlignment(TextAlignment.CENTER);

		setTop(name);
		setCenter(imgView);
		setBottom(cost);
		setPadding(new Insets(Driver.PADDING / 2));

		setAlignment(name, Pos.CENTER);
		setAlignment(cost, Pos.CENTER);
	}

	public ItemViewer(Item item, boolean displayPrice) {
		this.item = item;

		File imageFile = item.getFile();
		String pathToImage = imageFile.getParent() + "\\" + imageFile.getName();
		Image image = new Image(pathToImage, Shop.VIEW_WIDTH, Shop.VIEW_HEIGHT, true, true);
		ImageView imgView = new ImageView(image);

		Text name = new Text(item.getScreenName());
		name.setFont(new Font("Calibri Light", 14));
		name.setTextAlignment(TextAlignment.CENTER);

		if (displayPrice) {
			Text cost = new Text("" + item.getPrice());
			cost.setFont(new Font("Calibri Light", 12));
			cost.setTextAlignment(TextAlignment.CENTER);

			setBottom(cost);
			setAlignment(cost, Pos.CENTER);
		}

		setTop(name);
		setCenter(imgView);
		setPadding(new Insets(Driver.PADDING / 2));

		setAlignment(name, Pos.CENTER);
	}

	public Item getItem() {
		return item;
	}

	public String getItemName() {
		return item.getScreenName();
	}
}
