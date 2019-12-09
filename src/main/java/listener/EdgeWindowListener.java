package listener;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static edgeconvert.EdgeConvertGUI.*;
import static listener.CreateDDLButtonListener.saveFile;
import static screen.DefineRelationScreen.jfDR;
import static screen.DefineTableScreen.jfDT;

public class EdgeWindowListener implements WindowListener {
    public void windowActivated(WindowEvent we) {
    }

    public void windowClosed(WindowEvent we) {
    }

    public void windowDeactivated(WindowEvent we) {
    }

    public void windowDeiconified(WindowEvent we) {
    }

    public void windowIconified(WindowEvent we) {
    }

    public void windowOpened(WindowEvent we) {
    }

    public void windowClosing(WindowEvent we) {
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
                writeSave();
            }
            if ((answer == JOptionPane.CANCEL_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
                if (we.getSource() == jfDT) {
                    jfDT.setVisible(true);
                }
                if (we.getSource() == jfDR) {
                    jfDR.setVisible(true);
                }
                return;
            }
        }
        System.exit(0); //No was selected
    }
}