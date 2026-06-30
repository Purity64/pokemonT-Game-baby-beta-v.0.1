import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static java.lang.String.valueOf;

public class UIGameStart {
    GamePanel gp;
    UiCreateroom uiCreateroom;
    Font arial_30, arial_20, arial_14B;
    ArrayList<String> arr = new ArrayList<>();
    int i = 1;
    int i_BoxRenderevent = 1;
    int i_AnimeName = 1;
    int i_PokemonShow = 1;
    String[] KeyLocationPokemon_Show = Gameconfig_Pokemon.Keymap_PokemonShowAnime_Hsm;
    Timer diceTimer , PokemonShow , AnimeTime;



    public UIGameStart(GamePanel gp) {
        this.gp = gp;
        this.uiCreateroom = new UiCreateroom(gp);

        arial_30 = new Font("Arial", Font.BOLD, 30);
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_14B = new Font("Arial", Font.BOLD, 14);




        diceTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (i <= Gameconfig_Pokemon.RenderdiceArrayList.size()) {
                    Gameconfig_Pokemon.jk = Gameconfig_Pokemon.RenderdiceArrayList.get(i-1);
                    i++;
                    gp.repaint();
                }else {
                    ((Timer)e.getSource()).stop();

                    // 1. Timer สำหรับซ่อนลูกเต๋า (ทำงานครั้งเดียวหลังผ่านไป 0.5 วิ)
                    Timer hideTimer = new Timer(500, e2 -> {
                        Gameconfig_Pokemon.jk = null;
                        gp.repaint();
                        ((Timer)e2.getSource()).stop();
                    });
                    hideTimer.setRepeats(false);
                    hideTimer.start();

                    // 2. Timer สำหรับเล่น Animation ของ Box Event
                    if (Gameconfig_Pokemon.Is_Dice_TrensPokemon == false){
                        Timer showBoxEvent = new Timer(50, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e3) {
                                if (i_BoxRenderevent <= Gameconfig_Event.RenderingBoxeventArray.size()) {
                                    Gameconfig_Event.RenderimgBoxevent = Gameconfig_Event.RenderingBoxeventArray.get(i_BoxRenderevent - 1);
                                    i_BoxRenderevent++;
                                    gp.repaint();
                                } else {
                                    // เล่นจบแล้ว รอ 2 วิแล้วซ่อนกล่อง (Option เสริม)
                                    ((Timer)e3.getSource()).stop();
                                    new Timer(2000, e4 -> {
                                        Gameconfig_Event.RenderimgBoxevent = null;
                                        i_BoxRenderevent = 1; // Reset ค่าไว้ใช้ครั้งหน้า
                                        gp.repaint();
                                        ((Timer)e4.getSource()).stop();

                                    }).start();

                                    if (Gameconfig_Event.IsEvent) {
                                        Timer delayTimer = new Timer(3000, e5 -> {
                                            Event();
                                            Gameconfig_Event.IsEvent = false;
                                            ((Timer)e5.getSource()).stop();
                                        });
                                        delayTimer.setRepeats(false);
                                        delayTimer.start();
                                    }
                                }
                            }
                        });
                        showBoxEvent.start();
                    }else {
                        if (Gameconfig_Pokemon.Is_Update_Dice_TrensPokemon == true){
                            String pokename = DB_getData.getlast_Show_Pokemon_Name(DB_getData.getroom());
                            DB_pokemon.AddPokemonTouser(pokename);
                            DB_pokemon.Updateis_Trens(false);
                        }
                    }


                }
            }
        });

        AnimeTime = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( i_AnimeName <= 200){
                    i_AnimeName = i_AnimeName + 2;
                    GameConfig.Anime_NamePlayshow = (i_AnimeName / 100.0) * gp.getWidth();

                    gp.repaint();
                }else {
                    GameConfig.Anime_NamePlayshow = gp.getWidth() * 2;
                    ((Timer)e.getSource()).stop();

                }
            }
        });

        PokemonShow = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Gameconfig_Pokemon.KeyLocation_PokemonShowAnime_Hsm >= 0 && Gameconfig_Pokemon.KeyLocation_PokemonShowAnime_name != null && !Gameconfig_Pokemon.KeyLocation_PokemonShowAnime_name.isEmpty() && i_PokemonShow <= 40){
                    Gameconfig_Pokemon.PokemonShowAnime_Render = Gameconfig_Pokemon.PokemonShowAnime_Hsm.get(Gameconfig_Pokemon.KeyLocation_PokemonShowAnime_name).get(KeyLocationPokemon_Show[Gameconfig_Pokemon.KeyLocation_PokemonShowAnime_Hsm]).get(i_PokemonShow - 1);
                    i_PokemonShow++;
                }else {
                    ((Timer)e.getSource()).stop();

                }
            }
        });
    }

    public void drawUIGameStart(Graphics2D g2){
        String Hwid = HardwareID.getHWID();
        String room = DB_getData.getroom();
        GameConfig.Player = DB_getData.getRoomUser(room);


        if (Hwid != null && Hwid.equalsIgnoreCase(GameConfig.Player[0])){
            GameConfig.PlayerSlot = "player_1";
        }else if (Hwid != null && Hwid.equalsIgnoreCase(GameConfig.Player[1])){
            GameConfig.PlayerSlot = "player_2";
        } else if (Hwid != null && Hwid.equalsIgnoreCase(GameConfig.Player[2])) {
            GameConfig.PlayerSlot = "player_3";
        } else if (Hwid != null && Hwid.equalsIgnoreCase(GameConfig.Player[3])) {
            GameConfig.PlayerSlot = "player_4";
        }

        arr = DB_getData.getUserCrad();
        String tren = DB_getData.getTren();


        DB_dbmanager.Auto_Update_gameState();
        g2.setColor(new Color(34, 139, 34));
        g2.drawImage(Gameconfig_Pokemon.BG_menu_GaneRuning , 0 , 0,gp.getWidth(), gp.getHeight() , null);


        int mainboxHeight = gp.getHeight();
        int mainboxX = gp.getWidth() ;


        int screenW = gp.getWidth();
        int screenH = gp.getHeight();

        // 1. หา Ratio เทียบกับความละเอียดพื้นฐาน (1920x1080)
        double scaleX = screenW / 1920.0;
        double scaleY = screenH / 1080.0;

        int dynamicStartX = (int)(1160 * scaleX);
        int dynamicStartY = (int)(340 * scaleY);

        if (GameConfig.Player[0] != null && !GameConfig.Player[0].isEmpty() ){
            drawUIPlayerBox(g2, GameConfig.Player[0], 10, 10, 250, 130, false, Color.blue);

            int matrix[] = DB_getData.getXY(GameConfig.Player[0]);
            int xy[] = Util_matrixfuntion.MapLocation(matrix[0], matrix[1]);
            g2.setColor(Color.blue);

            // 3. ปรับค่า mx, my จากแมพให้เป็นค่าตามสเกลหน้าจอจริง
            int scaledMX = (int)(xy[0] * scaleX);
            int scaledMY = (int)(xy[1] * scaleY);

            // 4. คำนวณตำแหน่งสุดท้าย (Final Position)
            int finalX = screenW - (dynamicStartX + scaledMX);
            int finalY = screenH - (dynamicStartY + scaledMY);

            drawplayer(g2, finalX, finalY);
        }

        if (GameConfig.Player[1] != null && !GameConfig.Player[1].isEmpty()  ){
            drawUIPlayerBox(g2, GameConfig.Player[1],gp.getWidth() - 260, 10, 250, 130, false , Color.red);

            int matrix[] = DB_getData.getXY(GameConfig.Player[1]);
            int xy[] = Util_matrixfuntion.MapLocation(matrix[0], matrix[1]);
            g2.setColor(Color.red);

            // 3. ปรับค่า mx, my จากแมพให้เป็นค่าตามสเกลหน้าจอจริง
            int scaledMX = (int)(xy[0] * scaleX);
            int scaledMY = (int)(xy[1] * scaleY);

            // 4. คำนวณตำแหน่งสุดท้าย (Final Position)
            int finalX = screenW - (dynamicStartX + scaledMX);
            int finalY = screenH - (dynamicStartY + scaledMY);

            drawplayer(g2, finalX, finalY);
        }

        if ( GameConfig.Player[2] != null && !GameConfig.Player[2].isEmpty() ){
            drawUIPlayerBox(g2, GameConfig.Player[2], 10,mainboxHeight - 150 , 250, 130, true , Color.yellow);

            int matrix[] = DB_getData.getXY(GameConfig.Player[2]);
            int xy[] = Util_matrixfuntion.MapLocation(matrix[0], matrix[1]);
            g2.setColor(Color.yellow);

            // 3. ปรับค่า mx, my จากแมพให้เป็นค่าตามสเกลหน้าจอจริง
            int scaledMX = (int)(xy[0] * scaleX);
            int scaledMY = (int)(xy[1] * scaleY);

            // 4. คำนวณตำแหน่งสุดท้าย (Final Position)
            int finalX = screenW - (dynamicStartX + scaledMX);
            int finalY = screenH - (dynamicStartY + scaledMY);

            drawplayer(g2, finalX, finalY);
        }

        if (GameConfig.Player[3] != null && !GameConfig.Player[3].isEmpty() ){
            drawUIPlayerBox(g2,  GameConfig.Player[3],gp.getWidth() - 260, mainboxHeight - 150, 250, 130, true , Color.green);

            int matrix[] = DB_getData.getXY(GameConfig.Player[3]);
            int xy[] = Util_matrixfuntion.MapLocation(matrix[0], matrix[1]);
            g2.setColor(Color.green);

            // 3. ปรับค่า mx, my จากแมพให้เป็นค่าตามสเกลหน้าจอจริง
            int scaledMX = (int)(xy[0] * scaleX);
            int scaledMY = (int)(xy[1] * scaleY);

            // 4. คำนวณตำแหน่งสุดท้าย (Final Position)
            int finalX = screenW - (dynamicStartX + scaledMX);
            int finalY = screenH - (dynamicStartY + scaledMY);

            drawplayer(g2, finalX, finalY);
        }



        if(GameConfig.PlayerSlot.equalsIgnoreCase(tren)){
            int tren_Break_count = DB_getData.get_Break(Hwid);
            if (tren_Break_count <= 0){
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(gp.getWidth() - 150 , mainboxHeight - 400  , 100,50,10,10);

                g2.setColor(Color.black);
                uiCreateroom.drawCenteredString(g2,	valueOf(Gameconfig_Pokemon.toy) , gp.getWidth() - 150 , mainboxHeight - 400 , 100 , 50 , GameConfig.KertazFont);
            }else {
                DB_dbmanager.Update_Nextren();
                DB_pokemon.Update_break(Hwid , -1);
            }

        }



        g2.setColor(Color.WHITE);
        g2.drawImage(Gameconfig_Pokemon.jk, (screenW / 2 ) - 270, (screenH / 2 ) - 370, 500, 500, null);
        g2.drawImage(Gameconfig_Event.RenderimgBoxevent, (screenW / 2 ) - 330, (screenH / 2 ) - 290, 500 , 500 , null);







        int newtoy = DB_getData.getLast_dice_val(room);
        long lesttoy = DB_getData.getDice_trigger(room);

        if (lesttoy != Gameconfig_Pokemon.LaseDice_trigger && newtoy != 0){
            RenderToyImg(newtoy);
            Gameconfig_Pokemon.LaseDice_trigger = lesttoy;


        }

        //animetionShowname

        if (AnimeTime.isRunning()){
            String PlayertrenToken = GameConfig.Player[Integer.parseInt(String.valueOf(tren.charAt(tren.length() - 1))) - 1];
            String NamepayerTren = DB_pokemon.GetnameUser(PlayertrenToken);
            g2.setColor(Color.WHITE);
            g2.fillRect(screenW  - (int)GameConfig.Anime_NamePlayshow , (screenH / 2)- 100 , screenW , 200 );
            g2.setColor(Color.BLUE);
            g2.fillRect(screenW  - (int)GameConfig.Anime_NamePlayshow , (screenH / 2)- 95 , screenW  , 190);
            g2.setColor(Color.white);
            uiCreateroom.drawCenteredString(g2,NamepayerTren,screenW  - (int)GameConfig.Anime_NamePlayshow ,(screenH / 2)- 95 , screenW  , 190 , FontConfig.KertazFont);
        }

        if (!tren.equalsIgnoreCase(GameConfig.Lest_Tren)){

            RenderAnimeShowNameNextTren();
        }
        GameConfig.Lest_Tren = tren;

        //pokemoonshow
        long last_Show_Pokemon_Time = DB_getData.getlast_Show_Pokemon_Time(room);
        Gameconfig_Pokemon.isShow_pokemon = DB_getData.getlast_Show_Pokemon_Ishow(room);
        if (last_Show_Pokemon_Time != Gameconfig_Pokemon.last_Show_Pokemon_Time){
            String pokename = DB_getData.getlast_Show_Pokemon_Name(room);
            Gameconfig_Pokemon.KeyLocation_PokemonShowAnime_Hsm = DB_getData.getlast_Show_Pokemon_Pattern(room);
            int evo = DB_getData.getlast_Show_Pokemon_Evo(room);
            if (pokename != null && !pokename.isEmpty() && evo > 0 && evo <= 3){
                RenderAnimeShowPokemon(pokename , evo);
            }
        }
        Gameconfig_Pokemon.last_Show_Pokemon_Time = last_Show_Pokemon_Time ;
        if ( Gameconfig_Pokemon.isShow_pokemon){
            String pokename = DB_getData.getlast_Show_Pokemon_Name(room);
            g2.drawImage(Gameconfig_Pokemon.PokemonShowAnime_Render, (screenW / 2 ) - 330, (screenH / 2 ) - 370, 500, 500, null);

            if (tren.equalsIgnoreCase(GameConfig.PlayerSlot)){
                drawUIPokemonshow(g2 , screenW ,screenH , pokename);
            }
        }





        if(Gameconfig_Pokemon.isShowCardSkill){
            int cradCount = arr.size();
            int deflutCardX = (gp.getWidth() / 2 ) + 100 ;
            if (cradCount == 3){
                deflutCardX = deflutCardX + 300;
            } else if (cradCount == 2) {
                deflutCardX = deflutCardX + 200;
            }

            for (int i = 0 ; i < cradCount ; i++){
                drawUICradSkill(g2 ,arr.get(i) ,gp.getWidth()- (deflutCardX - (300 * i) ), mainboxHeight - 150  );
            }
        }

        if (Gameconfig_Pokemon.iscardopen ){
            g2.setColor(Color.white);
            UIusercard(g2,Gameconfig_Pokemon.Cardtoken ,(gp.getWidth() / 2 ) - 150 , mainboxHeight - 700 , 250 , 400 , tren);
        }

        if(Gameconfig_Pokemon.IsShopSell){
            drewUISellPokemon(g2,screenW,screenH);
        }

        if (Gameconfig_Event.isShopOpen){
            drewUIShop(g2 , screenW , screenH);
        }

        if (Gameconfig_Event.isPokemoncenterOpen){
            drewUIPokemonCenter(g2 , screenW , screenH , Hwid);
        }



        if (Gameconfig_Event.isPokemoncenterOpen || Gameconfig_Pokemon.isShow_pokemon){
            Gameconfig_Pokemon.isShowCardSkill = false;
        }else {
            Gameconfig_Pokemon.isShowCardSkill = true;
        }


        if (GameConfig.showMessageModal){

            GameConfig.modalTitle = "Pokeball หมด";
            GameConfig.modalMessage = "ต้องมี Pokeball อย่างน้อย 1 ลูก";
            UI_AlertSystem.drawMessageDialog(g2 , gp , GameConfig.modalTitle , GameConfig.modalMessage );
        }

    }

    public void drawUIPlayerBox(Graphics2D g2, String token  ,int x, int y, int width, int height, boolean isTop , Color color) {
        // 1. วาดกล่องหลัก (Background)
        g2.setColor(color);
        g2.fillRoundRect(x, y, width, height, 20, 20);

        // 2. วาดช่องรูปโปรไฟล์และแถบชื่อ
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x + 10, y + 10, 50, 50, 20, 20);

        String pokemonBox[] = DB_pokemon.SelectPokemonByToken(DB_pokemon.GetPokemon(token));
        g2.drawImage(DB_pokemon.getPokeimgByName(pokemonBox[0]),x + 13 , y + 13 ,45 , 45 ,null);


        g2.fillRoundRect(x + 80, y + 10, 160, 35, 20, 20); // Name Bar
        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2 , DB_pokemon.GetnameUser(token) , x + 80 , y + 10 , 140 , 35 , arial_20);

        int itemY;
        if (isTop){
            itemY = y - 60;
        }else {
            itemY = y + 140;
        }
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x  , itemY , width , 50 , 20 , 20);
        g2.drawImage(Gameconfig_Pokemon.pokeball_img , x + 10 ,itemY + 10  , 30  , 30 , null);
        g2.setColor(Color.black);
        g2.setFont(arial_30);
        int componetBox[] = DB_pokemon.GetAllComponet(token);
        g2.drawString(valueOf(componetBox[0]), x + 45 , itemY + 35  );

        g2.drawImage(Gameconfig_Pokemon.coins_img , x + 90 ,itemY + 10  , 30  , 30 , null);
        g2.drawString(	valueOf(componetBox[1]), x + 125 , itemY + 35  );

        g2.drawImage(Gameconfig_Pokemon.poke_card_img , x + 180 ,itemY + 10  , 30  , 30 , null);
        g2.drawString(valueOf(componetBox[2]), x + 210 , itemY + 35  );
        // 4. วาดช่องสกิลด้านล่าง
        g2.setColor(Color.WHITE);
        String pokemon[] = DB_pokemon.GetAllPokemon(token);
        for (int i = 0; i < 4; i++) {
            String pokemonname[] = DB_pokemon.SelectPokemonByToken(pokemon[i]);
            BufferedImage pokeimg = DB_pokemon.getPokeimgByName(pokemonname[0]);
            g2.fillRoundRect(x + 10 + (i * 60), y + 70, 50, 50, 20, 20);
            if(pokeimg == null ){
                pokeimg = Gameconfig_Pokemon.pokeball_Nunull_img;
            }
            g2.drawImage(pokeimg,x + 13  + (i * 60) , y + 73 ,45 , 45 ,null);


        }
    }

    public void drawUICradSkill(Graphics2D g2 ,String token, int x , int y ){
        int overY = y ;
        String detail[] = DB_getData.getCradSkillDetail(token);
        if (gp.mouseX >= x && gp.mouseX <= x + 250 && gp.mouseY >= y && gp.mouseY <= y + 400 && Gameconfig_Pokemon.iscardopen == false && Gameconfig_Pokemon.isShowCardSkill){
            gp.setCursor(new Cursor(Cursor.HAND_CURSOR));
            overY = overY - 100;
        }else{
            gp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }
        g2.setColor(Color.white);
        g2.fillRoundRect(x , overY , 250 , 400 ,40 , 40);
        g2.drawImage(Gameconfig_Pokemon.BG_crad_skill_img ,x,overY , 250 , 400 , null);


        g2.setColor(Color.white);
        uiCreateroom.drawCenteredString(g2,detail[0],x + 25 , overY + 55, 200 , 40 , GameConfig.KertazFont);

        BufferedImage img = DB_pokemon.getCardByName(detail[1]);
        g2.drawImage(img,x+35 , overY + 130 , 175 , 125 , null);

        g2.setFont(GameConfig.thaiFont);
        g2.setColor(Color.black);
        Util_drawtext.drawWrappedText(g2,detail[2] , x + 35 , overY + 270 , 175);
    }
    public void UIGameStartClick(int x , int y){
        String tren = DB_getData.getTren();
        int mainboxHeight = gp.getHeight();
        int cradCount = arr.size();
        int deflutCardX = (gp.getWidth() / 2 ) + 100 ;
        if (cradCount == 3){
            deflutCardX = deflutCardX + 300;
        } else if (cradCount == 2) {
            deflutCardX = deflutCardX + 200;
        }

        for (int i = 0 ; i < cradCount ; i++){

            if (MouseOver.mouseOver(x , y ,gp.getWidth()- (deflutCardX - (300 * i) ) ,mainboxHeight - 150,250 , 400 ) && Gameconfig_Pokemon.iscardopen == false  && Gameconfig_Pokemon.isShowCardSkill){
                Gameconfig_Pokemon.iscardopen = true;
                Gameconfig_Pokemon.Cardtoken = arr.get(i) ;
            }
        }

        if (Gameconfig_Pokemon.iscardopen){
            int cradopenX = (gp.getWidth() / 2 ) - 150;
            int overY = mainboxHeight - 700;
            if (MouseOver.mouseOver(x , y ,cradopenX ,overY + 420 , 100 , 50 ) && GameConfig.PlayerSlot.equalsIgnoreCase(tren) ){
                //ok
                DB_pokemon.usedCardSkill(Gameconfig_Pokemon.Cardtoken);
                DB_pokemon.setCountCardSkill();
                Gameconfig_Pokemon.iscardopen = false;
                Gameconfig_Pokemon.Cardtoken = "";
            }

            if (MouseOver.mouseOver(x , y , cradopenX + 150 , overY + 420 , 100 , 50)){
                //close
                Gameconfig_Pokemon.iscardopen = false;
            }
        }

        if(MouseOver.mouseOver(x , y , gp.getWidth() - 150 ,mainboxHeight - 400 , 100 , 50 ) && GameConfig.PlayerSlot.equalsIgnoreCase(tren)){
            Toy();
        }

        //pokemon showui
        if(Gameconfig_Pokemon.isShow_pokemon){
            int mainX = (gp.getWidth() / 2 ) - 250;
            int mainY = gp.getHeight() - 250;
            if(MouseOver.mouseOver(x , y ,mainX + 510 , mainY  , 50 , 50  )){
                    TrensPokemon();
            }
            if(MouseOver.mouseOver(x, y ,mainX + 510 , mainY + 60 , 50 , 50 )){
                DB_pokemon.ResetPokemonShow();
                DB_dbmanager.Update_Nextren();
            }
        }

        //shopSell open

        if (Gameconfig_Pokemon.IsShopSell){
            if (MouseOver.mouseOver(x, y ,(gp.getWidth()  / 2 ) + 180 , (gp.getHeight()  / 2 )  + 182 , 100 , 50)){
                Gameconfig_Pokemon.IsShopSell = false;
                DB_dbmanager.Update_Nextren();
            }
            if (MouseOver.mouseOver(x,y,(gp.getWidth()  / 2 ) + 70 , (gp.getHeight()  / 2 )  + 182 , 100 , 50)){
                DB_pokemon.SellPokemon();
                for (int i = 0; i < Gameconfig_Pokemon.IsShopSellArr.length; i++) {
                    Gameconfig_Pokemon.IsShopSellArr[i] = 1;
                }
                Gameconfig_Pokemon.IsShopSell = false;
                DB_pokemon.ResetPokemonDB(HardwareID.getHWID());
                DB_dbmanager.Update_Nextren();
            }

            int startX = -270;
            int gap = 150;
            for (int i = 0 ; i < Gameconfig_Pokemon.IsShopSellArr.length ; i++){
                int currentX = (gp.getWidth()  / 2) + startX + (i * gap);
                int currentY = (gp.getHeight() / 2);
                int yShopSell = 0;
                if (i > 2 ){
                    yShopSell = 200;
                    currentX = (gp.getWidth()  / 2) + startX + ((i - 3) * gap);
                }
                if (MouseOver.mouseOver(x,y,currentX, currentY +( -220 + yShopSell ), 120, 120) && Gameconfig_Pokemon.IsShopSellArr[i] > 0){

                    if (Gameconfig_Pokemon.IsShopSellArr[i] == 2){
                        Gameconfig_Pokemon.IsShopSellArr[i] = 1;
                    }else {
                        Gameconfig_Pokemon.IsShopSellArr[i] = 2;
                    }
                }
            }

        }

        //shop
        if (Gameconfig_Event.isShopOpen){
            if (MouseOver.mouseOver(x , y , (gp.getWidth() / 2 ) - 150 , (gp.getHeight() / 2) + 90 , 70 , 50 )){
                int componetBox[] = DB_pokemon.GetAllComponet(HardwareID.getHWID());
                int money = componetBox[1];
                if (Gameconfig_Event.Shop_CountPokeball * Gameconfig_Event.pokeball_Price < money){
                    Gameconfig_Event.Shop_CountPokeball++;
                }

            }

            if (MouseOver.mouseOver(x , y , (gp.getWidth() / 2 ) + 90 , (gp.getHeight() / 2) + 90 , 70 , 50 )){
                Gameconfig_Event.Shop_CountPokeball--;
                if (Gameconfig_Event.Shop_CountPokeball  < 1){
                    Gameconfig_Event.Shop_CountPokeball = 1;
                }
            }

            if (MouseOver.mouseOver(x , y , (gp.getWidth() / 2 ) + 80, (gp.getHeight() / 2) + 180 , 80 , 50 )){
                if (Gameconfig_Event.Shop_CountPokeball > 0){
                    boolean statuss = DB_pokemon.Buypokeball(HardwareID.getHWID());
                    if (statuss){
                        Gameconfig_Event.isShopOpen = false;
                        Gameconfig_Event.Shop_CountPokeball = 0;
                        DB_dbmanager.Update_Nextren();
                    }
                }
            }

            if (MouseOver.mouseOver(x , y , (gp.getWidth() / 2 ) + 180 , (gp.getHeight()/ 2) + 180 , 80 , 50 )){
                Gameconfig_Event.Shop_CountPokeball = 0;
                Gameconfig_Event.isShopOpen = false;
                DB_dbmanager.Update_Nextren();
            }
        }

        if (Gameconfig_Event.isPokemoncenterOpen){
            int centerX = gp.getWidth() / 2;
            int centerY = gp.getHeight() / 2;

            String hwid = HardwareID.getHWID();
            ArrayList<String> pokemon = DB_pokemon.getAllPokemonInUser(hwid);
            for (int i = 0; i < pokemon.size(); i++) {
                int offsetX = 0;
                int offsetY = 0;

                switch (i) {
                    case 0: offsetX = -200; offsetY = -210; break;
                    case 1: offsetX = -40;  offsetY = -210; break;
                    case 2: offsetX = 120;  offsetY = -210; break;
                    case 3: offsetX = -260; offsetY = -50;  break;
                    case 4: offsetX = -100; offsetY = -50;  break;
                }


                if (MouseOver.mouseOver(x , y ,centerX + offsetX, centerY + offsetY, 130, 150)){
                    Gameconfig_Event.isSelect_Pokemoncenter = pokemon.get(i);
                }
            }


            //ok
            if (MouseOver.mouseOver(x,y,centerX + 94,centerY - 13, 100 , 45 ) && Gameconfig_Event.isSelect_Pokemoncenter != null && !Gameconfig_Event.isSelect_Pokemoncenter.isEmpty()){
                DB_pokemon.Heal_Pokemon(Gameconfig_Event.isSelect_Pokemoncenter , 100);
                Gameconfig_Event.isPokemoncenterOpen = false;
                DB_dbmanager.Update_Nextren();
            }


            //cencel
            if (MouseOver.mouseOver(x,y,centerX + 94,centerY + 40, 100 , 45 )){
                Gameconfig_Event.isPokemoncenterOpen = false;
                DB_dbmanager.Update_Nextren();
            }

        }



    }
    public void UIusercard(Graphics2D g2 ,String token , int x, int y , int width , int  height , String tren ){
        int overY = y ;

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0,0,gp.getWidth() ,gp.getHeight());

        String detail[] = DB_getData.getCradSkillDetail(token);


        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x,y,width,height , 20 , 20);
        g2.drawImage(Gameconfig_Pokemon.BG_crad_skill_img , x , y , width , height , null);

        g2.setColor(Color.white);
        uiCreateroom.drawCenteredString(g2,detail[0],x + 25 , overY + 55, 200 , 40 , GameConfig.KertazFont);

        BufferedImage img = DB_pokemon.getCardByName(detail[1]);
        g2.drawImage(img,x+35 , overY + 130 , 175 , 125 , null);

        g2.setFont(GameConfig.thaiFont);
        g2.setColor(Color.black);
        Util_drawtext.drawWrappedText(g2,detail[2] , x + 35 , overY + 270 , 175);

        if (GameConfig.PlayerSlot.equalsIgnoreCase(tren)){
            g2.setColor(Color.GREEN);
            g2.fillRoundRect(x ,overY + 420 , 100 , 50 , 20 , 20);
            g2.setColor(Color.black);
            uiCreateroom.drawCenteredString(g2,"ใช้งาน",x , overY + 420 ,100 , 50 , FontConfig.thaiFont25);
        }


        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x + 150 ,overY + 420 , 100 , 50 , 20 , 20);

        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2,"ยกเลิก",x + 150, overY + 420 ,100 , 50 , FontConfig.thaiFont25);

    }

    public void drawplayer(Graphics2D g2 ,int x , int y){
        g2.fillRect(x,y,20,40);
    }

    public void Toy(){
        // สุ่มเลขผลลัพธ์
        Random rand = new Random();
        int dice = rand.nextInt(6) + 1;
        Gameconfig_Pokemon.Is_Dice_TrensPokemon = false;
        RenderToyImg(1);

        Gameconfig_Pokemon.toy = 1;
        Util_matrixfuntion.UpDatenewlocation(1);

        DB_dbmanager.Updatelasetoy(1);


        Gameconfig_Event.IsEvent = true;




    }

    public void RenderToyImg(int dice ){
        Gameconfig_Pokemon.RenderdiceArrayList.clear();
        Gameconfig_Pokemon.RenderdiceArrayList.addAll(Gameconfig_Pokemon.hashmapdice.get(dice));

        Gameconfig_Event.RenderingBoxeventArray.clear();

        String tren = DB_getData.getTren();
        String Player =  GameConfig.Player[Integer.parseInt(String.valueOf(tren.charAt(tren.length() - 1))) - 1];

        int PlayerLocation[] = DB_getData.getXY(Player);
        String boxEvent = Util_matrixfuntion.ChackLocationEvent(PlayerLocation[0] , PlayerLocation[1]);
        String BoxArr[] = boxEvent.split("_");
        String newBox = "box";
        for (String a : BoxArr){
            newBox = newBox.concat(a);
        }
        DB_pokemon.UpdateBoxEvent(DB_getData.getroom() , newBox);
        Gameconfig_Event.KeyRenderimgBoxevent = DB_getData.getBoxEvent(DB_getData.getroom());
        System.out.println(Gameconfig_Event.KeyRenderimgBoxevent);
        if (Gameconfig_Event.KeyRenderimgBoxevent != null && !Gameconfig_Event.KeyRenderimgBoxevent.isEmpty()){
            Gameconfig_Event.RenderingBoxeventArray.addAll(Gameconfig_Event.RenderimgBoxeventHashMap.get(Gameconfig_Event.KeyRenderimgBoxevent));

        }

        i = 1;
        if(diceTimer.isRunning()) {
            diceTimer.stop();
        }
        diceTimer.start();


    }

    public  void RenderAnimeShowNameNextTren(){
        i_AnimeName = 1;
        if (AnimeTime.isRunning()){
            AnimeTime.stop();
        }
        AnimeTime.start();
    }

    public  void RenderAnimeShowPokemon(String Pokemonname , int evo){
        Gameconfig_Pokemon.KeyLocation_PokemonShowAnime_name = Pokemonname;
        i_PokemonShow = 1;
        if (PokemonShow.isRunning()){
            PokemonShow.stop();
        }
        PokemonShow.start();
    }

    public void drawUIPokemonshow(Graphics2D g2 , int screenW , int screenH ,String pokemonname){
        Gameconfig_Pokemon.Dice_TrensPokemon_name = pokemonname;
        int mainX = (screenW / 2 ) - 250;
        int mainY = screenH - 250;

        HashMap<String , String> hsm = Gameconfig_Pokemon.PokemonSkill_Hsm.get(pokemonname);
        g2.setColor(Color.BLUE);
        g2.fillRoundRect(mainX - 5, mainY - 5, 510 , 210 , 20 , 20);
        g2.setColor(Color.white);
        g2.fillRoundRect(mainX, mainY, 500 , 200 , 20 , 20);
        g2.setColor(Color.BLUE);
        g2.fillRoundRect(mainX + 10 , mainY + 10 , 180 , 180 , 10 ,10 );
        BufferedImage img = Util_PokemonFuntion.getPokemonImg(pokemonname, 2);
        if (img != null){
            g2.drawImage(img , mainX - 150 , mainY - 150 , 500 , 500 , null);
        }

        //toy
        g2.setColor(Color.BLUE);
        g2.fillRoundRect((mainX + 200 )  , mainY + 10 , 50 , 50 , 10 , 10 );
        g2.setColor(Color.white);
        uiCreateroom.drawCenteredString(g2,hsm.get("toy") ,(mainX + 200 ) , mainY + 10 , 50 , 50 , FontConfig.KertazFont30 );


        //name
        g2.setColor(Color.BLUE);
        g2.fillRoundRect( mainX + 260  , mainY + 10 , 220 , 50 , 10 , 10);
        g2.setColor(Color.white);
        uiCreateroom.drawCenteredString(g2,pokemonname,(mainX + 260 ) , mainY + 10 , 220 , 50 , FontConfig.KertazFont40 );

        //type
        g2.setColor(Color.BLUE);
        g2.fillRoundRect((mainX + 420 )  , mainY + 80 , 60 , 50 , 10 , 10 );
        g2.drawImage(Gameconfig_IMG.shield_icon ,(mainX + 430 )  , mainY + 85  , 40,40 , null );

        g2.fillRoundRect((mainX + 420 )  , mainY + 140 , 60 , 50 , 10 , 10 );
        g2.drawImage(Gameconfig_IMG.shield_icon ,(mainX + 430 )  , mainY + 145 , 40,40  , null );

        //===
        g2.setColor(Color.BLUE);
        g2.fillRoundRect((mainX + 200 )  , mainY + 80 , 60 , 110 , 10 , 10 );
        g2.setColor(Color.white);
        g2.fillRoundRect((mainX + 205 )  , mainY + 85 , 50 , 50 , 10 , 10 );
        g2.drawImage(Gameconfig_Pokemon.coins_img , (mainX + 205 )  , mainY + 85 , 50 , 50  , null);
        uiCreateroom.drawCenteredString(g2,hsm.get("price") ,(mainX + 205 ) , mainY + 135 , 50 , 50 , FontConfig.KertazFont30 );

        g2.setColor(Color.BLUE);
        g2.fillRoundRect((mainX + 270 )  , mainY + 80 , 60 , 110 , 10 , 10 );
        g2.setColor(Color.white);
        g2.fillRoundRect((mainX + 275 )  , mainY + 85 , 50 , 50 , 10 , 10 );
        g2.drawImage(Gameconfig_IMG.sword_icon ,(mainX + 275 )  , mainY + 85   , null);
        uiCreateroom.drawCenteredString(g2,hsm.get("sword") ,(mainX + 275 ) , mainY + 135 , 50 , 50 , FontConfig.KertazFont30 );


        g2.setColor(Color.BLUE);
        g2.fillRoundRect((mainX + 340 )  , mainY + 80 , 60 , 110 , 10 , 10 );
        g2.setColor(Color.white);
        g2.fillRoundRect((mainX + 345 )  , mainY + 85 , 50 , 50 , 10 , 10 );
        g2.drawImage(Gameconfig_IMG.shield_icon ,(mainX + 345 )  , mainY + 85  , null );
        uiCreateroom.drawCenteredString(g2,hsm.get("hp") ,(mainX + 345 ) , mainY + 135 , 50 , 50 , FontConfig.KertazFont30 );


        //action
        g2.setColor(Color.white);
        g2.fillRoundRect(mainX + 510 , mainY  , 50 , 50 , 20 ,20);
        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2,String.valueOf(Gameconfig_Pokemon.Dice_TrensPokemon),mainX + 510 , mainY  , 50 , 50 , FontConfig.KertazFont30);

        g2.setColor(Color.white);
        g2.fillRoundRect(mainX + 510 , mainY + 60 , 50 , 50 , 20 ,20);
    }

    public void Event(){
        int xy[] = DB_getData.getXY(HardwareID.getHWID());
        String keyboxlocation = Util_matrixfuntion.ChackLocationEvent(xy[0] , xy[1]);
        String keyboxlocationArr[] = keyboxlocation.split("_");
        String box = keyboxlocationArr[0];
        if (box.equalsIgnoreCase("Nomal")){
            Gameconfig_Pokemon.getLast_Show_Pokemon_Pattern = 2;
            DB_pokemon.UpdataLastPokemon(Util_PokemonFuntion.RendomPokemon(Gameconfig_Pokemon.last_Show_Pokemon_evo) , Gameconfig_Pokemon.last_Show_Pokemon_evo);

        } else if (box.equalsIgnoreCase("Egg")) {
            Gameconfig_Pokemon.getLast_Show_Pokemon_Pattern = 2;
            DB_pokemon.UpdataLastPokemon("egg" , 1);
        } else if (box.equalsIgnoreCase("Start")) {
            Gameconfig_Pokemon.IsShopSell = true;
            DB_pokemon.Hatchingeggs();
        } else if (box.equalsIgnoreCase("Cave")) {
            DB_dbmanager.Update_Nextren();
            DB_pokemon.Update_break(HardwareID.getHWID() , 1);

        } else if (box.equalsIgnoreCase("City")) {
            Gameconfig_Event.isShopOpen = true;
        } else if (box.equalsIgnoreCase("PokemonCenter")) {
            Gameconfig_Event.isPokemoncenterOpen = true;
        } else {
            DB_dbmanager.Update_Nextren();
        }
    }

    public  void drewUISellPokemon(Graphics2D g2 ,int mainW , int mainH ){
        g2.drawImage(Gameconfig_IMG.BG_Sell_Pokemon , (mainW / 2 ) - 300 , (mainH / 2 ) - 250 , 600 , 500 , null);

        ArrayList<String> arr = DB_pokemon.getAllPokemonInUser(HardwareID.getHWID());
        ArrayList<String> arr2 = new ArrayList<String>();
        ArrayList<String> arr3 = new ArrayList<String>();

        ArrayList<String> arrSell = new ArrayList<>();
        int money = 0;
        int o = 0;
        String pokemonarr[] = new String[3];
        for(int i = 0 ; i < arr.size() ; i++){
            String name = arr.get(i);
            pokemonarr = DB_pokemon.SelectPokemonByToken(name);
            if (pokemonarr[0] != null && !pokemonarr[0].equalsIgnoreCase("egg")){
                arr2.add(pokemonarr[0]);
                arr3.add(arr.get(i));
                if (Gameconfig_Pokemon.IsShopSellArr[o] !=2 ){
                    Gameconfig_Pokemon.IsShopSellArr[o] = 1;

                }
                o++;
            }
        }


        int startX = -270; // จุดเริ่มต้นเทียบจากกึ่งกลางจอ
        int gap = 150;     // ระยะห่างระหว่างกล่องแต่ละใบ (กว้าง 120 + ระยะห่าง 30)

        for (int i = 0; i < arr2.size(); i++) {
            // คำนวณตำแหน่ง X ของแต่ละกล่องในรอบนั้นๆ
            int currentX = (mainW / 2) + startX + (i * gap);
            int currentY = (mainH / 2);
            int y = 0;
            if (i > 2 ){
                y = 200;
                currentX = (mainW / 2) + startX + ((i - 3) * gap);
            }

            // วาดกล่องขาว (บน)
            if (Gameconfig_Pokemon.IsShopSellArr[i] == 2){
                g2.setColor(Color.BLUE);
                money = money + Integer.parseInt(Gameconfig_Pokemon.PokemonSkill_Hsm.get(arr2.get(i)).get("price"));
                arrSell.add(arr3.get(i));
            }else {
                g2.setColor(Color.white);
            }




            g2.fillRoundRect(currentX, currentY +( -220 + y ), 120, 120, 20, 20);
            g2.drawImage(DB_pokemon.getPokeimgByName(arr2.get(i)) , currentX, currentY +( -220 + y ), 120, 120,null);

            // วาดแถบชื่อ (ล่าง)
            g2.fillRoundRect(currentX, currentY + (-90 + y  ), 120, 40, 20, 20);

            // วาดตัวเลข i
            g2.setColor(Color.black);
            uiCreateroom.drawCenteredString(g2, arr2.get(i), currentX, currentY + (-90 + y  ), 120, 40, FontConfig.KertazFont20);
        }
        Gameconfig_Pokemon.MoneySell = arrSell;

        //
        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2,String.valueOf(money) ,(mainW / 2 ) -150 , (mainH / 2 )  + 182 , 140 , 50 , FontConfig.KertazFont30 );

        g2.setColor(Color.GREEN);
        g2.fillRoundRect((mainW / 2 ) + 70 , (mainH / 2 )  + 182 , 100 , 50 , 20 , 20);

        g2.setColor(Color.GRAY);
        g2.fillRoundRect((mainW / 2 ) + 180 , (mainH / 2 )  + 182 , 100 , 50 , 20 , 20);

        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2,"ขาย" ,(mainW / 2 ) + 70 , (mainH / 2 )  + 182 , 100 , 50 , GameConfig.thaiFont);
        uiCreateroom.drawCenteredString(g2,"ยกเลิก" ,(mainW / 2 ) + 180 , (mainH / 2 )  + 182 , 100 , 50 , GameConfig.thaiFont);


    }

    public void TrensPokemon(){

        String pokename = DB_getData.getlast_Show_Pokemon_Name(DB_getData.getroom());
        int componetBox[] = DB_pokemon.GetAllComponet(HardwareID.getHWID());
        int pokeball = componetBox[0];
        if (pokename.equalsIgnoreCase("egg")){
            DB_pokemon.AddPokemonTouser(pokename);

        } else if(pokeball > 0){
            Gameconfig_Pokemon.Is_Dice_TrensPokemon = true;
            Random rand = new Random();
            int dice = rand.nextInt(6) + 1;
            Gameconfig_Pokemon.Dice_TrensPokemon = dice;
            DB_pokemon.UpdatePokeball(HardwareID.getHWID() , -1);

            HashMap<String , String> hsm = Gameconfig_Pokemon.PokemonSkill_Hsm.get(pokename);
            DB_dbmanager.Updatelasetoy(dice);
            Gameconfig_Pokemon.Is_Update_Dice_TrensPokemon = DB_getData.get_isTrens(DB_getData.getroom());

            if (dice == Integer.parseInt(hsm.get("toy")) ){
                Gameconfig_Pokemon.Is_Update_Dice_TrensPokemon = true;
                DB_pokemon.Updateis_Trens(true);
            }
        }else {
            GameConfig.showMessageModal = true;
        }



    }

    public void drewUIShop(Graphics2D g2 ,int x ,int y ){
        g2.drawImage(Gameconfig_IMG.BG_Shop, (x / 2 ) - 300 , (y / 2) - 250 , 600 , 500 , null);


        g2.setColor(Color.black);
        uiCreateroom.drawCenteredString(g2,String.valueOf( Gameconfig_Event.Shop_CountPokeball ) , (x / 2 ) - 60, (y / 2) + 90 , 125 , 50 , FontConfig.KertazFont30);

        //money
        g2.setColor(Color.white);
        uiCreateroom.drawCenteredString(g2,String.valueOf(Gameconfig_Event.Shop_CountPokeball * Gameconfig_Event.pokeball_Price) , (x / 2 ) - 140 , (y / 2) + 180 , 170 , 50  , FontConfig.KertazFont30);


    }

    public void drewUIPokemonCenter(Graphics2D g2, int screenW, int screenH , String token) {
        // 1. วาด Overlay สีดำจางๆ ด้านหลังเพื่อให้ UI ลอยเด่นขึ้นมา
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, screenW, screenH);

        // 2. วาดพื้นหลังหลัก
        int bgW = 600;
        int bgH = 500;
        int centerX = screenW / 2;
        int centerY = screenH / 2;
        g2.drawImage(Gameconfig_IMG.BG_PokemonCenter, centerX - 300, centerY - 250, bgW, bgH, null);

        ArrayList<String> pokemon = DB_pokemon.getAllPokemonInUser(token);
        for (int i = 0; i < pokemon.size(); i++) {
            int offsetX = 0;
            int offsetY = 0;
            String PokemonDetail[] = DB_pokemon.SelectPokemonByToken(pokemon.get(i));

            switch (i) {
                case 0: offsetX = -200; offsetY = -210; break;
                case 1: offsetX = -40;  offsetY = -210; break;
                case 2: offsetX = 120;  offsetY = -210; break;
                case 3: offsetX = -260; offsetY = -50;  break;
                case 4: offsetX = -100; offsetY = -50;  break;
            }

            // วาดเงาหรือกรอบ Slot (ตัวอย่างใช้สีขาวโปร่งแสง)
            if (Gameconfig_Event.isSelect_Pokemoncenter != null && Gameconfig_Event.isSelect_Pokemoncenter.equalsIgnoreCase(pokemon.get(i))){
                g2.setColor(Color.BLUE);
            }else {
                g2.setColor(new Color(0, 0, 0 , 10));
            }
            g2.fillRoundRect(centerX + offsetX, centerY + offsetY, 130, 150, 20, 20);
            g2.drawImage(DB_pokemon.getPokeimgByName(PokemonDetail[0]) , centerX + offsetX + 5, centerY + offsetY + 5 , 120 , 100, null);

            g2.setColor(Color.WHITE);
            uiCreateroom.drawCenteredString(g2,"HP "+PokemonDetail[2]+"/"+PokemonDetail[1],centerX + offsetX + 5, centerY + offsetY + 115 , 120 , 30 , FontConfig.KertazFont20);


            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(centerX + offsetX, centerY + offsetY, 130, 150, 20, 20);
        }
    }
}
