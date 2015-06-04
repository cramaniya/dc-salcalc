package com.cramaniya.apps.dc.salcalc.ui;

import com.cramaniya.apps.dc.salcalc.configuration.WagePropertyConfig;
import com.cramaniya.apps.dc.salcalc.event.AlertBox;
import com.cramaniya.apps.dc.salcalc.event.ConfirmBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Class for menu GUI.
 *
 * @author Citra Ramaniya
 */
@Setter
@Getter
@Slf4j
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
		/*
		 * File menu
		 */
		Menu fileMenu = new Menu("File");
//		fileMenu.getItems().add(new MenuItem("Open..."));
//		fileMenu.getItems().add(new MenuItem("Save..."));
//		fileMenu.getItems().add(new SeparatorMenuItem());

		MenuItem exit = new MenuItem(("Exit..."));
		exit.setOnAction(e -> closeProgram());
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		fileMenu.getItems().add(exit);

		/*
		 * Help menu
		 */
		Menu settingsMenu = new Menu("Settings");
		MenuItem wageConfig = new MenuItem("Wage Configuration...");
		wageConfig.setOnAction(event -> openSettings());

//		CheckMenuItem autoSave = new CheckMenuItem("Enable Autosave");
//		autoSave.setSelected(true);
//		autoSave.setOnAction(event -> {
//			if (autoSave.isSelected()) {
//				log.debug("Enable Autosave");
//			} else {
//				log.debug("Disable Autosave");
//			}
//		});
		settingsMenu.getItems().addAll(wageConfig);

		// Help menu
		Menu helpMenu = new Menu("Help");
		MenuItem aboutMenu = new MenuItem("About");
		aboutMenu.setOnAction(e -> openAbout());
		helpMenu.getItems().add(aboutMenu);

		menuBar.getMenus().addAll(fileMenu, settingsMenu, helpMenu);
	}

	private void closeProgram() {
		Boolean result = ConfirmBox.display("Close Program", "Are you sure you wanna quit?");
		if (result) {
			window.close();
		}
	}

	private void openSettings() {
		WageConfigDialog wageConfigDialog = new WageConfigDialog();
		wageConfigDialog.display();
	}

	private void openAbout() {
		AlertBox.display("About", WagePropertyConfig.APP_NAME + " version " + WagePropertyConfig.APP_VERSION,
				"Developer: Citra Ramaniya\nCopyright © 2015 Citra Ramaniya\n"
		);
	}

}
