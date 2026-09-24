import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) {
        Path FFmpegPath = Paths.get(System.getProperty("user.dir")).getParent();
        FFmpegPath = FFmpegPath.resolve("bin");

        Path applicationRoot = Paths.get(System.getProperty("user.dir")).getParent();

        UserInterface ui = new UserInterface(FFmpegPath);
//        System.out.print(FFmpegPath);

//        String fileName = "\"Test Gif\"";Test mp4.mp4Test mp4.mp4Test mp4.mp4
//        String mkdirCommand = "mkdir " + fileName;
//        System.out.print(mkdirCommand);

//        String[] FFmpegCommand = {"cmd.exe", "/c", "ffmpeg", "-i", "../Test Gif.mp4", "../Test Gif/Test Gif_%04d.png"};

//        String[] commandInput = {"cmd.exe", "/c", "mkdir test"};
    }
}