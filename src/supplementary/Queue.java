package supplementary;

import java.awt.Component;
import java.util.Deque;
import java.util.LinkedList;

import devices.Monitor;
import main.TestTv;

// ���� �����

public class Queue implements IFromTo{
	
	// ����������� �����, �� ������ ����������� �������
	private Deque<Monitor> que;
	
	// ������������ ����� �����
	private int maxSize;
	
	// ��������� ���������
	private TestTv gui;
	
	public Queue(TestTv gui) { 
		this.gui = gui;
		this.maxSize = 10;
		this.que = new LinkedList<>();
	}

	@Override
	public void onOut(Monitor mn) {
	}
	
	// ���� �������� ����� ������
	@Override
	public void onIn(Monitor mn) {
		synchronized (this) { 
			if (que.size() < maxSize  && gui.getWork()) { // ���� � ���� � ���� � ���������� ������
				synchronized (this) {
					// ���� � ���� � ���� - ������ ������
					que.add((Monitor) mn);
				}
				// ����������� ��� ���������
				this.notify();
			}
		}	
	}

	@Override
	public Component getComponent() {
		 return null;
	}
	
	// ����� ��������� ������� � �����
	public Monitor deleteMon() {
		Monitor mn = que.pop();
		return mn;
	}
	
	// ����� ��� ���������� �� ����� �����
	public boolean isEmpty() {
		return que.size() == 0;
	}
	
	// ����� ��� �������� �� ����� �����
	public boolean isFull() {
		return que.size() == maxSize;
	}
	

	
	
}
