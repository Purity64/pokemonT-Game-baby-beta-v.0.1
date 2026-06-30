import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameConfig {
    //room
    public static String JoinroomID ;
    public static int gameState ;
    public static final int titleState = 0;
    public static final int createroom = 1;
    public static final int joinroom = 2;
    public static final int waitingroom = 3;
    public static final int GameRoom = 4;

    public static final String Game_status_waiting = "waiting";
    public static final String Game_status_runing = "runing";

    //player
    public static String PlayerSlot;
    public static String[] Player = new String[4];



    //font
    public static final Font thaiFont = new Font("Tahoma", Font.PLAIN, 15);
    public static Font KertazFont;

    static {
        try {
            // โหลดไฟล์จากโฟลเดอร์ res
            File fontFile = new File("res/font/Kertaz.ttf");
            KertazFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(30f);
            FontConfig.KertazFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(60f);
            FontConfig.KertazFont20 = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(20f);
            FontConfig.KertazFont30 = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(30f);
            FontConfig.KertazFont40 = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(40f);

            // ลงทะเบียนฟอนต์กับระบบ Graphics Environment (เพื่อให้เรียกใช้งานได้เสถียรขึ้น)
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(KertazFont);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.print("dwdw");
            KertazFont = new Font("Arial", Font.PLAIN, 20);
        }
    }

    //createroom
    public  static  int createroom_countPayer = 2;

    //error message
    public static boolean showMessageModal = false;
    public static String modalTitle = "";
    public static String modalMessage = "";

    //animetion
    public static double Anime_NamePlayshow;
    public static String Lest_Tren = "player_1";


}

