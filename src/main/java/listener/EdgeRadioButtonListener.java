package listener;

import pojo.EdgeField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edgeconvert.EdgeConvertGUI.dataSaved;
import static screen.DefineTableScreen.*;

public class EdgeRadioButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
        for (int i = 0; i < jrbDataType.length; i++) {
            if (jrbDataType[i].isSelected()) {
                currentDTField.setDataType(i);
                break;
            }
        }
        if (jrbDataType[0].isSelected()) {
            jtfDTVarchar.setText(Integer.toString(EdgeField.VARCHAR_DEFAULT_LENGTH));
            jbDTVarchar.setEnabled(true);
        } else {
            jtfDTVarchar.setText("");
            jbDTVarchar.setEnabled(false);
        }
        jtfDTDefaultValue.setText("");
        currentDTField.setDefaultValue("");
        dataSaved = false;
    }
}
