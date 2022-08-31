package supplementary;

import java.awt.Component;

import javax.swing.JTextField;

import devices.Monitor;

// Клас що рахує кількість моніторів

public class Counter implements IFromTo {
	
	// Місце куде буде виводитись кількість
	private JTextField textField;
	
	// Кількість
	private int count;
	
	public Counter(JTextField textField) {
		super();
		this.textField = textField;
		this.textField.setText(" ");
		this.count = 0;
	}
	
	@Override
	public void onOut(Monitor mn) {	
	}
	
	// При додаванні монітора кількість збільшується та заноситься у текстове поле
	@Override
	public void onIn(Monitor mn) {
		count++;
	    textField.setText(String.valueOf(count));
	}

	@Override
	public Component getComponent() {
		return textField;
	}
	
	// Метод для встановлення значення власноруч
	public void setCount(int count) { 
		this.count = count;
		textField.setText(String.valueOf(count)); 
	}
	
}
