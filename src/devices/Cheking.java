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

// Клас перевірка

public class Cheking extends AbstractDevice implements Runnable {
	
	// Черга звідки беруть монітори на перевірку
	private Queue queue;
	
	// Лічильник перевірених моніторів
	private Counter counter;

	// Лічильник відхилених моніторів
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
		Queue setQ = gui.getSettingQ(); // Черга на налаштування
		Queue posQ = gui.getPostQ(); // Черга на пакування
		do {
			this.display(states[0]); // Відображаємо перший стан
			synchronized (queue) { 
				while (queue.isEmpty()) { // Поки черга пуста
					try {// чекаємо доки додасться монітор
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.queue.notify(); // Черга повідомила про додавання елемента
			}
			Monitor mn = this.queue.deleteMon(); // Забираємо монітор з черги
			if (mn == null) // Якщо не забрали продовжуємо
				continue;
			try { // Переміщуємо елемент на перевірку
				mn.moveFromTo(this.gui.ILblWaitCheck, this.gui.ILblCheck, 0, 0).join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			;
			// Збільшуємо лічильник
			counter.onIn(mn);
			try { // Чекаємо деякий час
				Thread.sleep(speed.getValue() * 80);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Генеруємо рандомне число
			Random rnd = new Random();
			// Якщо згенероване число націло ділиться на 2
			if (rnd.nextInt() % 2 == 0) {
				this.display(states[2]); // Стан перевірки стає червоним
				try { // переміщуємо об'єкт
					mn.moveFromTo(this.gui.ILblCheck, this.gui.ILblSetWait_1, 1, 0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (setQ.isFull()) { // Якщо черга на налаштування повна
					try { // Переміщуємо об'єкт
						mn.moveFromTo(this.gui.ILblSetWait_1, this.gui.ILblSetRef, 0, 0).join();
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					refused.onIn(mn); // Додаємо монітор у лічильник на відхилення
					gui.getFrame().remove(mn.getLabel()); // Та прибираємо напис
					mn = null;
				} else { // Інакше переміщуємо монітор до черги на налаштування
					mn.moveFromToQ(this.gui.ILblSetWait_1, this.gui.ILblSetWait, 1, setQ, 0);
				}
			} else { // Якщо згенероване число непарне
				this.display(states[1]); // Стан перевірки стане зеленим
				try { // Переміщуємо об'єкт
					mn.moveFromTo(this.gui.ILblCheck, this.gui.ILblPostWait_1, 3, 0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (posQ.isFull()) { // Якщо черга на пакування повна
					try { // Переміщуємо об'єкт
						mn.moveFromTo(this.gui.ILblPostWait_1, this.gui.ILblPostRef, 0, 0).join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					refused.onIn(mn); // Додаємо монітор у лічильник на відхилення
					gui.getFrame().remove(mn.getLabel()); // Прибираємо напис
					mn = null;
				} else { // Інакше переміщуємо монітор до черги на пакування
					mn.moveFromToQ(this.gui.ILblPostWait_1, this.gui.ILblPostWait, 3, posQ, 0);
				}
			}
		} while (gui.getWork()); // Працює поки працює застосунок
	}
}
