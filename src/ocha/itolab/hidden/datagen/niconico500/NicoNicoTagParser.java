package ocha.itolab.hidden.datagen.niconico500;

import java.io.*;
import java.util.*;
import java.net.*;

public class NicoNicoTagParser {
	//static String path = "C:/itot/projects/InfoVis/Hidden/data/";
	static String path = "./";
	static String inputfilename = "NicoNicoVideoMoodDataSet500.csv";
	static String urlpath = "http://www.nicozon.net/watch/";
	static BufferedReader breader = null;
	
	static ArrayList<String> videonamelist = new ArrayList<String>();
	static ArrayList<ArrayList> videotaglist = new ArrayList<ArrayList>();
	static ArrayList<String> tlist = new ArrayList<String>();
	static ArrayList<int[]> tcount = new ArrayList<int[]>();
	
	public static void main(String args[]) {
		
		// read the input file
		openReader(path + inputfilename);
		read();
		closeReader();

		// parse the tags 
		openSites();
	}
	

	/**
	 * Open the input file
	 * @param filename
	 */
	static void openReader(String filename) {
		
		try {
			File file = new File(filename);
			breader = new BufferedReader(new FileReader(file));
			breader.ready();
			
		} catch (Exception e) {
			System.err.println(e);
			System.exit(-1);
		}
	}
	


	/**
	 * Close the input file
	 */
	static void closeReader() {
		try {
			breader.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	
	/**
	 * Read the input file
	 */
	static void read() {
		String wordarray[] = null;
		int n = 0;
		
		while(true) {
			try {
				n++;
				
				// EOFまで読み続ける
				String line = breader.readLine();
				if (line == null) return;
				if(n <= 2) continue;
				
				StringTokenizer token = new StringTokenizer(line, ",");
				String name = "";
				while(token.countTokens() > 0) {
					name = token.nextToken();
				}
				videonamelist.add(name);
				System.out.println(name);
				
			} catch(Exception e) {
				System.err.println("read: " + e);
			}
		}
	}
	
	
	/**
	 * Access to the websites and parse the tags
	 */
	static void openSites() {
		
		try {
		
			// for each video name
			for(String videoname : videonamelist) {
				
				int inttag[] = new int[10];
				ArrayList<String> tags = new ArrayList<String>();
				videotaglist.add(tags);
				
				
				String fullurl = urlpath + videoname;
				URL u = new URL(fullurl);
				InputStream is = u.openStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader r = new BufferedReader(isr);
				
				String line;
				while ((line = r.readLine()) != null) {
					int index1 = line.indexOf("tagname");
					if(index1 < 0) continue;
					String line2 = line.substring(index1 + 9);
					int index2 = line2.indexOf("</a>");
					line2 = line2.substring(0, index2);
					tags.add(line2);
				}
					if(tags.contains(new String("初音ミク"))){
					//System.out.print("ミク");
					inttag[0] = 1;
					}else{inttag[0] = 0;}
					if(tags.contains(new String("鏡音レン"))){
					//System.out.print("レン");
					inttag[1] = 1;
					}
					if(tags.contains(new String("鏡音リン"))){
					//System.out.print("リン");
					inttag[2] = 1;
					}
					if(tags.contains(new String("巡音ルカ"))){
					//System.out.print("ルカ");
					inttag[3] = 1;
					}
					if(tags.contains(new String("GUMI"))){
					inttag[4] = 1;
					}
					if(tags.contains(new String("KAITO"))){
					inttag[5] = 1;
					}
					if(tags.contains(new String("MEIKO"))){
					inttag[6] = 1;
					}
				r.close();
				for(int count = 0;count < 7;count++){
				System.out.print("," + inttag[count]);
				}
				System.out.print("\n");
			}
			//FileOutputStream fos = new FileOutputStream("newfile.csv");
			//	OutputStreamWriter osw = new OutputStreamWriter(fos, "SJIS");
			//	BufferedWriter bw = new BufferedWriter(osw);
			//	PrintWriter pw = new PrintWriter(bw);
			//	pw.print("あいうえお");
				//for(int count = 0;count < 10;count++){
				//pw.print(inttag[count]);
				//}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
