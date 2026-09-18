import java.nio.file.Path;
import java.nio.file.Paths;

public class Driver {
    public static void main(String[] args) {
//        UserInterface ui = new UserInterface();
        Path FFmpegPath = Paths.get(System.getProperty("user.dir")).getParent();
        FFmpegPath = FFmpegPath.resolve("bin").resolve("ffmpeg.exe");
        System.out.print(FFmpegPath);

        ConsoleBridge consoleBridge = new ConsoleBridge();
    }
}