import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class UIwaitingroom {
    GamePanel gp;
    UiCreateroom uiCreateroom;
    Font arial_30, arial_20, arial_14B;

    UI_AlertSystem UIAler = new UI_AlertSystem();



    public UIwaitingroom(GamePanel gp) {
        this.gp = gp;
        this.uiCreateroom = new UiCreateroom(gp);
        arial_30 = new Font("Arial", Font.BOLD, 30);
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_14B = new Font("Arial", Font.BOLD, 14);
    }

    public void drawUIwaitingroom(Graphics2D g2){
        DB_dbmanager.Auto_Update_gameState();
        int mainboxWidth = 500;
        int mainboxHeight = gp.getHeight() - 100;
        int mainboxX = gp.getWidth() - 650;
        int mainboxY = 50;


        int pading = 30;
        int buttonWidht = mainboxWidth - pading*2;
        int buttonX = mainboxX + pading;

        gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        g2.drawImage(Gameconfig_Pokemon.BG_menu_Witeingroom , 0 , 0 ,gp.getWidth(), gp.getHeight() , null);





        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRoundRect(mainboxX  , mainboxY , mainboxWidth + 100 , mainboxHeight , 20 , 20);
        // player zoon
        String box[] = DB_getData.getRoomUser(GameConfig.JoinroomID);

        g2.setColor(Color.BLACK);
        g2.setFont(arial_30);

        g2.drawString( "Player "+ box[4] + " / " + box[5],mainboxX+ 20 , mainboxY + 40 );

        g2.drawString( "RoomID : "+ GameConfig.JoinroomID,mainboxX+ 400 , mainboxY + 40 );
        if (box[0] != null && !box[0].isEmpty() ){
            drawBoxcharacter(g2 , mainboxX , mainboxY , Color.blue , box[0]);
        }

        if (box[1] != null && !box[1].isEmpty()  ){
            drawBoxcharacter(g2 , mainboxX + 150 , mainboxY , Color.blue, box[1]);
        }

        if ( box[2] != null && !box[2].isEmpty() ){
            drawBoxcharacter(g2 , mainboxX + 300 , mainboxY , Color.blue , box[2]);
        }

        if (box[3] != null && !box[3].isEmpty() ){
            drawBoxcharacter(g2 , mainboxX + 450 , mainboxY , Color.blue , box[3]);
        }

        // Detail Pokemon
        drawBoxPokemon(g2,Gameconfig_Pokemon.User_Select_Pokemon,mainboxX , mainboxY + 200 , DB_pokemon.getPokeimgByName(Gameconfig_Pokemon.User_Select_Pokemon) , false);
        g2.setColor(Color.WHITE);

        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(135), mainboxX + 160, mainboxY + 320);
        g2.fillRect(mainboxX + 120, mainboxY + 320, 50, 50);
        g2.setTransform(old);

        g2.fillRoundRect(mainboxX + 145, mainboxY + 257, 400, 200 , 20 ,20);
        g2.setColor(Color.black);
        g2.setFont(GameConfig.thaiFont);

        String pokemonSkiltext[] = DB_pokemon.getPokemonSkills(Gameconfig_Pokemon.User_Select_Pokemon);

        Util_drawtext.drawWrappedText(g2,"Skill 1 : "+pokemonSkiltext[0], mainboxX + 155 , mainboxY + 267 ,380);
        Util_drawtext.drawWrappedText(g2,"Skill 2 : "+pokemonSkiltext[1], mainboxX + 155 , mainboxY + 367 ,380);


        //Select pokemon zoon
        int selectpokem_zoon_mainX = mainboxX - 750;
        int selectpokem_zoon_mainboxWidth = mainboxWidth + 200;

        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRoundRect(selectpokem_zoon_mainX, mainboxY ,  selectpokem_zoon_mainboxWidth , mainboxHeight , 20 , 20);

        drawBoxPokemon(g2,Gameconfig_Pokemon.pikachu ,selectpokem_zoon_mainX , mainboxY , Gameconfig_Pokemon.pikachu_img , true );

        drawBoxPokemon(g2,Gameconfig_Pokemon.snorlax ,selectpokem_zoon_mainX + 150 , mainboxY , Gameconfig_Pokemon.snorlax_img , true);




        //blackbutton

        String Player_master = DB_dbmanager.chackplayerthisroom(GameConfig.JoinroomID);
        int buttonLevenRoomY = 600;
        int buttonSelectY = 520;
        if (Player_master.equalsIgnoreCase("player_1")){
            buttonLevenRoomY = 630;
            buttonSelectY = 570;

            g2.setColor(Color.YELLOW);
            uiCreateroom.clickbutton(g2 , mainboxX + 50 , mainboxY + 500 , mainboxWidth , 50 , Color.YELLOW);

            g2.setColor(Color.BLACK);
            uiCreateroom.drawCenteredString(g2 , "Game Start" ,mainboxX + 50 , mainboxY + 500 , mainboxWidth, 50 , arial_20);
        }

        g2.setColor(Color.blue);
        uiCreateroom.clickbutton(g2 , mainboxX + 50 , mainboxY + buttonSelectY , mainboxWidth , 50 , Color.blue);
        g2.setColor(Color.GRAY);
        uiCreateroom.clickbutton(g2 , mainboxX + 50 , mainboxY + buttonLevenRoomY , mainboxWidth , 50 , Color.GRAY);
        g2.setColor(Color.BLACK);
        uiCreateroom.drawCenteredString(g2 , "Select Pokemon" ,mainboxX + 50 , mainboxY + buttonSelectY , mainboxWidth, 50 , arial_20);
        uiCreateroom.drawCenteredString(g2 , "Leave Room" ,mainboxX + 50 , mainboxY + buttonLevenRoomY , mainboxWidth, 50 , arial_20);


    }

    public void drawBoxcharacter(Graphics2D g2 , int x , int y  , Color  color , String token){
        int mainboxWidth = 120;
        int mainboxHeight = 120;
        g2.setColor(color);
        g2.fillRoundRect(x + 15, y + 55 , mainboxWidth  , mainboxHeight , 20 , 20  );
        g2.setColor(Color.white);
        g2.fillRoundRect(x + 20, y + 60 , mainboxWidth - 10  , mainboxHeight - 10 , 20 , 20  );

        BufferedImage img = DB_pokemon.imgpokemon(token);
        if (img != null) {
            g2.drawImage(img, x + 30, y + 70, mainboxWidth - 30, mainboxHeight - 30, null);
        }

        g2.setColor(Color.black);
        g2.setFont(arial_20);
        uiCreateroom.drawCenteredString(g2 ,DB_pokemon.GetnameUser(token) , x + 15 , y + 130 ,mainboxWidth , mainboxHeight , arial_20);
    }


    public  void drawBoxPokemon(Graphics2D g2 , String text , int x , int y  , BufferedImage img , boolean openyellow){
        int width = 120 ;
        int height = 120;

        if (Gameconfig_Pokemon.User_Select_Pokemon != null && Gameconfig_Pokemon.User_Select_Pokemon.equalsIgnoreCase(text) && openyellow ){
            g2.setColor(Color.BLUE);
            g2.fillRoundRect(x + 15, y + 55 , width  , height , 20 , 20  );
        }
        String pokemon_user = DB_pokemon.GetPokemon(HardwareID.getHWID());
        if (pokemon_user != null && pokemon_user != "" && pokemon_user.equalsIgnoreCase(text) && openyellow){
            g2.setColor(Color.yellow);
        }else {
            g2.setColor(Color.white);
        }
        g2.fillRoundRect(x + 20, y + 60 , width - 10  , height - 10 , 20 , 20  );


        g2.fillRoundRect(x + 20, y + 190 , width - 10  , height - 90 , 20 , 20  );

        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2 , text , x+20 , y + 190 , width - 10 , height - 90 ,arial_20);

        if (img != null) {
            g2.drawImage(img, x + 30, y + 70, width - 30, height - 30, null);
        }
    }

    public void UIwaitingroomclick(int x , int  y){
        int mainboxWidth = 500;
        int mainboxHeight = gp.getHeight() - 100;
        int mainboxX = gp.getWidth() - 650;
        int mainboxY = 50 ;

        String room = DB_dbmanager.chackplayerthisroom(GameConfig.JoinroomID);
        int buttonLevenRoomY = 600;
        int buttonSelectY = 520;
        if (room.equalsIgnoreCase("player_1")){
            buttonLevenRoomY = 630;
            buttonSelectY = 570;

            if (MouseOver.mouseOver(x , y , mainboxX + 50 , mainboxY + 500 , mainboxWidth , 50)){
                DB_dbmanager.UpdateGame_Status(GameConfig.Game_status_runing);
                DB_getData.UpdateUserGameStart(DB_getData.getroom());
                GameConfig.gameState = GameConfig.GameRoom;
            }
        }

        if (MouseOver.mouseOver(x , y ,mainboxX + 50 , mainboxY + buttonLevenRoomY , mainboxWidth , 50 )){
            String token = HardwareID.getHWID();

            if (room.equalsIgnoreCase("player_1")){
                DB_dbmanager.removeRooms(token);
            }else{
                DB_dbmanager.removePlayerFromRoom(token , room);
            }

            DB_dbmanager.update_current_players(room);
            GameConfig.gameState = GameConfig.titleState;
        }

        if (MouseOver.mouseOver(x , y ,mainboxX + 50 , mainboxY + buttonSelectY , mainboxWidth , 50)){
            DB_pokemon.Selectpokemon(Gameconfig_Pokemon.User_Select_Pokemon , HardwareID.getHWID());
            System.out.println("SELECT : "+ Gameconfig_Pokemon.User_Select_Pokemon);
        }


        int selectpokem_zoon_mainX = mainboxX - 750;
        int selectpokem_zoon_mainboxWidth = mainboxWidth + 200;

        if (MouseOver.mouseOver(x , y , selectpokem_zoon_mainX, mainboxY + 90,  120 , 120)){
            Gameconfig_Pokemon.User_Select_Pokemon = Gameconfig_Pokemon.pikachu;
        }

        if (MouseOver.mouseOver(x , y , selectpokem_zoon_mainX + 150, mainboxY + 90 ,  120 , 120)){
            Gameconfig_Pokemon.User_Select_Pokemon = Gameconfig_Pokemon.snorlax;
        }

    }
}
