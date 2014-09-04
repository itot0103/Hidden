package ocha.itolab.hidden.core.tool;

import java.util.*;
import java.io.*;

import mdsj.MDSJ;
import ocha.itolab.hidden.core.data.*;


public class NumericDissimilarity {
	static IndividualSet iset;
	static BufferedReader reader = null;
	static BufferedWriter writer = null;
	static String filename;
	
	
	/**
	 * Calculate dissimilarity among dimensions
	 * @param iset
	 */
	public static void calculate(IndividualSet is) {
		iset = is;
		filename = is.filename.replace(".csv", ".numeric");
		
		// Read from file
		if(readFromFile() == true)
			return;
		
		// Calculate distances between pairs of dimensions
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			if(i % 10 == 0)
				System.out.println("NumericDissimilarity: " + i);
			for(int j = (i + 1); j < iset.getNumNumeric(); j++)  {
				calculateCorrelationOnePair(i, j);
			}
		}
			
		// calculate positions applying MDS
		System.out.println("NumericDissimilarity: applyMDS");
		applyMDS();
		
		// Write to file
		System.out.println("NumericDissimilarity: writeToFile");
		writeToFile();
	}

	
	/**
	 * Calculate rank correlation for one pair of dimensions
	 */
	static void calculateCorrelationOnePair(int id1, int id2) {
		int n = 0;
		double count = 0;
		ArrayList<double[]> list1 = new ArrayList();
		ArrayList<double[]> list2 = new ArrayList();
		ArrayList<OneNumeric> dimensions = iset.numerics.dimensions;
		
		
		// for each individual
		for(int i = 0; i < iset.getNumIndividual(); i++) {
			OneIndividual p = iset.getOneIndividual(i);
			double s1[] = new double[1];
			double s2[] = new double[1];
			double numeric[] = p.getNumericValues();
			s1[0] = numeric[id1];
			s2[0] = numeric[id2];
			list1.add(s1);
			list2.add(s2);
			n++;
		}
		
		// determine the dissimilarity
		for(int i = 0; i < n - 1; i ++){
			for(int j = i + 1; j < n; j ++){
				double f0[] = list1.get(i);
				double f1[] = list1.get(j);
				double f2[] = list2.get(i);
				double f3[] = list2.get(j);
				if (f0[0] > f1[0]) {
					if(f2[0] > f3[0]){
						count ++;
					}
					if(f2[0] < f3[0]){
						count --;
					}
				}
				if (f0[0] < f1[0]) {
					if(f2[0] < f3[0]){
						count ++;
					}
					if(f2[0] > f3[0]){
						count --;
					}
				}
			}
		}
	
		// calculate the rank correlation
		double num_t = (double)(n * (n - 1) / 2);
		double r = 1.0 - Math.abs((double)count / num_t);
		
		
		// substitute the correlation
		OneNumeric d1 = dimensions.get(id1);
		d1.setDissimilarity(r, id2);
		OneNumeric d2 = dimensions.get(id2);
		d2.setDissimilarity(r, id1);
		
	}
	
	

	/**
	 * Calculate positions applying MDS
	 */
	static void applyMDS() {
		ArrayList<OneNumeric> dimensions = iset.numerics.dimensions;
		
		// Copy dissimilarity to an allocated array
		double input[][] = new double[iset.getNumNumeric()][];
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			input[i] = new double[iset.getNumNumeric()];
		}
		double maxdist = 0.0;
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			OneNumeric d1 = dimensions.get(i);
			for(int j = 0; j < iset.getNumNumeric(); j++) {
				double d = d1.getDissimilarity(j);
				if(d > maxdist) maxdist = d;
			}
		}
		
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			OneNumeric d1 = dimensions.get(i);
			for(int j = 0; j < iset.getNumNumeric(); j++) {
				OneNumeric d2 = dimensions.get(j);
				input[i][j] = d1.getDissimilarity(j) / maxdist;
				//System.out.println("   id1=" + i + "  id2=" + j + "  dist=" + input[i][j]);		
			}
		}
		
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			OneNumeric d1 = dimensions.get(i);
			for(int j = 0; j < iset.getNumNumeric(); j++) {
				OneNumeric d2 = dimensions.get(j);
				d1.setDissimilarity(input[i][j], j);
				d2.setDissimilarity(input[i][j], i);
			}
		}
		
		// Apply MDS
		double[][] output = MDSJ.classicalScaling(input);
		
		// Calculate positions
		double min1 = +1.0e+30, max1 = -1.0e+30;
		double min2 = +1.0e+30, max2 = -1.0e+30;
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			min1 = (min1 < output[0][i]) ? min1 : output[0][i];
			max1 = (max1 > output[0][i]) ? max1 : output[0][i];
			min2 = (min2 < output[1][i]) ? min2 : output[1][i];
			max2 = (max2 > output[1][i]) ? max2 : output[1][i];
		}
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			OneNumeric d1 = dimensions.get(i);
			double x = ((output[0][i] - min1) / (max1 - min1)) * 2.0 - 1.0;
			double y  = ((output[1][i] - min2) / (max2 - min2)) * 2.0 - 1.0;
			d1.setPosition(x, y);
			//System.out.println("     dimension" + i + " x=" + x + " y=" + y);
		}
	}
	
	
	/**
	 * ファイルに書く
	 */
	static void writeToFile() {
		openWriter(filename);
		println(Integer.toString(iset.getNumNumeric()));
		
		for(int i = 0; i < iset.getNumNumeric(); i++) {
			OneNumeric d = iset.numerics.dimensions.get(i);
			double x = d.getX();
			double y = d.getY();
			String line = (i + 1) + "," + x + "," + y;
			for(int j = i + 1; j < iset.getNumNumeric(); j++) {
				line += ("," + d.getDissimilarity(j));
			}
			println(line);
		}
		
		closeWriter();
	}
	
	
	
	/**
	 * ファイルを読む
	 */
	static boolean readFromFile() {
		openReader(filename);
		if(reader == null) return false;
		int num = 0, i = 0;
		
		while(true) {
			try {
			
				// EOFまで読み続ける
				String line = reader.readLine();
				if (line == null) break;

				if(num <= 0) {
					num = Integer.parseInt(line);
					continue;
				}

				StringTokenizer token = new StringTokenizer(line, ",");
				token.nextToken();
				
				OneNumeric d = iset.numerics.dimensions.get(i);
				double x = Double.parseDouble(token.nextToken());
				double y = Double.parseDouble(token.nextToken());
				d.setPosition(x, y);
				
				for(int j = (i + 1); j < num; j++) {
					double dis = Double.parseDouble(token.nextToken());
					OneNumeric d2 = iset.numerics.dimensions.get(j);
					d.setDissimilarity(dis, j);
					d2.setDissimilarity(dis, i);
				}
				i++;
			
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		closeReader();
		return true;
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
	
