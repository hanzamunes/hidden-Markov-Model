package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class ShowDoc
{

	private JFrame frmDoktxt;
	private JTextField txtTitleBox;

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
					String dok = args[0];
					String title = args[1];
					String text = args[2];
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					ShowDoc window = new ShowDoc(dok,title,text);
					window.frmDoktxt.setVisible(true);
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
	public ShowDoc(String dok, String title, String text)
	{
		initialize(dok,title,text);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String dok,String title,String text)
	{
		frmDoktxt = new JFrame();
		frmDoktxt.setTitle(dok);
		frmDoktxt.setBounds(100, 100, 691, 430);
		frmDoktxt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmDoktxt.getContentPane().setLayout(null);
		
		JLabel lblTitle = new JLabel("Title :");
		lblTitle.setBounds(10, 8, 46, 14);
		frmDoktxt.getContentPane().add(lblTitle);
		
		txtTitleBox = new JTextField();
		txtTitleBox.setEnabled(false);
		txtTitleBox.setBounds(63, 5, 602, 28);
		frmDoktxt.getContentPane().add(txtTitleBox);
		txtTitleBox.setColumns(10);
		txtTitleBox.setText(title);
		
		JLabel lblText = new JLabel("Text  :");
		lblText.setBounds(10, 41, 46, 14);
		frmDoktxt.getContentPane().add(lblText);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 66, 645, 314);
		frmDoktxt.getContentPane().add(scrollPane);
		
		JTextArea txtContent = new JTextArea();
		scrollPane.setViewportView(txtContent);
		txtContent.setEditable(false);
		txtContent.setWrapStyleWord(true);
		txtContent.setLineWrap(true);
		txtContent.setText(text);
	}
}
