import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) {
        Path applicationRoot = Paths.get(System.getProperty("user.dir"));
        if (!Files.exists(Paths.get(applicationRoot.resolve("bin").toString())) ||
                !Files.exists(Paths.get(applicationRoot.resolve("Default Output").toString())) ||
                !Files.exists(Paths.get(applicationRoot.resolve("Default Input").toString())) ||
                !Files.exists(Paths.get(applicationRoot.resolve("UI Extension (Java)").toString()))) {

            //If the applicationRoot provided doesn't appear to have the right structure, the program will try to move up one path to see if the application root is above it
            System.out.print("Provided directory does not appear to be Application Root\nAttempting to move up one directory...");
            applicationRoot = applicationRoot.getParent();
            if (!Files.exists(Paths.get(applicationRoot.resolve("bin").toString())) ||
                    !Files.exists(Paths.get(applicationRoot.resolve("Default Output").toString())) ||
                    !Files.exists(Paths.get(applicationRoot.resolve("Default Input").toString())) ||
                    !Files.exists(Paths.get(applicationRoot.resolve("UI Extension (Java)").toString()))) {

                // If it fails to find the correct structure again, it will simply print an error and end the program
                System.out.print("\nError: Could not find valid application root directory");
                System.exit(-1);
            }
        }


        UserInterface ui = new UserInterface(applicationRoot);
//        System.out.print(FFmpegPath);

//        String fileName = "\"Test Gif\"";Test mp4.mp4Test mp4.mp4Test mp4.mp4
//        String mkdirCommand = "mkdir " + fileName;
//        System.out.print(mkdirCommand);

//        String[] FFmpegCommand = {"cmd.exe", "/c", "ffmpeg", "-i", "../Test Gif.mp4", "../Test Gif/Test Gif_%04d.png"};

//        String[] commandInput = {"cmd.exe", "/c", "mkdir test"};
    }
}