import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserInterface {
    JFrame frame;
    Path applicationRoot;
    Path FFmpegPath;
    String FFMpegExecutablePath;
    ConsoleBridge rootConsoleBridge;
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

        this.rootConsoleBridge = new ConsoleBridge(initialDirectory);
        this.fileChooser = new JFileChooser();

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

        JLabel targetMP4Label = new JLabel("Target MP4");
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
        panel1.add(targetMP4Label, constraints);

        JTextField targetMP4TextField = new JTextField();
        targetMP4TextField.setPreferredSize(new Dimension(280, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(targetMP4TextField, constraints);

        JButton targetMP4FolderButton = new JButton();
        targetMP4FolderButton.setPreferredSize(new Dimension(24, 24));
        targetMP4FolderButton.setIcon(fileIcon);
        targetMP4FolderButton.setBackground(Color.WHITE);
        targetMP4FolderButton.addActionListener(e -> {
            fileChooser.setFileFilter(new FileNameExtensionFilter("MP4 Files", "mp4"));
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                targetMP4TextField.setText(fileChooser.getSelectedFile().toString());
            }
        });
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 289, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel1.add(targetMP4FolderButton, constraints);

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

        JLabel outputLocationLabel = new JLabel("Output Location");
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
        panel2.add(outputLocationLabel, constraints);

        JTextField outputLocationTextField = new JTextField();
        outputLocationTextField.setPreferredSize(new Dimension(280, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(outputLocationTextField, constraints);

        JButton outputLocationFolderButton = new JButton();
        outputLocationFolderButton.setPreferredSize(new Dimension(24, 24));
        outputLocationFolderButton.setIcon(fileIcon);
        outputLocationFolderButton.setBackground(Color.WHITE);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 289, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        panel2.add(outputLocationFolderButton, constraints);

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
            String inputFilePathArgument = "";
            String sanitizedTargetMP4UserInput = targetMP4TextField.getText().strip();
            UserInputTypes targetMP4UserInputType = identifyUserInput(sanitizedTargetMP4UserInput, false);

            String createdFolderName = null;

            switch (targetMP4UserInputType) {
                case EMPTY -> {
                    SwingUtilities.invokeLater(() -> {
                        consoleOutputTextArea.setText("Please input a file path or the name of an MP4 file you have placed in the Default Input Folder");
                    });
                    return;
                }
                case PATH_INVALID, PATH_NOT_ABSOLUTE -> {
                    SwingUtilities.invokeLater(() -> {
                        consoleOutputTextArea.setText("Error: Inputted file path is invalid");
                    });
                    return;
                }
                case PATH_NONEXISTENT -> {
                    SwingUtilities.invokeLater(() -> {
                        consoleOutputTextArea.setText("Error: File path '" + targetMP4TextField.getText() + "' not found");
                    });
                    return;
                }
                case NAME_NONEXISTENT -> {
                    SwingUtilities.invokeLater(() -> {
                        consoleOutputTextArea.setText("Error: File with name '" + targetMP4TextField.getText() + "' not found in Default Input folder. (Note: Please include the .mp4 extension)");
                    });
                    return;
                }

                case PATH_EXISTING -> {
                    if (sanitizedTargetMP4UserInput.endsWith(".mp4")) {
                        inputFilePathArgument = "\"" + sanitizedTargetMP4UserInput + "\"";
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            consoleOutputTextArea.setText("Error: The provided file path does not refer to an MP4 file");
                        });
                        return;
                    }
                }
                case NAME_EXISTING -> {
                    if (sanitizedTargetMP4UserInput.endsWith(".mp4")) {
                        inputFilePathArgument = applicationRoot.resolve("Default Input").resolve(sanitizedTargetMP4UserInput).toString();
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            consoleOutputTextArea.setText("Error: The provided file name does not refer to an MP4 file");
                        });
                        return;
                    }
                }

                case DEFAULT -> {
                    SwingUtilities.invokeLater(() -> {
                        consoleOutputTextArea.setText("Error: An unknown error has occurred");
                    });
                    return;
                }
            }


            String outputLocationArgument = "";

            /*Process input to the outputLocationTextField
             * Case 1. Empty user input
             * Case 2. Directory doesn't already exist*/
            String sanitizedOutputLocationUserInput = outputLocationTextField.getText().strip();
            UserInputTypes outputLocationUserInputType = identifyUserInput(sanitizedOutputLocationUserInput, true);
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH;mm.ss");
            String timePrefix = currentTime.format(timeFormatter);

            switch (outputLocationUserInputType) {
                case EMPTY, DEFAULT, PATH_INVALID, PATH_NONEXISTENT, PATH_NOT_ABSOLUTE -> {
                    String[] mkdirCommand = {""};
                    String targetMP4ExtensionRemoved = null;
                    Path checkPath = null;

                    if (targetMP4UserInputType == UserInputTypes.NAME_EXISTING) {
                        targetMP4ExtensionRemoved = sanitizedTargetMP4UserInput.substring(0, sanitizedTargetMP4UserInput.length() - 4);
                        checkPath = Paths.get(applicationRoot.resolve("Default Output").resolve(targetMP4ExtensionRemoved).toString());
                    } else if (targetMP4UserInputType == UserInputTypes.PATH_EXISTING) {
                            String tempFileName = Paths.get(sanitizedTargetMP4UserInput).getFileName().toString();
                            targetMP4ExtensionRemoved = tempFileName.substring(0, tempFileName.length() - 4);
                            checkPath = Paths.get(applicationRoot.resolve("Default Output").resolve(targetMP4ExtensionRemoved).toString());
                    } else {
                        createdFolderName = "\"[" + timePrefix + "]\"";
                    }

                    if (!Objects.isNull(checkPath) && !Objects.isNull(targetMP4ExtensionRemoved)) {
                        if (Files.isDirectory(checkPath)) {
                            // In the case that a directory with target MP4's name already exists in the default folder, the application adds a prefix [Current Date and Time]
                            createdFolderName = "\"[" + timePrefix + "] " + targetMP4ExtensionRemoved + "\"";
                        } else {
                            createdFolderName = "\"" + targetMP4ExtensionRemoved + "\"";
                        }
                    }

                    mkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", createdFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(mkdirCommand);
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;

                    // Once the directory has been created, trim the " from createdFolderName, so it may be used in path arguments
                    createdFolderName = createdFolderName.replace("\"", "");
                    outputLocationArgument = "\"" + applicationRoot.resolve("Default Output").resolve(createdFolderName).toString() + "_%04d.png\"";
                }

                // The NAME_NONEXISTENT case is unique because, if the user inputs a name that doesn't exist, it's assumed that the user wants to create a folder with that name
                // Note that the same cannot be said about PATH_NONEXISTENT, because this runs the risk of creating a directory at an unknown location which the user may not be able to find, whereas other cases will go to the Default Output folder
                case NAME_NONEXISTENT, NAME_EXISTING -> {
                    String[] mkdirCommand = {""};

                    if (Files.isDirectory(Paths.get(applicationRoot.resolve("Default Output").resolve(sanitizedOutputLocationUserInput).toString()))) {
                        createdFolderName = "\"[" + timePrefix + "] " + sanitizedOutputLocationUserInput + "\"";
                    } else {
                        createdFolderName = "\"" + sanitizedOutputLocationUserInput + "\"";
                    }

                    mkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", createdFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(mkdirCommand);
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;
                    createdFolderName = createdFolderName.replace("\"", "");

                    outputLocationArgument = "\"" + applicationRoot.resolve("Default Output").resolve(createdFolderName).toString() + "_%04d.png\"";
                }

                case PATH_EXISTING -> {
                    String[] mkdirCommand = {""};
                    String targetMP4ExtensionRemoved = null;
                    Path checkPath = null;

                    if (!Files.isDirectory(Paths.get(sanitizedOutputLocationUserInput))) {
                        SwingUtilities.invokeLater(() -> {
                            consoleOutputTextArea.setText("Error: The path provided in the Output Location is invalid");
                        });

                        return;
                    }

                    if (targetMP4UserInputType == UserInputTypes.NAME_EXISTING) {
                        targetMP4ExtensionRemoved = sanitizedTargetMP4UserInput.substring(0, sanitizedTargetMP4UserInput.length() - 4);
                        checkPath = Paths.get(sanitizedOutputLocationUserInput).resolve(targetMP4ExtensionRemoved);

                        if (Files.isDirectory(checkPath)) {
                            createdFolderName = "\"[" + timePrefix + "] " + targetMP4ExtensionRemoved + "\"";
                        } else {
                            createdFolderName = "\"" + targetMP4ExtensionRemoved + "\"";
                        }
                    } else if (targetMP4UserInputType == UserInputTypes.PATH_EXISTING) {
                        String tempFileName = Paths.get(sanitizedTargetMP4UserInput).getFileName().toString();
                        targetMP4ExtensionRemoved = tempFileName.substring(0, tempFileName.length() - 4);
                        checkPath = Paths.get(sanitizedOutputLocationUserInput).resolve(targetMP4ExtensionRemoved);

                        if (Files.isDirectory(checkPath)) {
                            createdFolderName = "\"[" + timePrefix + "] " + targetMP4ExtensionRemoved + "\"";
                        } else {
                            createdFolderName = "\"" + targetMP4ExtensionRemoved + "\"";
                        }
                    }

                    mkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", createdFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(mkdirCommand);
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;
                    createdFolderName = createdFolderName.replace("\"", "");

                    outputLocationArgument = "\"" + Paths.get(sanitizedOutputLocationUserInput).resolve(createdFolderName) + "_%04d.png\"";
                }
            }

//            String[] fullCommand = {""};
//            if (desiredFPSCheckBox.isSelected()) {
//                fullCommand = new String[]{this.FFMpegExecutablePath, "-i", inputFilePathArgument, "-vf", "\"fps=" + frameSpinner.getValue().toString() + "\"", outputLocationArgument};
//            } else {
//                fullCommand = new String[]{this.FFMpegExecutablePath, "-i", inputFilePathArgument, outputLocationArgument};
//            }
//
//            /*
//            System.out.print("\n");
//            for (String arg : fullCommand) {
//                System.out.print(arg + " ");
//            }
//            * */
//
//            try {
//                executeMP4ToPNGSequenceCommand(fullCommand);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//
//            String[] getFPSCommand = {this.FFMpegExecutablePath, "-i", inputFilePathArgument};
//            try {
//                executeGetMP4FramerateCommand(getFPSCommand);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
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
//            SwingUtilities.invokeLater(() -> {
//                System.out.print("Testing: " + outputLocationTextField.getText());
//                identifyUserInput(outputLocationTextField.getText(), true);
//            });

            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH-mm-ss");
            System.out.print("\n" + currentTime.format(timeFormatter));
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
        NAME_NONEXISTENT,
        NAME_EXISTING,
        PATH_NONEXISTENT,
        PATH_EXISTING,
        PATH_INVALID
        // invalidChars = "/:*?\"<>|";
        ,
        PATH_NOT_ABSOLUTE
    }

    public UserInputTypes identifyUserInput(String userInput, boolean print) {
        if (userInput.equals("")) {
            if (print) System.out.print("\nUser input nothing");
            return UserInputTypes.EMPTY;
        }


        try {
            // First check for invalid paths. If a path is invalid, the code redirects to the catch block
            Path inputtedPath = Paths.get(userInput);
            if (print) System.out.print("\nProcessing the following user input: " + inputtedPath);

            // Second, check for basic names of files that aren't paths
            if (!inputtedPath.isAbsolute() && inputtedPath.getParent() == null) {
                if (!Files.exists(inputtedPath)) {
                    Path defaultOutputFolderPath = applicationRoot.resolve("Default Output");
                    Path defaultInputFolderPath = applicationRoot.resolve("Default Input");
                    if (Files.exists(defaultOutputFolderPath.resolve(userInput)) || Files.exists(defaultInputFolderPath.resolve(userInput))) {
                        // If the user simply inputs the name of a file, the application will only check the default folders if it exists, since it cannot reasonably check everywhere else in the application or the device
                        if (print)
                            System.out.print("\nUser input the name of an existing file or directory found in the default folders");
                        return UserInputTypes.NAME_EXISTING;
                    } else {
                        if (print)
                            System.out.print("\nUser input the name of a file or directory that doesn't exist in the default folders");
                        return UserInputTypes.NAME_NONEXISTENT;
                    }
                }
            }

            // Third, check if the input contains any form of path redirection
            if (!inputtedPath.isAbsolute()) {
                if (print) System.out.print("\nUser input a path that isn't absolute");
                return UserInputTypes.PATH_NOT_ABSOLUTE;
            }

            // Fourth, check if the input is a path to an existing file or directory
            if (Files.exists(inputtedPath)) {
                if (print) System.out.print("\nUser input a path to an existing file or directory");
                return UserInputTypes.PATH_EXISTING;
            } else {
                if (print) System.out.print("\nUser input a path to a file or directory that doesn't exist");
                return UserInputTypes.PATH_NONEXISTENT;
            }
        } catch (InvalidPathException | NullPointerException e) {
            if (print) System.out.print("\nUser input a file or directory name with invalid characters or syntax");
            return UserInputTypes.PATH_INVALID;
        }
    }

    /* The parseUserInput method is designed to process various forms of user input for program use, including both paths and names
     *
     * For example, if the user inputs a file with an extension such as mp4, the method should return the name of the file without the extension
     * Another example, if the user inputs a file path to a directory, the method should return the name of the directory
     * File or directory names also cannot have leading or trailing spaces such as "     directory name        ", so the method should trim these
     * Finally, path names cannot have invalid characters or "\\" together, so the method should attempt to trim these
     * */
    public String parseUserInput(String userInput, boolean print) {
        String returnString = userInput;

        if (identifyUserInput(userInput, false) == UserInputTypes.PATH_INVALID) {
            String invalidChars = "/:*?\"<>|";
            for (Character invalid : invalidChars.toCharArray()) {
                if (returnString.contains(invalid.toString())) {
                    System.out.print("\nInvalid character: " + invalid.toString() + " detected");

                    returnString = returnString.replace(invalid.toString(), "");
                }
            }

            if (returnString.contains("\\\\")) {
                returnString.replace("\\\\", "\\");
            }
        }

        System.out.print(returnString);

        return returnString;
    }

    public void executeMP4ToPNGSequenceCommand(String[] inputCommand) throws IOException {
        this.rootConsoleBridge.changeCommand(inputCommand);

        rootConsoleBridge.executeCommand(false, consoleBridgeOutput -> {
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
