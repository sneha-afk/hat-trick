import java.io.File;

import javafx.scene.control.ToggleButton;

/**
 * Represents a save file for the game. It contains the actual File object and a
 * ToggleButton.
 */
public class SaveFile extends ToggleButton {
	private File file;

	public SaveFile(String buttonLabel, File file) {
		super(buttonLabel);
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isFileEmpty() {
		return file.length() == 0;
	}
}
