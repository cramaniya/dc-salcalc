package com.cramaniya.apps.dc.salcalc.ui;

import com.cramaniya.apps.dc.salcalc.configuration.WagePropertyConfig;
import com.cramaniya.apps.dc.salcalc.event.AlertBox;
import com.cramaniya.apps.dc.salcalc.model.Wage;
import com.cramaniya.apps.dc.salcalc.util.WageCSVUtils;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import static com.cramaniya.apps.dc.salcalc.util.GuiUtils.*;
import static com.cramaniya.apps.dc.salcalc.util.StringUtils.formatToIndonesiaCurrency;

/**
 * Class for Content GUI.
 *
 * @author Citra Ramaniya
 */
@Setter
@Getter
@Slf4j
public class ContentUi {

	private GridPane layout = new GridPane();

	private ComboBox<Month> months;
	private ComboBox<Integer> years;

	private LabelAndTextField workingHoursInput;
	private LabelAndTextField workingHoursSundayInput;
	private LabelAndTextField workingHoursPublicHolidayInput;
	private LabelAndTextField workingHoursPublicHolidaySundyInput;

	private LabelAndTextField callOut1Input;
	private LabelAndTextField callOut2Input;
	private LabelAndTextField callOut3Input;

	private CheckBox jamsostekCheckBox;
	private CheckBox koperasiCheckBox;
	private CheckBox taxCheckBox;
	private LabelAndTextField taxInput;

	private LabelAndTextField totalWorkingHoursInput;
	private LabelAndTextField wageWorkingHoursInput;
	private LabelAndTextField wageWorkingHoursSundaysInput;
	private LabelAndTextField wageWorkingHoursHolidaysInput;
	private LabelAndTextField wageWorkingHoursTotalInput;

	private LabelAndTextField wageMealsInput;
	private LabelAndTextField wageIncentivesInput;
	private LabelAndTextField totalDeductionInput;

	private LabelAndTextField totalWageInput;

	private int rowIndex = 0;

	private WageCSVUtils wageCsv;

	public ContentUi() {
		layout.setPadding(new Insets(15, 15, 15, 15));
		layout.setVgap(8);
		layout.setHgap(10);

		wageCsv = new WageCSVUtils(WagePropertyConfig.resourcesDir.getPath() + "/" + WagePropertyConfig.STORAGE_FILE);
	}

	public void createContent() {

		LocalDate currentDate = LocalDate.now();

		// Month
		Label monthLabel = new Label("Month");
		months = new ComboBox<>();
		for (Month month : Month.values()) {
			months.getItems().add(month);
		}
		months.getSelectionModel().select(currentDate.getMonth());
		// Year
		Label yearLabel = new Label("Year");
		years = new ComboBox<>();
		Integer startYear = currentDate.getYear() - 5;
		for (Integer i = startYear; i < currentDate.getYear() + 5; i++) {
			years.getItems().add(i);
		}
		years.getSelectionModel().select(Integer.valueOf(currentDate.getYear()));
		layout.addRow(rowIndex, monthLabel, months, yearLabel, years);

		// Working hours
		Label workingHoursHeader = new Label("Working hours calculation");
		layout.add(workingHoursHeader, 0, ++rowIndex, 4, 1);
		workingHoursInput = createTextField("On work days", "Working hours", "160");
		workingHoursSundayInput = createTextField("On Sundays", "Sunday working hours", "");
		// Public holidays
		workingHoursPublicHolidayInput = createTextField("On public holidays", "Working hours on public holiday", "");
		addRowToLayout(layout, ++rowIndex, workingHoursInput, workingHoursSundayInput);
		addRowToLayout(layout, ++rowIndex, workingHoursPublicHolidayInput);

		// Call out
		int rowHeader = ++rowIndex;
		Label callOutHeader = new Label("Call out calculation");
		layout.add(callOutHeader, 0, rowIndex, 2, 1);
		// Call out area 1
		callOut1Input = createTextField("Area 1", "Call outs in area 1", "");
		addRowToLayout(layout, ++rowIndex, callOut1Input);
		// Call out area 2
		callOut2Input = createTextField("Area 2", "Call outs in area 2", "");
		addRowToLayout(layout, ++rowIndex, callOut2Input);
		// Call out area 3
		callOut3Input = createTextField("Area 3", "Call outs in area 3", "");
		addRowToLayout(layout, ++rowIndex, callOut3Input);

		// Wage deduction
		rowIndex = rowHeader;
		Label deductionHeader = new Label("Deduction");
		layout.add(deductionHeader, 2, rowIndex, 4, 1);
		Label deductionLabel = new Label("Deduction");
		// jamsostek
		jamsostekCheckBox = new CheckBox("Jamsostek");
		jamsostekCheckBox.setId("jamsostekChk");
		layout.addRow(++rowIndex, deductionLabel, jamsostekCheckBox);
		// koperasi
		koperasiCheckBox = new CheckBox("Koperasi");
		koperasiCheckBox.setId("koperasiChk");
		layout.addRow(++rowIndex, new Pane(), koperasiCheckBox);
		// tax
		taxCheckBox = new CheckBox("Tax");
		taxCheckBox.setId("taxChk");
		layout.addRow(++rowIndex, new Pane(), taxCheckBox);
		taxInput = createTextField("Tax", "Tax", true, "");
		addRowToLayout(layout, 2, ++rowIndex, taxInput);

		setStyleClassToElements("text-header", workingHoursHeader, callOutHeader, deductionHeader);

		/**
		 * Listener
		 */
		taxCheckBox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
			taxInput.getTextField().setDisable(!newValue);
		});

		/**
		 * Right Buttons
		 */
		HBox rightButtonLayout = new HBox(10);
		rightButtonLayout.setPadding(new Insets(10, 10, 10, 10));
		// calculate
		Button calculateButton = new Button("Calculate");
		calculateButton.setOnAction(event -> calculateWage());
		// clear
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(event -> clearInputs(layout, true, taxInput));

		rightButtonLayout.getChildren().addAll(clearButton, calculateButton);
		layout.add(rightButtonLayout, 3, ++rowIndex);

		/**
		 * Left Button save
		 */
		HBox leftButtonLayout = new HBox(10);
		leftButtonLayout.setPadding(new Insets(10, 10, 10, 10));
		// save
		Button saveButton = new Button("Save");
		saveButton.setOnAction(e -> saveWage());
		// load
		Button loadButton = new Button("Load");
		loadButton.setOnAction(event -> loadWage());

		leftButtonLayout.getChildren().addAll(loadButton, saveButton);
		layout.add(leftButtonLayout, 0, rowIndex);

		layout.add(new Separator(), 0, ++rowIndex, 4, 1);
		createEmptyRow(layout, ++rowIndex);

		/**
		 * Result
		 */
		// Total working hours
		totalWorkingHoursInput = createTextField("Total Working Hours", "", true, "");
		addRowToLayout(layout, ++rowIndex, totalWorkingHoursInput);
		// Wage working hours total
		wageWorkingHoursTotalInput = createTextField("Wage Working Hours", "", true, "");
		// normal working hours
		wageWorkingHoursInput = createTextField("Mon - Fri", "", true, "");
		// Sundays working hours
		wageWorkingHoursSundaysInput = createTextField("Sunday", "", true, "");
		// Holidays working hours
		wageWorkingHoursHolidaysInput = createTextField("Holidays", "", true, "");
		addRowToLayout(layout, ++rowIndex, wageWorkingHoursTotalInput, wageWorkingHoursInput);
		layout.add(wageWorkingHoursSundaysInput.getLabel(), 2, ++rowIndex);
		layout.add(wageWorkingHoursSundaysInput.getTextField(), 3, rowIndex);
		layout.add(wageWorkingHoursHolidaysInput.getLabel(), 2, ++rowIndex);
		layout.add(wageWorkingHoursHolidaysInput.getTextField(), 3, rowIndex);


		// Meals money
		wageMealsInput = createTextField("Meals", "", true, "");
		addRowToLayout(layout, ++rowIndex, wageMealsInput);

		// Incentives money
		wageIncentivesInput = createTextField("Incentives", "", true, "");
		addRowToLayout(layout, ++rowIndex, wageIncentivesInput);

		// Total deduction
		totalDeductionInput = createTextField("Deduction", "", true, "");
		addRowToLayout(layout, ++rowIndex, totalDeductionInput);

		createEmptyRow(layout, ++rowIndex);

		// Total
		totalWageInput = createTextField("Total Net", "", true, "");
		addRowToLayout(layout, ++rowIndex, totalWageInput);

		setStyleClassToElements("text-result",
				totalWorkingHoursInput.getTextField(),
				wageWorkingHoursInput.getTextField(),
				wageWorkingHoursTotalInput.getTextField(),
				wageWorkingHoursSundaysInput.getTextField(),
				wageWorkingHoursHolidaysInput.getTextField(),
				wageMealsInput.getTextField(),
				wageIncentivesInput.getTextField(),
				totalDeductionInput.getTextField(),
				totalWageInput.getTextField());

		setStyleClassToElements("text-green",
				wageWorkingHoursTotalInput.getTextField(),
				wageMealsInput.getTextField(),
				wageIncentivesInput.getTextField());

		totalDeductionInput.getTextField().setStyle("-fx-text-fill: red;");
		totalWageInput.getTextField().setStyle("-fx-text-fill: blue;");
	}

	private void loadWage() {
		Wage loadedWage = wageCsv.findWage(getWage());

		if (loadedWage == null) {
			Month month = months.getSelectionModel().getSelectedItem();
			Integer year = years.getSelectionModel().getSelectedItem();
			AlertBox.display("Alert", "No data found for " + getMonthString(month) + " " + year + "!", "");
			return;
		}

		// Working hours
		workingHoursInput.getTextField().setText(String.valueOf(loadedWage.getNormalWorkingHours()));
		// Public holidays
		workingHoursPublicHolidayInput.getTextField().setText(String.valueOf(loadedWage.getHolidayWorkingHours()));

		// Call out area 1
		callOut1Input.getTextField().setText(String.valueOf(loadedWage.getNumOfCallOutArea1()));
		// Call out area 2
		callOut2Input.getTextField().setText(String.valueOf(loadedWage.getNumOfCallOutArea2()));
		// Call out area 3
		callOut3Input.getTextField().setText(String.valueOf(loadedWage.getNumOfCallOutArea3()));

		jamsostekCheckBox.setSelected(loadedWage.getJamsostekDeduction());
		koperasiCheckBox.setSelected(loadedWage.getKoperasiDeduction());
		taxCheckBox.setSelected(loadedWage.getTaxDeduction());
		taxInput.getTextField().setText(String.valueOf(loadedWage.getTax()));
	}

	private String getMonthString(Month month) {
		return StringUtils.capitalize(month.name().toLowerCase());
	}

	/**
	 * Saves wage to a csv file.
	 */
	private void saveWage() {
		Wage wage = getWage();
		wage.setCreatedTs(new Date());
		wageCsv.addToCSV(wage);

		AlertBox.display("Entry saved", "Saved data for " + getMonthString(wage.getMonth()) + " " + wage.getYear(), "");
	}

	/**
	 * calculate the wage.
	 */
	private void calculateWage() {

		// remove error class
		clearInputs(layout, false, taxInput);

		Wage wage = getWage();

		// set the fields
		totalWorkingHoursInput.getTextField().setText(String.valueOf(wage.getTotalWorkingHours()));
		wageWorkingHoursTotalInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageWorkingHoursTotal()));
		wageWorkingHoursInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageWorkingHours()));
		wageWorkingHoursSundaysInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageWorkingHoursSundays()));
		wageWorkingHoursHolidaysInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageWorkingHoursHolidays()));
		wageMealsInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageMeals()));
		wageIncentivesInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageIncentives()));
		totalDeductionInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageDeduction()));
		totalWageInput.getTextField().setText(formatToIndonesiaCurrency(wage.getWageTotal()));

	}

	private Wage getWage() {
		int workingHours = getInt(workingHoursInput, null);
		int workingHoursSundays = getInt(workingHoursSundayInput, null);
		int workingHoursHoliday = getInt(workingHoursPublicHolidayInput, null);
		int numCallOut1 = getInt(callOut1Input, null);
		int numCallOut2 = getInt(callOut2Input, null);
		int numCallOut3 = getInt(callOut3Input, null);

		BigDecimal tax = BigDecimal.ZERO;
		if (taxCheckBox.isSelected()) {
			tax = getBigDecimal(taxInput, null);
		}

		return new Wage(months.getSelectionModel().getSelectedItem(), years.getSelectionModel().getSelectedItem
				(), workingHours, workingHoursSundays, workingHoursHoliday, numCallOut1, numCallOut2, numCallOut3,
				jamsostekCheckBox.isSelected(), koperasiCheckBox.isSelected(), taxCheckBox.isSelected(), tax);
	}


}
