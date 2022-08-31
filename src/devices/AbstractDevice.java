package devices;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import main.TestTv;
import supplementary.IFromTo;
import supplementary.Queue;

// Клас абстрактного пристрою 

public abstract class AbstractDevice implements IFromTo {
	
	// Графічний інтерфейс
	protected TestTv gui;
	
	// Слайдер для визначення швидкості
	protected JSlider speed;
	
	// Напис, який відображає зображення об'єкта
	protected JLabel label;
	
	// Массив "станів" об'єкта
	protected String[] states;
	
	// Черга об'єкта
	protected Queue q = null;

	public AbstractDevice() {
		super();
	}

	// Метод відображення зображення
	protected void display(String pct) {
		// pct – це рядок символів,
		// що визначає місце знаходження ресурсу,
		// URL це Uniform Resource Locator -
		// об’єкт, що визначає місце знаходження ресурсу.
		URL u = getClass().getResource(pct);
		ImageIcon image = new ImageIcon(u);
		label.setIcon(image);
	}

	// Метод повернення напису
	public JLabel getLabel() {
		return label;
	}
	
	@Override
	public void onOut(Monitor mn) {
	}

	@Override
	public void onIn(Monitor mn) {
	}

	@Override
	public Component getComponent() {
		return label;
	}
}
