import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DB_getImage {

    public static BufferedImage getimg(String part){
        BufferedImage img;
        try{
            return ImageIO.read(new File("res/images/"+part));
        }catch (IOException e) {
            System.out.println("หาไฟล์ภาพโปเกมอนไม่เจอ: " + part);
        }
        return null;
    }


}
