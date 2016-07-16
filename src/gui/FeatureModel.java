package gui;

import java.util.ArrayList;

public class FeatureModel {
	private ArrayList<ArrayList<String>> wordFileList;
	private ArrayList<ArrayList<double[]>> waveFileList;
	private ArrayList<String> wordList;
	private ArrayList<double[]> allFeatureList;
	private ArrayList<ArrayList<double[][]>> allFeatureVector;
	private int FEATUREDIMENSION = 39;
	/**
	 * @param wordFileList
	 * @param waveFileList
	 * @param wordList
	 * @param allFeatureList
	 * @param allFeatureVector
	 * @param fEATUREDIMENSION
	 */
	public FeatureModel(ArrayList<ArrayList<String>> wordFileList,
			ArrayList<ArrayList<double[]>> waveFileList,
			ArrayList<String> wordList, ArrayList<double[]> allFeatureList,
			ArrayList<ArrayList<double[][]>> allFeatureVector,
			int fEATUREDIMENSION) {
		this.wordFileList = wordFileList;
		this.waveFileList = waveFileList;
		this.wordList = wordList;
		this.allFeatureList = allFeatureList;
		this.allFeatureVector = allFeatureVector;
		FEATUREDIMENSION = fEATUREDIMENSION;
	}
	/**
	 * @return the wordFileList
	 */
	public ArrayList<ArrayList<String>> getWordFileList() {
		return wordFileList;
	}
	/**
	 * @param wordFileList the wordFileList to set
	 */
	public void setWordFileList(ArrayList<ArrayList<String>> wordFileList) {
		this.wordFileList = wordFileList;
	}
	/**
	 * @return the waveFileList
	 */
	public ArrayList<ArrayList<double[]>> getWaveFileList() {
		return waveFileList;
	}
	/**
	 * @param waveFileList the waveFileList to set
	 */
	public void setWaveFileList(ArrayList<ArrayList<double[]>> waveFileList) {
		this.waveFileList = waveFileList;
	}
	/**
	 * @return the wordList
	 */
	public ArrayList<String> getWordList() {
		return wordList;
	}
	/**
	 * @param wordList the wordList to set
	 */
	public void setWordList(ArrayList<String> wordList) {
		this.wordList = wordList;
	}
	/**
	 * @return the allFeatureList
	 */
	public ArrayList<double[]> getAllFeatureList() {
		return allFeatureList;
	}
	/**
	 * @param allFeatureList the allFeatureList to set
	 */
	public void setAllFeatureList(ArrayList<double[]> allFeatureList) {
		this.allFeatureList = allFeatureList;
	}
	/**
	 * @return the allFeatureVector
	 */
	public ArrayList<ArrayList<double[][]>> getAllFeatureVector() {
		return allFeatureVector;
	}
	/**
	 * @param allFeatureVector the allFeatureVector to set
	 */
	public void setAllFeatureVector(
			ArrayList<ArrayList<double[][]>> allFeatureVector) {
		this.allFeatureVector = allFeatureVector;
	}
	/**
	 * @return the fEATUREDIMENSION
	 */
	public int getFEATUREDIMENSION() {
		return FEATUREDIMENSION;
	}
	/**
	 * @param fEATUREDIMENSION the fEATUREDIMENSION to set
	 */
	public void setFEATUREDIMENSION(int fEATUREDIMENSION) {
		FEATUREDIMENSION = fEATUREDIMENSION;
	}
	
	

}
