package mfcc;

import java.util.ArrayList;


public class Mfcc {
	FFT fft = new FFT();
	DCT dct;
	public int noOfFrames;
	public int numCepstra=12;
	public Delta delta;
	public Energy en;
	public  Mfcc()
	{
	}
	
	public double[] CeptralLiftering(double[] data)
	{
		int n = data.length;
		double[] result = new double[data.length];
		for (int i=0;i<data.length;i++)
		{
			result[i] = data[i] *(1+(n/2)*Math.sin(i*Math.PI/n));
		}
		return result;
	}
	
	public double[] DCRemoval (double[] data)
	{
		double[] result = new double[data.length];
		double avarage=0;
		for (int i=0;i<data.length;i++)
		{
			avarage = avarage + data[i];
		}
		avarage = avarage/data.length;
		for (int i=0;i<data.length;i++)
		{
			result[i] = data[i]-avarage;
		}
		return result;
	}
	
	/*public double[] DCT(double[] source)
	{
		int data = source.length;
		double[] result = new double[data];
		for (int i=0;i<data;i++)
		{
			double a = i*Math.PI/data;
			for (int j=0;j<data;j++)
			{
				result[i] = result[i] + (source[j]*Math.cos(a*(j-0.5)));
			}
		}
		return result;
	}*/
	
	public ArrayList<double[]> FrameBlocking (double[] data, int frameSize, int overlap)
	{
		int length = data.length;
		ArrayList<double[]> result = new ArrayList<double[]>();
		int index = 0;
		int bound = length-overlap;
		while (index<bound)
		{
			double[] frameData = new double[frameSize];
			for (int i=0;i<frameSize;i++)
			{
				int frameNumber = index+i;
				if (frameNumber>=length)
				{
					frameData[i]=0;
				}
				else
				{
					frameData[i] = data[frameNumber];
				}
			}
			index = index + overlap;
			result.add(frameData);
		}
		return result;
	}
	
	public double[] MelFrequencyWrapping(double[] source, double fs)
	{
		double melLow = 2595*Math.log10(1+(300/700));
		double melHigh = 2595*Math.log10(1+(8000/700));
		int p = 26;
		int data = source.length;
		double divider = (melHigh-melLow)/(p+1);
		double[] fb = new double[p+2];
		for (int i=0;i<=p+1;i++)
		{
			double mel = melLow+i*divider;
			double inverseMel = 700*(Math.pow(10, mel/2595)-1);
			fb[i] = data/fs*inverseMel;
		}
		double[] x = new double[p];
		for (int i = 1; i<= p;i++)
		{
			for (int k=0;k<data;k++)
			{
				if (k<fb[i-1])
				{
				}
				else if ((fb[i-1]<k) && (k<=fb[i]))
				{
					x[i-1] = x[i-1] + (source[k]*((k-fb[i-1])/(fb[i]-fb[i-1])));
				}
				else if ((fb[i]<k) && (k<=fb[i+1]))
				{
					x[i-1] = x[i-1] + (source[k]*((fb[i+1]-k)/(fb[i+1]-fb[i])));
				}
			}
		}
		return x;
	}
	
	public double[] PreEmphasize (double[] data, double alpha)
	{
		double[] result = new double[data.length];
		result[0] = data[0];
		for (int i=1;i<data.length;i++)
		{
			result[i] = data[i] - alpha*data[i-1];
		}
		return result;
	}
	
	public double[] Windowing (double[] data, int frameSize)
	{
		int length = data.length;
		double[] result = new double[length];
		double divider = frameSize-1;
		double pi2 = 2*Math.PI;
		for (int i=0;i<length;i++)
		{
			result[i] = data[i] * (0.54-0.46*Math.cos(pi2*i/divider));
		}
		return result;
	}
	
	public double[][] doCepstralMeanNormalization (double[][] mfccFeature)
	{
		double sum;
		double mean;
		double[][] mCeps = new double[mfccFeature.length][numCepstra];
		for (int i=0;i<numCepstra;i++)
		{
			sum = 0.0;
			for (int j=0;j<mfccFeature.length;j++)
			{
				sum = sum + mfccFeature[j][i];
			}
			mean = sum / noOfFrames;
			for (int j=0;j<mfccFeature.length;j++)
			{
				mCeps[j][i] = mfccFeature[j][i] - mean;
			}
		}
		return mCeps;
	}
	
	public double[][] GetFeatureVector (double[] data, double alpha, int size, int overlap)
	{
		double[] dcRemoval_1 = DCRemoval(data);
		System.out.println("Selesai DCRemoval");
		double[] preEmphasized = PreEmphasize(dcRemoval_1,alpha);
		System.out.println("Selesai PReEmphasize");
		ArrayList<double[]> frame = FrameBlocking(preEmphasized,size,overlap);
		System.out.println("Selesai FrameBlocking");
		ArrayList<double[]> result = new ArrayList<double[]>();
		dct = new DCT(numCepstra,26);
		double[][] mfccFeature = new double[frame.size()][];
		double[][] framedSignal = new double[frame.size()][];
		System.out.println ("Frame size = " + frame.size());
		for (int i=0;i<frame.size();i++)
		{
			double[] window = Windowing (frame.get(i),size);
			System.out.println("Selesai windowing");
			framedSignal[i] = window;
			Complex[] signal = new Complex[window.length];
			for (int x=0;x<window.length;x++)
			{
				Complex c = new Complex (window[x],0);
				signal[x] = c;
			}
			Complex[] hasil = fft.fft1D(signal);
			System.out.println("Selesai fft");
			double[] frequencyValue = new double[window.length];
			for (int k = 0; k < window.length; k++) {
				double nilaiReal = hasil[k].re();
				double nilaiImag = hasil[k].im();
				frequencyValue[k] = Math.sqrt(nilaiReal * nilaiReal + nilaiImag * nilaiImag);
			}
			double[] melFrequency = MelFrequencyWrapping(frequencyValue,16000);
			System.out.println("Selesai MelWrapping");
			double[] cepstrum = dct.performDCT(melFrequency);
			System.out.println("Selesai DCT");
			double[] cepstral = CeptralLiftering(cepstrum);
			System.out.println("Selesai Liftering");
			mfccFeature[i] = cepstral;
		}
		delta = new Delta();
		en = new Energy(size);
		noOfFrames = frame.size();
		double[][] normalCeps = doCepstralMeanNormalization(mfccFeature);
		delta.setRegressionWindow(2);
		double[][] deltaMfcc = delta.performDelta2D(mfccFeature);
		delta.setRegressionWindow(1);
		double[][] deltaDeltaMfcc = delta.performDelta2D(deltaMfcc);
		double[] energyVal = en.calcEnergy(framedSignal);
		delta.setRegressionWindow(1);
		double[] deltaEnergy = delta.performDelta1D(energyVal);
		delta.setRegressionWindow(1);
		double[] deltaDeltaEnergy = delta.performDelta1D(deltaEnergy);
		double[][] featureVector = new double[frame.size()][3*numCepstra+3];
		for (int i=0;i<frame.size();i++)
		{
			for (int j = 0; j < numCepstra; j++) {
				featureVector[i][j] = mfccFeature[i][j];
			}
			for (int j = numCepstra; j < 2 * numCepstra; j++) {
				featureVector[i][j] = deltaMfcc[i][j - numCepstra];
			}
			for (int j = 2 * numCepstra; j < 3 * numCepstra; j++) {
				featureVector[i][j] = deltaDeltaMfcc[i][j - 2 * numCepstra];
			}
			featureVector[i][3 * numCepstra] = energyVal[i];
			featureVector[i][3 * numCepstra + 1] = deltaEnergy[i];
			featureVector[i][3 * numCepstra + 2] = deltaDeltaEnergy[i];
		}
		return featureVector;
	}
}
