package gui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import stdAudio.StdAudio;
import WavFile.WavFileException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.jfree.chart.*;
import org.jfree.data.general.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import mfcc.*;
import hmm.*;
import codebook.*;


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
	private int FEATUREDIMENSION = 39;
	/**
	 * Launch the application.
	 */
	
	class LoadData extends SwingWorker<Integer,Integer>
	{
		protected Integer doInBackground() throws Exception
	    {
			btnMulaiLatih.setEnabled(false);
	        // Do a time-consuming task.
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
			trainHmm();
	        return 42;
	    }

	    protected void done()
	    {
	    	JOptionPane.showMessageDialog(null,"Pelatihan Selesai", "Notice",JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
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
		setBounds(100, 100, 472, 246);
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
				new LoadData().execute();
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
		
		JButton btnKembaliKeMenu = new JButton("Menu Utama");
		btnKembaliKeMenu.setBounds(10, 148, 106, 26);
		contentPane.add(btnKembaliKeMenu);
		
		JButton btnPengujian = new JButton("Pengujian");
		btnPengujian.setBounds(346, 148, 98, 26);
		contentPane.add(btnPengujian);

	}
	
	public void generateCodebook()
	{
		ProgressMonitor1 pbar = new ProgressMonitor1(wordList.size()+1,"Generating Codebook");
		allFeatureList = new ArrayList<double[]>();
		allFeatureVector = new ArrayList<ArrayList<double[][]>>();
		double alpha = (double)0.9;
		Mfcc mfcc = new Mfcc();
		int totalFrames=0;
		for (int i=0;i<wordList.size();i++)
		{
			ArrayList<double[][]> tes = new ArrayList<double[][]>();
			for (int j=0;j<waveFileList.get(i).size();j++)
			{
				double[][] result = mfcc.GetFeatureVector(waveFileList.get(i).get(j), alpha, 400, 160);
				tes.add(result);
				for (int k=0;k<result.length;k++)
				{
					allFeatureList.add(result[k]);
					totalFrames++;
				}
				/*System.out.println ("feature Vector hasil prosesnya = ");
				for (int x=0;x<result.length;x++)
				{
					for (int y=0;y<result[x].length;y++)
					{
						System.out.print (result[x][y]+" ");
					}
					System.out.println();
				}*/
				
			}
			allFeatureVector.add(tes);
			pbar.counter=i+1;
		}
		double allFeatures[][] = new double[totalFrames][FEATUREDIMENSION];
		for (int i = 0; i < totalFrames; i++) {
			double[] tmp = allFeatureList.get(i);
			allFeatures[i] = tmp;
		}
		Points pts[] = new Points[totalFrames];
		for (int j = 0; j < totalFrames; j++) {
			pts[j] = new Points(allFeatures[j]);
		}
		Codebook cbk = new Codebook(pts);
		cbk.saveToFile();
		pbar.counter++;
		pbar = null;
	}
	
	private Points[] getPointsFromFeatureVector (double[][] features)
	{
		Points[] pts = new Points[features.length];
		for (int j=0;j<features.length;j++)
		{
			pts[j] = new Points (features[j]);
		}
		return pts;
	}
	
	public void trainHmm()
	{
		ProgressMonitor2 pbar1 = new ProgressMonitor2(wordList.size(),"Train HMM");
		Codebook cb = new Codebook();
		for (int i=0;i<wordList.size();i++)
		{
			int[][] quantized = new int[waveFileList.get(i).size()][];
			for (int j=0;j<waveFileList.get(i).size();j++)
			{
				Points[] pts = getPointsFromFeatureVector(allFeatureVector.get(i).get(j));
				quantized[j] = cb.quantize(pts);
			}
			HiddenMarkov mkv = new HiddenMarkov(2,256);
			mkv.setTrainSeq(quantized);
			mkv.train();
			mkv.save(wordList.get(i));
			pbar1.counter=i+1;
			
		}
		pbar1 = null;
	}
}
