package com.cxbook.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Base64TOFile {
	/**
	 * ���ַ���תΪbyte[]����
	 * 
	 * @param fileString
	 *            �ļ�ʹ��encode����ת�ɵ��ַ�������
	 * @return
	 * @throws Exception
	 */
	public static byte[] decode(String fileString) {

		return Base64.decode(fileString);
	}

	/**
	 * �����ļ�
	 * 
	 * @param filepath
	 * @param b
	 * @return
	 */
	public static boolean createBase64File(String filepath, byte[] b) {

		BufferedOutputStream bufferedOutputStream = null;
		File file = null;
		try {
			file = new File(filepath);

			if (file.exists() && file.isFile()) {
				file.delete();
			}

			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			bufferedOutputStream.write(b);
			bufferedOutputStream.flush();
			bufferedOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
