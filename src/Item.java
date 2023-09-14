import java.io.File;

public class Item {
	private String screenName;
	private File file;
	private String type;
	private int price;

	/**
	 * @param screenName Name displayed in the game
	 * @param file       Location in the source files
	 * @param type       "ball_sprite", "background"
	 * @param price      Price of the item in the shop
	 */
	public Item(String screenName, File file, String type, int price) {
		this.screenName = screenName;
		this.file = file;
		this.type = type;
		this.price = price;
	}

	public Item(String str) {
		int indexOfColon = str.indexOf(":");
		int indexOfComma = str.indexOf(",");

		screenName = str.substring(indexOfColon + 1, indexOfComma);
		indexOfColon = str.indexOf(":", indexOfColon + 1);
		indexOfComma = str.indexOf(",", indexOfComma + 1);

		String fileName = str.substring(indexOfColon + 1, indexOfComma);
		file = new File(fileName);
		indexOfColon = str.indexOf(":", indexOfColon + 1);
		indexOfComma = str.indexOf(",", indexOfComma + 1);

		type = str.substring(indexOfColon + 1, indexOfComma);
		indexOfColon = str.indexOf(":", indexOfColon + 1);

		price = Integer.parseInt(str.substring(indexOfColon + 1, str.indexOf("]")));
	}

	@Override
	public String toString() {
		String fileName = file.getParent() + "\\" + getFileName();

		return "Item [SCREEN_NAME:" + screenName + ", FILE_LOCATION:" + fileName + ", TYPE:" + type + ", PRICE:" + price
				+ "]";
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return file.getName();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
