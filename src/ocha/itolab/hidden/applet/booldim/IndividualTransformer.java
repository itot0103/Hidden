package ocha.itolab.hidden.applet.booldim;

import java.lang.Math;


/**
 * �`��̎��_����i�g��k���A��]�A���s�ړ��j�̃p�����[�^���Ǘ�����N���X
 * @author itot
 */
public class IndividualTransformer {

	double viewShift[] = new double[3];
	double viewRotate[] = new double[16];
	double viewScaleX, viewScaleY;
	double viewShiftBak[] = new double[3];
	double center[] = new double[3];
	double viewScaleBakX, viewScaleBakY;
	double Xrotate, Yrotate, XrotateBak, YrotateBak;
	double shiftX, shiftZ;
	
	DrawerUtility du = null;

	/**
	 * Constructor
	 */
	public IndividualTransformer() {
		viewReset();
	}

	public void setDrawerUtility(DrawerUtility du){
		this.du = du;
	}
	
	/**
	 * ���_�p�����[�^�����Z�b�g����
	 */
	public void viewReset() {
		for (int i = 0; i < 16; i++) {
			if (i % 5 == 0)
				viewRotate[i] = 1.0;
			else
				viewRotate[i] = 0.0;
		}
		viewScaleX = viewScaleBakX = 1.0;
		viewScaleY = viewScaleBakY = 1.0;
		viewShift[0] = viewShiftBak[0] = 0.0;
		viewShift[1] = viewShiftBak[1] = 0.0;
		viewShift[2] = viewShiftBak[2] = 0.0;
		Xrotate = XrotateBak = 0.0;
		Yrotate = YrotateBak = 0.0;

	}


	/**
	 * �}�E�X�{�^���������ꂽ���[�h��ݒ肷��
	 */
	public void mousePressed() {
		viewScaleBakX = viewScaleX;
		viewScaleBakY = viewScaleY;
		viewShiftBak[0] = viewShift[0];
		viewShiftBak[1] = viewShift[1];
		viewShiftBak[2] = viewShift[2];
		XrotateBak = Xrotate;
		YrotateBak = Yrotate;
	}

	/**
	 * �I�u�W�F�N�g���W���猳�f�[�^��̈ʒu�ɕϊ�����
	 * @param x �}�E�X�|�C���^��x���W�l(�I�u�W�F�N�g���W)
	 * @param y �}�E�X�|�C���^��y���W�l(�I�u�W�F�N�g���W)
	 */
	public void getPickedPosition(double x, double y){
		double objX, objY;
		
		// �g��k���E�ړ��O��object���W�l
		objX = (x - viewShiftBak[0]) / viewScaleBakX;
		objY = (y - viewShiftBak[1]) / viewScaleBakY;
	}
	
	/**
	 * �}�E�X�̃h���b�O����ɉ����ăp�����[�^�𐧌䂷��
	 * @param x �}�E�X�|�C���^��x���W�l
	 * @param y �}�E�X�|�C���^��y���W�l
	 * @param width ��ʗ̈�̕�
	 * @param height ��ʗ̈�̍���
	 * @param dragMode �h���b�O���[�h�i1:ZOOM, 2:SHIFT, 3:ROTATE�j
	 */
	public void drag(int x, int y, int width, int height, int dragMode) {
		
		if(dragMode == 1) { // ZOOM
			
			if (x > 0) {
				viewScaleX =
					viewScaleBakX * (1 + (double) (2 * x) / (double) width);
			} else {
				viewScaleX = viewScaleBakX * (1 + (double) x / (double) width);
			}
			if (viewScaleX < 0.2)
				viewScaleX = 0.2;
			
			if (y > 0) {
				viewScaleY =
					viewScaleBakY * (1 + (double) (2 * y) / (double) height);
			} else {
				viewScaleY = viewScaleBakY * (1 + (double) y / (double) height);
			}
			viewShift[0] = viewShiftBak[0] * viewScaleX / viewScaleBakX;
			viewShift[1] = viewShiftBak[1] * viewScaleY / viewScaleBakY;
		}
		
		if (dragMode == 2) { // SHIFT
			
			 float diffX = (float)x * 3.0f / width;
             float diffY = (-3.0f) * (float)y / height;
           
            viewShift[0] = viewShiftBak[0] + diffX;
 			viewShift[1] = viewShiftBak[1] + diffY;
		}
		
		if (dragMode == 3) { // ROTATE
			Xrotate = XrotateBak + (double) x * Math.PI / (double) width;
			Yrotate = YrotateBak + (double) y * Math.PI / (double) height;
			double cosX = Math.cos(Yrotate);
			double sinX = Math.sin(Yrotate);
			double cosY = Math.cos(Xrotate);
			double sinY = Math.sin(Xrotate);

			viewRotate[0] = cosY;
			viewRotate[1] = 0;
			viewRotate[2] = -sinY;
			viewRotate[4] = sinX * sinY;
			viewRotate[5] = cosX;
			viewRotate[6] = sinX * cosY;
			viewRotate[8] = cosX * sinY;
			viewRotate[9] = -sinX;
			viewRotate[10] = cosX * cosY;
		}

	}
	


	/**
	 * �\���̊g��x��Ԃ�
	 * @return �\���̊g��x
	 */
	public double getViewScaleX() {
		return viewScaleX;
	}
	
	/**
	 * �\���̊g��x��Ԃ�
	 * @return �\���̊g��x
	 */
	public double getViewScaleY() {
		return viewScaleY;
	}
	
	/**
	 * �\���̊g��x���Z�b�g����
	 * @param v �\���̊g��x
	 */
	public void setViewScaleX(double v) {
		viewScaleX = v;
	}
	
	/**
	 * �\���̊g��x���Z�b�g����
	 * @param v �\���̊g��x
	 */
	public void setViewScaleY(double v) {
		viewScaleY = v;
	}


	/**
	 * counter�̒��S���W�l��Ԃ�
	 * @param i ���W��(1:X, 2:Y, 3:Z)
	 * @return ���S���W�l
	 */
	public double getCenter(int i) {
		return center[i];
	}
	
	/**
	 * counter�̒��S���W�l���Z�b�g����
	 * @param g ���S���W�l
	 * @param i ���W��(1:X, 2:Y, 3:Z)
	 */
	public void setCenter(double g, int i) {
		center[i] = g;
	}

	/**
	 * ���_�̉�]�̍s��l��Ԃ�
	 * @param i �s�񒆂̗v�f�̈ʒu
	 * @return �s��l
	 */
	public double getViewRotate(int i) {
		return viewRotate[i];
	}
	
	/**
	 * ���_�̉�]�̍s��l���Z�b�g����
	 * @param v �s��l
	 * @param i �s�񒆂̗v�f�̈ʒu
	 */
	public void setViewRotate(double v, int i) {
		viewRotate[i] = v;
	}

	/**
	 * ���_�̕��s�ړ��ʂ�Ԃ�
	 * @param i ���W�� (0:X, 1:Y, 2:Z)
	 * @return ���s�ړ���
	 */
	public double getViewShift(int i) {
		return viewShift[i];
	}
	
	/**
	 * ���_�̕��s�ړ��ʂ��Z�b�g����
	 * @param v ���s�ړ���
	 * @param i ���W�� (1:X, 2:Y, 3:Z)
	 */
	public void setViewShift(double v, int i) {
		viewShift[i] = v;
	}

}
