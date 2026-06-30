import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Util_matrixfuntion {


    public static int[] main(int x , int y , int toy){
        int n = 11;
        int[][] matrix = new int[n][n];

        int n1 = n -1;



        int yDf = y;
        int xDf = x;

        int xy[] = new int[2];


        if(x == 0 && y < n1){
            yDf = y;
            y = y + toy;
            if(y > n1 ){
                x = toy - (n1 - yDf);
                y = n1;

            }
        }else if(y == n1 && x < n1){
            xDf = x;
            x = x + toy;

            if(x > n1){
                x = n1;
                y = y - (toy - (n1 - xDf));
            }
        }else if(y <= n1 && x == n1 && y != 0){
            y = y - toy;
            if(y < 0){
                y = 0;
            }
        }else if(x <= n1 && y == 0){
            xDf = x;
            x = x - toy;
            if(x < 0){
                y = toy - xDf ;
                x = 0;

            }
        }

        xy[0] = x;
        xy[1] = y;
        return xy;
    }

    public static int[] MapLocation(int x , int y){
        int location[] = new int[2];
        int mx , my;
        int pokemonevo = 1;
        mx = 300;
        my = 110 ;
        if (x == 9 && y == 0){
            mx = 300;
            my = 170;
        } else if (x == 8 && y == 0) {
            mx = 300;
            my = 210;
        }else if (x == 7 && y == 0) {
            mx = 310;
            my = 250;
        }else if (x == 6 && y == 0) {
            mx = 310;
            my = 290;
        }else if (x == 5 && y == 0) {
            mx = 320;
            my = 325;
        }else if (x == 4 && y == 0) {
            mx = 320;
            my = 355;
        }else if (x == 3 && y == 0) {
            mx = 320;
            my = 385;
        }else if (x == 2 && y == 0) {
            mx = 320;
            my = 415;
        }else if (x == 1 && y == 0) {
            mx = 320;
            my = 435;
        }else if (x == 0 && y == 0) {
            mx = 320;
            my = 470;
        }else if (x == 0 && y == 1) {
            mx = 240;
            my = 470;
        }else if (x == 0 && y == 2) {
            mx = 180;
            my = 470;
        }else if (x == 0 && y ==3 ) {
            mx = 100;
            my = 470;
        }else if (x == 0 && y == 4 ) {
            mx = 40;
            my = 470;
        }else if (x == 0 && y == 5) {
            mx = -20;
            my = 470;
        }else if (x == 0 && y == 6) {
            mx = -90;
            my = 470;
        }else if (x == 0 && y == 7) {
            mx = -150;
            my = 470;
        }else if (x == 0 && y == 8) {
            mx = -210;
            my = 470;
        }else if (x == 0 && y == 9) {
            mx = -270;
            my = 470;
        }else if (x == 0 && y == 10) {
            mx = -340;
            my = 470;
        }else if (x == 1 && y == 10) {
            mx = -370;
            my = 450;
        }else if (x == 2 && y == 10) {
            mx = -385;
            my = 425;
        }else if (x == 3 && y == 10) {
            mx = -420;
            my = 400;
        }else if (x == 4 && y == 10) {
            mx = -440;
            my = 380;
        }else if (x == 5  && y == 10) {
            mx = -460;
            my = 355;
        }else if (x ==6  && y == 10) {
            mx = -500;
            my = 325;
        }else if (x ==7  && y == 10) {
            mx = -540;
            my = 290;
        }else if (x == 8  && y == 10) {
            mx = -570;
            my = 260;
        }else if (x == 9  && y == 10) {
            mx = -610;
            my = 220;
        }else if (x == 10  && y == 10) {
            mx = -660;
            my = 180;
        }else if (x == 10  && y == 9) {
            mx = -575;
            my = 170;
        }else if (x == 10  && y == 8) {
            mx = -500;
            my = 170;
        }else if (x == 10  && y == 7) {
            mx = -410;
            my = 150;
        }else if (x == 10  && y == 6) {
            mx = -320;
            my = 150;
        }else if (x == 10  && y == 5) {
            mx = -230;
            my = 140;
        }else if (x == 10  && y == 4) {
            mx = -130;
            my = 140;
        }else if (x == 10  && y == 3) {
            mx = -30;
            my = 130;
        }else if (x == 10  && y == 2) {
            mx = 70;
            my = 130;
        }else if (x == 10  && y == 1) {
            mx = 170;
            my = 110;
        }
        location[0] = mx;
        location[1] = my;
        Gameconfig_Pokemon.last_Show_Pokemon_evo = pokemonevo;
        return location;
    }

    public static void UpDatenewlocation(int toy){
        String token = HardwareID.getHWID();
        String sql = "UPDATE player SET x = ? , y = ?  WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            int xy[] = DB_getData.getXY(token);
            int newxy[] = main(xy[0] , xy[1] , toy);
            stmt.setInt(1,newxy[0]);
            stmt.setInt(2,newxy[1]);
            stmt.setString(3,token);
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String ChackLocationEvent(int x , int y){
        String keygetbox = "Start";

        if (x == 9 && y == 0){
            keygetbox = "Nomal_Green";
        } else if (x == 8 && y == 0) {
            keygetbox = "Even";
        }else if (x == 7 && y == 0) {
            keygetbox = "Nomal_Green";
        }else if (x == 6 && y == 0) {
            keygetbox = "Nomal_Green";
        }else if (x == 5 && y == 0) {
            keygetbox = "City";
        }else if (x == 4 && y == 0) {
            keygetbox = "Nomal_Green";
        }else if (x == 3 && y == 0) {
            keygetbox = "Nomal_Green";
        }else if (x == 2 && y == 0) {
            keygetbox = "Even";
        }else if (x == 1 && y == 0) {
            keygetbox = "Nomal_Green";
        }else if (x == 0 && y == 0) {
            keygetbox = "Gym";
        }else if (x == 0 && y == 1) {
            keygetbox = "Nomal_Black";
        }else if (x == 0 && y == 2) {
            keygetbox = "Cave";
        }else if (x == 0 && y ==3 ) {
            keygetbox = "Nomal_Black";
        }else if (x == 0 && y == 4 ) {
            keygetbox = "Nomal_Black";
        }else if (x == 0 && y == 5) {
            keygetbox = "City";
        }else if (x == 0 && y == 6) {
            keygetbox = "Nomal_Black";
        }else if (x == 0 && y == 7) {
            keygetbox = "Nomal_Black";
        }else if (x == 0 && y == 8) {
            keygetbox = "Egg";
        }else if (x == 0 && y == 9) {
            keygetbox = "Nomal_Black";
        }else if (x == 0 && y == 10) {
            keygetbox = "Rocket";
        }else if (x == 1 && y == 10) {
            keygetbox = "Nomal_Blue";
        }else if (x == 2 && y == 10) {
            keygetbox = "PokemonCenter";
        }else if (x == 3 && y == 10) {
            keygetbox = "Nomal_Blue";
        }else if (x == 4 && y == 10) {
            keygetbox = "Nomal_Blue";
        }else if (x == 5  && y == 10) {
            keygetbox = "City";
        }else if (x ==6  && y == 10) {
            keygetbox = "Nomal_Blue";
        }else if (x ==7  && y == 10) {
            keygetbox = "Nomal_Blue";
        }else if (x == 8  && y == 10) {
            keygetbox = "Even";
        }else if (x == 9  && y == 10) {
            keygetbox = "Nomal_Blue";
        }else if (x == 10  && y == 10) {
            keygetbox = "Gym";
        }else if (x == 10  && y == 9) {
            keygetbox = "Nomal_Red";
        }else if (x == 10  && y == 8) {
            keygetbox = "Cave";
        }else if (x == 10  && y == 7) {
            keygetbox = "Nomal_Red";
        }else if (x == 10  && y == 6) {
            keygetbox = "Nomal_Red";
        }else if (x == 10  && y == 5) {
            keygetbox = "City";
        }else if (x == 10  && y == 4) {
            keygetbox = "Nomal_Red";
        }else if (x == 10  && y == 3) {
            keygetbox = "Nomal_Red";
        }else if (x == 10  && y == 2) {
            keygetbox = "Nomal_Red";
        }else if (x == 10  && y == 1) {
            keygetbox = "LegenPokemon";
        }
        return keygetbox;

    }




}
