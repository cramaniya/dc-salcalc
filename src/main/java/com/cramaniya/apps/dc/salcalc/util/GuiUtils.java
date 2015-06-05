package com.cramaniya.apps.dc.salcalc.util;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Citra Ramaniya
 */
@Slf4j
public class GuiUtils {

	/**
	 * adds a row to the layout.
	 *
	 * @param layout            GridPane layout
	 * @param rowIndex          row index
	 * @param labelAndTextField LabelAndTextField objects
	 */
	public static void addRowToLayout(GridPane layout, int rowIndex, LabelAndTextField... labelAndTextField) {
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

	/**
	 * adds a row to the layout.
	 *
	 * @param layout            GridPane layout
	 * @param colIndexStart     starting column index
	 * @param rowIndex          row index
	 * @param labelAndTextField LabelAndTextField objects
	 */
	public static void addRowToLayout(GridPane layout, int colIndexStart, int rowIndex, LabelAndTextField...
			labelAndTextField) {
		int colLabel = colIndexStart;
		int colInput = colIndexStart + 1;
		for (LabelAndTextField input : labelAndTextField) {
			layout.add(input.getLabel(), colLabel, rowIndex);
			layout.add(input.getTextField(), colInput, rowIndex);
		}
	}

	/**
	 * Returns integer value from a string.
	 *
	 * @return integer value
	 */
	public static String getString(Integer value) {
		if (value == null) {
			return "";
		} else {
			return String.valueOf(value);
		}
	}

	/**
	 * Returns integer value from a string.
	 *
	 * @param input LabelAndTextField
	 * @return integer value
	 */
	public static int getInt(LabelAndTextField input, Integer defaultValue) {
		String value = input.getTextField().getText();
		if (value == null || "".equals(value.trim())) {
			if (defaultValue == null) {
				value = "0";
			} else {
				value = String.valueOf(defaultValue);
			}
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			log.error("Error: " + input.getLabel().getText() + " " + e.getMessage());
			input.getTextField().getStyleClass().add("text-red");
			return 0;
		}
	}

	/**
	 * Returns BigDecimal value from text value of {@link LabelAndTextField#getTextField()}
	 *
	 * @param input LabelAndTextField
	 * @return BigDecimal value
	 */
	public static BigDecimal getBigDecimal(LabelAndTextField input, BigDecimal defaultValue) {
		String value = input.getTextField().getText();
		if (value == null || "".equals(value.trim())) {
			if (defaultValue == null) {
				return BigDecimal.ZERO;
			}
			return defaultValue;
		}
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			log.error("Error: " + input.getLabel().getText() + " " + e.getMessage());
			input.getTextField().getStyleClass().add("text-red");
			return BigDecimal.ZERO;
		}
	}

	/**
	 * Creates empty row.
	 */
	public static void clearInputs(Pane layout, boolean resetTextValue, LabelAndTextField... disabledTextField) {
		for (Node node : layout.getChildren()) {
			if (node instanceof TextField) {
				TextField textField = (TextField) node;
				textField.getStyleClass().remove("text-red");
				if (resetTextValue) {
					textField.setText("");
				}
			} else if (node instanceof CheckBox) {
				if (resetTextValue) {
					CheckBox checkBox = (CheckBox) node;
					checkBox.setSelected(false);
				}
			}
		}
		if (resetTextValue) {
			for (LabelAndTextField input : disabledTextField) {
				input.getTextField().setDisable(true);
			}
		}
	}

	public static void createEmptyRow(GridPane layout, int rowIndex, int height) {
		Pane emptyRow = new Pane();
		emptyRow.setMinHeight(height);
		layout.add(emptyRow, 0, rowIndex);
	}

	public static void createEmptyRow(GridPane layout, int rowIndex) {
		Pane emptyRow = new Pane();
		emptyRow.setMinHeight(20);
		layout.add(emptyRow, 0, rowIndex);
	}

	/**
	 * Create text field.
	 *
	 * @param labelName    the label name
	 * @param promptText   the text prompt
	 * @param defaultValue the default value
	 * @return LabelAndTextField
	 */
	public static LabelAndTextField createTextField(String labelName, String promptText, String defaultValue) {
		return createTextField(labelName, promptText, false, defaultValue);
	}

	/**
	 * Create text field.
	 *
	 * @param labelName    the label name
	 * @param promptText   the text prompt
	 * @param defaultValue the default value
	 * @param toolTipText  tooltip text
	 * @return LabelAndTextField
	 */
	public static LabelAndTextField createTextField(String labelName, String promptText, String defaultValue, String
			toolTipText) {
		return createTextField(labelName, promptText, false, defaultValue, toolTipText);
	}

	/**
	 * Create text field. Has the same function as {@link #createTextField(String, String, String)}, but
	 * with extra 'disabled' parameter.
	 *
	 * @param labelName    the label name
	 * @param promptText   the text prompt
	 * @param disabled     is the textfield disabled?
	 * @param defaultValue the default value
	 * @return LabelAndTextField
	 */
	public static LabelAndTextField createTextField(String labelName, String promptText, boolean disabled, String
			defaultValue) {
		return createTextField(labelName, promptText, disabled, defaultValue, null);
	}

	/**
	 * Create text field. Has the same function as {@link #createTextField(String, String, String)}, but
	 * with extra 'disabled' parameter.
	 *
	 * @param labelName    the label name
	 * @param promptText   the text prompt
	 * @param disabled     is the textfield disabled?
	 * @param defaultValue the default value
	 * @param toolTipText  tooltip text
	 * @return LabelAndTextField
	 */
	public static LabelAndTextField createTextField(String labelName, String promptText, boolean disabled, String
			defaultValue, String toolTipText) {

		assert labelName != null && !"".equals(labelName.trim());

		Label label = new Label(labelName);

		TextField textField = new TextField();
		textField.setId(labelName.toLowerCase());
		textField.setPromptText(promptText);
		textField.setDisable(disabled);
		textField.setText(defaultValue);
		if (toolTipText != null && toolTipText.trim().length() > 0)
			textField.setTooltip(new Tooltip(toolTipText));

		return new LabelAndTextField(label, textField);
	}

	public static void setStyleClassToElements(String styleClass, Control... elements) {
		for (Control element : elements) {
			element.getStyleClass().add(styleClass);
		}
	}

	public static void setStyleToElements(String style, Control... elements) {
		for (Control element : elements) {
			element.setStyle(style);
		}
	}

	/**
	 * inner class for a pair of {@link Label} and {@link TextField}.
	 */
	@Getter
	public static class LabelAndTextField {

		private Label label;

		private TextField textField;

		public LabelAndTextField(Label label, TextField textField) {
			this.label = label;
			this.textField = textField;
		}
	}
}
