import java.awt.*;

public class UiCreateroom {
    GamePanel gp;
    Font arial_30, arial_20, arial_14B;

    UI_AlertSystem UIAler = new UI_AlertSystem();

    public UiCreateroom(GamePanel gp) {
        this.gp = gp;
        arial_30 = new Font("Arial", Font.BOLD, 30);
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_14B = new Font("Arial", Font.BOLD, 14);
    }




    public void drawCreateRoomScreen(Graphics2D g2) {

        gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        g2.drawImage(Gameconfig_Pokemon.Bg_mune_Join_AND_create_img , 0 , 0 ,gp.getWidth(), gp.getHeight() , null);

        // 2. วาด Main Box
        int mainboxWidth = 500;
        int mainboxHeight = gp.getHeight() - 100;
        int mainboxX = gp.getWidth() - 550;
        int mainboxY = 50;

        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRoundRect(mainboxX, mainboxY, mainboxWidth, mainboxHeight, 30, 30);

        // 3. วาดข้อความหัวข้อ
        g2.setColor(Color.BLACK);
        g2.setFont(arial_30);
        g2.drawString("ROOM SETTINGS", mainboxX + 130, mainboxY + 50);

        g2.setFont(arial_20);
        g2.drawString("Players", mainboxX + 50, mainboxY + 110);

        // 4. วาดปุ่มเลือกผู้เล่น (ใช้ xOffset เดิมที่คุณถนัด หรือจะใช้ mainboxX + offset ก็ได้)
        playbutton(g2, 430, 180, 50, 40, "2P" , 2);
        playbutton(g2, 370, 180, 50, 40, "3P" , 3);
        playbutton(g2, 310, 180, 50, 40, "4P", 4);

        UI_inputbutton.CreaterInputButtin(g2 , mainboxX + 50 , mainboxY + 300 , mainboxWidth - 100 , 50 , "Room ID" , "createroomID");

        UI_inputbutton.CreaterInputButtin(g2 , mainboxX + 50 , mainboxY + 400 , mainboxWidth - 100 , 50 , "Payer Name", "payername");

        // 5. วาดปุ่มด้านล่าง (Create & Back)
        int paddingX = 50;
        int buttonWidth = mainboxWidth - (paddingX * 2);
        int buttonHeight = 50;
        int gap = 20;

        // --- ปุ่ม CREATE ---
        int blueBoxY = mainboxY + 500;
        g2.setColor(Color.BLUE);
        clickbutton( g2,mainboxX + paddingX ,blueBoxY ,  buttonWidth , buttonHeight , Color.blue );

        g2.setColor(Color.WHITE); // เปลี่ยนเป็นสีขาวให้อ่านง่ายบนพื้นน้ำเงิน
        g2.setFont(arial_20);
        drawCenteredString(g2, "CREATE", mainboxX + paddingX, blueBoxY, buttonWidth, buttonHeight, arial_20);
        // --- ปุ่ม BACK ---
        int grayBoxY = blueBoxY + buttonHeight + gap; // ย้ายมาประกาศตรงนี้ก่อนใช้งาน
        g2.setColor(Color.GRAY);
        clickbutton( g2,mainboxX + paddingX ,grayBoxY ,  buttonWidth , buttonHeight , Color.GRAY );

        g2.setColor(Color.BLACK);
        drawCenteredString(g2, "BACK", mainboxX + paddingX, grayBoxY, buttonWidth, buttonHeight, arial_20);
        if (GameConfig.showMessageModal){
            UIAler.drawMessageDialog(g2, gp,  GameConfig.modalTitle, GameConfig.modalMessage);
        }
    }



    private void playbutton(Graphics2D g2, int xOffset, int y, int width, int height, String text , int countpayer) {
        int x = gp.getWidth() - xOffset;

        // เช็ค Hover
        if (gp.mouseX >= x && gp.mouseX <= x + width && gp.mouseY >= y && gp.mouseY <= y + height) {
            g2.setColor(Color.YELLOW);
            gp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }

        if (GameConfig.createroom_countPayer == countpayer){
            g2.setColor(Color.YELLOW);
        }else {
            g2.setColor(Color.LIGHT_GRAY);
        }

        g2.fillRoundRect(x, y, width, height, 10, 10);

        g2.setColor(Color.BLACK);
        g2.setFont(arial_14B);
        g2.drawString(text, x + 12, y + 25);
    }
    public void clickbutton(Graphics2D g2 , int x , int y , int width , int height , Color defaultColor ){

        if (gp.mouseX >= x && gp.mouseX <= x + width && gp.mouseY >= y && gp.mouseY <= y + height){
            gp.setCursor(new Cursor(Cursor.HAND_CURSOR));
            g2.setColor(Color.red);
        }else{
            g2.setColor(defaultColor);
        }

        g2.fillRoundRect(x, y, width, height, 20, 20);



    }

    public void drawCenteredString(Graphics2D g2, String text, int boxX, int boxY, int boxWidth, int boxHeight, Font font) {
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics(font);

        // 1. หาความกว้างของข้อความ
        int textWidth = fm.stringWidth(text);

        // 2. คำนวณหาตำแหน่ง X ที่จะทำให้กึ่งกลาง
        int x = boxX + (boxWidth - textWidth) / 2;


        int y = boxY + (boxHeight / 2) + (fm.getAscent() / 2) - 2;

        g2.drawString(text, x, y);
    }

    public void UiCreateRoomClik(int x ,int y){
        int mainboxWidth = 500;
        int mainboxX = gp.getWidth() - 550;
        int mainboxY = 50;
        int paddingX = 50;
        int buttonWidth = mainboxWidth - (paddingX * 2);
        int buttonHeight = 50;

        int blueBoxY = mainboxY + 500; // ปุ่ม CREATE
        int grayBoxY = blueBoxY + buttonHeight + 20; // ปุ่ม BACK (gap=20)

        // 2. ตรวจสอบการคลิกปุ่ม BACK
        if (MouseOver.mouseOver(x, y, mainboxX + paddingX, grayBoxY, buttonWidth, buttonHeight)) {
            System.out.println("LOG: กดปุ่ม BACK -> กลับหน้า Title");
            GameConfig.gameState = GameConfig.titleState; // เปลี่ยนหน้า!
        }

        // 3. ตรวจสอบการคลิกปุ่ม CREATE
        if (MouseOver.mouseOver(x, y, mainboxX + paddingX, blueBoxY, buttonWidth, buttonHeight)) {
            System.out.println("LOG: กดปุ่ม CREATE -> กำลังสร้างห้อง...");
            if (DB_dbmanager.chackroom(UI_inputbutton.hsm.get("createroomID"))){
                GameConfig.showMessageModal = true;
                GameConfig.modalTitle = "I already have this room.";
                GameConfig.modalMessage = "";
            } else if (UI_inputbutton.hsm.get("createroomID").isEmpty()) {
                GameConfig.showMessageModal = true;
                GameConfig.modalTitle = "plse input room id";
                GameConfig.modalMessage = "";
            } else if (UI_inputbutton.hsm.get("payername").isEmpty()) {
                GameConfig.showMessageModal = true;
                GameConfig.modalTitle = "plse input player name";
                GameConfig.modalMessage = "";
            } else {
                DB_dbmanager.chackplayer();
                String room = UI_inputbutton.hsm.get("createroomID");
                DB_dbmanager.insertRoom(room,GameConfig.createroom_countPayer);
                DB_dbmanager.AddPlayer(UI_inputbutton.hsm.get("payername"));
                UI_inputbutton.resetinput();
                GameConfig.JoinroomID =room ;
                GameConfig.gameState = GameConfig.waitingroom;

            }
        }

        if (MouseOver.mouseOver(x, y, gp.getWidth() - 430, 180, 50, 40)) {
            GameConfig.createroom_countPayer = 2;
            System.out.println("เลือกโหมด 2 ผู้เล่น");

            // คุณอาจสร้างตัวแปรเก็บจำนวนคนไว้ใน GameConfig ก็ได้
        }
        if (MouseOver.mouseOver(x, y, gp.getWidth() - 370, 180, 50, 40)) {
            GameConfig.createroom_countPayer = 3;
            System.out.println("เลือกโหมด 3 ผู้เล่น");
            // คุณอาจสร้างตัวแปรเก็บจำนวนคนไว้ใน GameConfig ก็ได้
        }
        if (MouseOver.mouseOver(x, y, gp.getWidth() - 310, 180, 50, 40)) {
            GameConfig.createroom_countPayer = 4;
            System.out.println("เลือกโหมด 4 ผู้เล่น");
            // คุณอาจสร้างตัวแปรเก็บจำนวนคนไว้ใน GameConfig ก็ได้
        }

        if (MouseOver.mouseOver(x , y , mainboxX + 50 , mainboxY + 300 , mainboxWidth - 100 , 50  )){
            UI_inputbutton.activeId = "createroomID";
        }else if (MouseOver.mouseOver(x , y , mainboxX + 50 , mainboxY + 400 , mainboxWidth - 100 , 50  )){
            UI_inputbutton.activeId = "payername";
        }
        else{
            UI_inputbutton.activeId = "";
        }


    }




}