import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class DB_getData {
    public static String[] getRoomUser(String RoomId){
        String[] players = new String[6];
        String sql = "SELECT * FROM rooms WHERE  room_id  = ?";
        try  (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,RoomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                DB_dbmanager.update_current_players(RoomId);
                players[0] = rs.getString("player_1");
                players[1] = rs.getString("player_2");
                players[2] = rs.getString("player_3");
                players[3] = rs.getString("player_4");
                players[4] = String.valueOf(rs.getInt("current_players"));
                players[5] = String.valueOf(rs.getInt("max_players"));
            }

        }catch (SQLException e){ e.printStackTrace(); }
        return players;
    }
    public static String getroom() {
        // ใช้ SQL เดิมของคุณ
        String sql = "SELECT room_id FROM rooms WHERE (player_1 = ? OR player_2 = ? OR player_3 = ? OR player_4 = ?)";
        String hwid = HardwareID.getHWID();

        try (PreparedStatement pstmt = DB_Base.getConnection().prepareStatement(sql)) {

            // ต้องเซตค่าให้ครบทั้ง 4 ตำแหน่งที่ประกาศไว้ใน SQL
            pstmt.setString(1, hwid);
            pstmt.setString(2, hwid);
            pstmt.setString(3, hwid);
            pstmt.setString(4, hwid);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("room_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ""; // คืนค่า String ว่างถ้าไม่เจอ
    }
    public static String getGameState(String roomid){
        String sql = "SELECT status FROM rooms WHERE room_id = ? ";

        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,roomid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return rs.getString("status");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String[] getCradSkillDetail(String token){
        String skil[] = new String[3];
        String sql = "SELECT  name , img , detail FROM cradskill WHERE token = ?";
        try(PreparedStatement stmt =  DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                skil[0] = rs.getString("name");
                skil[1] = rs.getString("img");
                skil[2] = rs.getString("detail");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return skil;
    }

    public static ArrayList<String> getUserCrad(){
        String hwid = HardwareID.getHWID();
        ArrayList<String> crad = new  ArrayList<>();
        String sql = "SELECT crad_skil_1 , crad_skil_2 , crad_skil_3 FROM player WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,hwid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                for (int i = 1 ; i <= 3 ; i++){
                    String cd = rs.getString("crad_skil_"+i);
                    if (cd != null && !cd.isEmpty()){
                        crad.add(cd);
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return crad;
    }

    public static int[] getXY(String token){
        int xy[] = new int[2];
        String sql = "SELECT x , y  FROM player WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                xy[0] = rs.getInt("x");
                xy[1] = rs.getInt("y");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return xy;
    }

    public static void UpdateUserGameStart(String roomid){
        String user[] = getRoomUser(roomid);
        int n = 0;
        if(user[0] != null && !user[0].isEmpty()){
            n++;
        }

        if(user[1] != null && !user[1].isEmpty()){
            n++;
        }

        if(user[2] != null && !user[2].isEmpty()){
            n++;
        }

        if(user[3] != null && !user[3].isEmpty()){
            n++;
        }
        String sql = "UPDATE player SET pokemon_2 = null , pokemon_3 = null , pokemon_4 = null , pokemon_5 = null , pokeball = 5 , conis = 0 ,card = 3,crad_skil_1 = ? , crad_skil_2 = ? , crad_skil_3 = ? , x = 10 , y = 0 WHERE token = ?";

        for(int i = 0 ; i < n ; i++){
            try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
                stmt.setString(1,RandomCard());
                stmt.setString(2,RandomCard());
                stmt.setString(3,RandomCard());
                stmt.setString(4,user[i]);
                stmt.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static ArrayList<String> getAllCard(){
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT token FROM cradskill ";
        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String token = rs.getString("token");
                if (token != null) {
                    arr.add(token);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all cards: " + e.getMessage());
            e.printStackTrace();
        }
        return arr;
    }

    public static String RandomCard() {
        ArrayList<String> arr = Gameconfig_Pokemon.AllCardSkill;

        if (arr == null || arr.isEmpty()) {
            return null;
        }

        Random rand = new Random();

        int dice = rand.nextInt(arr.size());

        return arr.get(dice);
    }

    public static String getTren(){
        String room = DB_getData.getroom();
        String tren  = "";
        String sql = "SELECT tren FROM rooms WHERE room_id = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                tren = rs.getString("tren");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching gettren: " + e.getMessage());
            e.printStackTrace();
        }

        return tren;
    }

    public static long getDice_trigger(String room){
        String sql = "SELECT dice_trigger FROM rooms WHERE  room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
              return  rs.getLong("dice_trigger");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getDice_trigger: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static int getLast_dice_val(String room){
        String sql = "SELECT last_dice_val FROM rooms WHERE  room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return  rs.getInt("last_dice_val");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getLast_dice_val: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<String> getAll_NamePoekmon(){
        ArrayList<String> arr = new ArrayList<>();
        String sql ="SELECT name FROM pokemon";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                if (name != null && !name.isEmpty()){
                    arr.add(name);
                }
            }
        }catch (SQLException e) {
            System.err.println("Error getAll_NamePoekmon : " + e.getMessage());
            e.printStackTrace();
        }

        return arr;
    }

    public static ArrayList<String> getAll_NamePoekmon_evo(int evo){
        ArrayList<String> arr = new ArrayList<>();
        String sql ="SELECT name FROM pokemon WHERE evo = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setInt(1,evo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                if (name != null && !name.isEmpty()){
                    arr.add(name);
                }
            }
        }catch (SQLException e) {
            System.err.println("Error getAll_NamePoekmon_evo: " + e.getMessage());
            e.printStackTrace();
        }

        return arr;
    }

    public static long getlast_Show_Pokemon_Time(String room){
        String sql = "SELECT last_Show_Pokemon_Time FROM rooms WHERE  room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return  rs.getLong("last_Show_Pokemon_Time");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getlast_Show_Pokemon_Time: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static int getlast_Show_Pokemon_Evo(String room){
        String sql = "SELECT last_Show_Pokemon_Evo FROM rooms WHERE  room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return  rs.getInt("last_Show_Pokemon_Evo");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getlast_Show_Pokemon_Evo: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static String getlast_Show_Pokemon_Name(String room){
        String sql = "SELECT last_Show_Pokemon_Name FROM rooms WHERE  room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return  rs.getString("last_Show_Pokemon_Name");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getlast_Show_Pokemon_Name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static int getlast_Show_Pokemon_Pattern(String room){
        String sql = "SELECT last_Show_Pokemon_Pattern FROM rooms WHERE  room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return  rs.getInt("last_Show_Pokemon_Pattern");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getlast_Show_Pokemon_Pattern: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static Boolean getlast_Show_Pokemon_Ishow(String room){
        String sql = "SELECT last_Show_Pokemon_Ishow FROM rooms WHERE  room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return  rs.getBoolean("last_Show_Pokemon_Ishow");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching last_Show_Pokemon_Ishow: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static String[] getPokemonDate(String name){
        String[] data = new String[7];
        String sql = "SELECT * FROM pokemon WHERE name = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                data[0] = rs.getString("skill1");
                data[1] = rs.getString("skill2");
                data[2] = String.valueOf(rs.getInt("evo"));
                data[3] = String.valueOf(rs.getInt("toy"));
                data[4] = String.valueOf(rs.getInt("price"));
                data[5] = String.valueOf(rs.getInt("sword"));
                data[6] = String.valueOf(rs.getInt("hp"));
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getPokemonDate: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    public static int[] getLocation(){
        int[] xy = new int[2];
        String sql = "SELECT x , y FROM setlocation WHERE id = 1";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            ResultSet rs  = stmt.executeQuery();
            if (rs.next()){
               xy[0] = rs.getInt("x");
               xy[1] = rs.getInt("y");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getLocation: " + e.getMessage());
            e.printStackTrace();
        }
        return xy;
    }

    public static String[] getEgg(String usertoken){
        String arr[] = new String[5];
        String sql = "SELECT ";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){

        }catch (SQLException e) {
            System.err.println("Error fetching getEgg: " + e.getMessage());
            e.printStackTrace();
        }
        return arr;
    }

    public static String getBoxEvent(String room){
        String sql = "SELECT BoxEvent FROM rooms WHERE room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1 , room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return rs.getString("BoxEvent");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching getBoxEvent: " + e.getMessage());
            e.printStackTrace();
        }
        return  null;
    }

    public static boolean get_isTrens(String room){
        String sql = "SELECT  is_Trens FROM rooms WHERE room_id = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,room);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return rs.getBoolean("is_Trens");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching get_isTrens: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static int get_Break(String token){
        int break_count = 0;
        String sql = "SELECT break FROM player WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return rs.getInt("break");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching get_Break: " + e.getMessage());
            e.printStackTrace();
        }
        return break_count;
    }

    public static int[] get_HP_Pokemon(String token){
        int arr[] = new int[2];
        String sql = "SELECT Max_Hp , hp FROM player_pokemon WHERE Pokemon_Token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                arr[0] = rs.getInt("hp");
                arr[1] = rs.getInt("Max_Hp");
            }
        }catch (SQLException e) {
            System.err.println("Error fetching get_HP_Pokemon: " + e.getMessage());
            e.printStackTrace();
        }
        return arr;
    }

}
