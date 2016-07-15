package gui;

import java.io.*;

import javax.sound.sampled.*;

import WavFile.WavFile;
import WavFile.WavFileException;

public class LoadAudio {
	
	public static int[] loadAudioFromFile(File file) throws IOException, WavFileException
	{
		if (file.isFile())
		{
			WavFile wavFile = WavFile.openWavFile(file);
	         // Display information about the wav file
	         wavFile.display();

			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				AudioInputStream in = AudioSystem.getAudioInputStream(file);
				int read;
				byte[] buff = new byte[(int)file.length()];
				while ((read = in.read(buff)) > 0)
				{
				    out.write(buff, 0, read);
				}
				out.flush();
				byte[] audioBytes = out.toByteArray();
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
