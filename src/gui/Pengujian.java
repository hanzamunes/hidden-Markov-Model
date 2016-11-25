package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import codebook.*;
import hmm.*;
import database.*;
import mfcc.Mfcc;
import stdAudio.StdAudio;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import onlyEnergy.*;
import captureAudio.JSoundCapture;

public class Pengujian extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private String filePath;
	private String filePath2;
	private double alpha = (double)0.9;
	private String hasil;
	private Mfcc mfcc = new Mfcc();
	private JTextField textField_2;
	private int persen;
	private JButton btnRunTest;
	private String filePath1;
	private JTextField textField_4;
	private JTextField textField_3;
	private JButton btnRunTest_1;
	private String outputCSV;
	private double distance;
	private String kataInput;
	private double distance_seharusnya;
	private JTextField textField_5;
	private JTextField textField_6;
	private String tempFilePath;
	public static JButton btnRecognize;
	private String tempFileName;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					Pengujian frame = new Pengujian();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	class TesAkurasi extends SwingWorker<Integer,Integer>
	{
		protected Integer doInBackground() throws Exception
	    {
			outputCSV="nama file uji,hasil pengenalan,verdict,distance,distance dengan kata yang seharusnya,selisih\n";
			int sama=0;
			File directory = new File(filePath2);
			File[] fList = directory.listFiles();
			Random r = new Random();
			ProgressMonitor3 pbar = new ProgressMonitor3(100,"Testing Data");
			for (int i=0;i<100;i++)
			{
				int index = r.nextInt(fList.length);
				String namaFile = fList[index].getName();
				String[] temp = namaFile.split("_");
				outputCSV = outputCSV + namaFile+",";
				String namaKata = temp[temp.length-1].replace(".wav", "");
				kataInput = namaKata;
				double[] data = StdAudio.read(fList[index].getAbsolutePath());
				double[][] result = mfcc.GetFeatureVector(data, alpha, util.Constant.FRAME_LENGTH, util.Constant.FRAME_OVERLAP);
				//double[][] result = mfcc.GetFeatureVector(data, alpha, 1024);
				String temp1 = hmmGetWord(result);
				outputCSV = outputCSV + temp1+",";
				if (temp1.equals(namaKata))
				{
					sama++;
					outputCSV = outputCSV + "Pass"+",";
				}
				else
				{
					outputCSV = outputCSV + "Fail"+",";
				}
				outputCSV = outputCSV + String.valueOf(distance)+",";
				outputCSV = outputCSV + String.valueOf(distance_seharusnya)+",";
				outputCSV = outputCSV + String.valueOf(Math.abs (distance - distance_seharusnya)+"\n");
				pbar.counter = i+1;
			}
			//System.out.println (sama);
			persen = 0;
			persen = (int)(((double)sama/100)*100);
			String namafile = util.Constant.outputFile1;
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(namafile), "UTF-8"));
			out.write(outputCSV);
			out.close();
	        
	        return 42;
	    }

	    protected void done()
	    {
	    	textField_2.setText("");
			textField_2.setText(persen+"%");
			btnRunTest.setEnabled(true);
	    }
	}
	
	class TesAkurasiCustom extends SwingWorker<Integer,Integer>
	{
		protected Integer doInBackground() throws Exception
	    {
			outputCSV="nama file uji,hasil pengenalan,verdict,distance,distance dengan kata yang seharusnya,selisih\n";
			int sama=0;
			File directory = new File(filePath1);
			File[] fList = directory.listFiles();
			Random r = new Random();
			ProgressMonitor3 pbar = new ProgressMonitor3(fList.length,"Testing Data");
			for (int i=0;i<fList.length;i++)
			{
				String namaFile = fList[i].getName();
				String[] temp = namaFile.split("_");
				outputCSV = outputCSV + namaFile+",";
				String namaKata = temp[temp.length-1].replace(".wav", "");
				kataInput = namaKata;
				double[] data = StdAudio.read(fList[i].getAbsolutePath());
				double[][] result = mfcc.GetFeatureVector(data, alpha, util.Constant.FRAME_LENGTH, util.Constant.FRAME_OVERLAP);
				//double[][] result = mfcc.GetFeatureVector(data, alpha, 20);
				String temp1 = hmmGetWord(result);
				outputCSV = outputCSV + temp1+",";
				if (temp1.equals(namaKata))
				{
					sama++;
					outputCSV = outputCSV + "Pass"+",";
				}
				else
				{
					outputCSV = outputCSV + "Fail"+",";
				}
				outputCSV = outputCSV + String.valueOf(distance)+",";
				outputCSV = outputCSV + String.valueOf(distance_seharusnya)+",";
				outputCSV = outputCSV + String.valueOf(Math.abs (distance - distance_seharusnya)+"\n");
				pbar.counter = i+1;
			}
			//System.out.println (sama);
			persen = 0;
			persen = (int)(((double)sama/fList.length)*100);
			String namafile = util.Constant.outputFile2;
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(namafile), "UTF-8"));
			out.write(outputCSV);
			out.close();
	        return 42;
	    }

	    protected void done()
	    {
	    	
			textField_3.setText("");
			textField_3.setText(persen+"%");
			btnRunTest_1.setEnabled(true);
	    }
	}
	
	class LoadAudioRecorded extends SwingWorker<Integer,Integer>
	{
		protected Integer doInBackground() throws Exception
	    {
			System.out.println("masuk");
			VAD vad = new VAD();
			double[] data = StdAudio.read(tempFilePath);
			vad.fileName = tempFileName;
			boolean[] speech = vad.computeVAD(data, 160);
			//double[] cutsilent = vad.cutSilent(data, speech, 160);
			//StdAudio.save("hasilGabung.wav", cutsilent);
			double[][] potong = vad.splitWord(data, speech, 160);
			hasil = "";
			for (int i=0;i<potong.length;i++)
			{
				double[][] result = mfcc.GetFeatureVector(potong[i], alpha, util.Constant.FRAME_LENGTH, util.Constant.FRAME_OVERLAP);
				hasil = hasil + hmmGetWord(result);
				if (i != potong.length-1)
				{
					hasil = hasil + " ";
				}
			}
	        return 42;
	    }

	    protected void done()
	    {
	    	textField_6.setText("");
			textField_6.setText(hasil);
	    }
	}
	
	class LoadAudio extends SwingWorker<Integer,Integer>
	{
		protected Integer doInBackground() throws Exception
	    {
			VAD vad = new VAD();
			double[] data = StdAudio.read(filePath);
			//PrintStream out = new PrintStream(new FileOutputStream ("outputVad.txt"));
			//System.setOut(out);
			//boolean[] speech = vad.computeVAD(data, 160);
			//double[] cutsilent = vad.cutSilent(data, speech, 160);
			//StdAudio.save("hasilGabung.wav", cutsilent);
			//double[][] potong = vad.splitWord(data, speech, 160);
			/*if (vad.splitted)
			{
				hasil = "";
				for (int i=0;i<potong.length;i++)
				{
					String nama = "hasil potong "+i+".wav";
					StdAudio.save(nama,potong[i]);
					double[][] result = mfcc.GetFeatureVector(potong[i], alpha, 400, 160);
					hasil = hasil + hmmGetWord(result)+" ";
				}
			}
			else
			{*/
				System.out.println ("ga masuk split");
				double[][] result = mfcc.GetFeatureVector(data, alpha, util.Constant.FRAME_LENGTH, util.Constant.FRAME_OVERLAP);
				//double[][] result = mfcc.GetFeatureVector(data, alpha, 1024);
				hasil = "";
				hasil = hmmGetWord(result);
			//}
	        return 42;
	    }

	    protected void done()
	    {
	    	textField_1.setText("");
			textField_1.setText(hasil);
	    }
	}
	
	/**
	 * 
	 * @param features
	 * @return
	 */
	private Points[] getPointsFromFeatureVector(double[][] features) {
		// get Points object from all feature vector
		Points pts[] = new Points[features.length];
		for (int j = 0; j < features.length; j++) {
			pts[j] = new Points(features[j]);
		}
		return pts;
	}
	
	public String hmmGetWord(double[][] feature) {
		Points[] pts = getPointsFromFeatureVector(feature);
		Codebook cb = new Codebook();
		// quantize using Codebook
		int quantized[] = cb.quantize(pts);

		// read registered/trained words
		ObjectIODataBase db = new ObjectIODataBase();
		db.setType("hmm");
		String[] words = db.readRegistered();
		db = null;
		//System.out.println("registred words ::: count : " + words.length);
		HiddenMarkov[] hmmModels = new HiddenMarkov[words.length];


		// read hmmModels
		for (int i = 0; i < words.length; i++) {
			hmmModels[i] = new HiddenMarkov(words[i]);
		}

		// find the likelihood by viterbi decoding of quantized sequence
		double likelihoods[] = new double[words.length];
		for (int j = 0; j < words.length; j++) {
			likelihoods[j] = hmmModels[j].viterbi(quantized);
			//System.out.println("Likelihood with " + words[j] + " is " + likelihoods[j]);
		}
		
		// find the largest likelihood
		double highest = Double.NEGATIVE_INFINITY;
		int wordIndex = -1;
		for (int j = 0; j < words.length; j++) {
			if (likelihoods[j] > highest) {
				highest = likelihoods[j];
				wordIndex = j;
			}
			if (words[j].equals(kataInput))
			{
				distance_seharusnya = likelihoods[j];
			}
		}
		distance = highest;
		//System.out.println("Best matched word " + words[wordIndex]);
		return words[wordIndex];
	}

	/**
	 * Create the frame.
	 */
	public Pengujian() {
		setTitle("Pengujian");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 569, 343);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 533, 246);
		tabbedPane.add("Persentasi Keakuratan",panel2);
		
		JLabel lblPersentasiKeakuratan = new JLabel("Persentasi Keakuratan Program (100 Data Uji Random)");
		lblPersentasiKeakuratan.setBounds(12, 12, 386, 16);
		panel2.add(lblPersentasiKeakuratan);
		
		btnRunTest = new JButton("Run Test");
		btnRunTest.setEnabled(false);
		btnRunTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_2.setText("");
				btnRunTest.setEnabled(false);
				new TesAkurasi().execute();
			}
		});
		btnRunTest.setBounds(213, 79, 98, 26);
		panel2.add(btnRunTest);
		
		JLabel lblPersentasiKeakuratan_1 = new JLabel("Persentasi Keakuratan :");
		lblPersentasiKeakuratan_1.setBounds(12, 117, 149, 16);
		panel2.add(lblPersentasiKeakuratan_1);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(151, 115, 49, 20);
		panel2.add(textField_2);
		textField_2.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(12, 34, 386, 20);
		panel2.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnBrowse_1 = new JButton("Browse");
		btnBrowse_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser j = new JFileChooser();
				j.setCurrentDirectory(new java.io.File("."));
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				j.setAcceptAllFileFilterUsed(false);
				 
		        int rVal = j.showOpenDialog(null);
		        if (rVal == JFileChooser.APPROVE_OPTION) {
		          textField_5.setText(j.getSelectedFile().toString());
		          filePath2 = j.getSelectedFile().toString();
		          btnRunTest.setEnabled(true);
		        }
			}
		});
		btnBrowse_1.setBounds(410, 31, 98, 26);
		panel2.add(btnBrowse_1);
		contentPane.add(tabbedPane);
		
		JButton btnMenuUtama = new JButton("Menu Utama");
		btnMenuUtama.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Utama().setVisible(true);
				dispose();
			}
		});
		btnMenuUtama.setBounds(10, 276, 124, 26);
		contentPane.add(btnMenuUtama);
		
		JButton btnPelatihan = new JButton("Pelatihan");
		btnPelatihan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Pelatihan().setVisible(true);
				dispose();
			}
		});
		btnPelatihan.setBounds(445, 276, 98, 26);
		contentPane.add(btnPelatihan);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		tabbedPane.add("Pengujian Data",panel1);
		
		JLabel lblPengujianDenganData = new JLabel("Pengujian Dengan Data Tes");
		lblPengujianDenganData.setBounds(10, 11, 226, 14);
		panel1.add(lblPengujianDenganData);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(10, 36, 405, 20);
		panel1.add(textField);
		textField.setColumns(10);
		
		JButton btnPengenalan = new JButton("Pengenalan");
		btnPengenalan.setEnabled(false);
		btnPengenalan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new LoadAudio().execute();
			}
		});
		btnPengenalan.setBounds(10, 68, 506, 26);
		panel1.add(btnPengenalan);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser j = new JFileChooser();
				j.setCurrentDirectory(new java.io.File("."));
				j.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter ("WAV Files","wav");
				j.setFileFilter(filter);
				j.setAcceptAllFileFilterUsed(false);
				int rVal = j.showOpenDialog(null);
		        if (rVal == JFileChooser.APPROVE_OPTION) {
		          textField.setText(j.getSelectedFile().toString());
		          filePath = j.getSelectedFile().toString();
		          btnPengenalan.setEnabled(true);
		        }
			}
		});
		btnBrowse.setBounds(427, 35, 89, 23);
		panel1.add(btnBrowse);
		
		
		
		JLabel lblKataDariSuara = new JLabel("Kata Dari Suara Yang Diinput Adalah:");
		lblKataDariSuara.setBounds(144, 115, 226, 16);
		panel1.add(lblKataDariSuara);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(12, 143, 504, 20);
		panel1.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnDummy = new JButton("dummy");
		btnDummy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrintStream out = null;
				try {
					out = new PrintStream(new FileOutputStream("hasil.txt"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.setOut(out);
				double[] data = StdAudio.read(filePath);
				System.out.println ("nama file = "+filePath);
				double[][] result = mfcc.GetFeatureVector(data, alpha, util.Constant.FRAME_LENGTH, util.Constant.FRAME_OVERLAP);
				//double[][] result = mfcc.GetFeatureVector(data, alpha, 1024);
				/*for (int i=0;i<result.length;i++)
				{
					for (int j=0;j<result[i].length;j++)
					{
						System.out.print (result[i][j]+" ");
					}
					System.out.println ();
				}*/
				System.out.println ("masuk");
			}
		});
		btnDummy.setBounds(388, 5, 98, 26);
		panel1.add(btnDummy);
		btnDummy.setVisible(false);
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(null);
		tabbedPane.add("Record Data (LIVE)",panel3);
		
		JSoundCapture soundCapture = new JSoundCapture(true, true);
		soundCapture.setBounds(12, 12, 504, 88);
		panel3.add(soundCapture);
		
		btnRecognize = new JButton("Recognize");
		btnRecognize.setEnabled(false);
		btnRecognize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tempFileName = JSoundCapture.saveFileName+".wav";
				File tempFile = new File ("tempRecord",tempFileName);
				tempFilePath = tempFile.getAbsolutePath();
				System.out.println(tempFilePath);
				new LoadAudioRecorded().execute();
			}
		});
		btnRecognize.setBounds(215, 112, 110, 28);
		panel3.add(btnRecognize);
		
		textField_6 = new JTextField();
		textField_6.setEditable(false);
		textField_6.setBounds(12, 147, 504, 28);
		panel3.add(textField_6);
		textField_6.setColumns(10);
		
		JPanel panel4 = new JPanel();
		panel4.setLayout(null);
		tabbedPane.add("Persentage error (custom)", panel4);
		
		JButton button = new JButton("Browse");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser j = new JFileChooser();
				j.setCurrentDirectory(new java.io.File("."));
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				j.setAcceptAllFileFilterUsed(false);
				 
		        int rVal = j.showOpenDialog(null);
		        if (rVal == JFileChooser.APPROVE_OPTION) {
		          textField_4.setText(j.getSelectedFile().toString());
		          filePath1 = j.getSelectedFile().toString();
		          btnRunTest_1.setEnabled(true);
		        }
			}
		});
		button.setBounds(429, 12, 89, 23);
		panel4.add(button);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		textField_4.setBounds(12, 13, 405, 20);
		panel4.add(textField_4);
		
		btnRunTest_1 = new JButton("Run Test");
		btnRunTest_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField_3.setText("");
				btnRunTest_1.setEnabled(false);
				new TesAkurasiCustom().execute();
				
			}
		});
		btnRunTest_1.setEnabled(false);
		btnRunTest_1.setBounds(12, 45, 506, 26);
		panel4.add(btnRunTest_1);
		
		JLabel label = new JLabel("Persentasi Keakuratan :");
		label.setBounds(12, 97, 149, 16);
		panel4.add(label);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		textField_3.setBounds(151, 95, 49, 20);
		panel4.add(textField_3);
	}
}
