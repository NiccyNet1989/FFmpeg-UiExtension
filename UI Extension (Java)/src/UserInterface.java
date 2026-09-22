import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserInterface {
    JFrame frame;
    Path applicationRoot;
    Path FFmpegPath;
    String FFMpegExecutablePath;
    ConsoleBridge consoleBridge;


    private JTextField fpsTextField;
    private JTextArea consoleOutputTextArea;

    public UserInterface(Path initialDirectory) {
        // The UI keeps track of the application's root, and the location of FFmpeg. This may be used for reference.
        this.applicationRoot = Paths.get(System.getProperty("user.dir")).getParent();
        this.FFmpegPath = Paths.get(System.getProperty("user.dir")).getParent().resolve("bin");
        this.FFMpegExecutablePath = "\"" + Paths.get(System.getProperty("user.dir")).getParent().resolve("bin").resolve("ffmpeg.exe").toString() + "\"";

        this.consoleBridge = new ConsoleBridge(initialDirectory);

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
        frame.setSize(new Dimension(600, 450));
        frame.setResizable(false);

        // Adding a menu bar
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu operationsMenu = new JMenu("Operations");
        JMenu helpMenu = new JMenu("Help");
        JMenu exitMenu = new JMenu("Exit");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(operationsMenu);
        menuBar.add(helpMenu);
        menuBar.add(exitMenu);

        JMenuItem fileOpen = new JMenuItem("Open");
        fileMenu.add(fileOpen);

        JMenuItem editClear = new JMenuItem("Clear");
        editMenu.add(editClear);

        JMenuItem operationsMP4toPNGSequence = new JMenuItem("MP4 to PNG Sequence");
        JMenuItem operationsPNGSequencetoMP4 = new JMenuItem("PNG Sequence to MP4");
        operationsMenu.add(operationsMP4toPNGSequence);
        operationsMenu.add(operationsPNGSequencetoMP4);

        JMenuItem helpAbout = new JMenuItem("About");
        JMenuItem helpCredits = new JMenuItem("Credits");
        helpMenu.add(helpAbout);
        helpMenu.add(helpCredits);

        JMenuItem exitClose = new JMenuItem("Close");
        exitMenu.add(exitClose);


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
        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        frame.add(panel2, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;      // Position in grid
        constraints.gridy = 2;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 1;
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

//        constraints = new GridBagConstraints();
//        constraints.gridx = 1;      // Position in grid
//        constraints.gridy = 0;
//        constraints.gridwidth = 1;  // Scale of component
//        constraints.gridheight = 1;
//        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
//        constraints.weighty = 0.1;
//        constraints.ipadx = 0;      // Attempts to manually resize the component
//        constraints.ipady = 0;
//        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
//        constraints.anchor = GridBagConstraints.LINE_START;
//        panel1.add(new JPanel(), constraints);

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
        constraints.insets = new Insets(10, 280, 0, 0);   // Insets = (Top, Left, Bottom, Right)
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
        constraints.insets = new Insets(0, 280, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(frameSpinner, constraints);


        //==================================================
        // Part 3 - Panel 2

        JLabel outputFolderLabel = new JLabel("Output Folder");
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

//        constraints = new GridBagConstraints();
//        constraints.gridx = 1;      // Position in grid
//        constraints.gridy = 0;
//        constraints.gridwidth = 1;  // Scale of component
//        constraints.gridheight = 1;
//        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
//        constraints.weighty = 0.1;
//        constraints.ipadx = 0;      // Attempts to manually resize the component
//        constraints.ipady = 0;
//        constraints.insets = new Insets(0, 0, 0, 0);   // Insets = (Top, Left, Bottom, Right)
//        constraints.anchor = GridBagConstraints.LINE_START;
//        panel2.add(new JPanel(), constraints);

        JLabel fpsLabel = new JLabel("Video FPS");
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(10, 120, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(fpsLabel, constraints);

        fpsTextField = new JTextField();
        fpsTextField.setPreferredSize(new Dimension(40, 25));
        fpsTextField.setEnabled(false);
        fpsTextField.setDisabledTextColor(Color.BLACK);
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 120, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(fpsTextField, constraints);

        //==================================================
        // Part 4 - Panel 3

        consoleOutputTextArea = new JTextArea();
//        consoleOutputTextArea.setPreferredSize(new Dimension(500, 80));
        consoleOutputTextArea.setEditable(false);
        consoleOutputTextArea.setLineWrap(true);
        consoleOutputTextArea.setWrapStyleWord(true);
        consoleOutputTextArea.setDisabledTextColor(Color.BLACK);
        JScrollPane consoleOutputScrollPane = new JScrollPane(consoleOutputTextArea);
        consoleOutputScrollPane.setPreferredSize(new Dimension(500, 80));
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
        panel3.add(consoleOutputScrollPane, constraints);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            String inputFilePathArgument = "\"../" + filePathTextField.getText() + ".mp4\"";
            String outputFolderArgument = "\"../" + outputFolderTextField.getText() + "/" + filePathTextField.getText() + "_%04d.png\"";

            String[] fullCommand = {this.FFMpegExecutablePath, "-i", inputFilePathArgument, outputFolderArgument};
//            System.out.print("\n");
//            for (String arg : fullCommand) {
//                System.out.print(arg + " ");
//            }

            try {
                executeMP4ToPNGSequence(fullCommand, this.FFmpegPath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
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


    /*The below command is designed to handle the user's input into the filePath textfield component
     * It is designed to identify either a file path, the name of a file, or the name of a file including the .mp4 extension
     * With the use of this command, the program should become more robust to user-input*/
    public void parseFileInput() {

    }

    public void executeMP4ToPNGSequence(String[] inputCommand, Path FFmpegLocation) throws IOException {
        this.consoleBridge.changeCommand(inputCommand);
        this.consoleBridge.changeDirectory(FFmpegLocation);

        consoleBridge.executeCommand(consoleBridgeOutput -> {
            SwingUtilities.invokeLater(() -> {
                consoleOutputTextArea.append("\n" + consoleBridgeOutput);
            });
        });
    }
}
