package ocha.itolab.hidden.datagen.lidc;

import java.io.*;
import java.util.*;

public class FeatureFileConverter {
	static BufferedReader reader;
	static BufferedWriter writer;

	static String dirname = "C:/itot/projects/InfoVis/Hidden/data/LIDC/textureFeatures20140901/";
	static String outfilename1 = "LIDCfeaturesNumericOnly.csv";
	static String outfilename2 = "LIDCfeaturesWithDescription.csv";
	static String descfilename = "tcia-diagnosis-data-2012-04-20.csv";
	
	static ArrayList<String> namelist = new ArrayList<String>();
	static ArrayList<double[]> valuelist = new ArrayList<double[]>();
	
	static int numfeatures;
	static String featurenames = null, numeric = null;

	
	/**
	 * main method
	 * @param agrs
	 */
	public static void main(String agrs[]) {
		
		File directory = new File(dirname);
		String filelist[] = directory.list();
		
		
		
		
		// read a feature file, and list the name of feature values
		for(int i = 0; i < filelist.length; i++) {
			if(filelist[i].endsWith("csv")) continue;
			openReader(dirname + filelist[i]);
			extractFeatureNames();
			closeReader();
			if(featurenames != null) break;
		}
		
		// open the first output file
		openWriter(dirname + outfilename1);
		println(numeric + ",Category");
		println(featurenames + ",Name");
		
		// read feature files, store feature values, 
		// and convert to the first output file
		for(int i = 0; i < filelist.length; i++) {
			if(filelist[i].endsWith("csv")) continue;
			openReader(dirname + filelist[i]);
			convert1();
			closeReader();
		}
		
		closeWriter();
		
		// another conversion with annotations
		openWriter(dirname + outfilename2);
		openReader(dirname + descfilename);
		convert2();
		closeReader();
		closeWriter();
	}
	
	

	/**
	 * Extract feature names from a feature file
	 */
	static void extractFeatureNames() {
		boolean isBegin = false;
		int numline = 0;
		
		while(true) {
			try {
			
				// EOFまで読み続ける
				String line = reader.readLine();
				if (line == null) break;
			
				// 空行は飛ばす
				if(line.length() <= 0) continue;

				if(line.indexOf("ID:") >= 0) {
					isBegin = true; numline = 0;
					continue;
				}
				if(line.indexOf("END") >= 0) {
					isBegin = false; break;
				}
				if(isBegin == false) continue;
				if((++numline) % 2 == 0) continue;
				
				line = line.replace(",", " ");				
				if(featurenames == null) 
					featurenames = line;
				else
					featurenames += ("," + line);
				
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		StringTokenizer token = new StringTokenizer(featurenames, ",");
		numfeatures = token.countTokens();
		numeric = "Numeric";
		for(int i = 1; i < numfeatures; i++)
			numeric += (",Numeric");
	}
	
	
	/**
	 * ファイルを変換する
	 */
	static void convert1() {
		double values[] = null;
		String name = "", name2 = "";
		int count = 0, numnodule = 0, numline = 0;
		boolean isBegin = false;
		
		
		while(true) {
			try {
			
				// EOFまで読み続ける
				String line = reader.readLine();
				if (line == null) break;
			
				// 空行は飛ばす
				if(line.length() <= 0) continue;

				if(name == null || name.length() <= 0) {
					name = line.substring(7);
					continue;
				}
				
				if(line.indexOf("ID:") >= 0) {
					isBegin = true;
					count = 0;  numnodule++;
					numline = 0;
					name2 = name + "-" + Integer.toString(numnodule);
					values = new double[numfeatures];
					continue;
				}
				if(line.indexOf("END") >= 0) {
					isBegin = false;
					namelist.add(name2);
					valuelist.add(values);
					
					String line2 = "";
					for(int i = 0; i < values.length; i++)
						line2 += (values[i] + ",");
					line2 += name2;
					println(line2);
					
					continue;
				}
				if(isBegin == false) continue;
				
				//System.out.println("    numline=" + numline + " line=" + line);
				
				if((++numline) % 2 == 0)
					values[count++] = Double.parseDouble(line);
			
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		

	
	}
	
	
	/**
	 * ファイルを変換する
	 */
	static void convert2() {
		boolean isFirstLine = true;
		
		println(numeric + ",Category,Category,Category,Category,Category,Category");
		println(featurenames + ",Name,PatientLevel,Method,PrimaryTumorSite,NoduleLevel,MethodNoduleLevel");
	
		while(true) {
			try {
			
				// EOFまで読み続ける
				String line = reader.readLine();
				if (line == null) break;
				if (isFirstLine == true) {
					isFirstLine = false;  continue;
				}
				
				StringTokenizer token = new StringTokenizer(line, ",");

				// 既に登録されている患者か否かを確認する
				if(token.countTokens() < 6) continue;
				String name = token.nextToken();
				String attributes = "";
				for(int j = 0; j < 5; j++)
					attributes += ("," + token.nextToken());
				int id = -1;
				for(int i = 0; i < namelist.size(); i++) {
					String name2 = namelist.get(i);
					
					if(name2.startsWith(name) == true) {
						double values[] = valuelist.get(i);
						String output = "";
						for(int j = 0; j < numfeatures; j++)
							output += (Double.toString(values[j]) + ",");
						output += (name2 + attributes);
						println(output);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	

	
	/**
	 * ファイルを開く
	 */
	static BufferedWriter openWriter(String filename) {	
		try {
			 writer = new BufferedWriter(
			    		new FileWriter(new File(filename)));
		} catch (Exception e) {
			System.err.println(e);
			writer = null;
			return null;
		}
	
		return writer;
	}
	
	/**
	 * ファイルを閉じる
	 */
	static void closeWriter() {
		if(writer == null) return;
		
		try {
			writer.close();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}
	
	/**
	 * 改行つきで出力する
	 */
	static void println(String word) {
		try {
			writer.write(word, 0, word.length());
			writer.flush();
			writer.newLine();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}	
	
	
	/**
	 * ファイルを開く
	 */
	static BufferedReader openReader(String filename) {
		File file = new File(filename);
		
		try {
			reader = new BufferedReader(new FileReader(file));
			reader.ready();
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	
		return reader;
	}
	
	/**
	 * ファイルを閉じる
	 */
	static void closeReader() {
		try {
			reader.close();
		} catch (Exception e) {
			System.err.println(e);
			return;
		}
	}
	
	
	
}
