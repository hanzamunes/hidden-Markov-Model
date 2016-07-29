package onlyEnergy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import gui.XyChart;
import stdAudio.StdAudio;

class pair <A,B>{
	private A first;
	private B second;
	/**
	 * @param first
	 * @param second
	 */
	public pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	/**
	 * @return the first
	 */
	public A getFirst() {
		return first;
	}
	/**
	 * @param first the first to set
	 */
	public void setFirst(A first) {
		this.first = first;
	}
	/**
	 * @return the second
	 */
	public B getSecond() {
		return second;
	}
	/**
	 * @param second the second to set
	 */
	public void setSecond(B second) {
		this.second = second;
	}
	
}

public class VAD {
	
	public static boolean splitted;
	
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
		System.out.println ("distance");
	    Map <Integer,Integer> blok1 = new TreeMap<Integer,Integer>(blok);
	    ArrayList<ArrayList<Double>> temp1 = new ArrayList<ArrayList<Double>>();
	    int count = 0;
	    for(Entry<Integer, Integer> entry : blok1.entrySet()) {
	    	int awal = entry.getKey();
	    	int akhir = entry.getValue();
	    	int selisih = Math.abs (awal-akhir);
	    	System.out.println (awal+" "+akhir+" selisih = "+selisih);
	    	if (selisih >6)
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
	    	if (selisih >6)
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
		int assumption = (1600/160);
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
		XYSeriesCollection dataset1 = new XYSeriesCollection();
		dataset1.addSeries(energi);
		dataset1.addSeries(energithres);
		XyChart.dataSet = dataset1;
		XyChart chart1 = new XyChart("EnergyChart","Energy Chart");
		chart1.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart1 );          
	      chart1.setVisible( true );
	    XYSeries speechg = new XYSeries ("speech");
		for (int i=0;i<speech.length;i++)
		{
			System.out.println (speech[i]);
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
		XyChart chart2 = new XyChart("WordChart","Speech Boolean Chart");
		chart2.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart2 );          
	      chart2.setVisible( true );
	    
	    return speech;
	}

}
