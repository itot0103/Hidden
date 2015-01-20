package ocha.itolab.hidden.datagen.niconico500;

import java.io.*;
import java.util.*;

public class Onomatope {
	static String path = "C:/Users/itot_2/Desktop/";
	static String fname1 = "onomatope.csv";
	static String fname2 = "ono_vector2.csv";
	static String fname0 = "onomatope_nakamura2014.csv";
	static BufferedReader breader1 = null, breader2 = null;

	
	public static void main(String args[]) {
		breader1 = openReader(path + fname1);
		breader2 = openReader(path + fname2);
		
		try {
			String line1 = breader1.readLine();
			String line2 = breader2.readLine();
			
			
			while(true) {
				String oline = "";
				line1 = breader1.readLine();
				if(line1 == null) break;
				line1 = line1.substring(line1.indexOf(",") + 1);
				
				for(int i = 0; i < 15; i++) {
					line2 = breader2.readLine();
					if(line2 == null) break;
					StringTokenizer token = new StringTokenizer(line2, ",");
					token.nextToken();
					token.nextToken();
					String v = token.nextToken();
					oline += (v + ",");
				}
				oline += line1;
				System.out.println(oline);
				
			}
				
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		closeReader(breader1);
		closeReader(breader2);
	}
	
	
	
	
	
	/**
	 * Open the input file
	 * @param filename
	 */
	static  BufferedReader openReader(String filename) {
		BufferedReader reader = null;
		try {
			File file = new File(filename);
			reader = new BufferedReader(new FileReader(file));
			reader.ready();
			
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		
		return reader;
	}
	


	/**
	 * Close the input file
	 */
	static void closeReader(BufferedReader reader) {
		try {
			reader.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	

	
	
}
