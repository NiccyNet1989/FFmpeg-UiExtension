import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//This class is designed to bridge input between the Java program and the console
public class ConsoleBridge {
    String[] commandLineInput;
    Scanner scanner;
    File directory;
    ProcessBuilder processBuilder;
    Process process;
    OutputStream outputStream;
    InputStream inputStream;
    InputStream stderror;


    public ConsoleBridge() {
        this.commandLineInput = new String[]{""};
        this.scanner = new Scanner(System.in);
        this.directory = new File(System.getProperty("user.dir").toString());


        while (!commandLineInput.equals("exit")) {
            System.out.print("> ");
            this.commandLineInput = new String[]{scanner.nextLine()};

            this.processBuilder = new ProcessBuilder();
            processBuilder.directory(this.directory);
//            processBuilder.command("cmd.exe", "/c", this.commandLineInput);
            processBuilder.command(this.commandLineInput);

            try {
                this.process = processBuilder.start();

                this.outputStream = process.getOutputStream();
                this.inputStream = process.getInputStream();
                this.stderror = process.getErrorStream();

                process.waitFor(5, TimeUnit.SECONDS);

                outputStream.flush();
                outputStream.close();
                process.destroy();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    // The below constructor is intended to be used for one-time command console calls, and is what the UI will primarily be using
    public ConsoleBridge(String[] inputtedCommand, Path inputtedDirectory) {
        this.commandLineInput = inputtedCommand;
//        this.directory = new File(System.getProperty("user.dir").toString());
        this.directory = inputtedDirectory.toFile();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(this.directory);
        processBuilder.command(this.commandLineInput);

        try {
            this.process = processBuilder.start();

            this.outputStream = process.getOutputStream();
            this.inputStream = process.getInputStream();
            this.stderror = process.getErrorStream();

            if (process.waitFor() == 0) {
                outputStream.flush();
                outputStream.close();
                process.destroy();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
