package captureAudio;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class WavFileFilter extends FileFilter
{
     public boolean accept(File f)
    {
        if(f.isDirectory())
        {
            return true;
        }
        return f.getName().endsWith(".wav");
    }
 
    public String getDescription()
    {
        return "Wav files (*.wav)";
    }
}