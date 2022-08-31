package supplementary;

import java.awt.Component;

import javax.swing.JTextField;

import devices.Monitor;

// ���� �� ���� ������� �������

public class Counter implements IFromTo {
	
	// ̳��� ���� ���� ���������� �������
	private JTextField textField;
	
	// ʳ������
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
	
	// ��� �������� ������� ������� ���������� �� ���������� � �������� ����
	@Override
	public void onIn(Monitor mn) {
		count++;
	    textField.setText(String.valueOf(count));
	}

	@Override
	public Component getComponent() {
		return textField;
	}
	
	// ����� ��� ������������ �������� ���������
	public void setCount(int count) { 
		this.count = count;
		textField.setText(String.valueOf(count)); 
	}
	
}
