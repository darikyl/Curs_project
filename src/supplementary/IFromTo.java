package supplementary;

import java.awt.Component;

import devices.Monitor;

public interface IFromTo {
	/**
	* ����� ������� "��䳿" - ������� ���� ����������.
	* ����������� ����� ����� �������� ���� mn.
	* @param mn ����������, �� �������� � �ᒺ���
	*/
	public void onOut(Monitor mn);

	/**
	 * ����� ������� "��䳿" - ����� ���� ����������.
	 * �����������, ���� mn ��������� � �ᒺ���. 
	 * @param mn ����������,�� ������� �� �ᒺ���
	 */
	public void onIn(Monitor mn);

	/**
	 * �����, �� ���� ������ �� ����������, ���� ����������� ��'��� �� GUI
	 * @return ��������� �� ���������, ���� �������� ��'��� �� GUI
	 */
	public Component getComponent();

}
