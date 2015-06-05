package com.cramaniya.apps.dc.salcalc.ui;

import com.cramaniya.apps.dc.salcalc.event.AlertBox;
import com.cramaniya.apps.dc.salcalc.model.Wage;
import com.cramaniya.apps.dc.salcalc.util.WageCSVUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import static com.cramaniya.apps.dc.salcalc.configuration.WagePropertyConfig.*;
import static com.cramaniya.apps.dc.salcalc.util.GuiUtils.*;
import static com.cramaniya.apps.dc.salcalc.util.StringUtils.formatToIndonesianCurrency;

/**
 * Class for Content GUI.
 *
 * @author Citra Ramaniya
 */
@Setter
@Getter
@Slf4j
public class ContentUi {

	private HBox mainLayout = new HBox(10);

	private GridPane leftLayout = new GridPane();
	private GridPane rightLayout = new GridPane();
	private GridPane summaryLayout = new GridPane();

	private ComboBox<Month> months;
	private ComboBox<Integer> years;

	private LabelAndTextField workingHoursInput;
	private LabelAndTextField workingHoursSundayInput;
	private LabelAndTextField workingHoursPublicHolidayInput;
	private LabelAndTextField callOut1Input;
	private LabelAndTextField callOut2Input;
	private LabelAndTextField callOut3Input;

	private LabelAndTextField currentAdjustmentInput;
	private LabelAndTextField prevAdjustmentInput;

	private CheckBox jamsostekCheckBox;
	private CheckBox koperasiCheckBox;
	private CheckBox taxCheckBox;
	private LabelAndTextField taxInput;

	private LabelAndTextField totalWorkingHoursInput;
	private LabelAndTextField wageWorkingHoursInput;
	private LabelAndTextField wageWorkingHoursHolidaysInput;
	private LabelAndTextField wageWorkingHoursTotalInput;

	private LabelAndTextField wageAllowanceTotalInput;
	private LabelAndTextField wageMealsInput;
	private LabelAndTextField wageIncentivesInput;
	private LabelAndTextField wageDeductionInput;

	private LabelAndTextField wageGrossInput;
	private LabelAndTextField wageNetInput;

	private int rowIndex = 0;

	private WageCSVUtils wageCsv;

	public ContentUi() {
		mainLayout.setPadding(new Insets(15, 15, 20, 15));
		VBox.setVgrow(mainLayout, Priority.ALWAYS);

		leftLayout.setPadding(new Insets(15, 15, 15, 15));
		leftLayout.setVgap(8);
		leftLayout.setHgap(10);

		rightLayout.setPadding(new Insets(15, 15, 15, 15));
		rightLayout.setVgap(8);
		rightLayout.setHgap(10);

		summaryLayout.setVgap(2);
		summaryLayout.setHgap(5);
		summaryLayout.setAlignment(Pos.TOP_RIGHT);
		summaryLayout.setMinHeight(200);

		wageCsv = new WageCSVUtils(resourcesDir.getPath() + "/" + STORAGE_FILE);
	}

	public void createContent() {

		LocalDate currentDate = LocalDate.now();

		/**
		 * Inputs: Left Layout
		 */
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
		leftLayout.addRow(rowIndex, monthLabel, months, yearLabel, years);

		// Working hours
		Label workingHoursHeader = new Label("Working hours calculation");
		leftLayout.add(workingHoursHeader, 0, ++rowIndex, 4, 1);
		workingHoursInput = createTextField("Total working hours", "Working hours", "160", "Total working hours " +
				"logged");
		workingHoursSundayInput = createTextField("On Sundays", "Sunday working hours", "", "How many working hours " +
				"logged on Sundays?");
		// Public holidays
		workingHoursPublicHolidayInput = createTextField("On public holidays", "Public holidays working hours", "",
				"How many working hours logged on public holidays?");
		addRowToLayout(leftLayout, ++rowIndex, workingHoursInput, workingHoursSundayInput);
		addRowToLayout(leftLayout, ++rowIndex, workingHoursPublicHolidayInput);

		// Call out
		Label callOutHeader = new Label("Call out calculation");
		leftLayout.add(callOutHeader, 0, ++rowIndex, 4, 1);
		// Call out area 1
		callOut1Input = createTextField("Area 1", "Call outs in area 1", "");
		addRowToLayout(leftLayout, ++rowIndex, callOut1Input);
		// Call out area 2
		callOut2Input = createTextField("Area 2", "Call outs in area 2", "");
		addRowToLayout(leftLayout, ++rowIndex, callOut2Input);
		// Call out area 3
		callOut3Input = createTextField("Area 3", "Call outs in area 3", "");
		addRowToLayout(leftLayout, ++rowIndex, callOut3Input);

		// Adjustments
		Label adjustmentHeader = new Label("Adjustments");
		leftLayout.add(adjustmentHeader, 0, ++rowIndex, 4, 1);
		currentAdjustmentInput = createTextField("Current Adjustment", "Current adjustment", "");
		prevAdjustmentInput = createTextField("Previous Adjustment", "Previous adjustment", "");
		addRowToLayout(leftLayout, ++rowIndex, currentAdjustmentInput);
		addRowToLayout(leftLayout, ++rowIndex, prevAdjustmentInput);

		// Wage deduction
		Label deductionHeader = new Label("Deduction");
		leftLayout.add(deductionHeader, 0, ++rowIndex, 4, 1);
		Label deductionLabel = new Label("Deduction");
		// jamsostek
		jamsostekCheckBox = new CheckBox("Jamsostek");
		jamsostekCheckBox.setId("jamsostekChk");
		leftLayout.addRow(++rowIndex, deductionLabel, jamsostekCheckBox);
		// koperasi
		koperasiCheckBox = new CheckBox("Koperasi");
		koperasiCheckBox.setId("koperasiChk");
		leftLayout.addRow(++rowIndex, new Pane(), koperasiCheckBox);
		// tax
		taxCheckBox = new CheckBox("Tax");
		taxCheckBox.setId("taxChk");
		taxInput = createTextField("Tax", "Tax", true, "");
		leftLayout.addRow(++rowIndex, new Pane(), taxCheckBox, taxInput.getLabel(), taxInput.getTextField());

		// notes
		Label notesLabel = new Label("Notes");
		TextArea notesTextArea = new TextArea();
		notesTextArea.setPrefRowCount(2);
		leftLayout.addRow(++rowIndex, notesLabel);
		leftLayout.add(notesTextArea, 1, rowIndex, 3, 1);

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
		clearButton.setOnAction(event -> {
			clearInputs(leftLayout, true, taxInput);
			clearInputs(rightLayout, true, taxInput);
			summaryLayout.getChildren().clear();
		});

		rightButtonLayout.getChildren().addAll(clearButton, calculateButton);
		leftLayout.add(rightButtonLayout, 3, ++rowIndex);

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
		leftLayout.add(leftButtonLayout, 0, rowIndex);

		createEmptyRow(leftLayout, ++rowIndex);

		/**
		 * Result: Right Layout
		 */
		rowIndex = 0;
		Label resultHeader = new Label("Calculated Salary");
		rightLayout.add(resultHeader, 0, rowIndex, 4, 1);

		// Total working hours
//		totalWorkingHoursInput = createTextField("Total Working Hours", "", true, "");
//		addRowToLayout(rightLayout, ++rowIndex, totalWorkingHoursInput);
		// Wage working hours total
		wageWorkingHoursTotalInput = createTextField("Basic Working Hours", "", true, "");
		// normal working hours
		wageWorkingHoursInput = createTextField("Mon - Fri", "", true, "");
		// Sundays working hours
		// wageWorkingHoursSundaysInput = createTextField("Sunday", "", true, "");
		// Holidays working hours
		wageWorkingHoursHolidaysInput = createTextField("Holidays", "", true, "");
		addRowToLayout(rightLayout, ++rowIndex, wageWorkingHoursTotalInput, wageWorkingHoursInput);
		// rightLayout.add(wageWorkingHoursSundaysInput.getLabel(), 2, ++rowIndex);
		// rightLayout.add(wageWorkingHoursSundaysInput.getTextField(), 3, rowIndex);
		rightLayout.add(wageWorkingHoursHolidaysInput.getLabel(), 2, ++rowIndex);
		rightLayout.add(wageWorkingHoursHolidaysInput.getTextField(), 3, rowIndex);

		createEmptyRow(leftLayout, ++rowIndex);

		// Total allowance
		wageAllowanceTotalInput = createTextField("Total Allowance", "", true, "");
		addRowToLayout(rightLayout, ++rowIndex, wageAllowanceTotalInput);
//		// Meals money
//		wageMealsInput = createTextField("Meals", "", true, "");
//		addRowToLayout(rightLayout, ++rowIndex, wageMealsInput);
//		// Incentives money
//		wageIncentivesInput = createTextField("Incentives", "", true, "");
//		addRowToLayout(rightLayout, ++rowIndex, wageIncentivesInput);

		// Gross income
		wageGrossInput = createTextField("Gross Income", "", true, "");
		addRowToLayout(rightLayout, ++rowIndex, wageGrossInput);

		createEmptyRow(rightLayout, ++rowIndex);

		// Total expenses and Tax
		wageDeductionInput = createTextField("Expenses + Tax", "", true, "");
		addRowToLayout(rightLayout, ++rowIndex, wageDeductionInput);

		createEmptyRow(leftLayout, ++rowIndex);

		// Total
		wageNetInput = createTextField("Net Income", "", true, "");
		addRowToLayout(rightLayout, ++rowIndex, wageNetInput);

		createEmptyRow(leftLayout, ++rowIndex);
		rightLayout.add(new Separator(), 0, ++rowIndex, 4, 1);

		rightLayout.add(summaryLayout, 0, ++rowIndex, 4, 1);

		/**
		 * Set styles
		 */
		setStyleClassToElements("text-header", workingHoursHeader, callOutHeader, adjustmentHeader, deductionHeader,
				resultHeader);

		setStyleClassToElements("text-result",
//				totalWorkingHoursInput.getTextField(),
				wageWorkingHoursInput.getTextField(),
				wageWorkingHoursTotalInput.getTextField(),
				wageWorkingHoursHolidaysInput.getTextField(),
				wageAllowanceTotalInput.getTextField(),
//				wageMealsInput.getTextField(),
//				wageIncentivesInput.getTextField(),
				wageDeductionInput.getTextField(),
				wageGrossInput.getTextField(),
				wageNetInput.getTextField());

		setStyleClassToElements("text-green",
				wageWorkingHoursTotalInput.getTextField(),
//				wageMealsInput.getTextField(),
//				wageIncentivesInput.getTextField(),
				wageAllowanceTotalInput.getTextField()
		);

		setStyleClassToElements("text-align-right",
				wageWorkingHoursTotalInput.getTextField(),
				wageWorkingHoursInput.getTextField(),
				// wageWorkingHoursSundaysInput.getTextField(),
				wageWorkingHoursHolidaysInput.getTextField(),
//				wageMealsInput.getTextField(),
//				wageIncentivesInput.getTextField(),
				wageAllowanceTotalInput.getTextField(),
				wageGrossInput.getTextField(),
				wageDeductionInput.getTextField(),
				wageNetInput.getTextField()
		);

		wageDeductionInput.getTextField().setStyle("-fx-text-fill: red;");
		wageGrossInput.getTextField().setStyle("-fx-text-fill: blue;");
		wageNetInput.getTextField().setStyle("-fx-text-fill: blue;");

		// set layout
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		mainLayout.getChildren().addAll(leftLayout, separator, rightLayout);
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
		workingHoursSundayInput.getTextField().setText(String.valueOf(loadedWage.getSundayWorkingHours()));
		workingHoursPublicHolidayInput.getTextField().setText(String.valueOf(loadedWage.getHolidayWorkingHours()));

		// Call out area 1
		callOut1Input.getTextField().setText(String.valueOf(loadedWage.getNumOfCallOutArea1()));
		// Call out area 2
		callOut2Input.getTextField().setText(String.valueOf(loadedWage.getNumOfCallOutArea2()));
		// Call out area 3
		callOut3Input.getTextField().setText(String.valueOf(loadedWage.getNumOfCallOutArea3()));

		currentAdjustmentInput.getTextField().setText(String.valueOf(loadedWage.getCurrentAdjustment()));
		prevAdjustmentInput.getTextField().setText(String.valueOf(loadedWage.getPrevAdjustment()));

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

		AlertBox.display("Entry saved", "Saved data for " + getMonthString(wage.getMonth()) + " "
				+ wage.getYear(), "");
	}

	/**
	 * calculate the wage.
	 */
	private void calculateWage() {

		// remove error class
		clearInputs(leftLayout, false, taxInput);
		clearInputs(rightLayout, false, taxInput);

		Wage wage = getWage();

		// set the fields
//		totalWorkingHoursInput.getTextField().setText(String.valueOf(wage.getTotalWorkingHours()));
		wageWorkingHoursTotalInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageWorkingHoursTotal()));
		wageWorkingHoursInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageWorkingHours()));
		// wageWorkingHoursSundaysInput.getTextField().setText(formatToIndonesianCurrency(wage
		// .getWageWorkingHoursSundays()));
		wageWorkingHoursHolidaysInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageWorkingHoursHolidays()));

		wageAllowanceTotalInput.getTextField().setText(String.valueOf(wage.getWageAllowanceTotal()));
//		wageMealsInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageMeals()));
//		wageIncentivesInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageIncentives()));
		wageDeductionInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageDeduction()));
		wageGrossInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageGross()));
		wageNetInput.getTextField().setText(formatToIndonesianCurrency(wage.getWageNet()));

		createAndShowSummary(wage);
	}

	private static final String NEW_LINE_SEPARATOR = "\n";

	private void createAndShowSummary(Wage wage) {

		int rowIndex = 0;

		// reset grid
		summaryLayout.getChildren().clear();

		// working hours calculation
		createDetailRow(rowIndex, "Total hours incl. overtime (work days + Sundays):", String.format("%s hours (%s hours + %s hours)", wage.getTotalWorkingHours(), wage.getDecPlainWorkingHours(), wage.getExtraHours()));
		createDetailRow(++rowIndex, "Basic hours (work day + Sundays):", String.format("%s hours (%s hours + %s hours)", wage.getNormalWorkingHours(), wage.getIncentivesHours(), wage.getExtraHours()));
		createEmptyRow(summaryLayout, ++rowIndex, 10);

		// basic income
		createDetailRow(++rowIndex, "Basic income:", formatToIndonesianCurrency(wage.getWageWorkingHours()));
		createDetailRow(++rowIndex, "Overtime income:", formatToIndonesianCurrency(wage.getWageWorkingHoursHolidays()));
		createDetailRow(++rowIndex, Color.GREEN, true, "Total basic income (basic income + overtime):",
				formatToIndonesianCurrency(wage.getWageWorkingHoursTotal()));
		createEmptyRow(summaryLayout, ++rowIndex, 10);

		// allowance calculation
		createDetailRow(++rowIndex, "Meals allowance ((total hr - sundays) * meal rate):",
				formatToIndonesianCurrency(wage.getWageMeals()));
		createDetailRow(++rowIndex, "Incentives allowance ((basic hr - sundays) * incentives rate):",
				formatToIndonesianCurrency(wage.getWageIncentives()));
		createDetailRow(++rowIndex, "Call out allowance:",
				formatToIndonesianCurrency(wage.getWageCallOutTotal()));
		createDetailRow(++rowIndex, Color.GREEN, true, "Total allowance:", formatToIndonesianCurrency(wage.getWageAllowanceTotal()));
		createEmptyRow(summaryLayout, ++rowIndex, 10);

		createDetailRow(++rowIndex, "Current adjustment:", formatToIndonesianCurrency(wage.getCurrentAdjustment()));
		createDetailRow(++rowIndex, "Previous adjustment:", formatToIndonesianCurrency(wage.getPrevAdjustment()));

		createDetailRow(++rowIndex, Color.BLUE, true, "Gross income:", formatToIndonesianCurrency(wage.getWageGross()));
		createEmptyRow(summaryLayout, ++rowIndex, 10);

		createDetailRow(++rowIndex, "Jamsostek:", formatToIndonesianCurrency(currentWageProperty.getJamsostekDeduction()));
		createDetailRow(++rowIndex, "Koperasi:", formatToIndonesianCurrency(currentWageProperty.getKoperasiDeduction()));
		createDetailRow(++rowIndex, "Tax:", formatToIndonesianCurrency(wage.getTax()));
		createDetailRow(++rowIndex, Color.RED, true, "Total expense + tax:", formatToIndonesianCurrency(wage.getWageDeduction()));
		createEmptyRow(summaryLayout, ++rowIndex, 10);

		createDetailRow(++rowIndex, Color.BLUE, true, "Net income:", formatToIndonesianCurrency(wage.getWageGross()));


	}

	private void createDetailRow(int rowIndex, String... texts) {
		for (String textStr : texts) {
			Text text = new Text(textStr);
			text.setStyle("-fx-font-size: 11px;");
			summaryLayout.addRow(rowIndex, text);
			GridPane.setHalignment(text, HPos.RIGHT);
		}
	}

	private void createDetailRow(int rowIndex, Color color, boolean bold, String... texts) {
		for (String textStr : texts) {
			Text text = new Text(textStr);
			String style = "-fx-font-size: 11px;";
			if (bold) {
				style += "-fx-font-weight: bold";
			}
			text.setStyle(style);
			text.setFill(color);
			summaryLayout.addRow(rowIndex, text);
			summaryLayout.setHalignment(text, HPos.RIGHT);
		}
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
		BigDecimal currentAdjustment = getBigDecimal(currentAdjustmentInput, null);
		BigDecimal previousAdjustment = getBigDecimal(prevAdjustmentInput, null);

		return new Wage(months.getSelectionModel().getSelectedItem(), years.getSelectionModel().getSelectedItem
				(), workingHours, workingHoursSundays, workingHoursHoliday, numCallOut1, numCallOut2, numCallOut3,
				jamsostekCheckBox.isSelected(), koperasiCheckBox.isSelected(), taxCheckBox.isSelected(), tax,
				currentAdjustment, previousAdjustment);
	}


}
