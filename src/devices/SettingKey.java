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

// Клас налаштування

public class SettingKey extends AbstractDevice implements Runnable {
	
	// Черга звідки беруть монітори на перевірку
	private Queue queue;
	
	// Лічильник налахтованих моніторів
	private Counter counter;

	// Лічильник відхилених моніторів
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
			label.setIcon(null); //Прибираємо зображення з напису
			synchronized (queue) {
				while (queue.isEmpty()) { // Поки черга пуста
					try {// чекаємо доки елемент додастя 
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.queue.notify();
			}
			Monitor mn = this.queue.deleteMon(); // Беремо монітор з черги
			if (mn == null) // Якщо монітор не взявся продовжуємо
				continue;
			try { // Переносимо монітор на налаштування
				mn.moveFromTo(this.gui.ILblSetWait, this.gui.ILblSet, 0, 0).join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			;
			// Додаємо монітор в лічильник
			counter.onIn(mn);
			// Цикл роботи налаштування
			for (int i = 0; i < 10; i++) {
				URL u = this.getClass().getResource(states[1]); // Додаємо зображення ключа на напис
				try {
					// Зчитуємо зображення
					Image image = ImageIO.read(u);
					// Якщо це непарний тік
					if (i % 2 == 1)
						image = change(image); // Змінюємо зображення
					// Змінюємо розміри
					image = image.getScaledInstance(SettingKey.this.label.getWidth(), SettingKey.this.label.getHeight(),
							Image.SCALE_SMOOTH);
					SettingKey.this.label.setIcon(new ImageIcon(image));
					SettingKey.this.label.setBounds(SettingKey.this.label.getX(), SettingKey.this.label.getY(),
							SettingKey.this.label.getWidth(), SettingKey.this.label.getHeight());
					try { // Чекаємо деякий час
						Thread.sleep(speed.getValue() * 50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try { // Чекаємо деякий час
				Thread.sleep(800);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			label.setIcon(null); // Прибираємо зображення з напису
			try { // Переміщуємо об'єкт
				mn.moveFromTo(this.gui.ILblSet, this.gui.ILblSetWait_2, 0, 35).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (checkQ.isFull()) { // Якщо черга на перевірку повна
				try { // Перевіщуємо на старт
					mn.moveFromTo(this.gui.ILblSetWait_2, this.gui.ILblStart, 0, 0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refused.onIn(mn); // Збільшуємо лічильник відхилених моніторів
				gui.getFrame().remove(mn.getLabel()); // Прибираємо напис з екрану
				mn = null;
			} else { // Інакше переносимо у чергу на перевірку
				mn.moveFromToQ(this.gui.ILblSetWait_2, this.gui.ILblWaitCheck, 0, checkQ, 0);
			}
		} while (gui.getWork()); // Поки застосунок працює
	}
	
	// Метод змінює зображення за допомогою афінних трансформацій
	public static Image change(Image img) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		// Відображуємо зображеня горизонтально
		tx.translate(0, -img.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		img = op.filter((BufferedImage) img, null);
		return img;
	}
}
