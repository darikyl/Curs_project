package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;

import devices.*;
import supplementary.*;

import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.ComponentOrientation;

// Графічний інтерфейс

public class TestTv {
	// Головний фрейм
	private JFrame frmTesttv;

	// Написи що позначають ключові точки зупинок на екрани
	private JLabel lblStart;
	private JLabel lblWaitCheck;
	private JLabel lblCheck;
	private JLabel lblSetWait_1;
	private JLabel lblSetWait;
	private JLabel lblSetWait_2;
	private JLabel lblPostWait_1;
	private JLabel lblPostWait;
	private JLabel lblPost;
	private JLabel lblEnd;
	private JLabel lblSet;
	private JLabel lblCheck_1;
	private JLabel lblSetRef;
	private JLabel lblPostRef;

	// Написи для переміщення моніторів
	public LabelToGo ILblWaitCheck;
	public LabelToGo ILblStart;
	public LabelToGo ILblCheck;
	public LabelToGo ILblSetWait_1;
	public LabelToGo ILblSetWait;
	public LabelToGo ILblSetWait_2;
	public LabelToGo ILblPostWait_1;
	public LabelToGo ILblPostWait;
	public LabelToGo ILblPost;
	public LabelToGo ILblEnd;
	public LabelToGo ILblSet;
	public LabelToGo ILblCheck_1;
	public LabelToGo ILblSetRef;
	public LabelToGo ILblPostRef;

	// Лічильики
	public Counter amountCnt;
	public Counter checkedCnt;
	public Counter settingCnt;
	public Counter postCnt;
	public Counter refCheckCnt;

	// Черги до цехів
	public Queue checkQ;
	public Queue settingQ;
	public Queue postQ;
	
	// Чера з усіма моніторами
	public Deque<Monitor> que;

	// Створення об'єктів
	public Creator creator;
	public Cheking check;
	public SettingKey key;
	public Post post;

	// Потоки
	public Thread cr;
	public Thread ch;
	public Thread ps;
	public Thread ky;

	// Кнопки
	private JButton btnStart;
	private JButton btnEnd;

	// Музичний супровід 
	public Sound sound = new Sound("/other/FactoryMusic.wav");
	
	// Змінна що відповідає за роботу застосунку
	public boolean work;

	// Місця куди вносяться дані з лічильників
	private JTextField textFieldCheck;
	private JTextField textFieldSet;
	private JTextField textFieldPost;
	private JTextField textFieldAmount;
	private JTextField textFieldRefCheck;

	// Слайдери для визначення швидкостей роботи пристроїв
	public JSlider sliderConveer;
	private JSlider sliderCheck;
	private JSlider sliderSetting;
	private JSlider sliderPost;

	// Зображуємо фон на головному екрані
	private URL url = TestTv.class.getResource("/other/background.jpg");
	private BufferedImage img = null;
	{
		try {
			img = ImageIO.read(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private JPanel panel = new JPanel() {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			Image scaledImg = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			g2d.drawImage(scaledImg, 0, 0, this);
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestTv window = new TestTv();

					window.frmTesttv.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestTv() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTesttv = new JFrame();
		frmTesttv.setTitle("TestTv");
		frmTesttv = frmTesttv;
		frmTesttv.setResizable(false);
		frmTesttv.getContentPane().setLayout(null);

		frmTesttv.setContentPane(panel);
		panel.setLayout(null);

		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() { // Коди натиснута кнопка Start
			public void actionPerformed(ActionEvent arg0) {
				work = true; // Ставимо що робота почалась
				button(); // Викликаємо метод що відповідає за заблокування кнопок
				sound.play(); // Вмикаємо музику
				doRun(); // Метод початок роботи
			}
		});
		btnStart.setBounds(10, 6, 121, 31);
		btnStart.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		panel.add(btnStart);

		btnEnd = new JButton("End");
		btnEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread() { // Новий потік
					public void run() {
						try {
							work = false; // Робота припинена
							sound.stop(); // Музика вимкнена
							ch.join(); // Перевірочний стіл закінчує роботу
							button(); // Змінюємо заблоковані кнопки
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		btnEnd.setBounds(10, 37, 121, 31);
		btnEnd.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		btnEnd.setEnabled(false);
		panel.add(btnEnd);

		JLabel lblNewLabel_4 = new JLabel("Conveer speed");
		lblNewLabel_4.setBounds(148, 9, 110, 22);
		lblNewLabel_4.setForeground(new Color(240, 248, 255));
		lblNewLabel_4.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		panel.add(lblNewLabel_4);

		sliderConveer = new JSlider();
		sliderConveer.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		sliderConveer.setBounds(141, 34, 130, 26);
		sliderConveer.setValue(15);
		sliderConveer.setMinimum(10);
		sliderConveer.setMaximum(20);
		panel.add(sliderConveer);

		textFieldSet = new JTextField();
		textFieldSet.setEditable(false);
		textFieldSet.setBounds(422, 252, 30, 30);
		textFieldSet.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		textFieldSet.setColumns(10);
		panel.add(textFieldSet);

		textFieldCheck = new JTextField();
		textFieldCheck.setEditable(false);
		textFieldCheck.setBounds(776, 395, 30, 28);
		textFieldCheck.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		textFieldCheck.setColumns(10);
		panel.add(textFieldCheck);

		textFieldPost = new JTextField();
		textFieldPost.setEditable(false);
		textFieldPost.setBounds(816, 284, 30, 28);
		textFieldPost.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		textFieldPost.setColumns(10);
		panel.add(textFieldPost);

		JLabel lblNewLabel_3 = new JLabel("Post speed");
		lblNewLabel_3.setBounds(698, 264, 89, 22);
		lblNewLabel_3.setForeground(new Color(240, 248, 255));
		lblNewLabel_3.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		panel.add(lblNewLabel_3);

		JLabel lblNewLabel_2 = new JLabel("Checking speed");
		lblNewLabel_2.setBounds(646, 375, 116, 22);
		lblNewLabel_2.setForeground(new Color(240, 248, 255));
		lblNewLabel_2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_1 = new JLabel("Setting speed");
		lblNewLabel_1.setBounds(302, 232, 102, 22);
		lblNewLabel_1.setForeground(new Color(240, 248, 255));
		lblNewLabel_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		panel.add(lblNewLabel_1);

		sliderSetting = new JSlider();
		sliderSetting.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		sliderSetting.setBounds(285, 255, 130, 26);
		sliderSetting.setValue(15);
		sliderSetting.setMinimum(10);
		sliderSetting.setMaximum(20);
		panel.add(sliderSetting);

		sliderPost = new JSlider();
		sliderPost.setSize(130, 26);
		sliderPost.setLocation(676, 284);
		sliderPost.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		sliderPost.setValue(10);
		sliderPost.setMinimum(5);
		sliderPost.setMaximum(15);
		panel.add(sliderPost);

		sliderCheck = new JSlider();
		sliderCheck.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		sliderCheck.setBounds(639, 397, 130, 26);
		sliderCheck.setValue(15);
		sliderCheck.setMinimum(10);
		sliderCheck.setMaximum(20);
		panel.add(sliderCheck);

		JLabel lblMain = new JLabel("");
		lblMain.setBackground(Color.RED);
		lblMain.setBounds(873, 53, 40, 20);
		lblMain.setForeground(Color.WHITE);
		panel.add(lblMain);

		lblStart = new JLabel("");
		lblStart.setForeground(Color.RED);
		lblStart.setBounds(10, 510, 40, 20);
		panel.add(lblStart);

		lblWaitCheck = new JLabel("");
		lblWaitCheck.setForeground(Color.RED);
		lblWaitCheck.setBounds(457, 510, 30, 19);
		panel.add(lblWaitCheck);

		lblCheck = new JLabel("");
		lblCheck.setForeground(Color.RED);
		lblCheck.setBounds(552, 510, 40, 20);
		panel.add(lblCheck);

		lblSetWait_1 = new JLabel("");
		lblSetWait_1.setForeground(Color.RED);
		lblSetWait_1.setBounds(552, 160, 40, 20);
		panel.add(lblSetWait_1);

		lblSetWait = new JLabel("");
		lblSetWait.setForeground(Color.RED);
		lblSetWait.setBounds(265, 160, 40, 20);
		panel.add(lblSetWait);

		lblSetWait_2 = new JLabel("");
		lblSetWait_2.setForeground(Color.RED);
		lblSetWait_2.setBounds(125, 510, 32, 20);
		panel.add(lblSetWait_2);

		lblPostWait_1 = new JLabel("");
		lblPostWait_1.setForeground(Color.RED);
		lblPostWait_1.setBounds(905, 510, 40, 20);
		panel.add(lblPostWait_1);

		lblPostWait = new JLabel("");
		lblPostWait.setForeground(Color.RED);
		lblPostWait.setBounds(905, 345, 40, 20);
		panel.add(lblPostWait);

		lblPost = new JLabel("");
		lblPost.setForeground(Color.RED);
		lblPost.setBounds(905, 220, 40, 20);
		panel.add(lblPost);

		lblEnd = new JLabel("");
		lblEnd.setForeground(Color.RED);
		lblEnd.setBounds(905, 33, 12, 20);
		panel.add(lblEnd);

		lblSet = new JLabel("");
		lblSet.setForeground(Color.RED);
		lblSet.setBounds(125, 160, 40, 20);
		panel.add(lblSet);

		textFieldRefCheck = new JTextField();
		textFieldRefCheck.setEditable(false);
		textFieldRefCheck.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		textFieldRefCheck.setColumns(10);
		textFieldRefCheck.setBounds(475, 37, 30, 28);
		panel.add(textFieldRefCheck);
		
		textFieldAmount = new JTextField();
		textFieldAmount.setEditable(false);
		textFieldAmount.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		textFieldAmount.setColumns(10);
		textFieldAmount.setBounds(281, 34, 30, 30);
		panel.add(textFieldAmount);
		frmTesttv.setBounds(0, 0, 1088, 650);
		frmTesttv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JLabel lblNewLabel_1_1 = new JLabel("Refused monitors:");
		lblNewLabel_1_1.setForeground(new Color(240, 248, 255));
		lblNewLabel_1_1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(339, 39, 142, 22);
		panel.add(lblNewLabel_1_1);

		lblCheck_1 = new JLabel("");
		lblCheck_1.setForeground(Color.RED);
		lblCheck_1.setBounds(512, 510, 40, 20);
		panel.add(lblCheck_1);

		lblSetRef = new JLabel("");
		lblSetRef.setForeground(Color.RED);
		lblSetRef.setBounds(552, 12, 40, 20);
		panel.add(lblSetRef);

		lblPostRef = new JLabel("");
		lblPostRef.setForeground(Color.RED);
		lblPostRef.setBounds(905, 600, 40, 20);
		panel.add(lblPostRef);

		ILblStart = new LabelToGo(lblStart);
		ILblWaitCheck = new LabelToGo(lblWaitCheck);
		ILblCheck = new LabelToGo(lblCheck);
		ILblSetWait_1 = new LabelToGo(lblSetWait_1);
		ILblSetWait = new LabelToGo(lblSetWait);
		ILblSetWait_2 = new LabelToGo(lblSetWait_2);
		ILblPostWait_1 = new LabelToGo(lblPostWait_1);
		ILblPostWait = new LabelToGo(lblPostWait);
		ILblPost = new LabelToGo(lblPost);
		ILblEnd = new LabelToGo(lblEnd);
		ILblSet = new LabelToGo(lblSet);
		ILblCheck_1 = new LabelToGo(lblCheck_1);
		ILblSetRef = new LabelToGo(lblSetRef);
		ILblPostRef = new LabelToGo(lblPostRef);
	}

	private int first = 0;  // Змінна відповідає за те чи перший раз було запущено програму

	// Метод початку роботи
	protected void doRun() { 
		if (first == 1) { // Якщо програма виконується не вперше
			clear(); // Очищаємо головний екран
		}
		first = 1;
		// Створюємо лічильники
		amountCnt = new Counter(textFieldAmount);
		checkedCnt = new Counter(textFieldCheck);
		settingCnt = new Counter(textFieldSet);
		postCnt = new Counter(textFieldPost);
		refCheckCnt = new Counter(textFieldRefCheck);
		// Створюємо черги
		checkQ = new Queue(this);
		settingQ = new Queue(this);
		postQ = new Queue(this);
		// Створюємо загальну чергу
		que = new LinkedList<>();
		// Вставляємо у лічильники 0
		amountCnt.setCount(0);
		checkedCnt.setCount(0);
		settingCnt.setCount(0);
		postCnt.setCount(0);
		refCheckCnt.setCount(0);
		// Створюємо джерело моніторів
		creator = new Creator(this, sliderConveer, checkQ, amountCnt, refCheckCnt, que);
		// Створюємо стіл перевірки
		check = new Cheking(this, checkedCnt, sliderCheck, checkQ, refCheckCnt);
		// Створюємо стіл налаштування
		key = new SettingKey(this, settingCnt, sliderSetting, settingQ, refCheckCnt);
		// Створюємо стфл пакування
		post = new Post(this, postCnt, sliderPost, postQ);
		// Створюємо потоки
		cr = new Thread(creator);
		ch = new Thread(check);
		ps = new Thread(post);
		ky = new Thread(key);
		// Запуск потоків
		cr.start();
		ch.start();
		ky.start();
		ps.start();
	}

	// Метод що блокує та розблоковує кнопки
	private void button() {
		if (work) { // Якщо застосунок працює
			btnStart.setEnabled(false); // блокуємо старт
			btnEnd.setEnabled(true); // розблоковуємо енд
		} else { // Інакше
			btnStart.setEnabled(true); // розблоковуємо старт
			btnEnd.setEnabled(false); // блокуємо енд
		}

	}

	// Метод для отримання слайдера для конвеера
	public JSlider getSliderConveer() {
		return sliderConveer;
	}

	// Метод для отримання слайдера для перевірки
	public JSlider getSliderCheck() {
		return sliderConveer;
	}

	// Метод для отримання слайдера для налаштування
	public JSlider getSliderSetting() {
		return sliderSetting;
	}

	// Метод для отримання слайдера для пакування
	public JSlider getSliderPost() {
		return sliderConveer;
	}

	// Метод для отримання статусу роботи застосунку
	public boolean getWork() {
		return work;
	}

	// Метод для отримання головного фрейму
	public JFrame getFrame() {
		return frmTesttv;
	}

	// Метод для отримання панелі
	public JPanel getPanel() {
		return panel;
	}

	// Метод для отримання черги на перевірку
	public Queue getCheckQ() {
		return checkQ;
	}

	// Метод для отримання черги на налаштування
	public Queue getSettingQ() {
		return settingQ;
	}

	// Метод для отримання черги на пакування
	public Queue getPostQ() {
		return postQ;
	}

	// Метод для очищення головного екрана та черг від моніторів
	public void clear() {
		while (!que.isEmpty()) { // Поки загальна черга з моніторів не пуста
			Monitor mn = que.pop(); // Видаляємо монітори
			this.getFrame().remove(mn.getLabel());
			mn = null;
		}
		while (!checkQ.isEmpty()) { // Поки черга на пакування не пуста
			Monitor mn = checkQ.deleteMon(); // Видаляємо монітори
			this.getFrame().remove(mn.getLabel());
			mn = null;
		}
		while (!settingQ.isEmpty()) {  // Поки черга на налаштування не пуста
			Monitor mn = settingQ.deleteMon(); // Видаляємо монітори
			this.getFrame().remove(mn.getLabel());
			mn = null;
		}
		while (!postQ.isEmpty()) {  // Поки черга на пакування не пуста
			Monitor mn = postQ.deleteMon(); // Видаляємо монітори
			this.getFrame().remove(mn.getLabel());
			mn = null;
		}
		this.getFrame().remove(check.getLabel()); // Вмдаляємо напис на столі перевірки
	}

}
