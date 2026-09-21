import java.io.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//This class is designed to bridge input between the Java program and the console
public class ConsoleBridge {
    String[] currentCommand;
    Scanner scanner;
    File directory;
    ProcessBuilder processBuilder;
    Process process;
    OutputStream outputStream;
    InputStream inputStream;
    InputStream stderror;


    public ConsoleBridge(Path inputtedDirectory) {
        this.currentCommand = new String[]{""};
        this.directory = inputtedDirectory.toFile();
        this.processBuilder = new ProcessBuilder();
        this.process = null;
        this.outputStream = null;
        this.inputStream = null;
        this.stderror = null;
    }


    // The below constructor is intended to be used for one-time command console calls, and is what the UI will primarily be using
    public ConsoleBridge(String[] inputtedCommand, Path inputtedDirectory) {
        this.currentCommand = inputtedCommand;
//        this.directory = new File(System.getProperty("user.dir").toString());
        this.directory = inputtedDirectory.toFile();

        this.processBuilder = new ProcessBuilder();
        processBuilder.directory(this.directory);
        processBuilder.command(this.currentCommand);

        try {
            this.process = processBuilder.start();


            this.outputStream = process.getOutputStream();
            this.inputStream = process.getInputStream();
            this.stderror = process.getErrorStream();

            System.out.print("Successfully created PNG Sequence");

            process.waitFor(5, TimeUnit.SECONDS);
            outputStream.flush();
            outputStream.close();
            process.destroy();

//            if (process.waitFor() == 0) {
//                outputStream.flush();
//                outputStream.close();
//                process.destroy();
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean changeCommand(String[] inputtedCommand) {
        this.currentCommand = inputtedCommand;
        this.processBuilder.command(this.currentCommand);
        return true;
    }

    public boolean changeDirectory(Path inputtedDirectory) {
        this.directory = inputtedDirectory.toFile();
        this.processBuilder.directory(this.directory);
        return true;
    }

    public boolean executeCommand() {
        if (this.currentCommand.equals(new String[]{""})) {
            System.out.print("\nError: No current command\n");
            return false;
        }


        try {
            terminateCurrentProcess();  // Starts by deleting the previous process

            this.process = processBuilder.start();

            this.outputStream = process.getOutputStream();
            this.inputStream = process.getInputStream();
            this.stderror = process.getErrorStream();

            System.out.print("\n\nExecuting current command: \n\t" + this.currentCommand[0]);
            for (int i = 1; i < currentCommand.length; i++) {
                System.out.print(" " + currentCommand[i]);
            }
            System.out.print("\nAt directory: \n\t" + this.directory + "\n");

            process.waitFor(5, TimeUnit.SECONDS);
            this.terminateCurrentProcess();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean terminateCurrentProcess() throws IOException {
        if (Objects.isNull(this.outputStream)) {
            return false;
        }

        outputStream.flush();
        outputStream.close();
        process.destroy();

        this.inputStream = null;
        this.stderror = null;

        return true;
    }
}