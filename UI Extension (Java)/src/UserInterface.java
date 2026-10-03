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
    JPanel MP4toPNGPanel = new JPanel();
    JPanel PNGtoMP4Panel = new JPanel();
    Path applicationRoot;
    Path FFmpegPath;
    String FFMpegExecutablePath;
    ConsoleBridge rootConsoleBridge;
    JFileChooser fileChooser;
    ImageIcon fileIcon;
    private JTextField m2pestimatedFpsTextField;
    private JTextField p2mestimatedFpsTextField;
    private JTextArea m2pconsoleOutputTextArea;
    private JTextArea p2mconsoleOutputTextArea;


    public UserInterface(Path initialDirectory) {
        //==================================================
        // Part 1 - Base Frame Development

        System.out.print("Attempting to create user interface...");

        // The UI keeps track of the application's root, and the location of FFmpeg. This may be used for reference.
        this.applicationRoot = Paths.get(System.getProperty("user.dir")).getParent();
        this.FFmpegPath = Paths.get(System.getProperty("user.dir")).getParent().resolve("bin");
        this.FFMpegExecutablePath = "\"" + Paths.get(System.getProperty("user.dir")).getParent().resolve("bin").resolve("ffmpeg.exe").toString() + "\"";
        this.fileIcon = new ImageIcon(Paths.get(System.getProperty("user.dir")).resolve("Folder Icon.png").toString());

        this.rootConsoleBridge = new ConsoleBridge(initialDirectory);
        this.fileChooser = new JFileChooser();

        JFrame mainFrame = new JFrame("FFmpeg UI Extension");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(600, 450));
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setBackground(Color.WHITE);

        JPanel cardLayoutContainer = new JPanel(new CardLayout());
        cardLayoutContainer.add(MP4toPNGPanel, "MP4 to PNG Sequence Panel");
        cardLayoutContainer.add(PNGtoMP4Panel, "PNG Sequence to MP4 Panel");
        CardLayout currentCard = (CardLayout) cardLayoutContainer.getLayout();
        mainFrame.add(cardLayoutContainer);

        GridBagConstraints constraints = new GridBagConstraints();

        // Basic frame components
        MP4toPNGPanel.setLayout(new GridBagLayout());
        MP4toPNGPanel.setBackground(Color.WHITE);
        MP4toPNGPanel.setSize(new Dimension(600, 450));

        PNGtoMP4Panel.setLayout(new GridBagLayout());
        PNGtoMP4Panel.setBackground(Color.WHITE);
        PNGtoMP4Panel.setSize(new Dimension(600, 450));

        // Adding a menu bar
        JMenuBar menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);

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
        operationsMP4toPNGSequence.addActionListener(e -> {
            currentCard.show(cardLayoutContainer, "MP4 to PNG Sequence Panel");
        });
        JMenuItem operationsPNGSequencetoMP4 = new JMenuItem("PNG Sequence to MP4");
        operationsPNGSequencetoMP4.addActionListener(e -> {
            currentCard.show(cardLayoutContainer, "PNG Sequence to MP4 Panel");
        });
        operationsMenu.add(operationsMP4toPNGSequence);
        operationsMenu.add(operationsPNGSequencetoMP4);

        JMenuItem helpAbout = new JMenuItem("About");
        JMenuItem helpCredits = new JMenuItem("Credits");
        helpMenu.add(helpAbout);
        helpMenu.add(helpCredits);

        JMenuItem exitClose = new JMenuItem("Close");
        exitMenu.add(exitClose);


        // Defining the three panels main panels where the components will reside
        JPanel m2pPanel1 = new JPanel();
        JPanel m2pPanel2 = new JPanel();
        JPanel m2pPanel3 = new JPanel();
        m2pPanel1.setLayout(new GridBagLayout());
        m2pPanel2.setLayout(new GridBagLayout());
        m2pPanel3.setLayout(new GridBagLayout());
        m2pPanel1.setBackground(Color.WHITE);
        m2pPanel2.setBackground(Color.WHITE);
        m2pPanel3.setBackground(Color.WHITE);

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
        MP4toPNGPanel.add(m2pPanel1, constraints);

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
        MP4toPNGPanel.add(m2pPanel2, constraints);

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
        MP4toPNGPanel.add(m2pPanel3, constraints);


        //==================================================
        // Part 2 - Panel 1

        JLabel m2ptargetMP4Label = new JLabel("Target MP4");
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
        m2pPanel1.add(m2ptargetMP4Label, constraints);

        JTextField m2ptargetMP4TextField = new JTextField();
        m2ptargetMP4TextField.setPreferredSize(new Dimension(280, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        m2pPanel1.add(m2ptargetMP4TextField, constraints);

        JButton m2ptargetMP4FolderButton = new JButton();
        m2ptargetMP4FolderButton.setPreferredSize(new Dimension(24, 24));
        m2ptargetMP4FolderButton.setIcon(fileIcon);
        m2ptargetMP4FolderButton.setBackground(Color.WHITE);
        m2ptargetMP4FolderButton.addActionListener(e -> {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("MP4 Files", "mp4"));
            int returnValue = fileChooser.showOpenDialog(MP4toPNGPanel);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                m2ptargetMP4TextField.setText(fileChooser.getSelectedFile().toString());
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
        m2pPanel1.add(m2ptargetMP4FolderButton, constraints);

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

        JLabel m2pframeSpinnerLabel = new JLabel("Desired FPS (Max 60)");
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
        m2pPanel1.add(m2pframeSpinnerLabel, constraints);

        SpinnerModel m2pnumberSpinnerModel = new SpinnerNumberModel(1, 1, 60, 1); // (Initial, min, max, step)
        JSpinner m2pframeSpinner = new JSpinner(m2pnumberSpinnerModel);
        m2pframeSpinner.setEnabled(false);
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
        m2pPanel1.add(m2pframeSpinner, constraints);

        JCheckBox m2pdesiredFPSCheckBox = new JCheckBox();
        m2pdesiredFPSCheckBox.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                if (m2pdesiredFPSCheckBox.isSelected()) {
                    m2pframeSpinner.setEnabled(true);
                } else {
                    m2pframeSpinner.setEnabled(false);
                }
            });

        });
        m2pdesiredFPSCheckBox.setBackground(Color.WHITE);
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
        m2pPanel1.add(m2pdesiredFPSCheckBox, constraints);


        //==================================================
        // Part 3 - Panel 2

        JLabel m2poutputLocationLabel = new JLabel("Output Location");
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
        m2pPanel2.add(m2poutputLocationLabel, constraints);

        JTextField m2poutputLocationTextField = new JTextField();
        m2poutputLocationTextField.setPreferredSize(new Dimension(280, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        m2pPanel2.add(m2poutputLocationTextField, constraints);

        JButton m2poutputLocationFolderButton = new JButton();
        m2poutputLocationFolderButton.setPreferredSize(new Dimension(24, 24));
        m2poutputLocationFolderButton.setIcon(fileIcon);
        m2poutputLocationFolderButton.setBackground(Color.WHITE);
        m2poutputLocationFolderButton.addActionListener(e -> {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(MP4toPNGPanel);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                m2poutputLocationTextField.setText(fileChooser.getSelectedFile().toString());
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
        m2pPanel2.add(m2poutputLocationFolderButton, constraints);

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

        JLabel m2pfpsLabel = new JLabel("Estimated FPS");
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
        m2pPanel2.add(m2pfpsLabel, constraints);

        m2pestimatedFpsTextField = new JTextField();
        m2pestimatedFpsTextField.setPreferredSize(new Dimension(40, 25));
        m2pestimatedFpsTextField.setEnabled(false);
        m2pestimatedFpsTextField.setDisabledTextColor(Color.BLACK);
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
        m2pPanel2.add(m2pestimatedFpsTextField, constraints);

        //==================================================
        // Part 4 - Panel 3

        m2pconsoleOutputTextArea = new JTextArea();
//        consoleOutputTextArea.setPreferredSize(new Dimension(500, 80));
        m2pconsoleOutputTextArea.setEditable(false);
        m2pconsoleOutputTextArea.setLineWrap(true);
        m2pconsoleOutputTextArea.setWrapStyleWord(true);
        m2pconsoleOutputTextArea.setDisabledTextColor(Color.BLACK);
        JScrollPane m2pconsoleOutputScrollPane = new JScrollPane(m2pconsoleOutputTextArea);
        m2pconsoleOutputScrollPane.setPreferredSize(new Dimension(500, 80));
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
        m2pPanel3.add(m2pconsoleOutputScrollPane, constraints);

        JButton m2pconfirmButton = new JButton("Confirm");
        m2pconfirmButton.addActionListener(e -> {
            String m2pinputFilePathArgument = "";
            String m2psanitizedTargetMP4UserInput = m2ptargetMP4TextField.getText().strip();
            UserInputTypes m2ptargetMP4UserInputType = identifyUserInput(m2psanitizedTargetMP4UserInput, false);

            String m2pcreatedFolderName = null;

            switch (m2ptargetMP4UserInputType) {
                case EMPTY -> {
                    SwingUtilities.invokeLater(() -> {
                        m2pconsoleOutputTextArea.setText("Please input a file path or the name of an MP4 file you have placed in the Default Input Folder");
                    });
                    return;
                }
                case PATH_INVALID, PATH_NOT_ABSOLUTE -> {
                    SwingUtilities.invokeLater(() -> {
                        m2pconsoleOutputTextArea.setText("Error: Inputted file path is invalid");
                    });
                    return;
                }
                case PATH_NONEXISTENT -> {
                    SwingUtilities.invokeLater(() -> {
                        m2pconsoleOutputTextArea.setText("Error: File path '" + m2ptargetMP4TextField.getText() + "' not found");
                    });
                    return;
                }
                case NAME_NONEXISTENT -> {
                    SwingUtilities.invokeLater(() -> {
                        m2pconsoleOutputTextArea.setText("Error: File with name '" + m2ptargetMP4TextField.getText() + "' not found in Default Input folder. (Note: Please include the .mp4 extension)");
                    });
                    return;
                }

                case PATH_EXISTING -> {
                    if (m2psanitizedTargetMP4UserInput.endsWith(".mp4")) {
                        m2pinputFilePathArgument = "\"" + m2psanitizedTargetMP4UserInput + "\"";
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            m2pconsoleOutputTextArea.setText("Error: The provided file path does not refer to an MP4 file");
                        });
                        return;
                    }
                }
                case NAME_EXISTING -> {
                    if (m2psanitizedTargetMP4UserInput.endsWith(".mp4")) {
                        m2pinputFilePathArgument = applicationRoot.resolve("Default Input").resolve(m2psanitizedTargetMP4UserInput).toString();
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            m2pconsoleOutputTextArea.setText("Error: The provided file name does not refer to an MP4 file");
                        });
                        return;
                    }
                }

                case DEFAULT -> {
                    SwingUtilities.invokeLater(() -> {
                        m2pconsoleOutputTextArea.setText("Error: An unknown error has occurred");
                    });
                    return;
                }
            }


            String m2poutputLocationArgument = "";

            /*Process input to the outputLocationTextField
             * Case 1. Empty user input
             * Case 2. Directory doesn't already exist*/
            String m2psanitizedOutputLocationUserInput = m2poutputLocationTextField.getText().strip();
            UserInputTypes m2poutputLocationUserInputType = identifyUserInput(m2psanitizedOutputLocationUserInput, false);
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH;mm.ss");
            String timePrefix = currentTime.format(timeFormatter);

            switch (m2poutputLocationUserInputType) {
                case EMPTY, DEFAULT, PATH_INVALID, PATH_NONEXISTENT, PATH_NOT_ABSOLUTE -> {
                    String[] m2pmkdirCommand = {""};
                    String m2ptargetMP4ExtensionRemoved = null;
                    Path m2pcheckPath = null;

                    if (m2ptargetMP4UserInputType == UserInputTypes.NAME_EXISTING) {
                        m2ptargetMP4ExtensionRemoved = m2psanitizedTargetMP4UserInput.substring(0, m2psanitizedTargetMP4UserInput.length() - 4);
                        m2pcheckPath = Paths.get(applicationRoot.resolve("Default Output").resolve(m2ptargetMP4ExtensionRemoved).toString());
                    } else if (m2ptargetMP4UserInputType == UserInputTypes.PATH_EXISTING) {
                        String m2ptempFileName = Paths.get(m2psanitizedTargetMP4UserInput).getFileName().toString();
                        m2ptargetMP4ExtensionRemoved = m2ptempFileName.substring(0, m2ptempFileName.length() - 4);
                        m2pcheckPath = Paths.get(applicationRoot.resolve("Default Output").resolve(m2ptargetMP4ExtensionRemoved).toString());
                    } else {
                        m2pcreatedFolderName = "\"[" + timePrefix + "]\"";
                    }

                    if (!Objects.isNull(m2pcheckPath) && !Objects.isNull(m2ptargetMP4ExtensionRemoved)) {
                        if (Files.isDirectory(m2pcheckPath)) {
                            // In the case that a directory with target MP4's name already exists in the default folder, the application adds a prefix [Current Date and Time]
                            m2pcreatedFolderName = "\"[" + timePrefix + "] " + m2ptargetMP4ExtensionRemoved + "\"";
                        } else {
                            m2pcreatedFolderName = "\"" + m2ptargetMP4ExtensionRemoved + "\"";
                        }
                    }

                    m2pmkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", m2pcreatedFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(m2pmkdirCommand);
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;

                    // Once the directory has been created, trim the " from createdFolderName, so it may be used in path arguments
                    m2pcreatedFolderName = m2pcreatedFolderName.replace("\"", "");
                    m2poutputLocationArgument = "\"" + applicationRoot.resolve("Default Output").resolve(m2pcreatedFolderName).resolve(m2pcreatedFolderName).toString() + "_%04d.png\"";
                }

                // The NAME_NONEXISTENT case is unique because, if the user inputs a name that doesn't exist, it's assumed that the user wants to create a folder with that name
                // Note that the same cannot be said about PATH_NONEXISTENT, because this runs the risk of creating a directory at an unknown location which the user may not be able to find, whereas other cases will go to the Default Output folder
                case NAME_NONEXISTENT, NAME_EXISTING -> {
                    String[] m2pmkdirCommand = {""};

                    if (Files.isDirectory(Paths.get(applicationRoot.resolve("Default Output").resolve(m2psanitizedOutputLocationUserInput).toString()))) {
                        m2pcreatedFolderName = "\"[" + timePrefix + "] " + m2psanitizedOutputLocationUserInput + "\"";
                    } else {
                        m2pcreatedFolderName = "\"" + m2psanitizedOutputLocationUserInput + "\"";
                    }

                    m2pmkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", m2pcreatedFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(m2pmkdirCommand);
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;
                    m2pcreatedFolderName = m2pcreatedFolderName.replace("\"", "");

                    m2poutputLocationArgument = "\"" + applicationRoot.resolve("Default Output").resolve(m2pcreatedFolderName).resolve(m2pcreatedFolderName).toString() + "_%04d.png\"";
                }

                case PATH_EXISTING -> {
                    String[] m2pmkdirCommand = {""};
                    String m2ptargetMP4ExtensionRemoved = null;
                    Path m2pcheckPath = null;

                    if (!Files.isDirectory(Paths.get(m2psanitizedOutputLocationUserInput))) {
                        SwingUtilities.invokeLater(() -> {
                            m2pconsoleOutputTextArea.setText("Error: The path provided in the Output Location is invalid");
                        });

                        return;
                    }

                    if (m2ptargetMP4UserInputType == UserInputTypes.NAME_EXISTING) {
                        m2ptargetMP4ExtensionRemoved = m2psanitizedTargetMP4UserInput.substring(0, m2psanitizedTargetMP4UserInput.length() - 4);
                        m2pcheckPath = Paths.get(m2psanitizedOutputLocationUserInput).resolve(m2ptargetMP4ExtensionRemoved);

                        if (Files.isDirectory(m2pcheckPath)) {
                            m2pcreatedFolderName = "\"[" + timePrefix + "] " + m2ptargetMP4ExtensionRemoved + "\"";
                        } else {
                            m2pcreatedFolderName = "\"" + m2ptargetMP4ExtensionRemoved + "\"";
                        }
                    } else if (m2ptargetMP4UserInputType == UserInputTypes.PATH_EXISTING) {
                        String m2ptempFileName = Paths.get(m2psanitizedTargetMP4UserInput).getFileName().toString();
                        m2ptargetMP4ExtensionRemoved = m2ptempFileName.substring(0, m2ptempFileName.length() - 4);
                        m2pcheckPath = Paths.get(m2psanitizedOutputLocationUserInput).resolve(m2ptargetMP4ExtensionRemoved);

                        if (Files.isDirectory(m2pcheckPath)) {
                            m2pcreatedFolderName = "\"[" + timePrefix + "] " + m2ptargetMP4ExtensionRemoved + "\"";
                        } else {
                            m2pcreatedFolderName = "\"" + m2ptargetMP4ExtensionRemoved + "\"";
                        }
                    }

                    m2pmkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", m2pcreatedFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(m2pmkdirCommand);
                    tempConsoleBridge.changeDirectory(Paths.get(m2psanitizedOutputLocationUserInput));
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;
                    m2pcreatedFolderName = m2pcreatedFolderName.replace("\"", "");

                    m2poutputLocationArgument = "\"" + Paths.get(m2psanitizedOutputLocationUserInput).resolve(m2pcreatedFolderName).resolve(m2pcreatedFolderName) + "_%04d.png\"";
                }
            }

            String[] m2pfullCommand = {""};
            if (m2pdesiredFPSCheckBox.isSelected()) {
                m2pfullCommand = new String[]{this.FFMpegExecutablePath, "-i", m2pinputFilePathArgument, "-vf", "\"fps=" + m2pframeSpinner.getValue().toString() + "\"", m2poutputLocationArgument};
            } else {
                m2pfullCommand = new String[]{this.FFMpegExecutablePath, "-i", m2pinputFilePathArgument, m2poutputLocationArgument};
            }

            /*
            System.out.print("\n");
            for (String arg : fullCommand) {
                System.out.print(arg + " ");
            }
            * */

            try {
                m2pExecuteCommand(m2pfullCommand);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            String[] m2pgetFPSCommand = {this.FFMpegExecutablePath, "-i", m2pinputFilePathArgument};
            try {
                executeGetMP4FramerateCommand(m2pgetFPSCommand);
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
        m2pPanel3.add(m2pconfirmButton, constraints);

        JButton m2pcancelButton = new JButton("Cancel");
        m2pcancelButton.addActionListener(e -> {
            System.out.print("Closing User Interface...");
            mainFrame.dispose();
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
        m2pPanel3.add(m2pcancelButton, constraints);


        //==================================================
        // Part 5 - Creating Separate Frame for PNG Sequence -> MP4 Functionality

        JPanel p2mPanel1 = new JPanel();
        JPanel p2mPanel2 = new JPanel();
        JPanel p2mPanel3 = new JPanel();
        p2mPanel1.setLayout(new GridBagLayout());
        p2mPanel2.setLayout(new GridBagLayout());
        p2mPanel3.setLayout(new GridBagLayout());
        p2mPanel1.setBackground(Color.WHITE);
        p2mPanel2.setBackground(Color.WHITE);
        p2mPanel3.setBackground(Color.WHITE);

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
        PNGtoMP4Panel.add(p2mPanel1, constraints);

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
        PNGtoMP4Panel.add(p2mPanel2, constraints);

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
        PNGtoMP4Panel.add(p2mPanel3, constraints);


        JLabel p2mtargetPNGSequenceLabel = new JLabel("Target PNG Sequence Location");
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
        p2mPanel1.add(p2mtargetPNGSequenceLabel, constraints);

        JTextField p2mtargetPNGSequenceTextField = new JTextField();
        p2mtargetPNGSequenceTextField.setPreferredSize(new Dimension(280, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        p2mPanel1.add(p2mtargetPNGSequenceTextField, constraints);

        JButton p2mtargetPNGSequenceFolderButton = new JButton();
        p2mtargetPNGSequenceFolderButton.setPreferredSize(new Dimension(24, 24));
        p2mtargetPNGSequenceFolderButton.setIcon(fileIcon);
        p2mtargetPNGSequenceFolderButton.setBackground(Color.WHITE);
        p2mtargetPNGSequenceFolderButton.addActionListener(e -> {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(PNGtoMP4Panel);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                p2mtargetPNGSequenceTextField.setText(fileChooser.getSelectedFile().toString());
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
        p2mPanel1.add(p2mtargetPNGSequenceFolderButton, constraints);

        JLabel p2mframeSpinnerLabel = new JLabel("Desired FPS (Max 60)");
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
        p2mPanel1.add(p2mframeSpinnerLabel, constraints);

        SpinnerModel p2mnumberSpinnerModel = new SpinnerNumberModel(1, 1, 60, 1); // (Initial, min, max, step)
        JSpinner p2mframeSpinner = new JSpinner(p2mnumberSpinnerModel);
        p2mframeSpinner.setEnabled(false);
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
        p2mPanel1.add(p2mframeSpinner, constraints);

        JCheckBox p2mdesiredFPSCheckBox = new JCheckBox();
        p2mdesiredFPSCheckBox.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                if (p2mdesiredFPSCheckBox.isSelected()) {
                    p2mframeSpinner.setEnabled(true);
                } else {
                    p2mframeSpinner.setEnabled(false);
                }
            });

        });
        p2mdesiredFPSCheckBox.setBackground(Color.WHITE);
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
        p2mPanel1.add(p2mdesiredFPSCheckBox, constraints);


        //==================================================
        // Part 3 - Panel 2

        JLabel p2moutputMP4LocationLabel = new JLabel("Output Location");
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
        p2mPanel2.add(p2moutputMP4LocationLabel, constraints);

        JTextField p2moutputMP4LocationTextField = new JTextField();
        p2moutputMP4LocationTextField.setPreferredSize(new Dimension(280, 25));
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(0, 10, 50, 0);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        p2mPanel2.add(p2moutputMP4LocationTextField, constraints);

        JButton p2moutputMP4LocationFolderButton = new JButton();
        p2moutputMP4LocationFolderButton.setPreferredSize(new Dimension(24, 24));
        p2moutputMP4LocationFolderButton.setIcon(fileIcon);
        p2moutputMP4LocationFolderButton.setBackground(Color.WHITE);
        p2moutputMP4LocationFolderButton.addActionListener(e -> {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(PNGtoMP4Panel);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                p2moutputMP4LocationLabel.setText(fileChooser.getSelectedFile().toString());
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
        p2mPanel2.add(p2moutputMP4LocationFolderButton, constraints);

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

        JLabel p2mfpsLabel = new JLabel("Estimated FPS");
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
        p2mPanel2.add(p2mfpsLabel, constraints);

        p2mestimatedFpsTextField = new JTextField();
        p2mestimatedFpsTextField.setPreferredSize(new Dimension(40, 25));
        p2mestimatedFpsTextField.setEnabled(false);
        p2mestimatedFpsTextField.setDisabledTextColor(Color.BLACK);
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
        p2mPanel2.add(p2mestimatedFpsTextField, constraints);

        //==================================================
        // Part 4 - Panel 3

        p2mconsoleOutputTextArea = new JTextArea();
        p2mconsoleOutputTextArea.setEditable(false);
        p2mconsoleOutputTextArea.setLineWrap(true);
        p2mconsoleOutputTextArea.setWrapStyleWord(true);
        p2mconsoleOutputTextArea.setDisabledTextColor(Color.BLACK);
        JScrollPane p2mconsoleOutputScrollPane = new JScrollPane(p2mconsoleOutputTextArea);
        p2mconsoleOutputScrollPane.setPreferredSize(new Dimension(500, 80));
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
        p2mPanel3.add(p2mconsoleOutputScrollPane, constraints);

        JButton p2mconfirmButton = new JButton("Confirm");
        p2mconfirmButton.addActionListener(e -> {
            String p2minputFilePathArgument = "";
            String p2msanitizedTargetPNGSequenceUserInput = p2mtargetPNGSequenceTextField.getText().strip();
            UserInputTypes p2mtargetPNGSequenceUserInputType = identifyUserInput(p2msanitizedTargetPNGSequenceUserInput, false);

            String p2mcreatedFolderName = null;

            switch (p2mtargetPNGSequenceUserInputType) {
                case EMPTY -> {
                    SwingUtilities.invokeLater(() -> {
                        p2mconsoleOutputTextArea.setText("Please input a file path or the name of an MP4 file you have placed in the Default Input Folder");
                    });
                    return;
                }
                case PATH_INVALID, PATH_NOT_ABSOLUTE -> {
                    SwingUtilities.invokeLater(() -> {
                        p2mconsoleOutputTextArea.setText("Error: Inputted file path is invalid");
                    });
                    return;
                }
                case PATH_NONEXISTENT -> {
                    SwingUtilities.invokeLater(() -> {
                        p2mconsoleOutputTextArea.setText("Error: File path '" + p2mtargetPNGSequenceTextField.getText() + "' not found");
                    });
                    return;
                }
                case NAME_NONEXISTENT -> {
                    SwingUtilities.invokeLater(() -> {
                        p2mconsoleOutputTextArea.setText("Error: File with name '" + p2mtargetPNGSequenceTextField.getText() + "' not found in Default Input folder. (Note: Please include the .mp4 extension)");
                    });
                    return;
                }

                case PATH_EXISTING -> {
                    if (p2msanitizedTargetPNGSequenceUserInput.endsWith(".mp4")) {
                        p2minputFilePathArgument = "\"" + p2msanitizedTargetPNGSequenceUserInput + "\"";
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            p2mconsoleOutputTextArea.setText("Error: The provided file path does not refer to an MP4 file");
                        });
                        return;
                    }
                }
                case NAME_EXISTING -> {
                    if (p2msanitizedTargetPNGSequenceUserInput.endsWith(".mp4")) {
                        p2minputFilePathArgument = applicationRoot.resolve("Default Input").resolve(p2msanitizedTargetPNGSequenceUserInput).toString();
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            p2mconsoleOutputTextArea.setText("Error: The provided file name does not refer to an MP4 file");
                        });
                        return;
                    }
                }

                case DEFAULT -> {
                    SwingUtilities.invokeLater(() -> {
                        p2mconsoleOutputTextArea.setText("Error: An unknown error has occurred");
                    });
                    return;
                }
            }


            String p2moutputLocationArgument = "";

            /*Process input to the outputLocationTextField
             * Case 1. Empty user input
             * Case 2. Directory doesn't already exist*/
            String p2msanitizedOutputLocationUserInput = p2moutputMP4LocationTextField.getText().strip();
            UserInputTypes p2moutputLocationUserInputType = identifyUserInput(p2msanitizedOutputLocationUserInput, false);
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH;mm.ss");
            String timePrefix = currentTime.format(timeFormatter);

            switch (p2moutputLocationUserInputType) {
                case EMPTY, DEFAULT, PATH_INVALID, PATH_NONEXISTENT, PATH_NOT_ABSOLUTE -> {
                    String[] p2mmkdirCommand = {""};
                    String p2mtargetPNGSSequenceExtensionRemoved = null;
                    Path p2mcheckPath = null;

                    if (p2mtargetPNGSequenceUserInputType == UserInputTypes.NAME_EXISTING) {
                        p2mtargetPNGSSequenceExtensionRemoved = p2msanitizedTargetPNGSequenceUserInput.substring(0, p2msanitizedTargetPNGSequenceUserInput.length() - 4);
                        p2mcheckPath = Paths.get(applicationRoot.resolve("Default Output").resolve(p2mtargetPNGSSequenceExtensionRemoved).toString());
                    } else if (p2mtargetPNGSequenceUserInputType == UserInputTypes.PATH_EXISTING) {
                        String p2mtempFileName = Paths.get(p2msanitizedTargetPNGSequenceUserInput).getFileName().toString();
                        p2mtargetPNGSSequenceExtensionRemoved = p2mtempFileName.substring(0, p2mtempFileName.length() - 4);
                        p2mcheckPath = Paths.get(applicationRoot.resolve("Default Output").resolve(p2mtargetPNGSSequenceExtensionRemoved).toString());
                    } else {
                        p2mcreatedFolderName = "\"[" + timePrefix + "]\"";
                    }

                    if (!Objects.isNull(p2mcheckPath) && !Objects.isNull(p2mtargetPNGSSequenceExtensionRemoved)) {
                        if (Files.isDirectory(p2mcheckPath)) {
                            // In the case that a directory with target MP4's name already exists in the default folder, the application adds a prefix [Current Date and Time]
                            p2mcreatedFolderName = "\"[" + timePrefix + "] " + p2mtargetPNGSSequenceExtensionRemoved + "\"";
                        } else {
                            p2mcreatedFolderName = "\"" + p2mtargetPNGSSequenceExtensionRemoved + "\"";
                        }
                    }

                    p2mmkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", p2mcreatedFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(p2mmkdirCommand);
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;

                    // Once the directory has been created, trim the " from createdFolderName, so it may be used in path arguments
                    p2mcreatedFolderName = p2mcreatedFolderName.replace("\"", "");
                    p2moutputLocationArgument = "\"" + applicationRoot.resolve("Default Output").resolve(p2mcreatedFolderName).resolve(p2mcreatedFolderName).toString() + "_%04d.png\"";
                }

                // The NAME_NONEXISTENT case is unique because, if the user inputs a name that doesn't exist, it's assumed that the user wants to create a folder with that name
                // Note that the same cannot be said about PATH_NONEXISTENT, because this runs the risk of creating a directory at an unknown location which the user may not be able to find, whereas other cases will go to the Default Output folder
                case NAME_NONEXISTENT, NAME_EXISTING -> {
                    String[] p2mmkdirCommand = {""};

                    if (Files.isDirectory(Paths.get(applicationRoot.resolve("Default Output").resolve(p2msanitizedOutputLocationUserInput).toString()))) {
                        p2mcreatedFolderName = "\"[" + timePrefix + "] " + p2msanitizedOutputLocationUserInput + "\"";
                    } else {
                        p2mcreatedFolderName = "\"" + p2msanitizedOutputLocationUserInput + "\"";
                    }

                    p2mmkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", p2mcreatedFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(p2mmkdirCommand);
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;
                    p2mcreatedFolderName = p2mcreatedFolderName.replace("\"", "");

                    p2moutputLocationArgument = "\"" + applicationRoot.resolve("Default Output").resolve(p2mcreatedFolderName).resolve(p2mcreatedFolderName).toString() + "_%04d.png\"";
                }

                case PATH_EXISTING -> {
                    String[] p2mmkdirCommand = {""};
                    String p2mtargetPNGSSequenceExtensionRemoved = null;
                    Path p2mcheckPath = null;

                    if (!Files.isDirectory(Paths.get(p2msanitizedOutputLocationUserInput))) {
                        SwingUtilities.invokeLater(() -> {
                            p2mconsoleOutputTextArea.setText("Error: The path provided in the Output Location is invalid");
                        });

                        return;
                    }

                    if (p2mtargetPNGSequenceUserInputType == UserInputTypes.NAME_EXISTING) {
                        p2mtargetPNGSSequenceExtensionRemoved = p2msanitizedTargetPNGSequenceUserInput.substring(0, p2msanitizedTargetPNGSequenceUserInput.length() - 4);
                        p2mcheckPath = Paths.get(p2msanitizedOutputLocationUserInput).resolve(p2mtargetPNGSSequenceExtensionRemoved);

                        if (Files.isDirectory(p2mcheckPath)) {
                            p2mcreatedFolderName = "\"[" + timePrefix + "] " + p2mtargetPNGSSequenceExtensionRemoved + "\"";
                        } else {
                            p2mcreatedFolderName = "\"" + p2mtargetPNGSSequenceExtensionRemoved + "\"";
                        }
                    } else if (p2mtargetPNGSequenceUserInputType == UserInputTypes.PATH_EXISTING) {
                        String p2mtempFileName = Paths.get(p2msanitizedTargetPNGSequenceUserInput).getFileName().toString();
                        p2mtargetPNGSSequenceExtensionRemoved = p2mtempFileName.substring(0, p2mtempFileName.length() - 4);
                        p2mcheckPath = Paths.get(p2msanitizedOutputLocationUserInput).resolve(p2mtargetPNGSSequenceExtensionRemoved);

                        if (Files.isDirectory(p2mcheckPath)) {
                            p2mcreatedFolderName = "\"[" + timePrefix + "] " + p2mtargetPNGSSequenceExtensionRemoved + "\"";
                        } else {
                            p2mcreatedFolderName = "\"" + p2mtargetPNGSSequenceExtensionRemoved + "\"";
                        }
                    }

                    p2mmkdirCommand = new String[]{"cmd.exe", "/c", "mkdir", p2mcreatedFolderName};

                    ConsoleBridge tempConsoleBridge = new ConsoleBridge(applicationRoot.resolve("Default Output"));
                    tempConsoleBridge.changeCommand(p2mmkdirCommand);
                    tempConsoleBridge.changeDirectory(Paths.get(p2msanitizedOutputLocationUserInput));
                    try {
                        tempConsoleBridge.executeCommand(true, tempOutput -> {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    tempConsoleBridge = null;
                    p2mcreatedFolderName = p2mcreatedFolderName.replace("\"", "");

                    p2moutputLocationArgument = "\"" + Paths.get(p2msanitizedOutputLocationUserInput).resolve(p2mcreatedFolderName).resolve(p2mcreatedFolderName) + "_%04d.png\"";
                }
            }

            String[] p2mfullCommand = {""};
            if (p2mdesiredFPSCheckBox.isSelected()) {
                p2mfullCommand = new String[]{this.FFMpegExecutablePath, "-i", p2minputFilePathArgument, "-vf", "\"fps=" + p2mframeSpinner.getValue().toString() + "\"", p2moutputLocationArgument};
            } else {
                p2mfullCommand = new String[]{this.FFMpegExecutablePath, "-i", p2minputFilePathArgument, p2moutputLocationArgument};
            }

            /*
            System.out.print("\n");
            for (String arg : fullCommand) {
                System.out.print(arg + " ");
            }
            * */

            try {
                p2mExecuteCommand(p2mfullCommand);
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
        p2mPanel3.add(p2mconfirmButton, constraints);

        JButton p2mcancelButton = new JButton("Cancel");
        p2mcancelButton.addActionListener(e -> {
            System.out.print("Closing User Interface...");
            mainFrame.dispose();
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
        p2mPanel3.add(p2mcancelButton, constraints);

        //==================================================
        // Part 6 - Deploying the UI

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
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

    public void m2pExecuteCommand(String[] inputCommand) throws IOException {
        this.rootConsoleBridge.changeCommand(inputCommand);

        rootConsoleBridge.executeCommand(true, consoleBridgeOutput -> {
            SwingUtilities.invokeLater(() -> {
                m2pconsoleOutputTextArea.append("\n" + consoleBridgeOutput);
            });
        });
    }

    public void p2mExecuteCommand(String[] inputCommand) throws IOException {
        this.rootConsoleBridge.changeCommand(inputCommand);

        rootConsoleBridge.executeCommand(true, consoleBridgeOutput -> {
            SwingUtilities.invokeLater(() -> {
                p2mconsoleOutputTextArea.append("\n" + consoleBridgeOutput);
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
                                this.m2pestimatedFpsTextField.setText(item.replaceAll("[^0-9]", ""));
                            });
                        }
                    }
                }
            });
        });
    }
}
