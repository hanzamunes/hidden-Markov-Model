package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JButton;

import stdAudio.StdAudio;
import WavFile.WavFileException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.*;
import org.jfree.data.general.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import mfcc.*;
import hmm.*;





public class Pelatihan extends JFrame {

	private ArrayList<ArrayList<String>> wordFileList;
	private ArrayList<ArrayList<double[]>> waveFileList;
	private ArrayList<String> wordList;
	private ArrayList<double[]> allFeatureList;
	private ArrayList<ArrayList<double[][]>> allFeatureVector;
	private String trainFilePath;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnMulaiLatih;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pelatihan frame = new Pelatihan();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Pelatihan() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 472, 412);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Input Suara Latih");
		lblNewLabel.setBounds(10, 11, 106, 14);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 29, 332, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser j = new JFileChooser();
				j.setCurrentDirectory(new java.io.File("."));
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				j.setAcceptAllFileFilterUsed(false);
				 
		        int rVal = j.showOpenDialog(null);
		        if (rVal == JFileChooser.APPROVE_OPTION) {
		          textField.setText(j.getSelectedFile().toString());
		          trainFilePath = j.getSelectedFile().toString();
		        }
				
			}
		});
		btnBrowse.setBounds(357, 28, 89, 23);
		contentPane.add(btnBrowse);
		
		btnMulaiLatih = new JButton("Mulai Latih");
		btnMulaiLatih.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				wordList = new ArrayList<String>();
				wordFileList = new ArrayList<ArrayList<String>>(); 
				wordFileList.clear();
				
				waveFileList = new ArrayList<ArrayList<double[]>> ();
				waveFileList.clear();
				
				File directory = new File(trainFilePath);
				File[] fList = directory.listFiles();
				for (File file : fList)
				{
					if (file.isDirectory())
					{
						ArrayList<String> temp1 =  new ArrayList<String>();
						ArrayList<double[]> temp = new ArrayList<double[]>();
						wordList.add(file.getName());
						File[] fList1 = file.listFiles();
						for (File dalam : fList1)
						{
							
							if (dalam.isFile())
							{
								temp.add (StdAudio.read(dalam.getAbsolutePath()));
								temp1.add(dalam.getName());
							}
						}
						//System.out.println ("temp1 = ");
						//System.out.println (temp1);
						waveFileList.add(temp);
						wordFileList.add(temp1);
					}
				}
				generateCodebook();
				/*XYSeries suara = new XYSeries (wordFileList.get(0).get(0));
				for (int i=0;i<waveFileList.get(0).get(0).length;i++)
				{
					suara.add((double)(i+1), waveFileList.get(0).get(0)[i]);
				}
				XYSeriesCollection dataset = new XYSeriesCollection();
				dataset.addSeries(suara);
				XyChart.dataSet = dataset;
				XyChart chart = new XyChart("Suara","tes");
				chart.pack( );          
			      RefineryUtilities.centerFrameOnScreen( chart );          
			      chart.setVisible( true ); */
				/*File tes = new File("C:\\Users\\hobert\\workspace\\Hidden Markov Model\\speechTrainWav\\aku\\sample_1_aku.wav");
				try {
					LoadAudio.loadAudioFromFile(tes);
				} catch (IOException | WavFileException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
			} 	
		});
		btnMulaiLatih.setBounds(10, 65, 436, 50);
		contentPane.add(btnMulaiLatih);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 148, 434, 23);
		contentPane.add(progressBar);
		
		JLabel lblCodebook = new JLabel("Codebook");
		lblCodebook.setBounds(10, 127, 68, 16);
		contentPane.add(lblCodebook);
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setStringPainted(true);
		progressBar_1.setBounds(10, 221, 434, 23);
		contentPane.add(progressBar_1);
		
		JLabel lblHmm = new JLabel("HMM");
		lblHmm.setBounds(12, 198, 55, 16);
		contentPane.add(lblHmm);

	}
	
	public void generateCodebook()
	{
		allFeatureVector = new ArrayList<ArrayList<double[][]>>();
		double alpha = (double)0.9;
		int totalFrames = 0;
		Mfcc mfcc = new Mfcc();
		for (int i=0;i<wordList.size();i++)
		{
			ArrayList<double[][]> tes = new ArrayList<double[][]>();
			for (int j=0;j<waveFileList.get(i).size();j++)
			{
				double[][] result = mfcc.GetFeatureVector(waveFileList.get(i).get(j), alpha, 400, 160);
				System.out.println ("masuk1");
				for (int x=0;x<result.length;x++)
				{
					for (int y=0;y<result[x].length;y++)
					{
						System.out.print (result[x][y]+" ");
					}
					System.out.println();
				}
				if (i==0 && j==0)
				{
					while (true)
					{
					}
				}
			}
		}
	}
}
