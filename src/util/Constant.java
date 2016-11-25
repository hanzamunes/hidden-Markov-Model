package util;

public class Constant {
	
	public static int HMM_STATES = 2;
	public static int OBSERVATION_POINTS = 1024;
	public static int FRAME_LENGTH = 400;
	public static int FRAME_OVERLAP = 160;
	public static int WORD_TOTAL = 0;
	public static int WORD_DATA_TOTAL = 0;
	//outputFolder untuk data suara panjang
	//public static String outputFile1 = "C:\\Users\\hobert\\workspace\\percobaan suara\\percobaan suara panjang\\hasil percobaan\\"+util.Constant.HMM_STATES+" states "+util.Constant.OBSERVATION_POINTS+" codebook "+util.Constant.FRAME_LENGTH+" frames frame static.csv";
	//public static String outputFile2 =  "C:\\Users\\hobert\\workspace\\percobaan suara\\percobaan suara panjang\\hasil percobaan\\"+util.Constant.HMM_STATES+" states "+util.Constant.OBSERVATION_POINTS+" codebook "+util.Constant.FRAME_LENGTH+" frames baru frame static.csv";
	
	//output folder untuk percobaan parameter
	public static String outputFile1 = "C:\\Users\\hobert\\workspace\\percobaan suara\\percobaan dengan frame tetap\\percobaan parameter\\hasil percobaan parameter\\"+HMM_STATES+" states "+OBSERVATION_POINTS+" codebook "+FRAME_LENGTH+" sample per frames with "+FRAME_OVERLAP+" overlap.csv";
	public static String outputFile2 = "C:\\Users\\hobert\\workspace\\percobaan suara\\percobaan dengan frame tetap\\percobaan parameter\\hasil percobaan parameter\\"+HMM_STATES+" states "+OBSERVATION_POINTS+" codebook "+FRAME_LENGTH+" sample per frames with "+FRAME_OVERLAP+" overlap baru.csv";

}
