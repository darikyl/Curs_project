package devices;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.IFromTo;
import supplementary.Queue;

// ���� ������������ �������� 

public abstract class AbstractDevice implements IFromTo {
	
	// ��������� ���������
	protected TestTv gui;
	
	// ������� ��� ���������� ��������
	protected JSlider speed;
	
	// �����, ���� �������� ���������� ��'����
	protected JLabel label;
	
	// ������ "�����" ��'����
	protected String[] states;
	
	// ����� ��'����
	protected Queue q = null;

	public AbstractDevice() {
		super();
	}

	// ����� ����������� ����������
	protected void display(String pct) {
		// pct � �� ����� �������,
		// �� ������� ���� ����������� �������,
		// URL �� Uniform Resource Locator -
		// �ᒺ��, �� ������� ���� ����������� �������.
		URL u = getClass().getResource(pct);
		ImageIcon image = new ImageIcon(u);
		label.setIcon(image);
	}

	// ����� ���������� ������
	public JLabel getLabel() {
		return label;
	}
	
	@Override
	public void onOut(Monitor mn) {
	}

	@Override
	public void onIn(Monitor mn) {
	}

	@Override
	public Component getComponent() {
		return label;
	}
}
