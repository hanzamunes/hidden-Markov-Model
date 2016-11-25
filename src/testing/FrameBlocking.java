package testing;

import java.util.ArrayList;
import java.util.Scanner;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import gui.XyChart;

public class FrameBlocking {
	
	static int overlap;
	
	public static ArrayList<double[]> frameData(double[] data, int totalFrame)
	{
		int dataLength = data.length;
		int dataPerFrame = (int) (Math.ceil((double) dataLength / (double) totalFrame)*1.25);
		overlap = (int) Math.ceil(0.25 * (double)dataPerFrame);
		System.out.println("parameter");
		System.out.println("panjang data = "+dataLength);
		System.out.println("panjang frame = "+dataPerFrame);
		System.out.println("overlap = "+overlap);
		ArrayList<double[]> result = new ArrayList<double[]>();
		int pointer = 0;
		while (result.size() < totalFrame)
		{
			double[] temp = new double[dataPerFrame];
			for (int i=0;i<dataPerFrame;i++)
			{
				if (pointer < data.length)
				{
					temp[i] = data[pointer];
				}
				else
				{
					temp[i] = 0;
				}
				pointer++;
			}
			result.add(temp);
			pointer = pointer - overlap;
		}
		return result;
	}
	
	public static void main (String[] Args)
	{
		Scanner scan = new Scanner (System.in);
		int a = scan.nextInt();
		int b = scan.nextInt();
		double[] data = new double[a];
		int filler = 0;
		for (int i=1;i<=a;i++)
		{
			data[i-1] = filler;
			if (i > a/2)
			{
				filler--;
			}
			else
			{
				filler++;
			}
			if (filler < 0)
			{
				filler = 0;
			}
		}
		ArrayList<double[]> hasil = frameData (data,b);
		for (int i=0;i<hasil.size();i++)
		{
			System.out.println("frame ke "+(i+1));
			for (int j=0;j<hasil.get(i).length;j++)
			{
				System.out.print(hasil.get(i)[j]+" ");
			}
			System.out.println();
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries dataAwal = new XYSeries ("data awal");
		//dataset.addSeries(dataAwal);
		for (int i=0;i<data.length;i++)
		{
			dataAwal.add(i,data[i]);
		}
		int position = 0;
		for (int i=0;i<hasil.size();i++)
		{
			XYSeries dataFrame = new XYSeries("data frame "+(i+1));
			for (int j=0;j<hasil.get(i).length;j++)
			{
				dataFrame.add(position,hasil.get(i)[j]);
				position++;
			}
			position = position - overlap;
			dataset.addSeries(dataFrame);
		}
		XyChart.dataSet = dataset;
		XyChart chart = new XyChart("frame","tes");
		chart.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart );          
	      chart.setVisible( true );
	}

}
