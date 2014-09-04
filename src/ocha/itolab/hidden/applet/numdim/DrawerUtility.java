package ocha.itolab.hidden.applet.numdim;


/**
 * �`�揈���̃N���X
 * @author itot
 */
public class DrawerUtility {
	
	IndividualTransformer trans = null;
	int numNode;
	
	double p1[] = new double[3];
	double p2[] = new double[3];
	double p3[] = new double[3];
	double p4[] = new double[3];
	int imageSize[] = new int[2];
	
	/**
	 * Constructor
	 * @param width �`��̈�̕�
	 * @param height �`��̈�̍���
	 */
	public DrawerUtility(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
	}
	
	/**
	 * Transformer���Z�b�g����
	 * @param t Transformer
	 */
	public void setTransformer(IndividualTransformer t) {
		this.trans = t;
	}
	

	
	/**
	 * �`��̈�̃T�C�Y��ݒ肷��
	 * @param width �`��̈�̕�
	 * @param height �`��̈�̍���
	 */
	public void setWindowSize(int width, int height) {
		imageSize[0] = width;
		imageSize[1] = height;
	}
	
	/**
	 * �h��Ԃ����̖ʂ��I���e���E�����𔻒肷��
	 * @param px ��ʏ��x���W�l
	 * @param py ��ʏ��y���W�l
	 * @param e1 1�ڂ̒��_�̍��W�l
	 * @param e2 2�ڂ̒��_�̍��W�l
	 * @return �I���e�Ȃ�true
	 */
	int whichSide(int px, int py, double e1[], double e2[]) {
		double a = (e1[1] - (double) py) * (e2[0] - (double) px);
		double b = (e1[0] - (double) px) * (e2[1] - (double) py);
		if (a > b)
			return -1;
		if (a < b)
			return 1;
		
		return 0;
	}
	
	/**
	 * �s�b�N�����ʒu���l�p�`�ʂ̓������𔻒肷��
	 * @param px ��ʏ��x���W�l
	 * @param py ��ʏ��y���W�l
	 * @param pp1 1�ڂ̒��_�̍��W�l
	 * @param pp2 2�ڂ̒��_�̍��W�l
	 * @param pp3 3�ڂ̒��_�̍��W�l
	 * @param pp4 4�ڂ̒��_�̍��W�l
	 * @return �����Ȃ�true
	 */
	public boolean isInside(
			int px,
			int py,
			double pp1[],
			double pp2[],
			double pp3[],
			double pp4[]) {
		
		boolean flag1 = false, flag2 = false;
		int ret;
		
		ret = whichSide(px, py, pp1, pp2);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		ret = whichSide(px, py, pp2, pp3);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		ret = whichSide(px, py, pp3, pp4);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		ret = whichSide(px, py, pp4, pp1);
		if (ret > 0)
			flag1 = true;
		if (ret < 0)
			flag2 = true;
		
		if (flag1 == false || flag2 == false)
			return true;
		return false;
	}
	

}
