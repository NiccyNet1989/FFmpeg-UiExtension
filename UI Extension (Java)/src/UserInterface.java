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
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(Color.WHITE);
        GridBagConstraints constraints = new GridBagConstraints();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 400));
        frame.setResizable(false);


        // Defining the three panels main panels where the components will reside
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel2.setLayout(new GridBagLayout());
        panel3.setLayout(new GridBagLayout());
        panel1.setBackground(Color.WHITE);
        panel2.setBackground(Color.WHITE);
        panel3.setBackground(Color.WHITE);

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

        constraints = new GridBagConstraints();
        constraints.gridx = 0;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.LINE_START;
        frame.add(panel1, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 50, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        frame.add(panel2, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;      // Position in grid
        constraints.gridy = 2;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 2;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.CENTER;
        frame.add(panel3, constraints);


        //==================================================
        // Part 2 - Panel 1

        JLabel filePathLabel = new JLabel("File Path");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.1;
        constraints.weighty = 0.1;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.insets = new Insets(10, 10, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(filePathLabel, constraints);

        JTextField filePathTextField = new JTextField();
        filePathTextField.setPreferredSize(new Dimension(300, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(filePathTextField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.LINE_START;
        panel1.add(new JPanel(), constraints);

        JLabel frameSpinnerLabel = new JLabel("Frames per PNG Extract (Max 60)");
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(10, 110, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(frameSpinnerLabel, constraints);

        SpinnerModel numberSpinnerModel = new SpinnerNumberModel(1, 1, 60, 1); // (Initial, min, max, step)
        JSpinner frameSpinner = new JSpinner(numberSpinnerModel);
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 110, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(frameSpinner, constraints);


        //==================================================
        // Part 3 - Panel 2

        JLabel outputFolderLabel = new JLabel("Output Folder Name");
        constraints = new GridBagConstraints();
        constraints.gridx = 0;          // X Position in grid
        constraints.gridy = 0;          // Y Position in grid
        constraints.gridwidth = 1;      // X scale of component
        constraints.gridheight = 1;     // Y scale of component
        constraints.weightx = 0.1;      // Adjust
        constraints.weighty = 0.1;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.insets = new Insets(10, 10, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(outputFolderLabel, constraints);

        JTextField outputFolderTextField = new JTextField();
        outputFolderTextField.setPreferredSize(new Dimension(300, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(outputFolderTextField, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.LINE_START;
        panel2.add(new JPanel(), constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.LINE_START;
        panel2.add(new JPanel(), constraints);

        //==================================================
        // Part 4 - Panel 3

        JTextField consoleOutput = new JTextField();
        consoleOutput.setPreferredSize(new Dimension(500, 80));
        consoleOutput.setEnabled(false);
        constraints = new GridBagConstraints();
        constraints.gridx = 1;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.PAGE_START;
        panel3.add(consoleOutput, constraints);

        JButton confirmButton = new JButton("Confirm");
//        confirmButton.addActionListener(e -> {
//            System.console().readLine()
//        });
        constraints = new GridBagConstraints();
        constraints.gridx = 1;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 0, 0, 100);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.CENTER;
        panel3.add(confirmButton, constraints);

        JButton cancelButton = new JButton("Cancel");
        constraints = new GridBagConstraints();
        constraints.gridx = 1;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 100, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.CENTER;
        panel3.add(cancelButton, constraints);

        //==================================================
        // Part 5 - Deploying the UI
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
