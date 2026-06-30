import java.awt.*;

public class UIJoinroom {
    GamePanel gp;
    UiCreateroom uiCreateroom;

    Font arial_30, arial_20, arial_14B;

    UI_AlertSystem UIAler = new UI_AlertSystem();



    public UIJoinroom(GamePanel gp) {
        this.gp = gp;
        this.uiCreateroom = new UiCreateroom(gp);
        arial_30 = new Font("Arial", Font.BOLD, 30);
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_14B = new Font("Arial", Font.BOLD, 14);
    }




    public  void drawJoinRoom (Graphics2D g2){
        int mainboxWidth = 500;
        int mainboxHeight = gp.getHeight() - 100;
        int mainboxX = gp.getWidth() - 550;
        int mainboxY = 50;

        int pading = 30;
        int buttonWidht = mainboxWidth - pading*2;
        int buttonX = mainboxX + pading;

        gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        g2.drawImage(Gameconfig_Pokemon.Bg_mune_Join_AND_create_img , 0 , 0 ,gp.getWidth(), gp.getHeight() , null);



        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRoundRect(mainboxX , mainboxY , mainboxWidth , mainboxHeight , 20 , 20);

        UI_inputbutton.CreaterInputButtin(g2,buttonX , mainboxY + 300 , buttonWidht , 50 , "RoomID" , "JoinRoomId");
        UI_inputbutton.CreaterInputButtin(g2,buttonX , mainboxY + 400 , buttonWidht , 50 , "Payer Name" , "JoinPayername");

        g2.setColor(Color.GRAY);
        //back button
        uiCreateroom.clickbutton(g2 , buttonX ,mainboxY + 600 , buttonWidht , 50 , Color.GRAY);
        //join button
        uiCreateroom.clickbutton(g2 , buttonX , mainboxY + 520 , buttonWidht , 50 , Color.blue);
        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2,"BACK",buttonX , mainboxY + 600 , buttonWidht , 50 , arial_30);
        uiCreateroom.drawCenteredString(g2 , "Join Room", buttonX , mainboxY + 520 , buttonWidht , 50 , arial_30);


        if (GameConfig.showMessageModal){
            UIAler.drawMessageDialog(g2, gp,  GameConfig.modalTitle, GameConfig.modalMessage);
        }
    }

    public void UIJoinClick(int x , int y){
        int mainboxWidth = 500;
        int mainboxHeight = gp.getHeight() - 100;
        int mainboxX = gp.getWidth() - 550;
        int mainboxY = 50;

        int pading = 30;
        int buttonX = mainboxX + pading;
        int buttonWidht = mainboxWidth - pading*2;
        int buttonY = mainboxY + 600;

        //input Roomid
        if (MouseOver.mouseOver(x , y , buttonX , mainboxY + 300 , buttonWidht , 50)){
            UI_inputbutton.activeId = "JoinRoomId";
        } else if (MouseOver.mouseOver(x, y , buttonX , mainboxY + 400 , buttonWidht , 50 )) {
            UI_inputbutton.activeId = "JoinPayername";
        } else {
            UI_inputbutton.activeId = "";
        }

        // joinbutton
        if (MouseOver.mouseOver(x , y ,buttonX , mainboxY + 520 , buttonWidht , 50)){
            String roomid = UI_inputbutton.hsm.get("JoinRoomId");
            String playername = UI_inputbutton.hsm.get("JoinPayername");
            if (UI_inputbutton.hsm.get("JoinRoomId").isEmpty()){
                GameConfig.showMessageModal = true;
                GameConfig.modalTitle = "please input RoomID";
                GameConfig.modalMessage = "";
            } else if (playername.isEmpty()) {
                GameConfig.showMessageModal = true;
                GameConfig.modalTitle = "please input PlayerName";
                GameConfig.modalMessage = "";
            } else if (DB_dbmanager.chackroom(roomid)){
                DB_dbmanager.chackplayer();
                String fil = DB_dbmanager.chackplayerthisroom(roomid);

                if (fil != null && !fil.isEmpty()){

                    DB_dbmanager.update_current_players(roomid);
                    if (!DB_dbmanager.isMaxPlayer(roomid)){
                        DB_dbmanager.joinRoom(fil ,roomid );
                        DB_dbmanager.AddPlayer(playername);
                        GameConfig.JoinroomID = roomid;
                        UI_inputbutton.resetinput();

                        GameConfig.gameState = GameConfig.waitingroom;
                    }else {
                        GameConfig.showMessageModal = true;
                        GameConfig.modalTitle = "Room Max";
                    }

                }




            }else {
                GameConfig.showMessageModal = true;
                GameConfig.modalTitle = "Not Have RoomID";
                GameConfig.modalMessage = "";
            }

        }
        //backbutton
        if (MouseOver.mouseOver(x , y ,buttonX , buttonY , buttonWidht , 50)){
            GameConfig.gameState = GameConfig.titleState;
        }



    }
}
