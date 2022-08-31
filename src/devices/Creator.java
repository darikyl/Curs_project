package devices;

import java.util.Deque;

import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.Counter;
import supplementary.Queue;

// ���� ���������

public class Creator implements Runnable {

	// ��������� ���������
	private TestTv gui;

	// ��������
	private JSlider speed;

	// ����� � ��� ���������� ������� �� ��������
	public Queue queue;

	// ˳�������, � ����� ��� ��� ������� �������
	public Counter counter;

	// ˳������� ���������� �������
	private Counter refused;

	// ����� � ��������� ������� �������
	private Deque<Monitor> que;

	public Creator(TestTv gui, JSlider speed, Queue queue, Counter counter, Counter refused, Deque<Monitor> que) {
		super();
		this.gui = gui;
		this.queue = queue;
		this.speed = speed;
		this.counter = counter;
		this.refused = refused;
		this.que = que;
	}

	@Override
	public void run() {
		do {
			// ��������� ����� �����
			JLabel label = new JLabel();
			label.setBounds(10, 440, 108, 72);
			// ������ �� ���������
			gui.getFrame().add(label);
			// ��������� ����� ������
			Monitor mn = new Monitor(gui, label);
			// ������ �� �������� �����
			que.add(mn);
			if (queue.isFull()) { // ���� ����� �����
				refused.onIn(mn); // �������� ������� ���������� �������
				gui.getFrame().remove(mn.getLabel()); // ��������� ������
				mn = null;
			} else { // ������
				counter.onIn(mn); // �������� ������� ������� �� ���������
				try { // ��������� ������ �� ����� � �������� ������
					mn.moveFromToQ(this.gui.ILblStart, this.gui.ILblWaitCheck, 0, queue, 0).join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			try { // ������ ������ ���
				Thread.sleep(speed.getValue() * 150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (gui.getWork()); // ���� ������ ����������
	}
}
