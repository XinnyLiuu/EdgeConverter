package edgeconvert;

import pojo.EdgeField;
import pojo.EdgeTable;
import screen.DefineRelationScreen;
import screen.DefineTableScreen;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static listener.CreateDDLButtonListener.saveFile;
import static screen.DefineRelationScreen.*;
import static screen.DefineTableScreen.*;
import static utils.Constants.DEFINE_RELATIONS;
import static utils.Constants.DEFINE_TABLES;

public class EdgeConvertGUI {
    public static boolean dataSaved = true;
    public static EdgeTable[] tables; //master copy of POJOs.EdgeTable objects
    public static EdgeField[] fields; //master copy of POJOs.EdgeField objects
    public static String truncatedFilename;
    public static EdgeConvertFileParser ecfp;
    public static EdgeConvertCreateDDL eccd;

    public EdgeConvertGUI() {
        this.showGUI();
    }

    public static void saveAs() {
        int returnVal;
        jfcEdge.addChoosableFileFilter(effSave);
        returnVal = jfcEdge.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = jfcEdge.getSelectedFile();
            if (saveFile.exists()) {
                int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            if (!saveFile.getName().endsWith("sav")) {
                String temp = saveFile.getAbsolutePath() + ".sav";
                saveFile = new File(temp);
            }
            jmiDTSave.setEnabled(true);
            truncatedFilename = saveFile.getName().substring(saveFile.getName().lastIndexOf(File.separator) + 1);
            jfDT.setTitle(DEFINE_TABLES + " - " + truncatedFilename);
            jfDR.setTitle(DEFINE_RELATIONS + " - " + truncatedFilename);
        } else {
            return;
        }

        writeSave();
    }

    public static void writeSave() {
        try {
            new CreateSQLFileWriter(fields, tables).makeIt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the GUI
     */
    public void showGUI() {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //use the OS native LAF, as opposed to default Java LAF
//		} catch (Exception e) {
//			System.out.println("Error setting native LAF: " + e);
//		}

        new DefineTableScreen(); // Show define table screen
        new DefineRelationScreen(); // Show define relation screen
    }

    private void clearDRControls() {
        jlDRTablesRelations.clearSelection();
        jlDRTablesRelatedTo.clearSelection();
        jlDRFieldsTablesRelations.clearSelection();
        jlDRFieldsTablesRelatedTo.clearSelection();
    }
}
