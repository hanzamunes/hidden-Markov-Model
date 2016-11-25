package onlyEnergy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import gui.XyChart;
import stdAudio.StdAudio;


public class VAD {
	
	public static boolean splitted;
	public static String fileName;
	static Writer write;
	
	
	public double[][] splitWord(double[] data, boolean[] speech, int frameSize)
	{
		Map<Integer,Integer> blok = new HashMap <Integer,Integer>();
	    int start=0,end=0;
	    while (start < speech.length)
	    {
	    	if (speech[start])
	    	{
	    		boolean cek=true;
	    		for (int i=start+1;i<speech.length;i++)
	    		{
	    			if (!speech[i])
	    			{
	    				end = i-1;
	    				cek = false;
	    				break;
	    			}
	    		}
	    		if (cek)
	    		{
	    			end = speech.length-1;
	    		}
	    		blok.put(start, end);
	    		start = end+1;
	    	}
	    	else
	    	{
	    		start++;
	    	}
	    }
		/*try {
			write.write("distance\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    Map <Integer,Integer> blok1 = new TreeMap<Integer,Integer>(blok);
	    ArrayList<ArrayList<Double>> temp1 = new ArrayList<ArrayList<Double>>();
	    int count = 0;
	    for(Entry<Integer, Integer> entry : blok1.entrySet()) {
	    	int awal = entry.getKey();
	    	int akhir = entry.getValue();
	    	int selisih = Math.abs (awal-akhir);
	    	/*try {
				write.write(awal+" "+akhir+" selisih = "+selisih+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    	if (selisih >20)
	    	{
	    		count++;
	    		ArrayList<Double> temp = new ArrayList<Double>();
	    		for (int i=awal;i<=akhir;i++)
	    		{
    				for (int j=(i*frameSize)+1;j<=(i+1)*frameSize;j++)
    				{
    					if (j < data.length)
    					{
    						temp.add(data[j]);
    					}
    				}
	    		}
	    		temp1.add(temp);
	    	}
	    }
	    if (count ==0)
		{
			this.splitted = false;
		}
		else
		{
			this.splitted = true;
		}
	    double[][] result = new double[temp1.size()][];
	    for (int i=0;i<temp1.size();i++)
	    {
	    	result[i] = new double[temp1.get(i).size()];
	    	for (int j=0;j<temp1.get(i).size();j++)
	    	{
	    		result[i][j] = temp1.get(i).get(j);
	    	}
	    }
	    return result;
	}
	
	public double[] cutSilent(double[] data, boolean[] speech,int frameSize)
	{
		ArrayList<Double> temp = new ArrayList<Double>();
		Map<Integer,Integer> blok = new HashMap <Integer,Integer>();
	    int start=0,end=0;
	    while (start < speech.length)
	    {
	    	if (speech[start])
	    	{
	    		boolean cek=true;
	    		for (int i=start+1;i<speech.length;i++)
	    		{
	    			if (!speech[i])
	    			{
	    				end = i-1;
	    				cek = false;
	    				break;
	    			}
	    		}
	    		if (cek)
	    		{
	    			end = speech.length-1;
	    		}
	    		blok.put(start, end);
	    		start = end+1;
	    	}
	    	else
	    	{
	    		start++;
	    	}
	    }
		System.out.println ("distance");
	    Map <Integer,Integer> blok1 = new TreeMap<Integer,Integer>(blok);
	    int count = 0;
	    for(Entry<Integer, Integer> entry : blok1.entrySet()) {
	    	int awal = entry.getKey();
	    	int akhir = entry.getValue();
	    	int selisih = Math.abs (awal-akhir);
	    	System.out.println (awal+" "+akhir+" selisih = "+selisih);
	    	if (selisih >20)
	    	{
	    		count++;
	    		for (int i=awal;i<=akhir;i++)
	    		{
    				for (int j=(i*frameSize)+1;j<=(i+1)*frameSize;j++)
    				{
    					if (j < data.length)
    					{
    						temp.add(data[j]);
    					}
    				}
	    		}
	    	}
	    }
	    if (count ==0)
		{
			this.splitted = false;
		}
		else
		{
			this.splitted = true;
		}
		double[] result = new double[temp.size()];
		for (int i=0;i<temp.size();i++)
		{
			result[i] = temp.get(i);
		}
		return result;
	}
	
	public boolean[] computeVAD (double[] data, int frameSize)
	{
		int totalFrame = (int)Math.ceil((double)data.length/(double)frameSize);
		RMSEnergy en = new RMSEnergy();
		double[] energy = en.CalcEnergy(data, frameSize);
		double threshold;
		boolean[] speech = new boolean[totalFrame];
		int frameCount =0, hangover = 4;
		XYSeries energi = new XYSeries ("energy");
		XYSeries energithres = new XYSeries ("energyThreshold");
		double init=0;
		int assumption = 40;
		threshold = 0;
		for (int i=0;i<assumption;i++)
		{
			threshold = threshold + energy[i];
		}
		threshold = threshold / assumption;
		for (int i=0;i<totalFrame;i++)
		{
			energi.add(i, energy[i]);
			if (energy[i]>threshold)
			{
				speech[i] = true; 
				frameCount=0;
			}
			else
			{
				if (frameCount == hangover)
				{
					speech[i] = false;
				}
				else
				{
					speech[i] = false;
					frameCount++;
				}
			}
			energithres.add(i, threshold);
		}
		ArrayList<Integer> blokStart = new ArrayList<Integer>();
		ArrayList<Integer> blokEnd = new ArrayList<Integer>();
	    int start=0,end=0;
	    while (start < speech.length)
	    {
	    	if (speech[start])
	    	{
	    		boolean cek=true;
	    		for (int i=start+1;i<speech.length;i++)
	    		{
	    			if (!speech[i])
	    			{
	    				end = i-1;
	    				cek = false;
	    				break;
	    			}
	    		}
	    		if (cek)
	    		{
	    			end = speech.length-1;
	    		}
	    		blokStart.add(start);
	    		blokEnd.add(end);
	    		start = end+1;
	    	}
	    	else
	    	{
	    		start++;
	    	}
	    }
	    /*try {
			write.write("distance\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    ArrayList <Integer> sel = new ArrayList<Integer>();
	    for (int i=0;i<blokStart.size();i++)
	    {
	    	int awal = blokStart.get(i);
	    	int akhir = blokEnd.get(i);
	    	int selisih = Math.abs(awal- akhir);
	    	sel.add(selisih);
	    	/*try {
				write.write (awal+" "+akhir+" selisih = "+selisih+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    }
	    for (int i=0;i<blokStart.size()-1;i++)
	    {
	    	if (Math.abs(blokEnd.get(i) - blokStart.get(i+1))<6 && sel.get(i+1)>20)
	    	{
	    		for (int j=blokEnd.get(i);j<=blokStart.get(i+1);j++)
	    		{
	    			speech[j]= true;
	    		}
	    	}
	    }
		
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		dataset1.addSeries(energi);
		dataset1.addSeries(energithres);
		XyChart.dataSet = dataset1;
		XyChart chart1 = new XyChart("EnergyChart "+fileName,"Energy Chart "+fileName);
		chart1.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart1 );
	      chart1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      chart1.setVisible( true );
	    XYSeries speechg = new XYSeries ("speech");
		for (int i=0;i<speech.length;i++)
		{
			if (speech[i])
			{
				speechg.add(i,1);
			}
			else
			{
				speechg.add(i,0);
			}
			
		}
		XYSeriesCollection dataset2 = new XYSeriesCollection();
		dataset2.addSeries(speechg);
		XyChart.dataSet = dataset2;
		XyChart chart2 = new XyChart("WordChart "+fileName,"Speech Boolean Chart "+fileName);
		chart2.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart2 );
	      chart2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      chart2.setVisible( true );
	    
	    return speech;
	}
	
	public static void main (String[] Args)
	{
		File dir = new File ("C:\\Users\\hobert\\workspace\\data suara pertanyaan");
		File[] listSound = dir.listFiles();
		int count = 0;
		for (File list : listSound)
		{
			if (!list.isDirectory())
			{
				String name = list.getName();
				String[] wordList = name.split("_");
				wordList[wordList.length-1] = wordList[wordList.length-1].replace(".wav", "");
				double[] data = StdAudio.read(list.getAbsolutePath());
				String saveLocation = "hasil potong suara/";
				String directoryName = "";
				for (int i=0;i<wordList.length;i++)
				{
					directoryName = directoryName + wordList[i];
					if (i != wordList.length-1)
					{
						directoryName += " ";
					}
				}
				fileName = directoryName;
				saveLocation= saveLocation + directoryName+"/";
				File mkd = new File (saveLocation);
				if (mkd.exists())
				{
					try {
						FileUtils.forceDelete(mkd);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("masuk");
				}
				mkd.mkdir();
				String consoleOutputLocation = saveLocation+"console output.txt";
				try {
					write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(consoleOutputLocation), "UTF-8"));
				} catch (UnsupportedEncodingException | FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				VAD splitSound = new VAD();
				boolean[] speech = splitSound.computeVAD(data, 160);
				double[][] speechToken = splitSound.splitWord(data, speech, 160);
				for (int i=0;i<speechToken.length;i++)
				{
					String fileName = saveLocation+"hasil_potong_"+i+".wav";
					StdAudio.save(fileName, speechToken[i]);
				}
				try {
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("selesai pecah");
			}
		}
		
	}

}
