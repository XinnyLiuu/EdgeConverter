package screen;

import edgeconvert.ExampleFileFilter;
import listener.CreateDDLButtonListener;
import listener.EdgeMenuListener;
import listener.EdgeRadioButtonListener;
import listener.EdgeWindowListener;
import pojo.EdgeField;
import pojo.EdgeTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

import static edgeconvert.EdgeConvertGUI.*;
import static screen.DefineRelationScreen.*;
import static utils.Constants.*;

public class DefineTableScreen {
	public static JFrame jfDT;
	public static JButton jbDTVarchar;
	public static JRadioButton[] jrbDataType;
	public static JTextField jtfDTVarchar;
	public static JTextField jtfDTDefaultValue;
	public static DefaultListModel dlmDTTablesAll, dlmDTFieldsTablesAll;
	public static EdgeTable currentDTTable;
	public static EdgeField currentDTField;
	public static ExampleFileFilter effEdge;
	public static ExampleFileFilter effSave;
	public static JButton jbDTCreateDDL;
	public static JMenuItem jmiDTOptionsShowProducts;
	public static JMenuItem jmiDTSave;
	public static JMenuItem jmiDTOpenEdge;
	public static JMenuItem jmiDTSaveAs;
	public static JButton jbDTDefineRelations;
	public static JMenuItem jmiDTOpenSave;
	public static JMenuItem jmiDTExit;
	public static JMenuItem jmiDTOptionsOutputLocation;
	public static JMenuItem jmiDTHelpAbout;
	public static JMenuItem jmiDTHelpFAQ;
	public static JMenuItem jmiDTHelpInstructions;

	private static ExampleFileFilter effClass;
	private static JButton jbDTDefaultValue;
	private static String[] strDataType;
	private static JCheckBox jcheckDTDisallowNull;
	private static JCheckBox jcheckDTPrimaryKey;

	private JButton jbDTMoveUp;
	private JButton jbDTMoveDown;
	private JList jlDTTablesAll, jlDTFieldsTablesAll;

	private EdgeWindowListener edgeWindowListener;
	private EdgeMenuListener menuListener;
	private CreateDDLButtonListener createDDLListener;
	private EdgeRadioButtonListener radioListener;

	public DefineTableScreen() {
		this.radioListener = new EdgeRadioButtonListener();
		this.createDDLListener = new CreateDDLButtonListener();
		this.menuListener = new EdgeMenuListener();
		this.edgeWindowListener = new EdgeWindowListener();
		this.createDTScreen();
	}

	public static void disableControls() {
		for (int i = 0; i < strDataType.length; i++) {
			jrbDataType[i].setEnabled(false);
		}
		jcheckDTPrimaryKey.setEnabled(false);
		jcheckDTDisallowNull.setEnabled(false);
		jbDTDefaultValue.setEnabled(false);
		jtfDTVarchar.setText("");
		jtfDTDefaultValue.setText("");
	}

	/**
	 * Creates define table screen
	 */
	private void createDTScreen() {
		jfDT = new JFrame(DEFINE_TABLES);
		jfDT.setLocation(HORIZ_LOC, VERT_LOC);
		Container cp = jfDT.getContentPane();
		jfDT.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jfDT.addWindowListener(edgeWindowListener);
		jfDT.getContentPane().setLayout(new BorderLayout());
		jfDT.setVisible(true);
		jfDT.setSize(HORIZ_SIZE + 150, VERT_SIZE);

		//setup menubars and menus
		JMenuBar jmbDTMenuBar = new JMenuBar();
		jfDT.setJMenuBar(jmbDTMenuBar);

		JMenu jmDTFile = new JMenu("File");
		jmDTFile.setMnemonic(KeyEvent.VK_F);
		jmbDTMenuBar.add(jmDTFile);
		jmiDTOpenEdge = new JMenuItem("Open Edge File");
		jmiDTOpenEdge.setMnemonic(KeyEvent.VK_E);
		jmiDTOpenEdge.addActionListener(menuListener);
		jmiDTOpenSave = new JMenuItem("Open Save File");
		jmiDTOpenSave.setMnemonic(KeyEvent.VK_V);
		jmiDTOpenSave.addActionListener(menuListener);
		jmiDTSave = new JMenuItem("Save");
		jmiDTSave.setMnemonic(KeyEvent.VK_S);
		jmiDTSave.setEnabled(false);
		jmiDTSave.addActionListener(menuListener);
		jmiDTSaveAs = new JMenuItem("Save As...");
		jmiDTSaveAs.setMnemonic(KeyEvent.VK_A);
		jmiDTSaveAs.setEnabled(false);
		jmiDTSaveAs.addActionListener(menuListener);
		jmiDTExit = new JMenuItem("Exit");
		jmiDTExit.setMnemonic(KeyEvent.VK_X);
		jmiDTExit.addActionListener(menuListener);
		jmDTFile.add(jmiDTOpenEdge);
		jmDTFile.add(jmiDTOpenSave);
		jmDTFile.add(jmiDTSave);
		jmDTFile.add(jmiDTSaveAs);
		jmDTFile.add(jmiDTExit);

		JMenu jmDTOptions = new JMenu("Options");
		jmDTOptions.setMnemonic(KeyEvent.VK_O);
		jmbDTMenuBar.add(jmDTOptions);
		jmiDTOptionsOutputLocation = new JMenuItem("Set Output File Definition Location");
		jmiDTOptionsOutputLocation.setMnemonic(KeyEvent.VK_S);
		jmiDTOptionsOutputLocation.addActionListener(menuListener);
		jmiDTOptionsShowProducts = new JMenuItem("Show Database Products Available");
		jmiDTOptionsShowProducts.setMnemonic(KeyEvent.VK_H);
		jmiDTOptionsShowProducts.setEnabled(false);
		jmiDTOptionsShowProducts.addActionListener(menuListener);
		jmDTOptions.add(jmiDTOptionsOutputLocation);
		jmDTOptions.add(jmiDTOptionsShowProducts);

		/**
		 * HELP section
		 */
		JMenu jmDTHelp = new JMenu("Help");
		jmDTHelp.setMnemonic(KeyEvent.VK_H);
		jmbDTMenuBar.add(jmDTHelp);

		jmiDTHelpAbout = new JMenuItem("About");
		jmiDTHelpAbout.setMnemonic(KeyEvent.VK_A);
		jmiDTHelpAbout.addActionListener(menuListener);
		jmDTHelp.add(jmiDTHelpAbout);

		jmiDTHelpFAQ = new JMenuItem("FAQ");
		jmiDTHelpFAQ.setMnemonic(KeyEvent.VK_F);
		jmiDTHelpFAQ.addActionListener(menuListener);
		jmDTHelp.add(jmiDTHelpFAQ);

		jmiDTHelpInstructions = new JMenuItem("Instructions");
		jmiDTHelpInstructions.setMnemonic(KeyEvent.VK_I);
		jmiDTHelpInstructions.addActionListener(menuListener);
		jmDTHelp.add(jmiDTHelpInstructions);

		jfcEdge = new JFileChooser();
		jfcOutputDir = new JFileChooser();
		effEdge = new ExampleFileFilter("edg", "Edge Diagrammer Files");
		effSave = new ExampleFileFilter("sav", "Edge Convert Save Files");
		jfcOutputDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JPanel jpDTBottom = new JPanel(new GridLayout(1, 2));

		jbDTCreateDDL = new JButton("Create DDL");
		jbDTCreateDDL.setEnabled(false);
		jbDTCreateDDL.addActionListener(createDDLListener);

		jbDTDefineRelations = new JButton(DEFINE_RELATIONS);
		jbDTDefineRelations.setEnabled(false);
		jbDTDefineRelations.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						jfDT.setVisible(false);
						jfDR.setVisible(true); //show the Define Relations screen
						clearDTControls();
						dlmDTFieldsTablesAll.removeAllElements();
					}
				}
		);

		jpDTBottom.add(jbDTDefineRelations);
		jpDTBottom.add(jbDTCreateDDL);
		jfDT.getContentPane().add(jpDTBottom, BorderLayout.SOUTH);

		JPanel jpDTCenter = new JPanel(new GridLayout(1, 3));
		JPanel jpDTCenterRight = new JPanel(new GridLayout(1, 2));
		dlmDTTablesAll = new DefaultListModel();
		jlDTTablesAll = new JList(dlmDTTablesAll);
		jlDTTablesAll.addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent lse) {
						int selIndex = jlDTTablesAll.getSelectedIndex();
						if (selIndex >= 0) {
							String selText = dlmDTTablesAll.getElementAt(selIndex).toString();
							setCurrentDTTable(selText); //set pointer to the selected table
							int[] currentNativeFields = currentDTTable.getNativeFieldsArray();
							jlDTFieldsTablesAll.clearSelection();
							dlmDTFieldsTablesAll.removeAllElements();
							jbDTMoveUp.setEnabled(false);
							jbDTMoveDown.setEnabled(false);
							for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
								dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
							}
						}
						disableControls();
					}
				}
		);

		dlmDTFieldsTablesAll = new DefaultListModel();
		jlDTFieldsTablesAll = new JList(dlmDTFieldsTablesAll);
		jlDTFieldsTablesAll.addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent lse) {
						int selIndex = jlDTFieldsTablesAll.getSelectedIndex();
						if (selIndex >= 0) {
							if (selIndex == 0) {
								jbDTMoveUp.setEnabled(false);
							} else {
								jbDTMoveUp.setEnabled(true);
							}
							if (selIndex == (dlmDTFieldsTablesAll.getSize() - 1)) {
								jbDTMoveDown.setEnabled(false);
							} else {
								jbDTMoveDown.setEnabled(true);
							}
							String selText = dlmDTFieldsTablesAll.getElementAt(selIndex).toString();
							setCurrentDTField(selText); //set pointer to the selected field
							enableControls();
							jrbDataType[currentDTField.getDataType()].setSelected(true); //select the appropriate radio button, based on value of dataType
							if (jrbDataType[0].isSelected()) { //this is the Varchar radio button
								jbDTVarchar.setEnabled(true); //enable the Varchar button
								jtfDTVarchar.setText(Integer.toString(currentDTField.getVarcharValue())); //fill text field with varcharValue
							} else { //some radio button other than Varchar is selected
								jtfDTVarchar.setText(""); //clear the text field
								jbDTVarchar.setEnabled(false); //disable the button
							}
							jcheckDTPrimaryKey.setSelected(currentDTField.getIsPrimaryKey()); //clear or set Primary Key checkbox
							jcheckDTDisallowNull.setSelected(currentDTField.getDisallowNull()); //clear or set Disallow Null checkbox
							jtfDTDefaultValue.setText(currentDTField.getDefaultValue()); //fill text field with defaultValue
						}
					}
				}
		);

		JPanel jpDTMove = new JPanel(new GridLayout(2, 1));
		jbDTMoveUp = new JButton("^");
		jbDTMoveUp.setEnabled(false);
		jbDTMoveUp.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						int selection = jlDTFieldsTablesAll.getSelectedIndex();
						currentDTTable.moveFieldUp(selection);
						//repopulate Fields List
						int[] currentNativeFields = currentDTTable.getNativeFieldsArray();
						jlDTFieldsTablesAll.clearSelection();
						dlmDTFieldsTablesAll.removeAllElements();
						for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
							dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
						}
						jlDTFieldsTablesAll.setSelectedIndex(selection - 1);
						dataSaved = false;
					}
				}
		);

		jbDTMoveDown = new JButton("v");
		jbDTMoveDown.setEnabled(false);
		jbDTMoveDown.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						int selection = jlDTFieldsTablesAll.getSelectedIndex(); //the original selected index
						currentDTTable.moveFieldDown(selection);
						//repopulate Fields List
						int[] currentNativeFields = currentDTTable.getNativeFieldsArray();
						jlDTFieldsTablesAll.clearSelection();
						dlmDTFieldsTablesAll.removeAllElements();
						for (int fIndex = 0; fIndex < currentNativeFields.length; fIndex++) {
							dlmDTFieldsTablesAll.addElement(getFieldName(currentNativeFields[fIndex]));
						}
						jlDTFieldsTablesAll.setSelectedIndex(selection + 1);
						dataSaved = false;
					}
				}
		);
		jpDTMove.add(jbDTMoveUp);
		jpDTMove.add(jbDTMoveDown);

		JScrollPane jspDTTablesAll = new JScrollPane(jlDTTablesAll);
		JScrollPane jspDTFieldsTablesAll = new JScrollPane(jlDTFieldsTablesAll);
		JPanel jpDTCenter1 = new JPanel(new BorderLayout());
		JPanel jpDTCenter2 = new JPanel(new BorderLayout());
		JLabel jlabDTTables = new JLabel("All Tables", SwingConstants.CENTER);
		JLabel jlabDTFields = new JLabel("Fields List", SwingConstants.CENTER);
		jpDTCenter1.add(jlabDTTables, BorderLayout.NORTH);
		jpDTCenter2.add(jlabDTFields, BorderLayout.NORTH);
		jpDTCenter1.add(jspDTTablesAll, BorderLayout.CENTER);
		jpDTCenter2.add(jspDTFieldsTablesAll, BorderLayout.CENTER);
		jpDTCenter2.add(jpDTMove, BorderLayout.EAST);
		jpDTCenter.add(jpDTCenter1);
		jpDTCenter.add(jpDTCenter2);
		jpDTCenter.add(jpDTCenterRight);

		strDataType = EdgeField.getStrDataType(); //get the list of currently supported data types
		jrbDataType = new JRadioButton[strDataType.length]; //create array of JRadioButtons, one for each supported data type
		ButtonGroup bgDTDataType = new ButtonGroup();
		JPanel jpDTCenterRight1 = new JPanel(new GridLayout(strDataType.length, 1));
		for (int i = 0; i < strDataType.length; i++) {
			jrbDataType[i] = new JRadioButton(strDataType[i]); //assign label for radio button from String array
			jrbDataType[i].setEnabled(false);
			jrbDataType[i].addActionListener(radioListener);
			bgDTDataType.add(jrbDataType[i]);
			jpDTCenterRight1.add(jrbDataType[i]);
		}
		jpDTCenterRight.add(jpDTCenterRight1);

		jcheckDTDisallowNull = new JCheckBox("Disallow Null");
		jcheckDTDisallowNull.setEnabled(false);
		jcheckDTDisallowNull.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent ie) {
						currentDTField.setDisallowNull(jcheckDTDisallowNull.isSelected());
						dataSaved = false;
					}
				}
		);

		jcheckDTPrimaryKey = new JCheckBox("Primary Key");
		jcheckDTPrimaryKey.setEnabled(false);
		jcheckDTPrimaryKey.addItemListener(
				new ItemListener() {
					public void itemStateChanged(ItemEvent ie) {
						currentDTField.setIsPrimaryKey(jcheckDTPrimaryKey.isSelected());
						dataSaved = false;
					}
				}
		);

		jbDTDefaultValue = new JButton("Set Default Value");
		jbDTDefaultValue.setEnabled(false);
		jbDTDefaultValue.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						String prev = jtfDTDefaultValue.getText();
						boolean goodData = false;
						int i = currentDTField.getDataType();
						do {
							String result = (String) JOptionPane.showInputDialog(
									null,
									"Enter the default value:",
									"Default Value",
									JOptionPane.PLAIN_MESSAGE,
									null,
									null,
									prev);

							if ((result == null)) {
								jtfDTDefaultValue.setText(prev);
								return;
							}
							switch (i) {
								case 0: //varchar
									if (result.length() <= Integer.parseInt(jtfDTVarchar.getText())) {
										jtfDTDefaultValue.setText(result);
										goodData = true;
									} else {
										JOptionPane.showMessageDialog(null, "The length of this value must be less than or equal to the Varchar length specified.");
									}
									break;
								case 1: //boolean
									String newResult = result.toLowerCase();
									if (newResult.equals("true") || newResult.equals("false")) {
										jtfDTDefaultValue.setText(newResult);
										goodData = true;
									} else {
										JOptionPane.showMessageDialog(null, "You must input a valid boolean value (\"true\" or \"false\").");
									}
									break;
								case 2: //Integer
									try {
										int intResult = Integer.parseInt(result);
										jtfDTDefaultValue.setText(result);
										goodData = true;
									} catch (NumberFormatException nfe) {
										JOptionPane.showMessageDialog(null, "\"" + result + "\" is not an integer or is outside the bounds of valid integer values.");
									}
									break;
								case 3: //Double
									try {
										double doubleResult = Double.parseDouble(result);
										jtfDTDefaultValue.setText(result);
										goodData = true;
									} catch (NumberFormatException nfe) {
										JOptionPane.showMessageDialog(null, "\"" + result + "\" is not a double or is outside the bounds of valid double values.");
									}
									break;
								case 4: //Timestamp
									try {
										jtfDTDefaultValue.setText(result);
										goodData = true;
									} catch (Exception e) {

									}
									break;
							}
						} while (!goodData);
						int selIndex = jlDTFieldsTablesAll.getSelectedIndex();
						if (selIndex >= 0) {
							String selText = dlmDTFieldsTablesAll.getElementAt(selIndex).toString();
							setCurrentDTField(selText);
							currentDTField.setDefaultValue(jtfDTDefaultValue.getText());
						}
						dataSaved = false;
					}
				}
		); //jbDTDefaultValue.addActionListener

		jtfDTDefaultValue = new JTextField();
		jtfDTDefaultValue.setEditable(false);

		jbDTVarchar = new JButton("Set Varchar Length");
		jbDTVarchar.setEnabled(false);
		jbDTVarchar.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						String prev = jtfDTVarchar.getText();
						String result = (String) JOptionPane.showInputDialog(
								null,
								"Enter the varchar length:",
								"Varchar Length",
								JOptionPane.PLAIN_MESSAGE,
								null,
								null,
								prev);
						if ((result == null)) {
							jtfDTVarchar.setText(prev);
							return;
						}
						int selIndex = jlDTFieldsTablesAll.getSelectedIndex();
						int varchar;
						try {
							if (result.length() > 5) {
								JOptionPane.showMessageDialog(null, "Varchar length must be greater than 0 and less than or equal to 65535.");
								jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
								return;
							}
							varchar = Integer.parseInt(result);
							if (varchar > 0 && varchar <= 65535) { // max length of varchar is 255 before v5.0.3
								jtfDTVarchar.setText(Integer.toString(varchar));
								currentDTField.setVarcharValue(varchar);
							} else {
								JOptionPane.showMessageDialog(null, "Varchar length must be greater than 0 and less than or equal to 65535.");
								jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
								return;
							}
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null, "\"" + result + "\" is not a number");
							jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
							return;
						}
						dataSaved = false;
					}
				}
		);

		jtfDTVarchar = new JTextField();
		jtfDTVarchar.setEditable(false);

		JPanel jpDTCenterRight2 = new JPanel(new GridLayout(6, 1));
		jpDTCenterRight2.add(jbDTVarchar);
		jpDTCenterRight2.add(jtfDTVarchar);
		jpDTCenterRight2.add(jcheckDTPrimaryKey);
		jpDTCenterRight2.add(jcheckDTDisallowNull);
		jpDTCenterRight2.add(jbDTDefaultValue);
		jpDTCenterRight2.add(jtfDTDefaultValue);
		jpDTCenterRight.add(jpDTCenterRight1);
		jpDTCenterRight.add(jpDTCenterRight2);
		jpDTCenter.add(jpDTCenterRight);
		jfDT.getContentPane().add(jpDTCenter, BorderLayout.CENTER);
		jfDT.validate();
	}

	private void enableControls() {
		for (int i = 0; i < strDataType.length; i++) {
			jrbDataType[i].setEnabled(true);
		}
		jcheckDTPrimaryKey.setEnabled(true);
		jcheckDTDisallowNull.setEnabled(true);
		jbDTVarchar.setEnabled(true);
		jbDTDefaultValue.setEnabled(true);
	}

	private void clearDTControls() {
		jlDTTablesAll.clearSelection();
		jlDTFieldsTablesAll.clearSelection();
	}

	private void setCurrentDTTable(String selText) {
		for (int tIndex = 0; tIndex < tables.length; tIndex++) {
			if (selText.equals(tables[tIndex].getName())) {
				currentDTTable = tables[tIndex];
				return;
			}
		}
	}

	private String getFieldName(int numFigure) {
		for (int fIndex = 0; fIndex < fields.length; fIndex++) {
			if (fields[fIndex].getNumFigure() == numFigure) {
				return fields[fIndex].getName();
			}
		}
		return "";
	}

	private void setCurrentDTField(String selText) {
		for (int fIndex = 0; fIndex < fields.length; fIndex++) {
			if (selText.equals(fields[fIndex].getName()) && fields[fIndex].getTableID() == currentDTTable.getNumFigure()) {
				currentDTField = fields[fIndex];
				return;
			}
		}
	}
}
