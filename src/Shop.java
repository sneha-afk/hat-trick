import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Shop {
	public static double VIEW_WIDTH = Driver.MIN_WINDOW_WIDTH / 8;
	public static double VIEW_HEIGHT = Driver.MIN_WINDOW_HEIGHT / 8;

	/** Should not be edited: contains the names of all items */
	private ArrayList<Item> totalInventory;

	private ArrayList<Item> currInventory;

	public Shop() {
		totalInventory = new ArrayList<Item>();
		currInventory = new ArrayList<Item>();
	}

	public Shop(File inventoryFile) {
		totalInventory = new ArrayList<Item>();
		currInventory = new ArrayList<Item>();

		try {
			Scanner in = new Scanner(inventoryFile);

			while (in.hasNextLine()) {
				String line = in.nextLine();
				totalInventory.add(new Item(line));
			}

			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Sorting totalInventory by name
		for (int out = totalInventory.size() - 2; out >= 0; out--) {
			int j = out;
			Item target = totalInventory.get(out);

			while (j + 1 < totalInventory.size()
					&& target.getScreenName().compareTo(totalInventory.get(j + 1).getScreenName()) > 0) {
				j++;
			}

			totalInventory.add(j, totalInventory.remove(out));
		}

		// Copying totalInventory before deleting items the player already has
		for (Item i : totalInventory) {
			currInventory.add(new Item(i.toString()));
		}

	}

	public void resetCurrInventory() {
		currInventory.clear();

		for (Item i : totalInventory) {
			currInventory.add(new Item(i.toString()));
		}
	}

	public void loadUserItems(ArrayList<String> playerItems) {
		if (currInventory.size() < totalInventory.size())
			resetCurrInventory();

		// Deleting the items the player has from currInventory
		for (String name : playerItems)
			remove(name);
	}

	public void add(Item i) {
		currInventory.add(i);
	}

	public void add(String name) {
		Item toAdd = null;
		for (Item i : totalInventory) {
			if (i.getScreenName().equals(name))
				toAdd = i;
		}

		if (toAdd != null)
			currInventory.add(toAdd);
	}

	public void remove(Item i) {
		Iterator<Item> iterate = currInventory.iterator();
		while (iterate.hasNext()) {
			Item iterateItem = iterate.next();

			if (iterateItem.getScreenName().equals(i.getScreenName()))
				iterate.remove();
		}
	}

	public void remove(String name) {
		Iterator<Item> iterate = currInventory.iterator();
		while (iterate.hasNext()) {
			Item i = iterate.next();

			if (i.getScreenName().equals(name))
				iterate.remove();
		}

	}

	public Item getItem(String name) {
		for (Item i : currInventory) {
			if (i.getScreenName().equals(name))
				return i;
		}

		return null;
	}

	public ArrayList<Item> getOfType(String type) {
		ArrayList<Item> items = new ArrayList<Item>();

		for (Item i : currInventory) {
			if (i.getType().equals(type))
				items.add(i);
		}

		return items;
	}

	public void sortByName() {
		for (int out = currInventory.size() - 2; out >= 0; out--) {
			int j = out;
			Item target = currInventory.get(out);

			while (j + 1 < currInventory.size()
					&& target.getScreenName().compareTo(currInventory.get(j + 1).getScreenName()) > 0) {
				j++;
			}

			currInventory.add(j, currInventory.remove(out));
		}
	}

	public void sortByType() {
		for (int out = currInventory.size() - 2; out >= 0; out--) {
			int j = out;
			Item target = currInventory.get(out);

			while (j + 1 < currInventory.size() && target.getType().compareTo(currInventory.get(j + 1).getType()) > 0) {
				j++;
			}

			currInventory.add(j, currInventory.remove(out));
		}
	}

	public ArrayList<Item> getTotalInventory() {
		return totalInventory;
	}

	public ArrayList<Item> getCurrInventory() {
		return currInventory;
	}

	public ArrayList<Item> getItemsNotInCurrInventory() {
		ArrayList<Item> toReturn = new ArrayList<Item>();

		for (Item i : totalInventory) {
			boolean found = false;
			for (Item check : currInventory) {
				if (i.getScreenName().equals(check.getScreenName()))
					found = true;
			}

			if (!found)
				toReturn.add(i);
		}

		return toReturn;
	}

	@Override
	public String toString() {
		return "Shop [totalInventory=" + totalInventory + ", currInventory=" + currInventory + "]";
	}

}
