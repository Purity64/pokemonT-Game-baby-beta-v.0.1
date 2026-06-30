import java.sql.*;
public class DB_dbmanager {
    GamePanel gp;
    public static UIGameStart uiGameStart;
    public DB_dbmanager(GamePanel gp){
        this.uiGameStart = new UIGameStart(gp);
    }
    private static final String URL = "jdbc:mysql://localhost:3306/pokat?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void insertRoom(String roomId, int maxPlayers) {
        String token = HardwareID.getHWID();
        String sql = "INSERT INTO rooms (room_id, max_players , player_1 ) VALUES (?, ? , ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, roomId);
            pstmt.setInt(2, maxPlayers);
            pstmt.setString(3,token);
            pstmt.executeUpdate();

            System.out.println("บันทึกห้อง " + roomId + " ลง MySQL แล้ว!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean chackroom(String roomid) {
        String sql = "SELECT id FROM rooms WHERE room_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, roomid);
            ResultSet rs = pstmt.executeQuery();

            // ถ้ามีข้อมูล (rs.next() เป็น true) แปลว่า "มีห้องนี้อยู่แล้ว"
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // ส่งข้อความ Error ไปที่ Modal ของคุณ
            GameConfig.showMessageModal = true;
            GameConfig.modalTitle = "SQL Error";
            GameConfig.modalMessage = "not connet db";
        }
        return false; // ถ้าไม่เจอห้อง หรือ Error ให้คืนค่าเป็น false
    }



    public static boolean chackplayer() {
        String token = HardwareID.getHWID();
        String sql = "SELECT player_1 , player_2 , player_3 , player_4  FROM rooms WHERE player_1 = ? OR player_2 = ? OR player_3 = ? OR player_4 = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // ใส่ค่า token ลงในเครื่องหมาย ? ทั้ง 4 ตำแหน่ง
            pstmt.setString(1, token);
            pstmt.setString(2, token);
            pstmt.setString(3, token);
            pstmt.setString(4, token);

            ResultSet rs = pstmt.executeQuery();

            // ถ้า rs.next() เป็น true แสดงว่าเจอ Token นี้ค้างอยู่ใน Database
            if (rs.next()) {
                String player1 = rs.getString("player_1");
                String player2 = rs.getString("player_2");
                String player3 = rs.getString("player_3");
                String player4 = rs.getString("player_4");
               if (token.equals(player1)){
                   removeRooms(token);
               } else if (token.equals(player2)) {
                   removePlayerFromRoom(token , "player_2");
               } else if (token.equals(player3)) {
                   removePlayerFromRoom(token , "player_3");
               } else if (token.equals(player4)) {
                   removePlayerFromRoom(token , "player_4");
               }else {
                   return false;
               }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            GameConfig.showMessageModal = true;
            GameConfig.modalTitle = "Database Error";
            GameConfig.modalMessage = "ไม่สามารถตรวจสอบสถานะผู้เล่นได้";
        }
        return false; // ไม่พบผู้เล่นในห้องใดๆ
    }


    public static void removeRooms(String token) {
        // ลบห้องที่ token นี้เป็นเจ้าของ (player_1)
        String sql = "DELETE FROM rooms WHERE player_1 = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, token);
            int result = pstmt.executeUpdate(); // ต้องมีคำสั่งนี้เพื่อให้ DB ทำงาน

            if(result > 0) {
                System.out.println("LOG: ลบห้องที่ " + token + " เป็นเจ้าของแล้ว");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void removePlayerFromRoom(String token, String fil) {
        String sql = "UPDATE rooms SET " + fil + " = NULL WHERE " + fil + " = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, token);
            int rows = pstmt.executeUpdate(); // <--- ต้องเพิ่มบรรทัดนี้!

            if (rows > 0) {
                System.out.println("LOG: ลบผู้เล่นออกจากช่อง " + fil + " สำเร็จ");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String chackplayerthisroom(String room) {
        String token = HardwareID.getHWID();
        String sql = "SELECT player_1, player_2, player_3, player_4   FROM rooms WHERE room_id = ? AND status = ?";

        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)) {
            stmt.setString(1, room);
            stmt.setString(2,"waiting");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {


                    String p1 = rs.getString("player_1");
                    String p2 = rs.getString("player_2");
                    String p3 = rs.getString("player_3");
                    String p4 = rs.getString("player_4");

                    // 1. เช็คก่อนว่าเราอยู่ในห้องนี้แล้วหรือยัง (ป้องกันการจอยซ้ำในห้องเดิม)
                    if (token.equals(p1)) return "player_1";
                    if (token.equals(p2)) return "player_2";
                    if (token.equals(p3)) return "player_3";
                    if (token.equals(p4)) return "player_4";

                    if (p1 == null) return "player_1";
                    if (p2 == null) return "player_2";
                    if (p3 == null) return "player_3";
                    if (p4 == null) return "player_4";

                }else {
                    GameConfig.showMessageModal = true;
                    GameConfig.modalTitle = "Player MAX";
                    return "";
                }


        } catch (SQLException e) { e.printStackTrace(); }
        return ""; // คืนค่า String ว่างถ้าห้องเต็มหรือหาไม่เจอ
    }

    public static boolean isMaxPlayer(String room){
        String sql = "SELECT max_players , current_players FROM rooms WHERE room_id  = ?";
        try (PreparedStatement pstmt = DB_Base.getConnection().prepareStatement(sql)){
            pstmt.setString(1 , room);
            pstmt.executeQuery();
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                int maxP = rs.getInt("max_players");
                int current_p = rs.getInt("current_players");
                System.out.println(maxP);
                System.out.println(current_p);
                if (current_p < maxP){
                    return  false;
                }else {
                    return  true;
                }
            }else {
                return  false;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void joinRoom (String fil , String room){
        String token = HardwareID.getHWID();
        String sql = "UPDATE rooms SET " + fil + " = ? WHERE room_id =  ?";
        if (fil.isEmpty() || fil == null){
            GameConfig.showMessageModal = true;
            GameConfig.modalTitle = "Nothave room ";
        }else {
            try (PreparedStatement pstmt = DB_Base.getConnection().prepareStatement(sql)) {
                pstmt.setString(1, token);
                pstmt.setString(2, room);
                pstmt.executeUpdate();

                update_current_players(room);

            } catch (SQLException e) { e.printStackTrace(); }
        }

    }

    public static void update_current_players(String room) {
        // SQL นี้จะนับว่าช่องไหนที่ไม่เป็น NULL บ้าง แล้วเอามาใส่ใน current_players
        String sql = "UPDATE rooms SET current_players = " +
                "(CASE WHEN player_1 IS NOT NULL THEN 1 ELSE 0 END + " +
                " CASE WHEN player_2 IS NOT NULL THEN 1 ELSE 0 END + " +
                " CASE WHEN player_3 IS NOT NULL THEN 1 ELSE 0 END + " +
                " CASE WHEN player_4 IS NOT NULL THEN 1 ELSE 0 END) " +
                "WHERE room_id = ?";

        try (PreparedStatement pstmt = DB_Base.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, room);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static  void AddPlayer(String newplayername){
        String token = HardwareID.getHWID();
        String sql = "SELECT name FROM player WHERE token = ?";

        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String name = rs.getString("name");
                if (!name.equals(newplayername)){
                    UPDATEplayername(newplayername , token);
                }
            }else{
                insertPlayername(newplayername , token);
            }

        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void UPDATEplayername(String name , String token){
        String sql = "UPDATE player SET name = ? WHERE token = ?";
        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)) {
            stmt.setString(1,name);
            stmt.setString(2,token);
            stmt.executeUpdate();

        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void insertPlayername(String name , String token){
        String sql = "INSERT INTO player(name , token) VALUES(? , ?) ";
        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,name);
            stmt.setString(2,token);
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void UpdateGame_Status(String status ){
        String sql = "UPDATE  rooms SET status = ? WHERE room_id = ?";
        String roomid = DB_getData.getroom();
        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,status);
            stmt.setString(2,roomid);
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void Auto_Update_gameState(){
        String room  = DB_getData.getroom();
        if (room != "" && room != null){
            String start = DB_getData.getGameState(room);
            if (start != "" && start != null){
                if (start.equalsIgnoreCase(GameConfig.Game_status_waiting)){
                    GameConfig.JoinroomID = room;
                    GameConfig.gameState = GameConfig.waitingroom;
                }else if (start.equalsIgnoreCase(GameConfig.Game_status_runing)){
                    GameConfig.gameState = GameConfig.GameRoom;
                }
            }else {
                GameConfig.gameState = GameConfig.titleState;
            }
        }else {
            GameConfig.gameState = GameConfig.titleState;
        }
    }

    public static void Update_Nextren(){
        String room = DB_getData.getroom();
        String sql = "UPDATE rooms SET tren = ? WHERE room_id = ?";
        String nexttren = "";
        String player = GameConfig.PlayerSlot; // เช่น "player_1"

        String[] RoomPlayer = DB_getData.getRoomUser(room);
        if (RoomPlayer == null || RoomPlayer[4] == null) return; // กันพังถ้าดึงข้อมูลไม่ได้

        int playercont = Integer.parseInt(RoomPlayer[4]);

        if (playercont == 1){
            nexttren = "player_1";
        }
        if (playercont == 2) {
            if (player.equalsIgnoreCase("player_1")) nexttren = "player_2";
            else if (player.equalsIgnoreCase("player_2")) nexttren = "player_1";
        }
        else if (playercont == 3) {
            if (player.equalsIgnoreCase("player_1")) nexttren = "player_2";
            else if (player.equalsIgnoreCase("player_2")) nexttren = "player_3";
            else if (player.equalsIgnoreCase("player_3")) nexttren = "player_1";
        }
        else if (playercont == 4) {
            if (player.equalsIgnoreCase("player_1")) nexttren = "player_2";
            else if (player.equalsIgnoreCase("player_2")) nexttren = "player_3";
            else if (player.equalsIgnoreCase("player_3")) nexttren = "player_4";
            else if (player.equalsIgnoreCase("player_4")) nexttren = "player_1";
        }

        if (!nexttren.equals("")) {
            try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)) {
                stmt.setString(1, nexttren);
                stmt.setString(2, room);
                stmt.executeUpdate();

                System.out.println("ระบบ: เปลี่ยนเทิร์นเป็น " + nexttren);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void Updatelasetoy(int dice){
        String room = DB_getData.getroom();
        long trigger = System.currentTimeMillis();
        String sql = "UPDATE rooms SET last_dice_val = ? , dice_trigger = ? WHERE room_id = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setInt(1,dice);
            stmt.setLong(2,trigger);
            stmt.setString(3,room);
            stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
