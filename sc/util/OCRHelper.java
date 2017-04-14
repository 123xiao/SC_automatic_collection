package com.sc.sc.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdesktop.swingx.util.OS;

public class OCRHelper {
	public static void main(String[] args) {
		saveToFile("http://www.shenzhenair.com/szair_B2C/getLoginCaptcha.action?t=" + UUID.randomUUID());
		String path = new File("testdata").getAbsolutePath();
		File imageFile = new File(path + "test.jpg");
		String recognizeText;
		try {
			recognizeText = new OCRHelper().recognizeText(imageFile);
			System.out.println(recognizeText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageFile.delete();
		// URL url = null;
		// try {
		// url = new
		// URL("http://www.shenzhenair.com/szair_B2C/getLoginCaptcha.action?t=0.5421236729186436");
		// File testDataDir = new File(url.toURI());
		// String recognizeText = new OCRHelper().recognizeText(testDataDir);
		// System.out.println(recognizeText);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		//
		// File testDataDir = new File("testdata");
		// System.out.println(testDataDir.listFiles().length);
		// int i = 0;
		// for (File file : testDataDir.listFiles()) {
		// i++;
		// String recognizeText = new OCRHelper().recognizeText(file);
		// System.out.print(recognizeText + "\t");
		//
		// if (i % 5 == 0) {
		// System.out.println();
		// }
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
	}

	public static void saveToFile(String destUrl) {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		try {
			url = new URL(destUrl);
			httpUrl = (HttpURLConnection) url.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			String path = new File("testdata").getAbsolutePath();
			fos = new FileOutputStream(path + "test.jpg");
			while ((size = bis.read(buf)) != -1) {
				fos.write(buf, 0, size);
			}
			fos.flush();
		} catch (IOException e) {
		} catch (ClassCastException e) {
		} finally {
			try {
				fos.close();
				bis.close();
				httpUrl.disconnect();
			} catch (IOException e) {
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件位置我防止在，项目同一路径
	 */
	private String tessPath = new File("D://Tool//Tesseract-OCR").getAbsolutePath();

	private final String LANG_OPTION = "-l";

	private final String EOL = System.getProperty("line.separator");

	/**
	 * @param imageFile
	 *            传入的图像文件
	 * @param imageFormat
	 *            传入的图像格式
	 * @return 识别后的字符串
	 */
	public String recognizeText(File imageFile) throws Exception {
		/**
		 * 设置输出文件的保存的文件目录
		 */
		File outputFile = new File(imageFile.getParentFile(), "output");

		StringBuffer strB = new StringBuffer();
		List<String> cmd = new ArrayList<String>();
		if (OS.isWindowsXP()) {
			cmd.add(tessPath + "\\tesseract");
		} else if (OS.isLinux()) {
			cmd.add("tesseract");
		} else {
			cmd.add(tessPath + "\\tesseract");
		}
		cmd.add("");
		cmd.add(outputFile.getName());
		cmd.add(LANG_OPTION);
		cmd.add("eng");

		ProcessBuilder pb = new ProcessBuilder();
		/**
		 * Sets this process builder's working directory.
		 */
		pb.directory(imageFile.getParentFile());
		cmd.set(1, imageFile.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);
		Process process = pb.start();
		// tesseract.exe 1.jpg 1 -l chi_sim
		// Runtime.getRuntime().exec("tesseract.exe 1.jpg 1 -l chi_sim");
		/**
		 * the exit value of the process. By convention, 0 indicates normal
		 * termination.
		 */
		// System.out.println(cmd.toString());
		int w = process.waitFor();
		if (w == 0)// 0代表正常退出
		{
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(outputFile.getAbsolutePath() + ".txt"), "UTF-8"));
			String str;

			while ((str = in.readLine()) != null) {
				strB.append(str).append(EOL);
			}
			in.close();
		} else {
			String msg;
			switch (w) {
			case 1:
				msg = "Errors accessing files. There may be spaces in your image's filename.";
				break;
			case 29:
				msg = "Cannot recognize the image or its selected region.";
				break;
			case 31:
				msg = "Unsupported image format.";
				break;
			default:
				msg = "Errors occurred.";
			}
			throw new RuntimeException(msg);
		}
		new File(outputFile.getAbsolutePath() + ".txt").delete();
		return strB.toString().replaceAll("\\s*", "");
	}
}