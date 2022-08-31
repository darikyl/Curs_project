package devices;

import java.util.Deque;

import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.Counter;
import supplementary.Queue;

// Клас Створювач

public class Creator implements Runnable {

	// Графічний інтерфейс
	private TestTv gui;

	// Швидкість
	private JSlider speed;

	// Черга в яку заносяться монітори на перевірку
	public Queue queue;

	// Лічильник, в якому дані про кількість моніторів
	public Counter counter;

	// Лічильник повернутих моніторів
	private Counter refused;

	// Черга з загальною кількістю моніторів
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
			// Створюємо новий напис
			JLabel label = new JLabel();
			label.setBounds(10, 440, 108, 72);
			// Додаємо на інтерфейс
			gui.getFrame().add(label);
			// Створюємо новий монітор
			Monitor mn = new Monitor(gui, label);
			// Додаємо до загальної черги
			que.add(mn);
			if (queue.isFull()) { // Якщо черга повна
				refused.onIn(mn); // Збільшуємо кількість повернутих моніторів
				gui.getFrame().remove(mn.getLabel()); // Створюємо монітор
				mn = null;
			} else { // Інакше
				counter.onIn(mn); // Збільшуємо кількість моніторів на лічильнику
				try { // Переміщуємо монітор до черги в окремому потоці
					mn.moveFromToQ(this.gui.ILblStart, this.gui.ILblWaitCheck, 0, queue, 0).join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			try { // Чекаємо деякий час
				Thread.sleep(speed.getValue() * 150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (gui.getWork()); // Поки працює застосунок
	}
}
