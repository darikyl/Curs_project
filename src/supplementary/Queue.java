package supplementary;

import java.awt.Component;
import java.util.Deque;
import java.util.LinkedList;

import devices.Monitor;
import main.TestTv;

// Клас черга

public class Queue implements IFromTo{
	
	// Двостороння черга, де будуть знаходитись монітори
	private Deque<Monitor> que;
	
	// Максимальний розмір черги
	private int maxSize;
	
	// Графічний інтерфейс
	private TestTv gui;
	
	public Queue(TestTv gui) { 
		this.gui = gui;
		this.maxSize = 10;
		this.que = new LinkedList<>();
	}

	@Override
	public void onOut(Monitor mn) {
	}
	
	// Коли додається новий монітор
	@Override
	public void onIn(Monitor mn) {
		synchronized (this) { 
			if (que.size() < maxSize  && gui.getWork()) { // Якщо є місце в черзі і застосунок працює
				synchronized (this) {
					// якщо в черзі є місце - додаємо монітор
					que.add((Monitor) mn);
				}
				// повідомляємо про додавання
				this.notify();
			}
		}	
	}

	@Override
	public Component getComponent() {
		 return null;
	}
	
	// Метод видалення монітора з черги
	public Monitor deleteMon() {
		Monitor mn = que.pop();
		return mn;
	}
	
	// Метод для визначення чи пуста черга
	public boolean isEmpty() {
		return que.size() == 0;
	}
	
	// Метод для перевірки чи повна черга
	public boolean isFull() {
		return que.size() == maxSize;
	}
	

	
	
}
