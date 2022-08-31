package main;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

// ���� ��� ���������� ����������

public class Sound {

	private Clip clip;

	public Sound(String fileName) {
		try {
			// ��������� �� ����
			URL url = this.getClass().getResource(fileName);

			if (url != null) { // ���� ��������� ����
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip(); // ����������� ���� � clip
				clip.open(audioInputStream); // ³�������� ����
			} else {
				JOptionPane.showMessageDialog(null, "File wasn't found", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ����� ��� ������� ����������� �����
	public void play() {
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		clip.start();
	}

	// ����� ��� ������� �����
	public void stop() {
		clip.stop();
	}
}
