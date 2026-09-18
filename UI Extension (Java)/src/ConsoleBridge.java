import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//This class is designed to bridge input between the Java program and the console
public class ConsoleBridge {
    String commandLineInput;
    Scanner scanner;
    File directory;

    public ConsoleBridge() {
        this.commandLineInput = "";
        this.scanner = new Scanner(System.in);
        this.directory = new File(System.getProperty("user.dir").toString());


        while (!commandLineInput.equals("exit")) {
            System.out.print("> ");
            this.commandLineInput = scanner.nextLine();

//            String[] commandBuild = {"cmd.exe", "mkdir test", System.getProperty("user.dir").toString()};

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(this.directory);
            processBuilder.command("cmd.exe", "/c", commandLineInput);

            try {
                Process process = processBuilder.start();

                OutputStream outputStream = process.getOutputStream();
                InputStream inputStream = process.getInputStream();
                InputStream stderror = process.getErrorStream();

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
}
