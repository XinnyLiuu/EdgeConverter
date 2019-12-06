package edgeconvert;

import pojo.EdgeField;
import pojo.EdgeTable;
import screen.DefineRelationScreen;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateSQLFileWriter {
    private EdgeField[] ef;
    private EdgeTable[] et;

    private String file = "default";

    public CreateSQLFileWriter(EdgeField[] ef, EdgeTable[] et) {
        this.et = et;
        this.ef = ef;
    }

    public void makeIt() throws IOException {
        File output = new File("./" + generateFileName() + ".sql");
        CreateDDLMySQL cmd = new CreateDDLMySQL(et, ef);
        FileWriter fw = new FileWriter(output);
        fw.write(cmd.getSQLString());
        fw.flush();
        fw.close();
    }

    public String generateFileName() { //prompts user for database name
        String dbNameDefault = "default";
        //String databaseName = "";

        do {
            file = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter the file/database name:",
                    "File/Database Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    dbNameDefault);
            if (file == null) {
                DefineRelationScreen.setReadSuccess(false);
                return "";
            }
            if (file.equals("")) {
                JOptionPane.showMessageDialog(null, "You must select a name for your file and database.");
            }
        } while (file.equals(""));
        return file;
    }
}
