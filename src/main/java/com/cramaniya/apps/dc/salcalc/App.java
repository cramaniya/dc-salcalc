package com.cramaniya.apps.dc.salcalc;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.cramaniya.apps.dc.salcalc.ui.ContentUi;
import com.cramaniya.apps.dc.salcalc.ui.MenuUi;

/**
 * @author Citra Ramaniya
 */
public class App extends Application {

	Stage window;

	Scene mainScene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.window = primaryStage;
		window.setTitle("Tiffany's Salary Calculation App");
		BorderPane rootLayout = new BorderPane();

		MenuUi menuUi = new MenuUi(window, new MenuBar());
		menuUi.createMenu();
		rootLayout.setTop(menuUi.getMenuBar());

		ContentUi contentUi = new ContentUi();
		contentUi.createContent();
		rootLayout.setCenter(contentUi.getLayout());

		HBox footer = new HBox();
		footer.setPadding(new Insets(15, 15, 15, 15));
		Label footerLabel = new Label("©2015 Citra Ramaniya");
		footer.getChildren().add(footerLabel);
		rootLayout.setBottom(footer);

		mainScene = new Scene(rootLayout);
		String stylesheet = this.getClass().getClassLoader().getResource("styles.css").toExternalForm();
		assert stylesheet != null;
		mainScene.getStylesheets().addAll(stylesheet);
		window.setScene(mainScene);
		window.show();
	}
}
