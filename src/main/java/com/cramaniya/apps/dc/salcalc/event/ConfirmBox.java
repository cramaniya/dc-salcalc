package com.cramaniya.apps.dc.salcalc.event;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Citra Ramaniya
 */
public class ConfirmBox {

	static boolean answer;

	public static boolean display(String title, String message) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);

		Label label = new Label();
		label.setText(message);
		Button yesBtn = new Button("Yes");
		yesBtn.setOnAction(event -> {
			answer = true;
			window.close();
		});

		Button noBtn = new Button("No");
		noBtn.setOnAction(event -> {
			answer = false;
			window.close();
		});

		HBox buttonLayout = new HBox(10);
		buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.getChildren().addAll(yesBtn, noBtn);

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(15, 15, 15, 15));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, buttonLayout);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

		return answer;
	}
}
