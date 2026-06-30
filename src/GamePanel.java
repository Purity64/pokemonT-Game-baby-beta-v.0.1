import javax.swing.JPanel;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePanel extends JPanel implements Runnable {


    public int mouseX, mouseY;

    Thread gameThread;
    UiCreateroom uiCreateroom = new UiCreateroom(this);
    UIJoinroom uiJoinroom = new UIJoinroom(this);
    UIwaitingroom uIwaitingroom = new UIwaitingroom(this);
    UIGameStart uiGameStart = new UIGameStart(this);
    DB_dbmanager dbManager;


    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.dbManager = new DB_dbmanager(this);

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

            }
        });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (GameConfig.gameState == GameConfig.titleState){
                    checkButtonClick(x,y );
                }else if (GameConfig.gameState == GameConfig.createroom){
                    uiCreateroom.UiCreateRoomClik(x, y);
                } else if (GameConfig.gameState == GameConfig.joinroom) {
                    uiJoinroom.UIJoinClick(x , y);
                } else if (GameConfig.gameState == GameConfig.waitingroom){
                    uIwaitingroom.UIwaitingroomclick(x , y);
                } else if (GameConfig.gameState == GameConfig.GameRoom) {
                    uiGameStart.UIGameStartClick(x , y);
                }

                if (GameConfig.showMessageModal) {
                    int modalW = 400;
                    int modalH = 200;
                    int modalX = (getWidth() - modalW) / 2;
                    int modalY = (getHeight() - modalH) / 2;
                    int btnX = modalX + 150;
                    int btnY = modalY + 140;

                    if (MouseOver.mouseOver(x, y, btnX, btnY, 100, 40)) {
                        System.out.println("LOG: ปิด Modal");
                        GameConfig.showMessageModal = false;
                        return;
                    }
                }

            }
        });

        this.addKeyListener(new java.awt.event.KeyAdapter() {
            // ใน KeyListener ของ GamePanel
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                String id = UI_inputbutton.activeId;

                // ถ้ามีช่องที่ถูกเลือกอยู่ (ไม่เป็นค่าว่าง)
                if (!id.equals("")) {
                    char c = e.getKeyChar();
                    String currentStr = UI_inputbutton.hsm.get(id);

                    if (c == '\b') { // Backspace
                        if (currentStr.length() > 0) {
                            UI_inputbutton.hsm.put(id, currentStr.substring(0, currentStr.length() - 1));
                        }
                    } else if (Character.isDefined(c) && c != '\n') { // พิมพ์ตัวอักษรปกติ
                        if (currentStr.length() < 9) { // จำกัดความยาว
                            UI_inputbutton.hsm.put(id, currentStr + c);
                        }
                    }
                }
            }
        });

        loadImages();
        String room  = DB_getData.getroom();
        if (room != "" && room != null){
            String start = DB_getData.getGameState(room);
            if (start != "" && start != null){
                if (start.equalsIgnoreCase(GameConfig.Game_status_waiting)){
                    GameConfig.JoinroomID = room;
                    GameConfig.gameState = GameConfig.waitingroom;
                }else if (start.equalsIgnoreCase(GameConfig.Game_status_runing)){
                    GameConfig.JoinroomID = room;
                    GameConfig.gameState = GameConfig.GameRoom;
                }
            }else {
                GameConfig.gameState = GameConfig.titleState;
            }
        }else {
            GameConfig.gameState = GameConfig.titleState;
        }

    }

    private void loadImages() {
        try {

                //event
                Gameconfig_Event.test = ImageIO.read(new File("res/images/test.png"));
                //cradskill
                Gameconfig_Pokemon.BG_crad_skill_img = ImageIO.read(new File("res/images/crad/cradskill.png"));
                //bg
                Gameconfig_Pokemon.BG_menu_img = ImageIO.read(new File("res/images/bg/BG_menu.png"));
                Gameconfig_Pokemon.Bg_mune_Join_AND_create_img = ImageIO.read(new File("res/images/bg/BG_menu_join_AND_create.png"));
                Gameconfig_Pokemon.BG_menu_Witeingroom = ImageIO.read(new File("res/images/bg/BG_menu_Witingroom.png"));
                Gameconfig_Pokemon.BG_menu_GaneRuning = ImageIO.read(new File("res/images/bg/BG_menu_GameRuning.png"));
                Gameconfig_IMG.BG_Sell_Pokemon = ImageIO.read(new File("res/images/bg/BG_menu_sell_Pokemon.png"));
                Gameconfig_IMG.BG_Shop = ImageIO.read(new File("res/images/bg/BG_menu_shop.png"));
                Gameconfig_IMG.BG_PokemonCenter = ImageIO.read(new File("res/images/bg/BG_Pokemoncenter.png"));

                Gameconfig_Pokemon.pokeball_img = ImageIO.read(new File("res/images/items/pokeball.png"));
                Gameconfig_Pokemon.coins_img = ImageIO.read(new File("res/images/items/coins.png"));
                Gameconfig_Pokemon.poke_card_img = ImageIO.read(new File("res/images/items/card.png"));
                Gameconfig_Pokemon.pokeball_Nunull_img = ImageIO.read(new File("res/images/items/pokebellNunull.png"));

                Gameconfig_IMG.shield_icon = ImageIO.read(new File("res/images/items/shield.png"));
                Gameconfig_IMG.sword_icon = ImageIO.read(new File("res/images/items/sword.png"));

                for(int m = 1 ; m <= 6 ; m++){
                    ArrayList<BufferedImage> tempArray = new ArrayList<>();
                    for (int i = 1; i <= 90; i++) {
                        try {
                            BufferedImage img = ImageIO.read(new File("res/images/dice/" + m + "/" + i + ".png"));
                            if (img != null) tempArray.add(img);
                        } catch (IOException e) {
                            System.out.println("หาไฟล์ไม่เจอ: " + m + "/" + i);
                        }
                    }
                    Gameconfig_Pokemon.hashmapdice.put(m, tempArray);
                }
                for(String a : Gameconfig_Event.EventBoxAyyaykeyForder){
                    ArrayList<BufferedImage> tempArray = new ArrayList<>();
                    for (int i = 1 ; i <= 40 ; i++){
                        try{
                            BufferedImage img = ImageIO.read(new File("res/images/box/" + a + "/" + i + ".png"));
                            if (img != null) tempArray.add(img);
                        } catch (IOException e) {
                            System.out.println("หาไฟล์ไม่เจอ: " + a + "/" + i);
                        }
                    }
                    System.out.println(a);
                    Gameconfig_Event.RenderimgBoxeventHashMap.put(a,tempArray);
                }

                ArrayList<String> tamegetAllPokemon = new ArrayList<>();
                tamegetAllPokemon.addAll(DB_getData.getAll_NamePoekmon());
                String[] KeyPokemon_show = Gameconfig_Pokemon.Keymap_PokemonShowAnime_Hsm;
                for (String a :  tamegetAllPokemon){
                    HashMap<String , ArrayList<BufferedImage>> TrmpHsm = new HashMap<>();
                    for (int m = 0 ; m < 3 ; m++){
                        ArrayList<BufferedImage> TempArray = new ArrayList<>();
                        for(int i = 1 ; i <= 40 ; i++){
                            int mx = m +1;
                            try{
                                BufferedImage img = ImageIO.read(new File("res/images/pokemon/"+ a + "/" + mx+ "/" + i +".png"));
                                if (img != null) TempArray.add(img);
                            } catch (IOException e) {
                                System.out.println("หาไฟล์ไม่เจอ: " + a + "/" + mx + "/" + i + ".png");
                                break;
                            }

                        }
                        TrmpHsm.put(KeyPokemon_show[m], TempArray);
                    }
                    Gameconfig_Pokemon.PokemonShowAnime_Hsm.put(a ,  TrmpHsm);

                    HashMap<String , String> TrmpHsmskill = new HashMap<>();
                    String[] arrskill = DB_getData.getPokemonDate(a);
                    String keymapskill[] = Gameconfig_Pokemon.Keymap_pokemonskill_hsm;
                    TrmpHsmskill.put(keymapskill[0] , arrskill[0]);
                    TrmpHsmskill.put(keymapskill[1] , arrskill[1]);
                    TrmpHsmskill.put(keymapskill[2] , arrskill[2]);
                    TrmpHsmskill.put(keymapskill[3] , arrskill[3]);
                    TrmpHsmskill.put(keymapskill[4] , arrskill[4]);
                    TrmpHsmskill.put(keymapskill[5] , arrskill[5]);
                    TrmpHsmskill.put(keymapskill[6] , arrskill[6]);

                    Gameconfig_Pokemon.PokemonSkill_Hsm.put(a , TrmpHsmskill);

                }
                Gameconfig_Pokemon.pikachu_img = ImageIO.read(new File("res/images/pokemon/"+Gameconfig_Pokemon.pikachu+"/1.png"));
                Gameconfig_Pokemon.snorlax_img = ImageIO.read(new File("res/images/pokemon/"+Gameconfig_Pokemon.snorlax+"/1.png"));

        } catch (IOException e) {
            System.out.println("ERROR: โหลดรูปภาพไม่สำเร็จ!");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            System.out.println("ERROR: หาไฟล์รูปภาพไม่เจอ!");
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            update();
            repaint(); // สั่งให้วาดหน้าจอใหม่ตลอดเวลา
            try { Thread.sleep(16); } catch (Exception e) {} // ประมาณ 60 FPS
        }
    }

    public void update() {
        // อัปเดตตรรกะตามสถานะเกม
        if (GameConfig.gameState == GameConfig.createroom) {
            // โค้ดขยับตัวละครจะอยู่ตรงนี้
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;



        if (GameConfig.gameState == GameConfig.titleState) {
            drawTitleScreen(g2);
        }else if (GameConfig.gameState == GameConfig.createroom) {
            uiCreateroom.drawCreateRoomScreen(g2);
        } else if (GameConfig.gameState == GameConfig.joinroom) {
            uiJoinroom.drawJoinRoom(g2);
        } else if (GameConfig.gameState == GameConfig.waitingroom) {
            uIwaitingroom.drawUIwaitingroom(g2);
        } else if (GameConfig.gameState == GameConfig.GameRoom) {
            uiGameStart.drawUIGameStart(g2);
        }

        g2.setColor(Color.WHITE);

        g2.dispose();
    }

    public void drawTitleScreen(Graphics2D g2) {
        g2.setColor(new Color(34, 139, 34));
        g2.drawImage(Gameconfig_Pokemon.BG_menu_img , 0 , 0 , getWidth() , getHeight() , null);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        g2.setFont(new Font("Arial", Font.BOLD, 80));
        g2.setColor(Color.GRAY);
        g2.drawString("PokamonT Game", 105, 205);
        g2.setColor(Color.YELLOW);
        g2.drawString("PokamonT Game", 100, 200);

        // ตั้งค่า Font สำหรับปุ่ม
        g2.setFont(new Font("Arial", Font.PLAIN, 40));

        // ปุ่มที่ 1: CREATE ROOM
        drawMenuButton(g2, "CREATE ROOM", 100, 400);

        // ปุ่มที่ 2: JOIN ROOM
        drawMenuButton(g2, "JOIN ROOM", 100, 500);

        // ปุ่มที่ 3: EXIT
        drawMenuButton(g2, "EXIT", 100, 600);
    }

    // ฟังก์ชันพิเศษสำหรับวาดปุ่มและเช็ค Hover
    private void drawMenuButton(Graphics2D g2, String text, int x, int y) {
        // 1. คำนวณขอบเขตของตัวอักษร (ใช้ FontMetrics เพื่อความแม่นยำ)
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();


        if (mouseX >= x && mouseX <= x + textWidth && mouseY >= y - fm.getAscent() && mouseY <= y + fm.getDescent()) {

            g2.setColor(Color.CYAN);
            g2.drawString("> " + text, x - 30, y);

            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

        }
    }

    private void checkButtonClick(int x, int y) {
        if (GameConfig.gameState == GameConfig.titleState) {
            // ใช้ FontMetrics เพื่อให้ขอบเขตปุ่มตรงกับที่วาดเป๊ะๆ
            FontMetrics fm = getFontMetrics(new Font("Arial", Font.PLAIN, 40));

            // ปุ่ม CREATE ROOM (x=100, y=400)
            if (checkBounds(x, y, 100, 400, fm, "CREATE ROOM")) {
                System.out.println("สร้างห้อง!");
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                GameConfig.gameState = GameConfig.createroom; // ตัวอย่าง: คลิกแล้วเริ่มเกม
            }

            // ปุ่ม JOIN ROOM (x=100, y=500)
            if (checkBounds(x, y, 100, 500, fm, "JOIN ROOM")) {
                GameConfig.gameState = GameConfig.joinroom;
            }

            // ปุ่ม EXIT (x=100, y=600)
            if (checkBounds(x, y, 100, 600, fm, "EXIT")) {
                System.exit(0);
            }
        }
    }

    // ฟังก์ชันช่วยเช็คว่าจุด (mx, my) อยู่ในขอบเขตของข้อความหรือไม่
    private boolean checkBounds(int mx, int my, int x, int y, FontMetrics fm, String text) {
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        return (mx >= x && mx <= x + width && my >= y - fm.getAscent() && my <= y + fm.getDescent());
    }
    // แก้ไขจาก string เป็น String และปรับการวาดความสูง
    private void playbutton(Graphics2D g2, int xOffset, int y, int width, int height, String text) {
        int x = getWidth() - xOffset;

        // ดึง FontMetrics มาเพื่อจัดตัวอักษรให้อยู่กลางปุ่ม
        FontMetrics fm = g2.getFontMetrics(new Font("Arial", Font.BOLD, 14));

        // เช็ค Hover สำหรับปุ่มสี่เหลี่ยม
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            g2.setColor(Color.YELLOW);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            g2.setColor(Color.LIGHT_GRAY); // สีปกติ
        }

        // วาดตัวปุ่ม
        g2.fillRoundRect(x, y, width, height, 10, 10);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(text, textX, textY);
    }


}