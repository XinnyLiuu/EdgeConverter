package screen;

import listener.CreateDDLButtonListener;
import listener.EdgeMenuListener;
import listener.EdgeWindowListener;
import pojo.EdgeField;
import pojo.EdgeTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static edgeconvert.EdgeConvertGUI.*;
import static screen.DefineTableScreen.*;
import static utils.Constants.*;

public class DefineRelationScreen {
    public static JFrame jfDR;
    public static JFileChooser jfcEdge, jfcGetClass, jfcOutputDir;
    public static JList jlDRTablesRelations;
    public static JList jlDRTablesRelatedTo;
    public static JList jlDRFieldsTablesRelations;
    public static JList jlDRFieldsTablesRelatedTo;
    public static JButton jbDRCreateDDL;
    public static JMenuItem jmiDROptionsShowProducts;
    public static JMenuItem jmiDROpenEdge;
    public static JMenuItem jmiDRSave;
    public static JMenuItem jmiDRSaveAs;
    public static JMenuItem jmiDROpenSave;
    public static JMenuItem jmiDRExit;
    public static JMenuItem jmiDROptionsOutputLocation;
    public static JMenuItem jmiDRHelpAbout;
    public static JMenuItem jmiDRHelpFAQ;
    public static JMenuItem jmiDRHelpInstructions;

    private static boolean readSuccess = true; // this tells GUI whether to populate JList components or not
    private static JButton jbDRBindRelation;
    private static DefaultListModel dlmDRTablesRelations, dlmDRTablesRelatedTo, dlmDRFieldsTablesRelations, dlmDRFieldsTablesRelatedTo;

    private DefineTableScreen dtScreen;
    private EdgeTable currentDRTable1, currentDRTable2; //pointers to currently selected table(s) on Define Tables (DT) and Define Relations (DR) screens
    private EdgeField currentDRField1, currentDRField2; //pointers to currently selected field(s) on Define Tables (DT) and Define Relations (DR) screens

    private EdgeWindowListener edgeWindowListener;
    private EdgeMenuListener menuListener;
    private CreateDDLButtonListener createDDLListener;

    public DefineRelationScreen() {
        this.menuListener = new EdgeMenuListener();
        this.edgeWindowListener = new EdgeWindowListener();
        this.createDRScreen();
    }

    public static boolean getReadSuccess() {
        return readSuccess;
    }

    public static void setReadSuccess(boolean value) {
        readSuccess = value;
    }

    private static void depopulateLists() {
        dlmDTTablesAll.clear();
        dlmDTFieldsTablesAll.clear();
        dlmDRTablesRelations.clear();
        dlmDRFieldsTablesRelations.clear();
        dlmDRTablesRelatedTo.clear();
        dlmDRFieldsTablesRelatedTo.clear();
    }

    public static void populateLists() {
        if (readSuccess) {
            jfDT.setVisible(true);
            jfDR.setVisible(false);
            disableControls();
            depopulateLists();
            for (int tIndex = 0; tIndex < tables.length; tIndex++) {
                String tempName = tables[tIndex].getName();
                dlmDTTablesAll.addElement(tempName);
                int[] relatedTables = tables[tIndex].getRelatedTablesArray();
                if (relatedTables.length > 0) {
                    dlmDRTablesRelations.addElement(tempName);
                }
            }
        }
        readSuccess = true;
    }

    /**
     * Creates Define Relations screen
     */
    public void createDRScreen() {
        jfDR = new JFrame(DEFINE_RELATIONS);
        jfDR.setSize(HORIZ_SIZE, VERT_SIZE);
        jfDR.setLocation(HORIZ_LOC, VERT_LOC);
        jfDR.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jfDR.addWindowListener(edgeWindowListener);
        jfDR.getContentPane().setLayout(new BorderLayout());

        //setup menubars and menus
        JMenuBar jmbDRMenuBar = new JMenuBar();
        jfDR.setJMenuBar(jmbDRMenuBar);
        JMenu jmDRFile = new JMenu("File");
        jmDRFile.setMnemonic(KeyEvent.VK_F);
        jmbDRMenuBar.add(jmDRFile);
        jmiDROpenEdge = new JMenuItem("Open Edge File");
        jmiDROpenEdge.setMnemonic(KeyEvent.VK_E);
        jmiDROpenEdge.addActionListener(menuListener);
        jmiDROpenSave = new JMenuItem("Open Save File");
        jmiDROpenSave.setMnemonic(KeyEvent.VK_V);
        jmiDROpenSave.addActionListener(menuListener);
        jmiDRSave = new JMenuItem("Save");
        jmiDRSave.setMnemonic(KeyEvent.VK_S);
        jmiDRSave.setEnabled(false);
        jmiDRSave.addActionListener(menuListener);
        jmiDRSaveAs = new JMenuItem("Save As...");
        jmiDRSaveAs.setMnemonic(KeyEvent.VK_A);
        jmiDRSaveAs.setEnabled(false);
        jmiDRSaveAs.addActionListener(menuListener);
        jmiDRExit = new JMenuItem("Exit");
        jmiDRExit.setMnemonic(KeyEvent.VK_X);
        jmiDRExit.addActionListener(menuListener);
        jmDRFile.add(jmiDROpenEdge);
        jmDRFile.add(jmiDROpenSave);
        jmDRFile.add(jmiDRSave);
        jmDRFile.add(jmiDRSaveAs);
        jmDRFile.add(jmiDRExit);

        JMenu jmDROptions = new JMenu("Options");
        jmDROptions.setMnemonic(KeyEvent.VK_O);
        jmbDRMenuBar.add(jmDROptions);
        jmiDROptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
        jmiDROptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
        jmiDROptionsOutputLocation.addActionListener(menuListener);
        jmiDROptionsShowProducts = new JMenuItem("Show Database Products Available");
        jmiDROptionsShowProducts.setMnemonic(KeyEvent.VK_H);
        jmiDROptionsShowProducts.setEnabled(false);
        jmiDROptionsShowProducts.addActionListener(menuListener);
        jmDROptions.add(jmiDROptionsOutputLocation);
        jmDROptions.add(jmiDROptionsShowProducts);

        /**
         * HELP section
         */
        JMenu jmDRHelp = new JMenu("Help");
        jmDRHelp.setMnemonic(KeyEvent.VK_H);
        jmbDRMenuBar.add(jmDRHelp);

        jmiDRHelpAbout = new JMenuItem("About");
        jmiDRHelpAbout.setMnemonic(KeyEvent.VK_A);
        jmiDRHelpAbout.addActionListener(menuListener);
        jmDRHelp.add(jmiDRHelpAbout);

        jmiDRHelpFAQ = new JMenuItem("FAQ");
        jmiDRHelpFAQ.setMnemonic(KeyEvent.VK_F);
        jmiDRHelpFAQ.addActionListener(menuListener);
        jmDRHelp.add(jmiDRHelpFAQ);

        jmiDRHelpInstructions = new JMenuItem("Instructions");
        jmiDRHelpInstructions.setMnemonic(KeyEvent.VK_I);
        jmiDRHelpInstructions.addActionListener(menuListener);
        jmDRHelp.add(jmiDRHelpInstructions);

        JPanel jpDRCenter = new JPanel(new GridLayout(2, 2));
        JPanel jpDRCenter1 = new JPanel(new BorderLayout());
        JPanel jpDRCenter2 = new JPanel(new BorderLayout());
        JPanel jpDRCenter3 = new JPanel(new BorderLayout());
        JPanel jpDRCenter4 = new JPanel(new BorderLayout());

        dlmDRTablesRelations = new DefaultListModel();
        jlDRTablesRelations = new JList(dlmDRTablesRelations);
        jlDRTablesRelations.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        int selIndex = jlDRTablesRelations.getSelectedIndex();
                        if (selIndex >= 0) {
                            String selText = dlmDRTablesRelations.getElementAt(selIndex).toString();
                            setCurrentDRTable1(selText);
                            int[] currentNativeFields, currentRelatedTables, currentRelatedFields;
                            currentNativeFields = currentDRTable1.getNativeFieldsArray();
                            currentRelatedTables = currentDRTable1.getRelatedTablesArray();
                            jlDRFieldsTablesRelations.clearSelection();
                            jlDRTablesRelatedTo.clearSelection();
                            jlDRFieldsTablesRelatedTo.clearSelection();
                            dlmDRFieldsTablesRelations.removeAllElements();
                            dlmDRTablesRelatedTo.removeAllElements();
                            dlmDRFieldsTablesRelatedTo.removeAllElements();
                            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                                dlmDRFieldsTablesRelations.addElement(getFieldName(currentNativeFields[fIndex]));
                            }
                            for (int rIndex = 0; rIndex < currentRelatedTables.length; rIndex++) {
                                dlmDRTablesRelatedTo.addElement(getTableName(currentRelatedTables[rIndex]));
                            }
                        }
                    }
                }
        );

        dlmDRFieldsTablesRelations = new DefaultListModel();
        jlDRFieldsTablesRelations = new JList(dlmDRFieldsTablesRelations);
        jlDRFieldsTablesRelations.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        int selIndex = jlDRFieldsTablesRelations.getSelectedIndex();
                        if (selIndex >= 0) {
                            String selText = dlmDRFieldsTablesRelations.getElementAt(selIndex).toString();
                            setCurrentDRField1(selText);
                            if (currentDRField1.getFieldBound() == 0) {
                                jlDRTablesRelatedTo.clearSelection();
                                jlDRFieldsTablesRelatedTo.clearSelection();
                                dlmDRFieldsTablesRelatedTo.removeAllElements();
                            } else {
                                jlDRTablesRelatedTo.setSelectedValue(getTableName(currentDRField1.getTableBound()), true);
                                jlDRFieldsTablesRelatedTo.setSelectedValue(getFieldName(currentDRField1.getFieldBound()), true);
                            }
                        }
                    }
                }
        );

        dlmDRTablesRelatedTo = new DefaultListModel();
        jlDRTablesRelatedTo = new JList(dlmDRTablesRelatedTo);
        jlDRTablesRelatedTo.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        int selIndex = jlDRTablesRelatedTo.getSelectedIndex();
                        if (selIndex >= 0) {
                            String selText = dlmDRTablesRelatedTo.getElementAt(selIndex).toString();
                            setCurrentDRTable2(selText);
                            int[] currentNativeFields = currentDRTable2.getNativeFieldsArray();
                            dlmDRFieldsTablesRelatedTo.removeAllElements();
                            for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
                                dlmDRFieldsTablesRelatedTo.addElement(getFieldName(currentNativeFields[fIndex]));
                            }
                        }
                    }
                }
        );

        dlmDRFieldsTablesRelatedTo = new DefaultListModel();
        jlDRFieldsTablesRelatedTo = new JList(dlmDRFieldsTablesRelatedTo);
        jlDRFieldsTablesRelatedTo.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent lse) {
                        int selIndex = jlDRFieldsTablesRelatedTo.getSelectedIndex();
                        if (selIndex >= 0) {
                            String selText = dlmDRFieldsTablesRelatedTo.getElementAt(selIndex).toString();
                            setCurrentDRField2(selText);
                            jbDRBindRelation.setEnabled(true);
                        } else {
                            jbDRBindRelation.setEnabled(false);
                        }
                    }
                }
        );

        JScrollPane jspDRTablesRelations = new JScrollPane(jlDRTablesRelations);
        JScrollPane jspDRFieldsTablesRelations = new JScrollPane(jlDRFieldsTablesRelations);
        JScrollPane jspDRTablesRelatedTo = new JScrollPane(jlDRTablesRelatedTo);
        JScrollPane jspDRFieldsTablesRelatedTo = new JScrollPane(jlDRFieldsTablesRelatedTo);
        JLabel jlabDRTablesRelations = new JLabel("Tables With Relations", SwingConstants.CENTER);
        JLabel jlabDRFieldsTablesRelations = new JLabel("Fields in Tables with Relations", SwingConstants.CENTER);
        JLabel jlabDRTablesRelatedTo = new JLabel("Related Tables", SwingConstants.CENTER);
        JLabel jlabDRFieldsTablesRelatedTo = new JLabel("Fields in Related Tables", SwingConstants.CENTER);
        jpDRCenter1.add(jlabDRTablesRelations, BorderLayout.NORTH);
        jpDRCenter2.add(jlabDRFieldsTablesRelations, BorderLayout.NORTH);
        jpDRCenter3.add(jlabDRTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter4.add(jlabDRFieldsTablesRelatedTo, BorderLayout.NORTH);
        jpDRCenter1.add(jspDRTablesRelations, BorderLayout.CENTER);
        jpDRCenter2.add(jspDRFieldsTablesRelations, BorderLayout.CENTER);
        jpDRCenter3.add(jspDRTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter4.add(jspDRFieldsTablesRelatedTo, BorderLayout.CENTER);
        jpDRCenter.add(jpDRCenter1);
        jpDRCenter.add(jpDRCenter2);
        jpDRCenter.add(jpDRCenter3);
        jpDRCenter.add(jpDRCenter4);
        jfDR.getContentPane().add(jpDRCenter, BorderLayout.CENTER);
        JPanel jpDRBottom = new JPanel(new GridLayout(1, 3));

        JButton jbDRDefineTables = new JButton(DEFINE_TABLES);
        jbDRDefineTables.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        jfDT.setVisible(true); //show the Define Tables screen
                        jfDR.setVisible(false);
                        clearDRControls();
                        depopulateLists();
                        populateLists();
                    }
                }
        );

        jbDRBindRelation = new JButton("Bind/Unbind Relation");
        jbDRBindRelation.setEnabled(false);
        jbDRBindRelation.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        int nativeIndex = jlDRFieldsTablesRelations.getSelectedIndex();
                        int relatedField = currentDRField2.getNumFigure();
                        if (currentDRField1.getFieldBound() == relatedField) { //the selected fields are already bound to each other
                            int answer = JOptionPane.showConfirmDialog(null, "Do you wish to unbind the relation on field " +
                                            currentDRField1.getName() + "?",
                                    "Are you sure?", JOptionPane.YES_NO_OPTION);
                            if (answer == JOptionPane.YES_OPTION) {
                                currentDRTable1.setRelatedField(nativeIndex, 0); //clear the related field
                                currentDRField1.setTableBound(0); //clear the bound table
                                currentDRField1.setFieldBound(0); //clear the bound field
                                jlDRFieldsTablesRelatedTo.clearSelection(); //clear the listbox selection
                            }
                            return;
                        }
                        if (currentDRField1.getFieldBound() != 0) { //field is already bound to a different field
                            int answer = JOptionPane.showConfirmDialog(null, "There is already a relation defined on field " +
                                            currentDRField1.getName() + ", do you wish to overwrite it?",
                                    "Are you sure?", JOptionPane.YES_NO_OPTION);
                            if (answer == JOptionPane.NO_OPTION || answer == JOptionPane.CLOSED_OPTION) {
                                jlDRTablesRelatedTo.setSelectedValue(getTableName(currentDRField1.getTableBound()), true); //revert selections to saved settings
                                jlDRFieldsTablesRelatedTo.setSelectedValue(getFieldName(currentDRField1.getFieldBound()), true); //revert selections to saved settings
                                return;
                            }
                        }
                        if (currentDRField1.getDataType() != currentDRField2.getDataType()) {
                            JOptionPane.showMessageDialog(null, "The datatypes of " + currentDRTable1.getName() + "." +
                                    currentDRField1.getName() + " and " + currentDRTable2.getName() +
                                    "." + currentDRField2.getName() + " do not match.  Unable to bind this relation.");
                            return;
                        }
                        if ((currentDRField1.getDataType() == 0) && (currentDRField2.getDataType() == 0)) {
                            if (currentDRField1.getVarcharValue() != currentDRField2.getVarcharValue()) {
                                JOptionPane.showMessageDialog(null, "The varchar lengths of " + currentDRTable1.getName() + "." +
                                        currentDRField1.getName() + " and " + currentDRTable2.getName() +
                                        "." + currentDRField2.getName() + " do not match.  Unable to bind this relation.");
                                return;
                            }
                        }
                        currentDRTable1.setRelatedField(nativeIndex, relatedField);
                        currentDRField1.setTableBound(currentDRTable2.getNumFigure());
                        currentDRField1.setFieldBound(currentDRField2.getNumFigure());
                        JOptionPane.showMessageDialog(null, "Table " + currentDRTable1.getName() + ": native field " +
                                currentDRField1.getName() + " bound to table " + currentDRTable2.getName() +
                                " on field " + currentDRField2.getName());
                        dataSaved = false;
                    }
                }
        );

        jbDRCreateDDL = new JButton("Create DDL");
        jbDRCreateDDL.setEnabled(false);
        jbDRCreateDDL.addActionListener(createDDLListener);

        jpDRBottom.add(jbDRDefineTables);
        jpDRBottom.add(jbDRBindRelation);
        jpDRBottom.add(jbDRCreateDDL);
        jfDR.getContentPane().add(jpDRBottom, BorderLayout.SOUTH);
    }

    private String getTableName(int numFigure) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (tables[tIndex].getNumFigure() == numFigure) {
                return tables[tIndex].getName();
            }
        }
        return "";
    }

    private String getFieldName(int numFigure) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (fields[fIndex].getNumFigure() == numFigure) {
                return fields[fIndex].getName();
            }
        }
        return "";
    }

    private void setCurrentDRTable1(String selText) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (selText.equals(tables[tIndex].getName())) {
                currentDRTable1 = tables[tIndex];
                return;
            }
        }
    }

    private void setCurrentDRField1(String selText) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (selText.equals(fields[fIndex].getName()) &&
                    fields[fIndex].getTableID() == currentDRTable1.getNumFigure()) {
                currentDRField1 = fields[fIndex];
                return;
            }
        }
    }

    private void setCurrentDRField2(String selText) {
        for (int fIndex = 0; fIndex < fields.length; fIndex++) {
            if (selText.equals(fields[fIndex].getName()) &&
                    fields[fIndex].getTableID() == currentDRTable2.getNumFigure()) {
                currentDRField2 = fields[fIndex];
                return;
            }
        }
    }

    private void setCurrentDRTable2(String selText) {
        for (int tIndex = 0; tIndex < tables.length; tIndex++) {
            if (selText.equals(tables[tIndex].getName())) {
                currentDRTable2 = tables[tIndex];
                return;
            }
        }
    }

    private void clearDRControls() {
        jlDRTablesRelations.clearSelection();
        jlDRTablesRelatedTo.clearSelection();
        jlDRFieldsTablesRelations.clearSelection();
        jlDRFieldsTablesRelatedTo.clearSelection();
    }
}
