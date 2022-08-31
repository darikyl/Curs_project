package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

// Клас для відтворення аудіодоріжки

public class Sound {

	private Clip clip;

	public Sound(String fileName) {
		try {
			// Посилання на звук
			URL url = this.getClass().getResource(fileName);

			if (url != null) { // Якщо посилання існує
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip(); // Завантажуємо звук у clip
				clip.open(audioInputStream); // Відкриваємо звук
			} else {
				JOptionPane.showMessageDialog(null, "File wasn't found", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Метод для початку програвання звуку
	public void play() {
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
	}

	// Метод для зупинки звуку
	public void stop() {
		clip.stop();
	}
}
