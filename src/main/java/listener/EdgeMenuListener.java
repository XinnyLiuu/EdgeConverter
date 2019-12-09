package listener;

import edgeconvert.EdgeConvertFileParser;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static edgeconvert.EdgeConvertGUI.*;
import static listener.CreateDDLButtonListener.*;
import static screen.DefineRelationScreen.*;
import static screen.DefineTableScreen.*;
import static utils.Constants.DEFINE_RELATIONS;
import static utils.Constants.DEFINE_TABLES;

public class EdgeMenuListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
        int returnVal;
        if ((ae.getSource() == jmiDTOpenEdge) || (ae.getSource() == jmiDROpenEdge)) {
            if (!dataSaved) {
                int answer = JOptionPane.showConfirmDialog(null, "You currently have unsaved data. Continue?",
                        "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            jfcEdge.addChoosableFileFilter(effEdge);
            returnVal = jfcEdge.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                parseFile = jfcEdge.getSelectedFile();
                ecfp = new EdgeConvertFileParser(parseFile);
                tables = ecfp.getEdgeTables();
                for (int i = 0; i < tables.length; i++) {
                    tables[i].makeArrays();
                }
                fields = ecfp.getEdgeFields();
                ecfp = null;
                populateLists();
                saveFile = null;
                jmiDTSave.setEnabled(false);
                jmiDRSave.setEnabled(false);
                jmiDTSaveAs.setEnabled(true);
                jmiDRSaveAs.setEnabled(true);
                jbDTDefineRelations.setEnabled(true);

                jbDTCreateDDL.setEnabled(true);
                jbDRCreateDDL.setEnabled(true);

                truncatedFilename = parseFile.getName().substring(parseFile.getName().lastIndexOf(File.separator) + 1);
                jfDT.setTitle(DEFINE_TABLES + " - " + truncatedFilename);
                jfDR.setTitle(DEFINE_RELATIONS + " - " + truncatedFilename);
            } else {
                return;
            }
            dataSaved = true;
        }

        if ((ae.getSource() == jmiDTOpenSave) || (ae.getSource() == jmiDROpenSave)) {
            if (!dataSaved) {
                int answer = JOptionPane.showConfirmDialog(null, "You currently have unsaved data. Continue?",
                        "Are you sure?", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            jfcEdge.addChoosableFileFilter(effSave);
            returnVal = jfcEdge.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                saveFile = jfcEdge.getSelectedFile();
                ecfp = new EdgeConvertFileParser(saveFile);
                tables = ecfp.getEdgeTables();
                fields = ecfp.getEdgeFields();
                ecfp = null;
                populateLists();
                parseFile = null;
                jmiDTSave.setEnabled(true);
                jmiDRSave.setEnabled(true);
                jmiDTSaveAs.setEnabled(true);
                jmiDRSaveAs.setEnabled(true);
                jbDTDefineRelations.setEnabled(true);

                jbDTCreateDDL.setEnabled(true);
                jbDRCreateDDL.setEnabled(true);

                truncatedFilename = saveFile.getName().substring(saveFile.getName().lastIndexOf(File.separator) + 1);
                jfDT.setTitle(DEFINE_TABLES + " - " + truncatedFilename);
                jfDR.setTitle(DEFINE_RELATIONS + " - " + truncatedFilename);
            } else {
                return;
            }
            dataSaved = true;
        }

        if ((ae.getSource() == jmiDTSaveAs) || (ae.getSource() == jmiDRSaveAs) ||
                (ae.getSource() == jmiDTSave) || (ae.getSource() == jmiDRSave)) {
            if ((ae.getSource() == jmiDTSaveAs) || (ae.getSource() == jmiDRSaveAs)) {
                saveAs();
            } else {
                writeSave();
            }
        }

        if ((ae.getSource() == jmiDTExit) || (ae.getSource() == jmiDRExit)) {
            if (!dataSaved) {
                int answer = JOptionPane.showOptionDialog(null,
                        "You currently have unsaved data. Would you like to save?",
                        "Are you sure?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if (answer == JOptionPane.YES_OPTION) {
                    if (saveFile == null) {
                        saveAs();
                    }
                }
                if ((answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
                    return;
                }
            }
            System.exit(0); //No was selected
        }

        if ((ae.getSource() == jmiDTOptionsOutputLocation) || (ae.getSource() == jmiDROptionsOutputLocation)) {
            setOutputDir();
        }

        if ((ae.getSource() == jmiDTOptionsShowProducts) || (ae.getSource() == jmiDROptionsShowProducts)) {
            JOptionPane.showMessageDialog(null, "The available products to create DDL statements are:\n" + displayProductNames());
        }

        /**
         * HELP ABOUT
         */
        if ((ae.getSource() == jmiDTHelpAbout) || (ae.getSource() == jmiDRHelpAbout)) {
            JOptionPane.showMessageDialog(null, "EdgeConvert ERD To DDL Conversion Tool\n" +
                    "by Stephen A. Capperell\n" +
                    "� 2007-2008");
        }

        /**
         * HELP FAQ
         */
        if ((ae.getSource() == jmiDTHelpFAQ) || (ae.getSource() == jmiDRHelpFAQ)) {
            JTextPane jtp = new JTextPane();
            Document doc = jtp.getDocument();

            try {
                doc.insertString(doc.getLength(), "What type of file can I use with this system? \n " +
                        "\t This system exclusively accepts Edge Files which will end with \".edg\". These types of files are a text-based representation of a graphical view of an ER Diagram for a database and can be created via the Edge Diagrammer software available here. A limited time free trial is available to all users! Link: http://www.pacestar.com/edge/trial.htm \n\n" +
                        "How do I make a .edg file? \n" +
                        "\t If you do not already have an edge file, you can download the Edge Diagrammer software from here and create one. This software is an easy to use system for diagramming a database in standard ERD format and can convert what you have created into an Edge file. Link: http://www.pacestar.com/edge/trial.htm \n\n" +
                        "What are the different attributes for each field? \n " +
                        "\t Once you have selected a Table and a Field in the \"Define Tables\" window, the right-most section of the GUI will be populated with that Fields attribute values. The radio buttons represent the possible Field types; varchar means letters or numbers, boolean means True or False, Integer is a whole number and Double is a number that may or may not contain decimals. \"Set Varchar Length\" sets a limit to the maximum amount of characters allowed if the Field is a varchar. The Primary Key box will be checked if the Field is a unique identifier of the selected Table. The Disallow Null box will be checked if the Field is required to have a value. Finally, the Set Default Value box will be populated with the value that will be automatically inserted into any record that does not already have a value for that Field; this will be useful for any field which does not allow null values.", new SimpleAttributeSet());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            jtp.setSize(1200, 850);
            jtp.setPreferredSize(new Dimension(1200, jtp.getPreferredSize().height));

            JOptionPane.showMessageDialog(null, jtp, "FAQ", JOptionPane.INFORMATION_MESSAGE);
        }

        /**
         * HELP Instructions
         */
        if ((ae.getSource() == jmiDTHelpInstructions) || (ae.getSource() == jmiDRHelpInstructions)) {
            JTextPane jtp = new JTextPane();
            Document doc = jtp.getDocument();

            try {
                doc.insertString(doc.getLength(), "Introduction \n " +
                        "\t This software is meant to assist database designers in converting an ERD to a DDL. ERD's, or Entity Relationship Diagrams, are graphical representations of a database, its tables, fields and any relationships that exist between tables. A DDL, or Data Definition Language, is a file containing the various SQL commands to automatically create a database. This software allows a user to load in an Edge file created in the Edge Diagrammer software, modify it as they see fit, and convert it into a DDL for use with a Database Management System. \n\n" +
                        "Opening a file \n" +
                        "\t Upon launching this software, you will be presented with a blank display of the Tables section of the system. To start, first click \"File\" -> \"Open Edge File\". This menu will only accept files which end with the \".edg\" extension. Navigate to the location of your Edge file and double click it or click it and then select Open. This will automatically load the data into the system and the \"All Tables\" column will display all of the tables defined in your Edge file.  \n\n" +
                        "Define Tables \n " +
                        "\t Once you have loaded in an Edge file, all of the tables present in the Edge file will be displayed. Clicking on any of the tables will then populate the Fields list with all of the filds which that table contains. If you then select one of the fields, the section on the right of the GUI will populate with a full description of the type, size, whether or not it is a primary key, whether or not it can be null and the default value of the field. You are able to change any of these attributes to whatever you see fit and the changes will be stored for you for future reference. \n\n " +
                        "Define Relations Section \n " +
                        "\t If you select the \"Define Relations\" button along the bottom it will take you to a new window which will display all of the tables from your Edge file in the top left box. Selecting one of these tables will display all of the Fields which are contained in that Table much like the last window, and it will also show any of the Tables which have a relationship with the selected Table. These related tables will be listed in the bottom left section titled \"Related Tables\". Much like the \"Tables with relations\" box, selecting a table in the \"Related Tables\" box will list all of that tables Fields in the \"Fields in Related Tables\" box in the bottom right. Nothing can be altered here so all of your relationships must be defined in your Edge file but this is window will be helpful for visualizing which tables are related and how they are related. \n\n" +
                        "Saving as a DDL \n " +
                        "\t In order to save your Edge file and any changes, you must first select \"Option\" -> \"Set Output File Definition\" which will open a file explorer. Navigate to where you want to save your DDL file and select a file with a DDL extension. If you do not have one already, you can create a blank .ddl file for the system to use. Once this has been set, you can simply press the \"Create DDL\" button in the bottom right and the contents of your Edge file and any changes you have made in the system will be converted to SQL and saved in the selected output file. If you skipped the step to set the output file, you will be prompted to select one when you press the \"Create DDL\" button.", new SimpleAttributeSet());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            jtp.setSize(1200, 850);
            jtp.setPreferredSize(new Dimension(1200, jtp.getPreferredSize().height));

            JOptionPane.showMessageDialog(null, jtp, "Instructions", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
