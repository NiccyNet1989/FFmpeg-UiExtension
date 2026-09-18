import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) {
//        UserInterface ui = new UserInterface();
//        Path FFmpegPath = Paths.get(System.getProperty("user.dir")).getParent();
//        FFmpegPath = FFmpegPath.resolve("bin");
//        System.out.print(FFmpegPath);

        String fileName = "\"Test Gif\"";
        String mkdirCommand = "mkdir " + fileName;
        System.out.print(mkdirCommand);

//        String[] ffmpegCommand = {"cmd.exe", "/c", "ffmpeg", "-i", "Test Gif.mp4", "Test Gif/Test Gif_%04d.png"};

        String[] commandInput = {"cmd.exe", "/c", "mkdir test"};
        ConsoleBridge consoleBridge = new ConsoleBridge(commandInput, Paths.get(System.getProperty("user.dir")).getParent());

        
    }
}