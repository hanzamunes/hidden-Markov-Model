package onlyEnergy;

public class RMSEnergy {

	public double[] CalcEnergy (double[] data,int frameSize)
	{
		int totalFrame = (int)Math.ceil((double)data.length/(double)frameSize);
		double[] result = new double [totalFrame];
		for (int j=1;j<=totalFrame;j++)
		{
			double hasil = 0;
			for (int i=((j-1)*frameSize)+1;i<=j*frameSize;i++)
			{
				if (i<data.length)
				{
					hasil = hasil + Math.pow(data[i], 2);
				}
				
				
			}
			result[j-1] = Math.sqrt(hasil/(double)frameSize);
		}
		return result;
	}
}
