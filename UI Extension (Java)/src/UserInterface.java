import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserInterface {
    JFrame frame;
    Path applicationRoot;
    Path FFmpegPath;
    String FFMpegExecutablePath;
    ConsoleBridge consoleBridge;

    JFileChooser fileChooser;
    ImageIcon fileIcon;


    private JTextField estimatedFpsTextField;
    private JTextArea consoleOutputTextArea;

    public UserInterface(Path initialDirectory) {
        // The UI keeps track of the application's root, and the location of FFmpeg. This may be used for reference.
        this.applicationRoot = Paths.get(System.getProperty("user.dir")).getParent();
        this.FFmpegPath = Paths.get(System.getProperty("user.dir")).getParent().resolve("bin");
        this.FFMpegExecutablePath = "\"" + Paths.get(System.getProperty("user.dir")).getParent().resolve("bin").resolve("ffmpeg.exe").toString() + "\"";
        this.fileIcon = new ImageIcon(Paths.get(System.getProperty("user.dir")).resolve("Folder Icon.png").toString());

        this.consoleBridge = new ConsoleBridge(initialDirectory);
        this.fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("mp4 files", "mp4");
        fileChooser.setFileFilter(filter);

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
        filePathTextField.setPreferredSize(new Dimension(280, 25));
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

        JButton filePathFolderButton = new JButton();
        filePathFolderButton.setPreferredSize(new Dimension(24, 24));
        filePathFolderButton.setIcon(fileIcon);
        filePathFolderButton.setBackground(Color.WHITE);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 289, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(filePathFolderButton, constraints);

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

        JLabel frameSpinnerLabel = new JLabel("Desired FPS (Max 60)");
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(10, 212, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(frameSpinnerLabel, constraints);

        SpinnerModel numberSpinnerModel = new SpinnerNumberModel(1, 1, 60, 1); // (Initial, min, max, step)
        JSpinner frameSpinner = new JSpinner(numberSpinnerModel);
        frameSpinner.setEnabled(false);
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 242, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(frameSpinner, constraints);

        JCheckBox desiredFPSCheckBox = new JCheckBox();
        desiredFPSCheckBox.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                if (desiredFPSCheckBox.isSelected()) {
                    frameSpinner.setEnabled(true);
                } else {
                    frameSpinner.setEnabled(false);
                }
            });

        });
        desiredFPSCheckBox.setBackground(Color.WHITE);
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 210, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(desiredFPSCheckBox, constraints);


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
        outputFolderTextField.setPreferredSize(new Dimension(280, 25));
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

        JButton outputPathFolderButton = new JButton();
        outputPathFolderButton.setPreferredSize(new Dimension(24, 24));
        outputPathFolderButton.setIcon(fileIcon);
        outputPathFolderButton.setBackground(Color.WHITE);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 289, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(outputPathFolderButton, constraints);

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

        JLabel fpsLabel = new JLabel("Estimated FPS");
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 0;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(10, 146, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(fpsLabel, constraints);

        estimatedFpsTextField = new JTextField();
        estimatedFpsTextField.setPreferredSize(new Dimension(40, 25));
        estimatedFpsTextField.setEnabled(false);
        estimatedFpsTextField.setDisabledTextColor(Color.BLACK);
        constraints = new GridBagConstraints();
        constraints.gridx = 2;      // Position in grid
        constraints.gridy = 1;
        constraints.gridwidth = 1;  // Scale of component
        constraints.gridheight = 1;
        constraints.weightx = 0.1;  // Adjusts how much empty space this component is given when window is resized
        constraints.weighty = 0.1;
        constraints.ipadx = 0;      // Attempts to manually resize the component
        constraints.ipady = 0;
        constraints.insets = new Insets(0, 146, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(estimatedFpsTextField, constraints);

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
            String outputFolderArgument = "";

            /*Process input to the outputFolderTextField
             * Case 1. Empty user input
             * Case 2. Directory doesn't already exist*/
            UserInputTypes outputFolderInputCode = identifyUserInput(outputFolderTextField.getText(), false);

            if (outputFolderInputCode == UserInputTypes.EMPTY) {

            }
            if (outputFolderInputCode == UserInputTypes.PATH_NONEXISTENT) {
                String[] mkdirCommand = {"cmd.exe", "/c", "mkdir", outputFolderTextField.getText()};
                ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output").resolve("MP4 to PNG Sequence"));
                tempConsoleBridge.changeCommand(mkdirCommand);
                tempConsoleBridge.changeDirectory(applicationRoot.resolve("Default Output").resolve("MP4 to PNG Sequence"));

                try {
                    tempConsoleBridge.executeCommand(true, consoleOutput -> {
                    });
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


                //NEEDS FIX
                //When parseUserInput is completed, the line below must replace outputFolderTextField.getText() with parseUserInput(outputFolderTextField.getText())
                //This is because, in its current state, the line below will attempt to create a file with "/" in its name, which is reserved by Windows OS at all times!
                outputFolderArgument = "\"" + applicationRoot.resolve("Default Output").resolve("MP4 to PNG Sequence") + "/" + outputFolderTextField.getText() + "_%04d.png\"";
            }
            if (outputFolderInputCode == UserInputTypes.PATH_EXISTING) {

            }
            if (outputFolderInputCode == UserInputTypes.NAME) {
                String[] mkdirCommand = {"cmd.exe", "/c", "mkdir", outputFolderTextField.getText()};
                ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output").resolve("MP4 to PNG Sequence"));
                tempConsoleBridge.changeCommand(mkdirCommand);
                tempConsoleBridge.changeDirectory(applicationRoot.resolve("Default Output").resolve("MP4 to PNG Sequence"));

                try {
                    tempConsoleBridge.executeCommand(true, consoleOutput -> {
                    });
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


                outputFolderArgument = "\"" + applicationRoot.resolve("Default Output").resolve("MP4 to PNG Sequence") + "/" + outputFolderTextField.getText() + "/" + filePathTextField.getText() + "_%04d.png\"";
            }


            String[] fullCommand = {""};
            if (desiredFPSCheckBox.isSelected()) {
                fullCommand = new String[]{this.FFMpegExecutablePath, "-i", inputFilePathArgument, "-vf", "\"fps=" + frameSpinner.getValue().toString() + "\"", outputFolderArgument};
            } else {
                fullCommand = new String[]{this.FFMpegExecutablePath, "-i", inputFilePathArgument, outputFolderArgument};
            }
//            System.out.print("\n");
//            for (String arg : fullCommand) {
//                System.out.print(arg + " ");
//            }

            try {
                executeMP4ToPNGSequenceCommand(fullCommand, this.FFmpegPath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


            String[] getFPSCommand = {this.FFMpegExecutablePath, "-i", inputFilePathArgument};
            try {
                executeGetMP4FramerateCommand(getFPSCommand);
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
        cancelButton.addActionListener(e -> {
            identifyUserInput(outputFolderTextField.getText(), true);
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
        constraints.insets = new Insets(0, 100, 0, 0);   // Insets = (Top, Left, Bottom, Right)
        constraints.anchor = GridBagConstraints.CENTER;
        panel3.add(cancelButton, constraints);

        //==================================================
        // Part 5 - Deploying the UI
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    /* The identifyUserInput method is designed to help identify the user's input into textField components
     * It returns values based on the enumerator userInputTypes
     * */
    private enum UserInputTypes {
        DEFAULT,
        EMPTY,
        NAME,
        PATH_NONEXISTENT,
        PATH_EXISTING
    }

    public UserInputTypes identifyUserInput(String userInput, boolean print) {
        if (userInput.equals("")) {
            if (print) System.out.print("\nUser input nothing");
            return UserInputTypes.EMPTY;
        }

        if (userInput.contains("\\")) {
            if (Files.isDirectory(Paths.get(userInput))) {
                if (print) {
                    System.out.print("\nUser input a path to an existing file or directory");
                }
                return UserInputTypes.PATH_EXISTING;
            } else {
                if (print) {
                    System.out.print("\nUser input a path to a file or directory that doesn't exist");
                }
                return UserInputTypes.PATH_NONEXISTENT;
            }
        } else {
            if (print) {
                System.out.print("\nUser input the name of a file or directory");
            }
            return UserInputTypes.NAME;
        }
    }

    /* The parseUserInput method is designed to process various forms of user file name input
     *
     * For example, if the user inputs a file with an extension such as mp4, the method should return the name of the file without the extension
     * Another example, if the user inputs a file path to a directory, the method should return the name of the directory
     * */
    public String parseUserInput() {


        return "";
    }

    public void executeMP4ToPNGSequenceCommand(String[] inputCommand, Path FFmpegLocation) throws IOException {
        this.consoleBridge.changeCommand(inputCommand);
        this.consoleBridge.changeDirectory(FFmpegLocation);

        consoleBridge.executeCommand(false, consoleBridgeOutput -> {
            SwingUtilities.invokeLater(() -> {
                consoleOutputTextArea.append("\n" + consoleBridgeOutput);
            });
        });
    }

    public void executeGetMP4FramerateCommand(String[] inputCommand) throws IOException {
        ConsoleBridge tempConsoleBridge = new ConsoleBridge(this.FFmpegPath);

        tempConsoleBridge.changeCommand(inputCommand);
        tempConsoleBridge.changeDirectory(this.FFmpegPath);

        tempConsoleBridge.executeCommand(false, consoleBridgeOutput -> {
            SwingUtilities.invokeLater(() -> {
//                estimatedFpsTextField.setText();
                if (consoleBridgeOutput.contains("fps")) {
                    String[] tempOutput = consoleBridgeOutput.toString().split("\\,+");
                    for (String item : tempOutput) {
                        if (item.contains("fps")) {
                            SwingUtilities.invokeLater(() -> {
                                this.estimatedFpsTextField.setText(item.replaceAll("[^0-9]", ""));
                            });
                        }
                    }
                }
            });
        });
    }
}
