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

// Клас Монітор

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

	/* Метод пересування
	 * Параметри: від та до
	 * cnt - позначає якиц стан Монітора брати
	 * set - значення зміщення монітора по OX
	 */
	public Thread moveFromTo(final IFromTo from, final IFromTo to, int cnt, int set) {
		// Створюємо потік руху монітора
		Thread t = new Thread() {
			public void run() {
				// Розміри монітора
				int hT = 72, wT = 108;
				// Параметри по Х
				int xFrom = from.getComponent().getX();
				int xTo = to.getComponent().getX();
				int lenX1 = Math.abs(xTo - xFrom);
				// Параметри по Y
				int yFrom = from.getComponent().getY();
				int yTo = to.getComponent().getY();
				int lenY1 = Math.abs(yTo - yFrom);
				int lenX = xTo - xFrom;
				int lenY = yTo - yFrom;
				// Довжина маршрута
				int len = 0;
				if (lenX1 == 0) { // Якщо різниця координат X дорівнює 0, довжина це різниця Y 
					len = lenY1;
				} else { // Інакше навпаки
					len = lenX1;
				}
				// Середній розмір транзакції
				int lenT = (hT + wT) / 2;
				// Кількість кроків
				int n = len / lenT + 2;
				if (n % 2 != 0) { // Кількість кроків має бути парним числом, для коректного відображення зміни картинок монітора
					n++;
				}
				// Кроки переміщення
				int dx = lenX / n;
				int dy = lenY / n;
				// Виклик методу обробки події "відправка"
				from.onOut(Monitor.this);
				// Перевірка стану монітора
				if (cnt == 0 || cnt == 5) { // Якщо стан 0 та 5, то не відбувається зміни картинок
					// Цикл переміщення
					for (int x = xFrom + set, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// Встановлюємо стан монітора
						URL u = this.getClass().getResource(states[cnt]);
						try {
							Image image = ImageIO.read(u);
							image = image.getScaledInstance(Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight(), Image.SCALE_SMOOTH);
							// Встановлюємо монітор на нове місце
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // Чекаємо деякий час
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
					// Виклик методу обробки події "прибуття"
					to.onIn(Monitor.this);
				} else { // Якщо стан інший
					//Цикл переміщення
					for (int x = xFrom - 25, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// Встановлюємо стан монітора
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
							// Встановлюємо монітор на нове місце
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // Чекаємо деякий час
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
					// Виклик методу обробки події "прибуття"
					to.onIn(Monitor.this);
				}
			}
		};
		// Запускаємо створенний потік руху монітора
		t.start();
		return t;
	}

	/* Метод пересування
	 * Параметри: від та до
	 * cnt - позначає який стан Монітора брати
	 * set - значення зміщення монітора по OX
	 * que - черга в яку заносити монітор
	 */
	public Thread moveFromToQ(final IFromTo from, final IFromTo to, int cnt, Queue que, int set) {
		// Створюємо потік руху монітора
		Thread t = new Thread() {
			public void run() {
				// Розміри монітора
				int hT = 72, wT = 108;
				// Параметри по Х
				int xFrom = from.getComponent().getX();
				int xTo = to.getComponent().getX();
				int lenX1 = Math.abs(xTo - xFrom);
				// Параметри по У
				int yFrom = from.getComponent().getY();
				int yTo = to.getComponent().getY();
				int lenY1 = Math.abs(yTo - yFrom);
				int lenX = xTo - xFrom;
				int lenY = yTo - yFrom;
				// Довжина маршрута
				int len = 0;
				if (lenX1 == 0) { // Якщо різниця координат X дорівнює 0, довжина це різниця Y 
					len = lenY1;
				} else { // Інакше навпаки
					len = lenX1;
				}
				// Середній розмір транзакції
				int lenT = (hT + wT) / 2;
				// Кількість кроків
				int n = len / lenT + 2;
				if (n % 2 != 0) { // Кількість кроків має бути парним числом, для коректного відображення зміни картинок монітора
					n++;
				}
				// Кроки переміщення
				int dx = lenX / n;
				int dy = lenY / n;
				// Виклик методу обробки події "відправка"
				from.onOut(Monitor.this);
				// Перевірка стану монітора
				if (cnt == 0 || cnt == 5) { // Якщо стан 0 та 5, то не відбувається зміни картинок
					// Цикл переміщення
					for (int x = xFrom + set, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// Встановлюємо стан монітора
						URL u = this.getClass().getResource(states[cnt]);
						try {
							Image image = ImageIO.read(u);
							image = image.getScaledInstance(Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight(), Image.SCALE_SMOOTH);
							// Встановлюємо монітор на нове місце
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // Чекаємо деякий час
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
					// Виклик методу обробки події "прибуття"
					to.onIn(Monitor.this);
					que.onIn(Monitor.this);
				} else { // Якщо стан інший
					//Цикл переміщення
					for (int x = xFrom - 25, y = yFrom - hT, i = 0; i < n; x += dx, y += dy, i++) {
						// Встановлюємо стан монітора
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
							// Встановлюємо монітор на нове місце
							Monitor.this.label.setIcon(new ImageIcon(image));
							Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth(),
									Monitor.this.label.getHeight());
							try { // Чекаємо деякий час
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
					// Виклик методу обробки події "прибуття"
					to.onIn(Monitor.this);
					que.onIn(Monitor.this);
				}
			}
		};
		// Запускаємо створенний потік руху монітора
		t.start();
		return t;
	}

	
	/* Метод для переміщення монітора за екран та вихад на екран в іншому стані
	 * Параметри: від
	 */
	public Thread movePost(final IFromTo from) {
		JSlider slider = Monitor.this.gui.getSliderPost(); // Дістаємо швидкість для пакування
		// Створюємо потік руху монітора
		Thread t = new Thread() {
			public void run() {
				// Параметри монітора
				int hT = 72, wT = 108;
				// Параметри по Х
				int xFrom = from.getComponent().getX();
				int xTo = xFrom + 400;
				// Параметри по У
				int yFrom = from.getComponent().getY();
				int yTo = yFrom - 50;
				int lenX = xTo - xFrom;
				int lenY = yTo - yFrom;
				// Кількість кроків
				int n = 4;
				// Кроки пересування
				int dx = lenX / n;
				int dy = lenY / n;
				// Цикл переміщення
				for (int i = 1, x = xFrom, y = yFrom - hT / i; i < n; x += dx, y += dy, i++) {
					URL u = this.getClass().getResource(states[0]); // Обираємо стан монітора
					try {
						Image image = ImageIO.read(u);
						// Встановлюємо монітор на нове місце, а також змінюємо рощмір
						image = image.getScaledInstance(Monitor.this.label.getWidth() / i,
								Monitor.this.label.getHeight() / i, Image.SCALE_SMOOTH);
						Monitor.this.label.setIcon(new ImageIcon(image)); 
						Monitor.this.label.setBounds(x, y, Monitor.this.label.getWidth() / i,
								Monitor.this.label.getHeight() / i);
						try { // Чекаємо деякий час
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
				// Цикл повернення
				for (int i = n - 1, x = Monitor.this.label.getX() - 25, y = Monitor.this.label.getY() + hT / i
						+ 10; i >= 1; x -= dx, y -= dy, i--) {
					URL u = this.getClass().getResource(states[5]); // Встановлюємо стан монітора
					try {
						Image image = ImageIO.read(u);
						image = image.getScaledInstance(wT / i, hT / i, Image.SCALE_SMOOTH);
						Monitor.this.label.setIcon(new ImageIcon(image));
						// Встановлюємо монітор та збільшуємо розміри
						Monitor.this.label.setBounds(x, y, wT / i, hT / i);
						try { // Чекаємо деякий час
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
		// Запускаємо створенний потік руху монітора
		t.start();
		return t;
	}

}
