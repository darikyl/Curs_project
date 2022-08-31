package devices;

import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.Counter;
import supplementary.Queue;

public class Post extends AbstractDevice implements Runnable {

	// Черга звідки беруть монітори на перевірку
	private Queue queue;

	// Лічильник перевірених моніторів
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
				while (queue.isEmpty()) { // Поки черга пуста
					try {// Чекаємо доки наповниться черга
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.queue.notify();
			}
			Monitor mn = this.queue.deleteMon(); // Забираємо монітор з черги
			if (mn == null) // Якщо монітора немає продовжуємо
				continue;
			try { // Переміщуємо монітор на пакування
				mn.moveFromTo(this.gui.ILblPostWait, this.gui.ILblPost, 0, -25).join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			;
			// Додаємо монітор в лічильник
			counter.onIn(mn);
			try { // Чекаєм деякий час
				Thread.sleep(speed.getValue() * 100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try { // "Пакуємо" монітор
				mn.movePost(this.gui.ILblPost).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try { // Переміщуємо монітор на кінець
				mn.moveFromTo(this.gui.ILblPost, this.gui.ILblEnd, 5, -25).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Видаляємо напис з екрану
			gui.getFrame().remove(mn.getLabel());
			mn = null;
		} while (gui.getWork()); // Поки працює застосунок
	}
}
