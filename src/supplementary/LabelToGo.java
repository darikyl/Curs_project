package supplementary;

import java.awt.Component;
import javax.swing.JLabel;

import devices.Monitor;

// Клас для позначення написів до яких рухаються об'єкти

public class LabelToGo implements IFromTo{
	private JLabel label;

	public LabelToGo(JLabel label) {
		super();
		this.label = label;
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
