import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) {
        Path FFmpegPath = Paths.get(System.getProperty("user.dir")).getParent();
        FFmpegPath = FFmpegPath.resolve("bin");

        UserInterface ui = new UserInterface(FFmpegPath);
//        System.out.print(FFmpegPath);

//        Path projectRoot = Paths.get(System.getProperty("user.dir")).getParent();
//        System.out.print(projectRoot);

//        String fileName = "\"Test Gif\"";
//        String mkdirCommand = "mkdir " + fileName;
//        System.out.print(mkdirCommand);

//        String[] FFmpegCommand = {"cmd.exe", "/c", "ffmpeg", "-i", "../Test Gif.mp4", "../Test Gif/Test Gif_%04d.png"};

//        String[] commandInput = {"cmd.exe", "/c", "mkdir test"};
    }
}