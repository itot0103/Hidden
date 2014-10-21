package ocha.itolab.hidden.core.data;

import java.util.*;
import ocha.itolab.hidden.core.tool.*;

public class IndividualSet {
	int numTotal, numNumeric, numCategory, numBoolean, classId, dimselectMode;
	String name[];
	public String filename;
	public String message;
	int type[];
	ArrayList<OneIndividual> plots;
	public NumericSet numerics;
	public CategorySet categories;
	public BooleanSet booleans;
	
	public static int TYPE_NUMERIC = 1;
	public static int TYPE_CATEGORY = 2;
	public static int TYPE_BOOLEAN = 3;
	
	public static int DIMSELECT_CORRELATION = 1;
	public static int DIMSELECT_CLASS_PURELITY = 2;
	
	
	
	/**
	 * Constructor
	 */
	public IndividualSet(int numn, int numc, int numb, int id) {
		numNumeric = numn;
		numCategory = numc;
		numBoolean = numb;
		numTotal = numn + numc + numb;
		classId = id;
		dimselectMode = DIMSELECT_CORRELATION;
		name = new String[numTotal];
		type = new int[numTotal];
		plots = new ArrayList();
	}
	
	
	/**
	 * 数値変数の数を得る
	 */
	public int getNumNumeric() {
		return numNumeric;
	}
	
	/**
	 * カテゴリ変数の数を得る
	 */
	public int getNumCategory() {
		return numCategory;
	}
	
	/**
	 * ブーレアン変数の数を得る
	 */
	public int getNumBoolean() {
		return numBoolean;
	}
	
	/**
	 * 変数の総数を得る
	 */
	public int getNumTotal() {
		return numTotal;
	}
	
	/**
	 * 変数の名前をセットする
	 */
	public void setValueName(int i, String n) {
		name[i] = n;
	}
	
	/**
	 * 変数の名前を返す
	 */
	public String getValueName(int i) {
		return name[i];
	}
	
	/**
	 * 変数のタイプをセットする
	 */
	public void setValueType(int i, int t) {
		type[i] = t;
	}
	
	/**
	 * 変数のタイプを返す
	 */
	public int getValueType(int i) {
		return type[i];
	}
	
	/**
	 * クラスIDをセットする
	 */
	public void setClassId(int id) {
		classId = id;
		if(id >= numCategory + numBoolean)
			classId = -1;
	}
	
	/**
	 * クラスIDを返す
	 */
	public int getClassId() {
		return classId;
	}
	
	/**
	 * 次元選択のモードをセットする
	 */
	public void setDimSelectMode(int mode) {
		dimselectMode = mode;
	}
	
	/**
	 * 次元間の非類似度の算出モードを返す
	 */
	public int getDimSelectMode() {
		return dimselectMode;
	}
	
	/**
	 * 1個のプロットを追加する
	 */
	public OneIndividual addOneIndividual() {
		OneIndividual p = new OneIndividual(numNumeric, numCategory, numBoolean, plots.size());
		plots.add(p);
		return p;
	}

	
	/**
	 * プロットの個数を返す
	 */
	public int getNumIndividual() {
		return plots.size();
	}
	
	
	/**
	 * 特定のプロットを返す
	 */
	public OneIndividual getOneIndividual(int id) {
		return (OneIndividual)plots.get(id);
	}
	
	
	
	/**
	 * ファイルを読み終えたあとに呼び出す
	 */
	public void finalize() {
		
		// 数値情報を構築する
		System.out.println("Setup numeric information ...");
		numerics = new NumericSet(this);
		NumericDissimilarity.calculate(this);
		
		// 最大値・最小値を求める
		for(int i = 0; i < numNumeric; i++) {
			numerics.min[i] = 1.0e+30;   numerics.max[i] = -1.0e+30;
		}
		for(int j = 0; j < plots.size(); j++) {
			OneIndividual p = (OneIndividual)plots.get(j);
			p.id = j;
			for(int i = 0; i < numNumeric; i++) {
				numerics.min[i] = (numerics.min[i] > p.numeric[i]) ? p.numeric[i] : numerics.min[i];
				numerics.max[i] = (numerics.max[i] < p.numeric[i]) ? p.numeric[i] : numerics.max[i];
			}
		}
		
		// カテゴリ情報を構築する
		System.out.println("Setup category information ...");
		categories = new CategorySet(this);

		// ブーレアン変数情報を構築する
		System.out.println("Setup boolean information ...");
		booleans = new BooleanSet(this);
		BooleanCoOccurrence.calculate(this);
	}
	
}

