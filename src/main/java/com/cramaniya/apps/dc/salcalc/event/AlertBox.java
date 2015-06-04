package com.cramaniya.apps.dc.salcalc.event;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Citra Ramaniya
 */
public class AlertBox {

	private static Stage window;

	public static void display(String title, String labelStr, String textStr) {
		window = new Stage();

		// blocks other input events (user interactions) until this stage is close
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);

		Label label = new Label();
		label.setText(labelStr);

		Text text = new Text();
		text.setText(textStr);

		Button closeBtn = new Button("Close");
		closeBtn.setOnAction(event -> window.close());

		VBox layout = new VBox(20);
		layout.setPadding(new Insets(15, 15, 15, 15));
		layout.getChildren().addAll(label, text, closeBtn);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}

}
