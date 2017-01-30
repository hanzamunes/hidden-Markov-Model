package gui;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGUI
{

	private JFrame frmMainProgram;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					MainGUI window = new MainGUI();
					window.frmMainProgram.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmMainProgram = new JFrame();
		frmMainProgram.setTitle("Main Program");
		frmMainProgram.setBounds(100, 100, 792, 431);
		BufferedImage bf = null;
		try {
			bf = ImageIO.read(new File("source\\bg.jpg"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frmMainProgram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BackgroundPanel panel = new BackgroundPanel(bf);
		frmMainProgram.setContentPane(panel);
		panel.setLayout(null);
		
		JTextArea txtrSistemTanyaJawab = new JTextArea();
		txtrSistemTanyaJawab.setBackground(new Color(255, 250, 250));
		txtrSistemTanyaJawab.setEditable(false);
		txtrSistemTanyaJawab.setLineWrap(true);
		txtrSistemTanyaJawab.setWrapStyleWord(true);
		txtrSistemTanyaJawab.setFont(new Font("Monospaced", Font.PLAIN, 23));
		txtrSistemTanyaJawab.setText("\t         Sistem Tanya Jawab\r\n\t\t       dengan\r\n\t       HMM Speech Recognition");
		txtrSistemTanyaJawab.setBounds(6, 6, 764, 105);
		panel.add(txtrSistemTanyaJawab);
		
		JButton btnSistemPengenalanSuara = new JButton("Sistem Pengenalan Suara");
		btnSistemPengenalanSuara.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Utama.main(null);
			}
		});
		btnSistemPengenalanSuara.setBounds(300, 132, 185, 65);
		panel.add(btnSistemPengenalanSuara);
		
		JButton btnSistemTanyaJawab = new JButton("Sistem Tanya Jawab");
		btnSistemTanyaJawab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuestionAnswering.main(null);
			}
		});
		btnSistemTanyaJawab.setBounds(299, 222, 185, 65);
		panel.add(btnSistemTanyaJawab);
		
		JButton btnSistemGabungan = new JButton("Sistem Gabungan");
		btnSistemGabungan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sistemTanyaJawab.main(null);
			}
		});
		btnSistemGabungan.setBounds(300, 310, 185, 65);
		panel.add(btnSistemGabungan);
		
		JButton btnAbout = new JButton("About");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				About.main(null);
				//frame.dispose();
			}
		});
		btnAbout.setBounds(680, 358, 90, 28);
		panel.add(btnAbout);
		
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					File ch = new File ("help.chm");
					Runtime.getRuntime().exec("hh.exe "+ch.getAbsolutePath());
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnHelp.setBounds(680, 328, 90, 28);
		panel.add(btnHelp);
	}
}
