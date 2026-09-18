import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class UserInterface {
    JFrame frame;

    public UserInterface() {
        //==================================================
        // Part 1 - Base Frame Development
        // Outputs the creation of the UI to the user via the console
        System.out.print("Attempting to create user interface...");

        // Basic frame components
        this.frame = new JFrame("User Interface");
        frame.setLayout(new GridLayout(0,1));
        GridBagConstraints constraints = new GridBagConstraints();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 550));


        // Defining the three panels main panels where the components will reside
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel2.setLayout(new GridBagLayout());
        panel3.setLayout(new GridBagLayout());
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);


        //==================================================
        // Part 2 - Panel 1

        /*
        //Default copy-pastable for easily managing constraints
        constraints = new GridBagConstraints();
        constraints.gridx = 0;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0,0,0,0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.LINE_START;
        */

        JLabel filePathLabel = new JLabel("File Path");
        constraints = new GridBagConstraints();
        constraints.gridx = 1;          // X Position in grid
        constraints.gridy = 0;          // Y Position in grid
        constraints.gridwidth = 1;      // X scale of component
        constraints.gridheight = 1;     // Y scale of component
        constraints.weightx = 0.1;      // Adjust
        constraints.weighty = 0.1;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.insets = new Insets(10,10,0,0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.LINE_START;
        panel1.add(filePathLabel, constraints);

        JTextField filePathTextField = new JTextField();
        filePathTextField.setPreferredSize(new Dimension(300, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0,10,0,0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(filePathTextField, constraints);

        //==================================================
        // Part 3 - Panel 2
        JTextField outputFolderName = new JTextField("Output Folder Name");
//        panel2.add(outputFolderName);


        //==================================================
        // Part 4 - Panel 3
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        panel3.add(confirmButton);
        panel3.add(cancelButton);


        //==================================================
        // Part 5 - Deploying the UI
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
