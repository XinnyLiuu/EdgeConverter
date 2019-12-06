package listener;

import edgeconvert.CreateSQLFileWriter;
import pojo.EdgeField;
import pojo.EdgeTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static edgeconvert.EdgeConvertGUI.fields;
import static edgeconvert.EdgeConvertGUI.tables;
import static screen.DefineRelationScreen.*;
import static screen.DefineTableScreen.jbDTCreateDDL;
import static screen.DefineTableScreen.jmiDTOptionsShowProducts;
import static utils.Constants.CANCELLED;

public class CreateDDLButtonListener implements ActionListener {
	public static PrintWriter pw;
	public static File parseFile;
	public static File saveFile;
	private static File outputDir;
	private static ArrayList alSubclasses;
	private static ArrayList alProductNames;
	private static String[] productNames;
	private static Object[] objSubclasses;
	private String databaseName;

	public static void setOutputDir() {
		int returnVal;
		File outputDirOld = outputDir;
		alSubclasses = new ArrayList();
		alProductNames = new ArrayList();

		returnVal = jfcOutputDir.showOpenDialog(null);

		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return;
		}

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			outputDir = jfcOutputDir.getSelectedFile();
		}

//		getOutputClasses();

//		if (alProductNames.size() == 0) {
//			JOptionPane.showMessageDialog(null, "The path:\n" + outputDir + "\ncontains no valid output definition files.");
//			outputDir = outputDirOld;
//			return;
//		}

		if ((parseFile != null || saveFile != null) && outputDir != null) {
			jbDTCreateDDL.setEnabled(true);
			jbDRCreateDDL.setEnabled(true);
		}

		JOptionPane.showMessageDialog(null, "The available products to create DDL statements are:\n" + displayProductNames());

		jmiDTOptionsShowProducts.setEnabled(true);
		jmiDROptionsShowProducts.setEnabled(true);
	}

	private static void getOutputClasses() {
//		File[] resultFiles;
//		Class resultClass = null;
//		Class[] paramTypes = {EdgeTable[].class, EdgeField[].class};
//		Class[] paramTypesNull = {};
//		Constructor conResultClass;
//		Object[] args = {tables, fields};
//		Object objOutput = null;
//
//		resultFiles = outputDir.listFiles();
//		alProductNames.clear();
//		alSubclasses.clear();
//
//		try {
//			for (int i = 0; i < resultFiles.length; i++) {
//				System.out.println(resultFiles[i].getName());
//
//				if (!resultFiles[i].getName().endsWith(".class")) {
//					continue; //ignore all files that are not .class files
//				}
//
//				resultClass = Class.forName(resultFiles[i].getName().substring(0, resultFiles[i].getName().lastIndexOf(".")));
//
//				if (resultClass.getSuperclass().getName().equals("EdgeConvertCreateDDL")) { //only interested in classes that extend EdgeConvert.EdgeConvertCreateDDL
//					if (parseFile == null && saveFile == null) {
//						conResultClass = resultClass.getConstructor(paramTypesNull);
//					} else {
//						conResultClass = resultClass.getConstructor(paramTypes);
//						objOutput = conResultClass.newInstance(args);
//					}
//					alSubclasses.add(objOutput);
//					Method getProductName = resultClass.getMethod("getProductName", null);
//					String productName = (String) getProductName.invoke(objOutput, null);
//					alProductNames.add(productName);
//				}
//			}

//		} catch (InstantiationException ie) {
//			ie.printStackTrace();
//		} catch (ClassNotFoundException cnfe) {
//			cnfe.printStackTrace();
//		} catch (IllegalAccessException iae) {
//			iae.printStackTrace();
//		} catch (NoSuchMethodException nsme) {
//			nsme.printStackTrace();
//		} catch (InvocationTargetException ite) {
//			ite.printStackTrace();
//		}
//		if (alProductNames.size() > 0 && alSubclasses.size() > 0) { //do not recreate productName and objSubClasses arrays if the new path is empty of valid files
//			productNames = (String[]) alProductNames.toArray(new String[alProductNames.size()]);
//			objSubclasses = alSubclasses.toArray(new Object[alSubclasses.size()]);
//		}
	}

	static String displayProductNames() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < productNames.length; i++) {
			sb.append(productNames[i] + "\n");
		}
		return sb.toString();
	}

	public void actionPerformed(ActionEvent ae) {
		try {
			new CreateSQLFileWriter(fields, tables).makeIt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getSQLStatements() {
		String strSQLString = "";
		String response = (String) JOptionPane.showInputDialog(
				null,
				"Select a product:",
				"Create DDL",
				JOptionPane.PLAIN_MESSAGE,
				null,
				productNames,
				null);

		if (response == null) {
			return CANCELLED;
		}

		int selected;
		for (selected = 0; selected < productNames.length; selected++) {
			if (response.equals(productNames[selected])) {
				break;
			}
		}

		try {
			Class selectedSubclass = objSubclasses[selected].getClass();
			Method getSQLString = selectedSubclass.getMethod("getSQLString", null);
			Method getDatabaseName = selectedSubclass.getMethod("getDatabaseName", null);
			strSQLString = (String) getSQLString.invoke(objSubclasses[selected], null);
			databaseName = (String) getDatabaseName.invoke(objSubclasses[selected], null);
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
		} catch (NoSuchMethodException nsme) {
			nsme.printStackTrace();
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		}

		return strSQLString;
	}
}