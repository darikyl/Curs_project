package devices;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.IFromTo;
import supplementary.Queue;

// ���� ������

public class Monitor extends AbstractDevice {

	public Monitor(TestTv gui, JLabel label) {
		super();
		this.gui = gui;
		this.speed = gui.getSliderConveer();
		this.label = label;
		this.states = new String[] { "/other/tel.png", "/other/tel2.png", "/other/tel22.png", "/other/tel31.png",
				"/other/tel32.png", "/other/post.png" };
		URL u = this.getClass().getResource(states[0]);
		Image image = null;
		try {
			image = ImageIO.read(u);
		} catch (IOException e) {
			e.printStackTrace();
		}
		label.setIcon(new ImageIcon(image.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH)));
	}

	/* ����� �����������
	 * ���������: �� �� ��
	 * cnt - ������� ���� ���� ������� �����
	 * set - �������� ������� ������� �� OX
	 */
	public Thread moveFromTo(final IFromTo from, final IFromTo to, int cnt, int set) {
		// ��������� ���� ���� �������
		Thread t = new Thread() {
			public void run() {
				// ������ �������
				int hT = 72, wT = 108;
				// ��������� �� �
				int xFrom = from.getComponent().getX();
				int xTo = to.getComponent().getX();
				int lenX1 = Math.abs(xTo - xFrom);
				// ��������� �� Y
				int yFrom = from.getComponent().getY();
				int yTo = to.getComponent().getY();
				int lenY1 = Math.abs(yTo - yFrom);
				int lenX = xTo - xFrom;
				int lenY = yTo - yFrom;
				// ������� ��������
				int len = 0;
				if (lenX1 == 0) { // ���� ������ ��������� X ������� 0, ������� �� ������ Y 
					len = lenY1;
				} else { // ������ �������
					len = lenX1;
				}
				// ������� ����� ����������
				int lenT = (hT + wT) / 2;
				// ʳ������ �����
				int n = len / lenT + 2;
				if (n % 2 != 0) { // ʳ������ ����� �� ���� ������ ������, ��� ���������� ����������� ���� �������� �������
					n++;
				}
				// ����� ����������
				int dx = lenX / n;
				int dy = lenY / n;
				// ������ ������ ������� ��䳿 "��������"
				from.onOut(Monitor.this);
				// �������� ����� �������
				if (cnt == 0 || cnt == 5) { // ���� ���� 0 �� 5, �� �� ���������� ���� ��������
					// ���� ����������
					for (int x = xFrom + set, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// ������������ ���� �������
						URL u = this.getClass().getResource(states[cnt]);
						try {
							Image image = ImageIO.read(u);
							image = image.getScaledInstance(Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight(), Image.SCALE_SMOOTH);
							// ������������ ������ �� ���� ����
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // ������ ������ ���
								Thread.sleep(speed.getValue() * 50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// ������ ������ ������� ��䳿 "��������"
					to.onIn(Monitor.this);
				} else { // ���� ���� �����
					//���� ����������
					for (int x = xFrom - 25, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// ������������ ���� �������
						URL u;
						if (i % 2 == 0) {
							u = this.getClass().getResource(states[cnt]);
						} else {
							u = this.getClass().getResource(states[cnt + 1]);
						}
						try {
							Image image = ImageIO.read(u);
							image = image.getScaledInstance(Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight(), Image.SCALE_SMOOTH);
							// ������������ ������ �� ���� ����
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // ������ ������ ���
								Thread.sleep(speed.getValue() * 50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// ������ ������ ������� ��䳿 "��������"
					to.onIn(Monitor.this);
				}
			}
		};
		// ��������� ���������� ���� ���� �������
		t.start();
		return t;
	}

	/* ����� �����������
	 * ���������: �� �� ��
	 * cnt - ������� ���� ���� ������� �����
	 * set - �������� ������� ������� �� OX
	 * que - ����� � ��� �������� ������
	 */
	public Thread moveFromToQ(final IFromTo from, final IFromTo to, int cnt, Queue que, int set) {
		// ��������� ���� ���� �������
		Thread t = new Thread() {
			public void run() {
				// ������ �������
				int hT = 72, wT = 108;
				// ��������� �� �
				int xFrom = from.getComponent().getX();
				int xTo = to.getComponent().getX();
				int lenX1 = Math.abs(xTo - xFrom);
				// ��������� �� �
				int yFrom = from.getComponent().getY();
				int yTo = to.getComponent().getY();
				int lenY1 = Math.abs(yTo - yFrom);
				int lenX = xTo - xFrom;
				int lenY = yTo - yFrom;
				// ������� ��������
				int len = 0;
				if (lenX1 == 0) { // ���� ������ ��������� X ������� 0, ������� �� ������ Y 
					len = lenY1;
				} else { // ������ �������
					len = lenX1;
				}
				// ������� ����� ����������
				int lenT = (hT + wT) / 2;
				// ʳ������ �����
				int n = len / lenT + 2;
				if (n % 2 != 0) { // ʳ������ ����� �� ���� ������ ������, ��� ���������� ����������� ���� �������� �������
					n++;
				}
				// ����� ����������
				int dx = lenX / n;
				int dy = lenY / n;
				// ������ ������ ������� ��䳿 "��������"
				from.onOut(Monitor.this);
				// �������� ����� �������
				if (cnt == 0 || cnt == 5) { // ���� ���� 0 �� 5, �� �� ���������� ���� ��������
					// ���� ����������
					for (int x = xFrom + set, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// ������������ ���� �������
						URL u = this.getClass().getResource(states[cnt]);
						try {
							Image image = ImageIO.read(u);
							image = image.getScaledInstance(Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight(), Image.SCALE_SMOOTH);
							// ������������ ������ �� ���� ����
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // ������ ������ ���
								Thread.sleep(speed.getValue() * 50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// ������ ������ ������� ��䳿 "��������"
					to.onIn(Monitor.this);
					que.onIn(Monitor.this);
				} else { // ���� ���� �����
					//���� ����������
					for (int x = xFrom - 25, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// ������������ ���� �������
						URL u;
						if (i % 2 == 0) {
							u = this.getClass().getResource(states[cnt]);
						} else {
							u = this.getClass().getResource(states[cnt + 1]);
						}
						try {
							Image image = ImageIO.read(u);
							image = image.getScaledInstance(Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight(), Image.SCALE_SMOOTH);
							// ������������ ������ �� ���� ����
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // ������ ������ ���
								Thread.sleep(speed.getValue() * 50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// ������ ������ ������� ��䳿 "��������"
					to.onIn(Monitor.this);
					que.onIn(Monitor.this);
				}
			}
		};
		// ��������� ���������� ���� ���� �������
		t.start();
		return t;
	}

	
	/* ����� ��� ���������� ������� �� ����� �� ����� �� ����� � ������ ����
	 * ���������: ��
	 */
	public Thread movePost(final IFromTo from) {
		JSlider slider = Monitor.this.gui.getSliderPost(); // ĳ����� �������� ��� ���������
		// ��������� ���� ���� �������
		Thread t = new Thread() {
			public void run() {
				// ��������� �������
				int hT = 72, wT = 108;
				// ��������� �� �
				int xFrom = from.getComponent().getX();
				int xTo = xFrom + 400;
				// ��������� �� �
				int yFrom = from.getComponent().getY();
				int yTo = yFrom - 50;
				int lenX = xTo - xFrom;
				int lenY = yTo - yFrom;
				// ʳ������ �����
				int n = 4;
				// ����� �����������
				int dx = lenX / n;
				int dy = lenY / n;
				// ���� ����������
				for (int i = 1, x = xFrom, y = yFrom - hT / i; i < n; x += dx, y += dy, i++) {
					URL u = this.getClass().getResource(states[0]); // ������� ���� �������
					try {
						Image image = ImageIO.read(u);
						// ������������ ������ �� ���� ����, � ����� ������� �����
						image = image.getScaledInstance(Monitor.this.label.getWidth() / i,
								Monitor.this.label.getHeight() / i, Image.SCALE_SMOOTH);
						Monitor.this.label.setIcon(new ImageIcon(image)); 
						Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth() / i,
								Monitor.this.label.getHeight() / i);
						try { // ������ ������ ���
							Thread.sleep(slider.getValue() * 50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth() / i,
								Monitor.this.label.getHeight() / i);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// ���� ����������
				for (int i = n - 1, x = Monitor.this.label.getX() - 25, y = Monitor.this.label.getY() + hT / i
						+ 10; i >= 1; x -= dx, y -= dy, i--) {
					URL u = this.getClass().getResource(states[5]); // ������������ ���� �������
					try {
						Image image = ImageIO.read(u);
						image = image.getScaledInstance(wT / i, hT / i, Image.SCALE_SMOOTH);
						Monitor.this.label.setIcon(new ImageIcon(image));
						// ������������ ������ �� �������� ������
						Monitor.this.label.setBounds(x, y, wT / i, hT / i);
						try { // ������ ������ ���
							Thread.sleep(slider.getValue() * 50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Monitor.this.label.setBounds(x, y, wT / i, hT / i);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		// ��������� ���������� ���� ���� �������
		t.start();
		return t;
	}

}
