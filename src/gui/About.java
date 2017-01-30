package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class About
{

	private JFrame frmAbout;

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
					UIManager.setLookAndFeel(
				            UIManager.getCrossPlatformLookAndFeelClassName());
					About window = new About();
					window.frmAbout.setVisible(true);
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
	public About()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmAbout = new JFrame();
		frmAbout.setTitle("About");
		frmAbout.setBounds(100, 100, 644, 413);
		frmAbout.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		BufferedImage bf = null;
		try {
			bf = ImageIO.read(new File("source\\foto.JPG"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JLabel foto = new JLabel();
		foto.setBounds(33, 71, 246, 243);
		Image resizedImage = bf.getScaledInstance(foto.getWidth(), foto.getHeight(), 1);
		frmAbout.getContentPane().setLayout(null);
		foto.setIcon(new ImageIcon (resizedImage));
		
		frmAbout.getContentPane().add(foto);
		
		JTextArea txtrSistemTanyaJawab = new JTextArea();
		txtrSistemTanyaJawab.setBackground(UIManager.getColor("ArrowButton.disabled"));
		txtrSistemTanyaJawab.setFont(new Font("Monospaced", Font.PLAIN, 20));
		txtrSistemTanyaJawab.setWrapStyleWord(true);
		txtrSistemTanyaJawab.setLineWrap(true);
		txtrSistemTanyaJawab.setEditable(false);
		txtrSistemTanyaJawab.setText("Sistem Tanya Jawab dengan HMM Speech Recognition");
		txtrSistemTanyaJawab.setBounds(339, 41, 246, 85);
		txtrSistemTanyaJawab.setBackground(new Color (240,240,240,240));
		frmAbout.getContentPane().add(txtrSistemTanyaJawab);
		
		JLabel lblDibuatOleh = new JLabel("                     Dibuat Oleh");
		lblDibuatOleh.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDibuatOleh.setBounds(339, 152, 246, 63);
		frmAbout.getContentPane().add(lblDibuatOleh);
		
		JTextArea txtrHobert = new JTextArea();
		txtrHobert.setBackground(UIManager.getColor("ArrowButton.background"));
		txtrHobert.setEditable(false);
		txtrHobert.setFont(new Font("Monospaced", Font.PLAIN, 16));
		txtrHobert.setText("\t  Hobert\r\n\t535130027");
		txtrHobert.setBounds(326, 207, 246, 73);
		txtrHobert.setBackground(new Color (240,240,240,240));
		frmAbout.getContentPane().add(txtrHobert);
		
	}
}
