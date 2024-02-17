import com.heshanthenura.backgroundcolor.MainApplication;

import java.io.File;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        File file = new File(MainApplication.class.getResource("img.jpg").toURI());
        System.out.println(file.getName());
    }
}
