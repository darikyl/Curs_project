package devices;

import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.Counter;
import supplementary.Queue;

public class Post extends AbstractDevice implements Runnable {

	// ����� ����� ������ ������� �� ��������
	private Queue queue;

	// ˳������� ���������� �������
	private Counter counter;

	public Post(TestTv gui, Counter counter, JSlider speed, Queue queue) {
		JLabel label = new JLabel();
		label.setBounds(10, 440, 108, 72);
		gui.getFrame().add(label);
		this.gui = gui;
		this.speed = gui.getSliderCheck();
		this.label = label;
		this.counter = counter;
		this.queue = gui.getPostQ();
		this.speed = speed;
	}

	@Override
	public void run() {
		do {
			synchronized (queue) {
				while (queue.isEmpty()) { // ���� ����� �����
					try {// ������ ���� ����������� �����
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.queue.notify();
			}
			Monitor mn = this.queue.deleteMon(); // �������� ������ � �����
			if (mn == null) // ���� ������� ���� ����������
				continue;
			try { // ��������� ������ �� ���������
				mn.moveFromTo(this.gui.ILblPostWait, this.gui.ILblPost, 0, -25).join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			;
			// ������ ������ � ��������
			counter.onIn(mn);
			try { // ����� ������ ���
				Thread.sleep(speed.getValue() * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try { // "������" ������
				mn.movePost(this.gui.ILblPost).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try { // ��������� ������ �� �����
				mn.moveFromTo(this.gui.ILblPost, this.gui.ILblEnd, 5, -25).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// ��������� ����� � ������
			gui.getFrame().remove(mn.getLabel());
			mn = null;
		} while (gui.getWork()); // ���� ������ ����������
	}
}
