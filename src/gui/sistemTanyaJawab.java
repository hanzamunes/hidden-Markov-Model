package gui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;
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
import answerProcessing.AnswerModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.*;
import org.jfree.data.general.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import org.xml.sax.SAXException;

import com.opencsv.CSVWriter;
import com.tutorialspoint.lucene.LuceneTester;

import mfcc.*;
import onlyEnergy.VAD;
import passage.QueryFunction;
import hmm.*;
import codebook.*;
import core.Utils;
import database.ObjectIODataBase;
import captureAudio.JSoundCapture;
import java.awt.Font;


public class sistemTanyaJawab extends JFrame {

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
	private JTextField textField_1;
	private Timer timer;
	static int counter = 0;
	private JTextField questionField;
	private JTextField textFieldWaktuJawaban;
	private JTextField textFieldWaktuSuara;
	private JTextField answerTextField;
	private String tempFileName;
	private static int second;
	private Timer time;
	private String tempFilePath;
	private String hasil;
	private Mfcc mfcc = new Mfcc();
	private double alpha = (double)0.9;
	private ArrayList<AnswerModel> answer;
	private String kataInput;
	private double distance_seharusnya;
	private double distance;
	private JButton btnShowSupportingPassage;
	private String output;
	/**
	 * Launch the application.
	 */
	
	class RunEvaluation extends SwingWorker<Integer,Integer>
	{
		protected Integer doInBackground() throws Exception
	    {
			output = "";
			String path = "C:\\Users\\hobert\\workspace\\data suara pertanyaan";
			File dir = new File (path);
			File[] dirList = dir.listFiles();
			output = output + "No,Pertanyaan,hasil speech recognition,hasil question Answer\n";
			int no = 0;
			for (File file:dirList)
			{
				no++;
				output = output + Integer.toString(no)+",";
				VAD vad = new VAD();
				double[] data = StdAudio.read(file.getAbsolutePath());
				vad.fileName = file.getName();
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
				output = output + file.getName().replace(".wav","").replace("_", " ").trim()+",";
				output = output + hasil+",";
				QueryFunction cek = new QueryFunction (hasil);
				if (cek.getEntity()!= "")
				{
					Utils.savePath9 = "C:\\Users\\hobert\\workspace\\percobaan suara\\percobaan gabungan\\debug\\"+hasil+".txt";
					LuceneTester tester = new LuceneTester();
					answer = tester.runSearcher(hasil,false,true,true);
					if (answer != null && !answer.isEmpty())
			    	{
			    		output = output +  answer.get(0).getAnswer();
	 		    	}
			    	else
			    	{
			    		output = output +  "NO ANSWER";
			    	}
				}
				else
				{
					output = output + "NO ANSWER";
				}
				output = output + "\n";
			}
			
			Writer out;
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\hobert\\workspace\\percobaan suara\\percobaan gabungan\\hasil.csv"), "UTF-8"));
			out.write(output);
			out.close();
			
	        return 42;
	    }

	    protected void done()
	    {
	    	System.out.println(output);
	    	JOptionPane.showMessageDialog(null, "selesai", "selesai",JOptionPane.INFORMATION_MESSAGE);
			
	    }
	}
	
	class SearchAnswer extends SwingWorker<Integer,Integer>
	{
		protected Integer doInBackground() throws Exception
	    {
			textFieldWaktuSuara.setText("");
			textFieldWaktuJawaban.setText("");
			System.out.println("masuk");
			second = 0;
			time = new Timer();
			time.schedule(new TimerTask(){
				public void run ()
				{
					second = second + 1;
				}
			}, 0, 1000);
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
			questionField.setText(hasil);
			textFieldWaktuSuara.setText(Integer.toString(second));
			System.out.println(Utils.savePath9);
			QueryFunction cek = new QueryFunction (hasil);
			if (cek.getEntity()!= "")
			{
				LuceneTester tester = new LuceneTester();
				answer = tester.runSearcher(hasil,false,true,true);
			}
	        return 42;
	    }

	    protected void done()
	    {
	    	answerTextField.setText("");
	    	if (answer != null && !answer.isEmpty())
	    	{
	    		answerTextField.setText(answer.get(0).getAnswer());
	    		btnShowSupportingPassage.setEnabled(true);
	    	}
	    	else
	    	{
	    		answerTextField.setText("NO ANSWER");
	    	}
	    	textFieldWaktuJawaban.setText(Integer.toString(second));
	    	time.cancel();
	    	time.purge();
			
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
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					sistemTanyaJawab frame = new sistemTanyaJawab();
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
	public sistemTanyaJawab() {
		setTitle("Sistem Tanya Jawab");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 554, 328);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JSoundCapture soundCapture = new JSoundCapture(true, true);
		soundCapture.setBounds(14, 7, 504, 88);
		contentPane.add(soundCapture);
		
		JButton button = new JButton("Get Answer");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				answerTextField.setText("");
				questionField.setText("");
				btnShowSupportingPassage.setEnabled(false);
				tempFileName = JSoundCapture.saveFileName+".wav";
				File tempFile = new File ("tempRecord",tempFileName);
				tempFilePath = tempFile.getAbsolutePath();
				System.out.println(tempFilePath);
				new SearchAnswer().execute();
			}
		});
		button.setBounds(64, 107, 105, 28);
		contentPane.add(button);
		
		JLabel label = new JLabel("Question");
		label.setBounds(182, 115, 55, 16);
		contentPane.add(label);
		
		questionField = new JTextField();
		questionField.setEditable(false);
		questionField.setColumns(10);
		questionField.setBounds(235, 109, 284, 28);
		contentPane.add(questionField);
		
		textFieldWaktuJawaban = new JTextField();
		textFieldWaktuJawaban.setFont(new Font("SansSerif", Font.PLAIN, 10));
		textFieldWaktuJawaban.setColumns(10);
		textFieldWaktuJawaban.setBounds(380, 147, 55, 30);
		contentPane.add(textFieldWaktuJawaban);
		
		JLabel label_1 = new JLabel("Waktu teks -> jawaban\r\n");
		label_1.setBounds(247, 149, 131, 16);
		contentPane.add(label_1);
		
		textFieldWaktuSuara = new JTextField();
		textFieldWaktuSuara.setFont(new Font("SansSerif", Font.PLAIN, 10));
		textFieldWaktuSuara.setColumns(10);
		textFieldWaktuSuara.setBounds(184, 149, 55, 28);
		contentPane.add(textFieldWaktuSuara);
		
		JLabel label_2 = new JLabel("Waktu suara -> teks");
		label_2.setBounds(64, 149, 116, 16);
		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("answer:");
		label_3.setBounds(15, 177, 55, 16);
		contentPane.add(label_3);
		
		answerTextField = new JTextField();
		answerTextField.setEditable(false);
		answerTextField.setColumns(10);
		answerTextField.setBounds(14, 193, 504, 28);
		contentPane.add(answerTextField);
		
		btnShowSupportingPassage = new JButton("Show Supporting Document");
		btnShowSupportingPassage.setEnabled(false);
		btnShowSupportingPassage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				org.w3c.dom.Document dok = null;
				try
				{
					dok = javax.xml.parsers.DocumentBuilderFactory.newInstance()
						    .newDocumentBuilder()
						    .parse(answer.get(0).getAnswerDocumentPath());
				} catch (SAXException | ParserConfigurationException e2)
				{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    final org.w3c.dom.NodeList field = dok.getElementsByTagName("field");
			    String text = field.item(2).getTextContent();
				String[] passing = new String[3];
				passing[0] = new File (answer.get(0).getAnswerDocumentPath()).getName();
				passing[1] = answer.get(0).getAnswerDocumentTitle();
				passing[2] = text;
				ShowDoc.main(passing);
				
			}
		});
		btnShowSupportingPassage.setBounds(161, 233, 192, 28);
		contentPane.add(btnShowSupportingPassage);
		
		JButton btnRunEvaluation = new JButton("Run Evaluation");
		btnRunEvaluation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new RunEvaluation().execute();
			}
		});
		btnRunEvaluation.setBounds(14, 254, 90, 28);
		contentPane.add(btnRunEvaluation);
		
		
	}
}
