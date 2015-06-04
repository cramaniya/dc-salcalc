package com.cramaniya.apps.dc.salcalc.ui;

import com.cramaniya.apps.dc.salcalc.configuration.WagePropertyConfig;
import com.cramaniya.apps.dc.salcalc.model.WageProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.cramaniya.apps.dc.salcalc.configuration.WagePropertyConfig.currentWageProperty;
import static com.cramaniya.apps.dc.salcalc.util.GuiUtils.*;

/**
 * Wage configuration setting GUI dialog.
 *
 * @author Citra Ramaniya
 */
public class WageConfigDialog {

	WageProperty wageProperty;

	public void display() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Wage Configuration Settings");
		window.setMinWidth(250);

		GridPane inputLayout = new GridPane();
		inputLayout.setPadding(new Insets(15, 15, 15, 15));
		inputLayout.setVgap(8);
		inputLayout.setHgap(10);

		int rowIndex = 0;

		/**
		 * Inputs
		 */
		LabelAndTextField workingHoursRate = createTextField("Working Hour Rate", "Working hour rate", String.valueOf(currentWageProperty
				.getWorkingHoursRate()));
		addRowToLayout(inputLayout, rowIndex, workingHoursRate);

		LabelAndTextField mealsRate = createTextField("Meals Rate", "Meals rate", String.valueOf(currentWageProperty
				.getMealsRate()));
		addRowToLayout(inputLayout, ++rowIndex, mealsRate);

		LabelAndTextField incentivesRate = createTextField("Incentives Rate", "Incentives rate", String.valueOf
				(currentWageProperty.getIncentivesRate()));
		addRowToLayout(inputLayout, ++rowIndex, incentivesRate);

		LabelAndTextField callOut1Rate = createTextField("Call Out Area 1 Rate", "Call out area 1 rate", String.valueOf
				(currentWageProperty.getCallOutAreaOneRate()));
		addRowToLayout(inputLayout, ++rowIndex, callOut1Rate);

		LabelAndTextField callOut2Rate = createTextField("Call Out Area 2 Rate", "Call out area 1 rate", String.valueOf
				(currentWageProperty.getCallOutAreaTwoRate()));
		addRowToLayout(inputLayout, ++rowIndex, callOut2Rate);

		LabelAndTextField callOut3Rate = createTextField("Call Out Area 3 Rate", "Call out area 1 rate", String.valueOf
				(currentWageProperty.getCallOutAreaThreeRate()));
		addRowToLayout(inputLayout, ++rowIndex, callOut3Rate);

		LabelAndTextField jamsostekDeduct = createTextField("Jamsostek Deduction", "Deduction for Jamsostek", String
				.valueOf(currentWageProperty.getJamsostekDeduction()));
		addRowToLayout(inputLayout, ++rowIndex, jamsostekDeduct);

		LabelAndTextField koperasiDeduct = createTextField("Koperasi Deduction", "Deduction for Koperasi", String
				.valueOf(currentWageProperty.getKoperasiDeduction()));
		addRowToLayout(inputLayout, ++rowIndex, koperasiDeduct);

		LabelAndTextField amPmShiftRate = createTextField("AM/PM Shift Working Hours", "Hours in a AM/PM shift", String
				.valueOf(currentWageProperty.getAmPmShiftWorkingHours()));
		addRowToLayout(inputLayout, ++rowIndex, amPmShiftRate);

		LabelAndTextField nightShiftRate = createTextField("Night Shift Working Hours", "Hours in a night shift", String
				.valueOf(currentWageProperty.getNightShiftWorkingHours()));
		addRowToLayout(inputLayout, ++rowIndex, nightShiftRate);

		LabelAndTextField sundayNightShiftRate = createTextField("Sunday Night Shift Working Hours", "Hours in a " +
				"Sunday night shift", String.valueOf(currentWageProperty.getSundayNightShiftWorkingHours()));
		addRowToLayout(inputLayout, ++rowIndex, sundayNightShiftRate);


		/**
		 * Buttons
		 */
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(event -> window.close());

		Button resetButton = new Button("Reset");
		resetButton.setOnAction(event -> {
			resetToDefault();
			window.close();
		});

		Button saveButton = new Button("Save");
		saveButton.setOnAction(event -> {
			saveConfiguration(workingHoursRate, mealsRate, incentivesRate, callOut1Rate, callOut2Rate, callOut3Rate,
					jamsostekDeduct, koperasiDeduct, amPmShiftRate, nightShiftRate, sundayNightShiftRate);
			window.close();
		});

		HBox buttonLayout = new HBox(10);
		buttonLayout.setAlignment(Pos.CENTER);
		buttonLayout.getChildren().addAll(cancelButton, resetButton, saveButton);

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(15, 15, 15, 15));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(inputLayout, buttonLayout);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

	}

	private void resetToDefault() {
		wageProperty = new WageProperty();
		wageProperty.setToDefaultValue();
		WagePropertyConfig.load(wageProperty, true);
	}

	private void saveConfiguration(LabelAndTextField workingHoursRate, LabelAndTextField mealsRate,
	                               LabelAndTextField incentivesRate, LabelAndTextField callOut1Rate,
	                               LabelAndTextField callOut2Rate, LabelAndTextField callOut3Rate,
	                               LabelAndTextField jamsostekDeduct, LabelAndTextField koperasiDeduct,
	                               LabelAndTextField amPmShiftRate, LabelAndTextField nightShiftRate,
	                               LabelAndTextField sundayNightShiftRate) {

		wageProperty = new WageProperty();
		wageProperty.setWorkingHoursRate(getBigDecimal(workingHoursRate, currentWageProperty.getWorkingHoursRate()));
		wageProperty.setMealsRate(getBigDecimal(mealsRate, currentWageProperty.getMealsRate()));
		wageProperty.setIncentivesRate(getBigDecimal(incentivesRate, currentWageProperty.getIncentivesRate()));

		wageProperty.setCallOutAreaOneRate(getBigDecimal(callOut1Rate, currentWageProperty.getCallOutAreaOneRate()));
		wageProperty.setCallOutAreaTwoRate(getBigDecimal(callOut2Rate, currentWageProperty.getCallOutAreaTwoRate()));
		wageProperty.setCallOutAreaThreeRate(getBigDecimal(callOut3Rate, currentWageProperty.getCallOutAreaThreeRate()));

		wageProperty.setJamsostekDeduction(getBigDecimal(jamsostekDeduct, currentWageProperty.getJamsostekDeduction()));
		wageProperty.setKoperasiDeduction(getBigDecimal(koperasiDeduct, currentWageProperty.getKoperasiDeduction()));

		wageProperty.setAmPmShiftWorkingHours(getInt(amPmShiftRate, currentWageProperty.getAmPmShiftWorkingHours()));
		wageProperty.setNightShiftWorkingHours(getInt(nightShiftRate, currentWageProperty.getNightShiftWorkingHours()));
		wageProperty.setSundayNightShiftWorkingHours(getInt(sundayNightShiftRate, currentWageProperty.getSundayNightShiftWorkingHours()));

		WagePropertyConfig.load(wageProperty, true);
	}

}
