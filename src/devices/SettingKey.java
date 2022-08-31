package devices;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.Counter;
import supplementary.Queue;

// ���� ������������

public class SettingKey extends AbstractDevice implements Runnable {
	
	// ����� ����� ������ ������� �� ��������
	private Queue queue;
	
	// ˳������� ������������ �������
	private Counter counter;

	// ˳������� ��������� �������
	private Counter refused;
	
	public SettingKey(TestTv gui, Counter counter, JSlider speed, Queue queue, Counter refused) {
		JLabel label = new JLabel();
		label.setBounds(80, 100, 70, 60);
		gui.getFrame().add(label);
		this.gui = gui;
		this.speed = gui.getSliderCheck();
		this.label = label;
		this.counter = counter;
		this.queue = gui.getSettingQ();
		this.speed = speed;
		this.states = new String[] { null, "/other/key.png" };
		this.refused = refused;
		label.setIcon(null);
	}

	@Override
	public void run() {
		Queue checkQ = gui.getCheckQ();
		do {
			label.setIcon(null); //��������� ���������� � ������
			synchronized (queue) {
				while (queue.isEmpty()) { // ���� ����� �����
					try {// ������ ���� ������� ������� 
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.queue.notify();
			}
			Monitor mn = this.queue.deleteMon(); // ������ ������ � �����
			if (mn == null) // ���� ������ �� ������ ����������
				continue;
			try { // ���������� ������ �� ������������
				mn.moveFromTo(this.gui.ILblSetWait, this.gui.ILblSet, 0, 0).join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			;
			// ������ ������ � ��������
			counter.onIn(mn);
			// ���� ������ ������������
			for (int i = 0; i < 10; i++) {
				URL u = this.getClass().getResource(states[1]); // ������ ���������� ����� �� �����
				try {
					// ������� ����������
					Image image = ImageIO.read(u);
					// ���� �� �������� ��
					if (i % 2 == 1)
						image = change(image); // ������� ����������
					// ������� ������
					image = image.getScaledInstance(SettingKey.this.label.getWidth(), SettingKey.this.label.getHeight(),
							Image.SCALE_SMOOTH);
					SettingKey.this.label.setIcon(new ImageIcon(image));
					SettingKey.this.label.setBounds(SettingKey.this.label.getX(), SettingKey.this.label.getY(),
							SettingKey.this.label.getWidth(), SettingKey.this.label.getHeight());
					try { // ������ ������ ���
						Thread.sleep(speed.getValue() * 50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try { // ������ ������ ���
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			label.setIcon(null); // ��������� ���������� � ������
			try { // ��������� ��'���
				mn.moveFromTo(this.gui.ILblSet, this.gui.ILblSetWait_2, 0, 35).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (checkQ.isFull()) { // ���� ����� �� �������� �����
				try { // ��������� �� �����
					mn.moveFromTo(this.gui.ILblSetWait_2, this.gui.ILblStart, 0, 0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refused.onIn(mn); // �������� �������� ��������� �������
				gui.getFrame().remove(mn.getLabel()); // ��������� ����� � ������
				mn = null;
			} else { // ������ ���������� � ����� �� ��������
				mn.moveFromToQ(this.gui.ILblSetWait_2, this.gui.ILblWaitCheck, 0, checkQ, 0);
			}
		} while (gui.getWork()); // ���� ���������� ������
	}
	
	// ����� ����� ���������� �� ��������� ������� �������������
	public static Image change(Image img) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		// ³��������� ��������� �������������
		tx.translate(0, -img.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		img = op.filter((BufferedImage) img, null);
		return img;
	}
}
