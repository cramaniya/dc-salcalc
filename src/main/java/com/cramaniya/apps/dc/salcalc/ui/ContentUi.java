package com.cramaniya.apps.dc.salcalc.ui;

import static com.cramaniya.apps.dc.salcalc.util.StringUtils.formatToIndonesiaCurrency;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import lombok.Getter;
import lombok.Setter;

import com.cramaniya.apps.dc.salcalc.business.SalaryCalculator;
import com.cramaniya.apps.dc.salcalc.domain.Salary;

/**
 * Class for Content GUI.
 *
 * @author Citra Ramaniya
 */
@Setter
@Getter
public class ContentUi {

	private LabelAndTextField workingHoursInput;

	private LabelAndTextField numOfHolidayInput;

	private LabelAndTextField callOut1Input;

	private LabelAndTextField callOut2Input;

	private LabelAndTextField callOut3Input;

	private LabelAndTextField baseSalaryInput;

	private LabelAndTextField addedHolidaySalaryInput;

	private LabelAndTextField mealsInput;

	private LabelAndTextField incentivesInput;

	private LabelAndTextField totalSalaryInput;

	private GridPane layout = new GridPane();

	private int columnIndexLabel = 0;

	private int columnIndexInput = 1;

	private int columnIndexLabel2 = 2;

	private int columnIndexInput2 = 3;

	private int rowIndex = 0;

	public ContentUi() {
		layout.setPadding(new Insets(15, 15, 15, 15));
		layout.setVgap(8);
		layout.setHgap(10);
	}

	public void createContent() {

		LocalDate currentDate = LocalDate.now();

		// Month
		Label monthLabel = new Label("Month");
		ComboBox<Month> months = new ComboBox<>();
		for (Month month : Month.values()) {
			months.getItems().add(month);
		}
		months.getSelectionModel().select(currentDate.getMonth());
		// Year
		Label yearLabel = new Label("Year");
		ComboBox<Integer> years = new ComboBox<>();
		Integer startYear = currentDate.getYear() - 5;
		for (Integer i = startYear; i < currentDate.getYear() + 5; i++) {
			years.getItems().add(i);
		}
		years.getSelectionModel().select(Integer.valueOf(currentDate.getYear()));
		layout.addRow(rowIndex, monthLabel, months, yearLabel, years);

		// Working hours
		workingHoursInput = createTextField("Working Hours", "Working hours", false, "160");
		// Public holidays
		numOfHolidayInput = createTextField("Public Holiday", "Working days on public holiday", false, "");
		addRowToLayout(++rowIndex, workingHoursInput, numOfHolidayInput);

		// Call out area 1
		callOut1Input = createTextField("Call out area 1", "Call outs in area 1", false, "");
		addRowToLayout(++rowIndex, callOut1Input);
		// Call out area 2
		callOut2Input = createTextField("Call out area 2", "Call outs in area 2", false, "");
		addRowToLayout(++rowIndex, callOut2Input);
		// Call out area 3
		callOut3Input = createTextField("Call out area 3", "Call outs in area 3", false, "");
		addRowToLayout(++rowIndex, callOut3Input);

		/**
		 * Buttons
		 */
		HBox hBox = new HBox(10);
		hBox.setPadding(new Insets(10, 10, 10, 10));
		// calculate
		Button calculateButton = new Button("Calculate");
		calculateButton.setOnAction(event -> calculateSalary());
		// clear
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(event -> clearInput());
		hBox.getChildren().addAll(clearButton, calculateButton);
		layout.add(hBox, 3, ++rowIndex);

		createEmptyRow(++rowIndex);

		// Salary with public holidays
		addedHolidaySalaryInput = createTextField("Salary\n(with public holidays)", "", true, "");
		// Base salary
		baseSalaryInput = createTextField("Base Salary", "", true, "");
		addRowToLayout(++rowIndex, addedHolidaySalaryInput, baseSalaryInput);

		// Meals money
		mealsInput = createTextField("Meals", "", true, "");
		addRowToLayout(++rowIndex, mealsInput);

		// Incentives money
		incentivesInput = createTextField("Incentives", "", true, "");
		addRowToLayout(++rowIndex, incentivesInput);

		createEmptyRow(++rowIndex);

		// Total
		totalSalaryInput = createTextField("Total", "", true, "");
		addRowToLayout(++rowIndex, totalSalaryInput);
	}

	private void calculateSalary() {

		int workingHours = getInt(workingHoursInput);
		int numWorkingOnHoliday = getInt(numOfHolidayInput);
		int numCallOut1 = getInt(callOut1Input);
		int numCallOut2 = getInt(callOut2Input);
		int numCallOut3 = getInt(callOut3Input);
		Salary salary = new Salary(workingHours, numWorkingOnHoliday, numCallOut1, numCallOut2, numCallOut3);
		SalaryCalculator.calculateSalary(salary);

		// set the fields
		workingHoursInput.getTextField().setText(String.valueOf(salary.getWorkingHours()));

		baseSalaryInput.getTextField().setText(formatToIndonesiaCurrency(salary.getSalaryBase()));
		addedHolidaySalaryInput.getTextField().setText(formatToIndonesiaCurrency(salary.getSalaryWithPublicHoliday()));
		mealsInput.getTextField().setText(formatToIndonesiaCurrency(salary.getMeals()));
		incentivesInput.getTextField().setText(formatToIndonesiaCurrency(salary.getIncentives()));
		totalSalaryInput.getTextField().setText(formatToIndonesiaCurrency(salary.getSalaryTotal()));

	}

	private void addRowToLayout(int rowIndex, LabelAndTextField... labelAndTextField) {
		List<Node> nodes = new ArrayList<>();
		for (LabelAndTextField input : labelAndTextField) {
			nodes.add(input.getLabel());
			nodes.add(input.getTextField());
		}
		// add to the layout
		Node[] nodeArray = new Node[nodes.size()];
		nodes.toArray(nodeArray);
		layout.addRow(rowIndex, nodeArray);
	}

	private int getInt(LabelAndTextField input) {
		String value = input.getTextField().getText();
		if (value == null || "".equals(value.trim())) {
			value = "0";
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			System.out.println("Error: " + input.getLabel().getText() + " " + e.getMessage());
			input.getTextField().getStyleClass().add("font-red");
			return 0;
		}
	}

	private void clearInput() {
		layout.getChildren().stream().filter(node -> node instanceof TextField).forEach(node -> {
			TextField textField = (TextField) node;
			textField.setText("");
		});

	}

	private void createEmptyRow(int rowIndex) {
		Pane emptyRow = new Pane();
		emptyRow.setMinHeight(20);
		layout.add(emptyRow, 0, rowIndex);
	}

	private LabelAndTextField createTextField(String labelName, String promptText, boolean disabled, String defaultValue) {

		assert labelName != null && !"".equals(labelName.trim());

		Label label = new Label(labelName);

		TextField textField = new TextField();
		textField.setPromptText(promptText);
		textField.setDisable(disabled);
		textField.setText(defaultValue);

		return new LabelAndTextField(label, textField);
	}

	@Getter
	private class LabelAndTextField {

		private Label label;

		private TextField textField;

		public LabelAndTextField(Label label, TextField textField) {
			this.label = label;
			this.textField = textField;
		}
	}

}
