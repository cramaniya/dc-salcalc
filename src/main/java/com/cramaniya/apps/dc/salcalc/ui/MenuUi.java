package com.cramaniya.apps.dc.salcalc.ui;

import com.cramaniya.apps.dc.salcalc.event.ConfirmBox;
import javafx.scene.control.*;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

/**
 * Class for menu GUI.
 * 
 * @author Citra Ramaniya
 */
@Setter
@Getter
public class MenuUi {

	private Stage window;

	private MenuBar menuBar;

	public MenuUi(Stage window, MenuBar menuBar) {
		assert window != null;
		this.window = window;

		assert menuBar != null;
		this.menuBar = menuBar;
	}

	public void createMenu() {
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().add(new MenuItem("Open..."));
		fileMenu.getItems().add(new MenuItem("Save..."));
		fileMenu.getItems().add(new SeparatorMenuItem());

		MenuItem exit = new MenuItem(("Exit..."));
		exit.setOnAction(e -> closeProgram());
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		fileMenu.getItems().add(exit);

		// Help menu
		Menu settingsMenu = new Menu("Settings");
		CheckMenuItem showLines = new CheckMenuItem("Show Base Salary");
		showLines.setOnAction(event -> {
			if (showLines.isSelected()) {
				System.out.println("Base salary shown");
			} else {
				System.out.println("Base salary hidden not shown");
			}
		});
		CheckMenuItem autoSave = new CheckMenuItem("Enable Autosave");
		autoSave.setSelected(true);
		settingsMenu.getItems().addAll(showLines, autoSave);

		// Help menu
		Menu helpMenu = new Menu("Help");
		helpMenu.getItems().add(new MenuItem("Documentation"));
		helpMenu.getItems().add(new MenuItem("About"));

		menuBar.getMenus().addAll(fileMenu, settingsMenu, helpMenu);
	}

	public void closeProgram() {
		Boolean result = ConfirmBox.display("Close Program", "Are you sure you wanna quit?");
		if (result) {
			window.close();
		}
	}

}
