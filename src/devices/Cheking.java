package devices;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.*;

// ���� ��������

public class Cheking extends AbstractDevice implements Runnable {
	
	// ����� ����� ������ ������� �� ��������
	private Queue queue;
	
	// ˳������� ���������� �������
	private Counter counter;

	// ˳������� ��������� �������
	private Counter refused;

	public Cheking(TestTv gui, Counter counter, JSlider speed, Queue queue, Counter refused) {
		JLabel label = new JLabel();
		label.setBounds(548, 513, 69, 43);
		gui.getFrame().add(label);
		this.gui = gui;
		this.speed = gui.getSliderCheck();
		this.label = label;
		this.counter = counter;
		this.queue = gui.getCheckQ();
		this.speed = speed;
		this.refused = refused;
		this.states = new String[] { "/other/workTableDef.png", "/other/workTableG.png", "/other/workTableR.png" };
		URL u = this.getClass().getResource(states[0]);
		Image image = null;
		try {
			image = ImageIO.read(u);
		} catch (IOException e) {
			e.printStackTrace();
		}
		label.setIcon(new ImageIcon(image.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH)));
	}

	@Override
	public void run() {
		Queue setQ = gui.getSettingQ(); // ����� �� ������������
		Queue posQ = gui.getPostQ(); // ����� �� ���������
		do {
			this.display(states[0]); // ³��������� ������ ����
			synchronized (queue) { 
				while (queue.isEmpty()) { // ���� ����� �����
					try {// ������ ���� ��������� ������
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.queue.notify(); // ����� ��������� ��� ��������� ��������
			}
			Monitor mn = this.queue.deleteMon(); // �������� ������ � �����
			if (mn == null) // ���� �� ������� ����������
				continue;
			try { // ��������� ������� �� ��������
				mn.moveFromTo(this.gui.ILblWaitCheck, this.gui.ILblCheck, 0, 0).join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			;
			// �������� ��������
			counter.onIn(mn);
			try { // ������ ������ ���
				Thread.sleep(speed.getValue() * 80);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// �������� �������� �����
			Random rnd = new Random();
			// ���� ����������� ����� ������ ������� �� 2
			if (rnd.nextInt() % 2 == 0) {
				this.display(states[2]); // ���� �������� ��� ��������
				try { // ��������� ��'���
					mn.moveFromTo(this.gui.ILblCheck, this.gui.ILblSetWait_1, 1, 0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (setQ.isFull()) { // ���� ����� �� ������������ �����
					try { // ��������� ��'���
						mn.moveFromTo(this.gui.ILblSetWait_1, this.gui.ILblSetRef, 0, 0).join();
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					refused.onIn(mn); // ������ ������ � �������� �� ���������
					gui.getFrame().remove(mn.getLabel()); // �� ��������� �����
					mn = null;
				} else { // ������ ��������� ������ �� ����� �� ������������
					mn.moveFromToQ(this.gui.ILblSetWait_1, this.gui.ILblSetWait, 1, setQ, 0);
				}
			} else { // ���� ����������� ����� �������
				this.display(states[1]); // ���� �������� ����� �������
				try { // ��������� ��'���
					mn.moveFromTo(this.gui.ILblCheck, this.gui.ILblPostWait_1, 3, 0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (posQ.isFull()) { // ���� ����� �� ��������� �����
					try { // ��������� ��'���
						mn.moveFromTo(this.gui.ILblPostWait_1, this.gui.ILblPostRef, 0, 0).join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refused.onIn(mn); // ������ ������ � �������� �� ���������
					gui.getFrame().remove(mn.getLabel()); // ��������� �����
					mn = null;
				} else { // ������ ��������� ������ �� ����� �� ���������
					mn.moveFromToQ(this.gui.ILblPostWait_1, this.gui.ILblPostWait, 3, posQ, 0);
				}
			}
		} while (gui.getWork()); // ������ ���� ������ ����������
	}
}
