package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Utama extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Utama frame = new Utama();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public Utama() throws IOException {
		setResizable(false);
		BufferedImage bf = ImageIO.read(new File("source\\background.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 565, 343);
		BackgroundPanel panel = new BackgroundPanel(bf);
		setContentPane(panel);
		panel.setLayout(null);
		
		BackgroundPanel panel_1 = new BackgroundPanel(new Color(135, 206, 235));
		panel_1.setBounds(12, 12, 535, 67);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JTextArea txtrPengenalanSuaraKe = new JTextArea();
		txtrPengenalanSuaraKe.setBounds(0, 0, 535, 67);
		panel_1.add(txtrPengenalanSuaraKe);
		txtrPengenalanSuaraKe.setEditable(false);
		txtrPengenalanSuaraKe.setWrapStyleWord(true);
		txtrPengenalanSuaraKe.setLineWrap(true);
		txtrPengenalanSuaraKe.setOpaque(false);
		txtrPengenalanSuaraKe.setFocusable(false);
		txtrPengenalanSuaraKe.setForeground(new Color(153, 0, 0));
		txtrPengenalanSuaraKe.setBackground(new Color(240, 248, 255));
		txtrPengenalanSuaraKe.setFont(new Font("Dialog", Font.PLAIN, 22));
		txtrPengenalanSuaraKe.setText("PENGENALAN SUARA KE TEKS MENGGUNAKAN METODE HIDDEN MARKOV MODEL");
		
		JButton btnNewButton = new JButton("Pelatihan");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Pelatihan().setVisible(true);
				btnNewButton.setEnabled(false);
			}
		});
		btnNewButton.setBounds(234, 111, 114, 42);
		panel.add(btnNewButton);
		
		JButton btnPengujian = new JButton("Pengujian");
		btnPengujian.setBounds(234, 165, 114, 41);
		panel.add(btnPengujian);
		
		JButton btnBantuan = new JButton("Bantuan");
		btnBantuan.setBounds(234, 218, 114, 37);
		panel.add(btnBantuan);
		
		JButton btnTentangPembuat = new JButton("Tentang Pembuat");
		btnTentangPembuat.setBounds(394, 276, 153, 26);
		panel.add(btnTentangPembuat);
	}
}
