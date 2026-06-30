import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.classfile.attribute.SyntheticAttribute;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.UUID;

public class DB_pokemon {
    public static  BufferedImage imgpokemon(String token){
        String pokemonName = "";
        String sql = "SELECT pokemon FROM player WHERE token = ?";
        try (PreparedStatement pool = DB_Base.getConnection().prepareStatement(sql)){
            pool.setString(1,token);
            ResultSet rs = pool.executeQuery();

            if (rs.next()) {
                pokemonName = rs.getString("pokemon");

            }
        }catch (SQLException e){ e.printStackTrace(); }

        if (pokemonName != null && !pokemonName.isEmpty()) {
            try {
                // ใช้การโหลดแบบ File ที่คุณเพิ่งแก้สำเร็จ
                return ImageIO.read(new File("res/images/pokemon/" + pokemonName + "/1.png"));
            } catch (IOException e) {
                System.out.println("หาไฟล์ภาพโปเกมอนไม่เจอ: " + pokemonName);
            }
        }

        return null;
    }

    public static BufferedImage getPokeimgByName(String name){
        if (name != null && !name.isEmpty()) {
            try {
                return ImageIO.read(new File("res/images/pokemon/" + name + "/1.png"));
            } catch (IOException e) {
                System.out.println("หาไฟล์ภาพโปเกมอนไม่เจอ: " + name);
            }
        }

        return null;
    }

    public static String [] getPokemonSkills(String PokemonName){
        String skills[] = new  String[2];
        String sql = "SELECT  skill1 , skill2 FROM pokemon WHERE  name = ?";

        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1 ,PokemonName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                skills[0] = rs.getString("skill1");
                skills[1] = rs.getString("skill2");
            }
        }catch (SQLException e){ e.printStackTrace(); }
        return skills;
    }

    public static void Selectpokemon(String pokemon , String token){
        String sql = "UPDATE player SET pokemon = ? WHERE token = ?";

        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,pokemon);
            stmt.setString(2,token);
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static String GetnameUser(String token){
        String name = "";
        String sql = "SELECT name FROM player WHERE token = ?";
        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1 , token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                 name = rs.getString("name");

            }
        }catch (SQLException e){ e.printStackTrace(); }
        return name;
    }

    public static String GetPokemon (String token){
        String pokemon = "";
        String sql = "SELECT pokemon FROM player WHERE token = ?";
        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                pokemon = rs.getString("pokemon");
            }
        }catch (SQLException e){ e.printStackTrace(); }
        return pokemon;
    }

    public static String[] GetAllPokemon(String token){
        String pokemon[] = new String[4];
        String sql = "SELECT pokemon_2 , pokemon_3 , pokemon_4 , pokemon_5 FROM player WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                pokemon[0] = rs.getString("pokemon_2");
                pokemon[1] = rs.getString("pokemon_3");
                pokemon[2] = rs.getString("pokemon_4");
                pokemon[3] = rs.getString("pokemon_5");
            }
        }catch (SQLException e){ e.printStackTrace(); }
        return pokemon;
    }

    public static int[] GetAllComponet(String token){
        String sql = "SELECT pokeball , conis , card FROM player WHERE token = ? ";
        int arr[] = new int[3];
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                arr[0] = rs.getInt("pokeball");
                arr[1] = rs.getInt("conis");
                arr[2] = rs.getInt("card");
            }
        }catch (SQLException e){ e.printStackTrace(); }
        return  arr;
    }

    public static BufferedImage getCardByName(String name){
        if (name != null && !name.isEmpty()) {
            try {
                return ImageIO.read(new File("res/images/crad/" + name + "/1.png"));
            } catch (IOException e) {
                System.out.println("หาไฟล์ภาพโปเกมอนไม่เจอ: " + name);
            }
        }

        return null;
    }

    public static void usedCardSkill(String Cardtoken){
        String token = HardwareID.getHWID();
        String fil = getLocationCardSkill(Cardtoken);


        if (fil != null && !fil.isEmpty()){
            String sql = "UPDATE player SET " + fil + " = ? WHERE token = ?";
            try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
                stmt.setString(1,null);
                stmt.setString(2,token);
                stmt.executeUpdate();
            }catch (SQLException e){ e.printStackTrace(); }
        }

    }

    public static String getLocationCardSkill(String token ){

        String user ;
        user = HardwareID.getHWID();
        String sql = "SELECT crad_skil_1 , crad_skil_2 , crad_skil_3 FROM player WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,user);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                String card1 = rs.getString("crad_skil_1");
                String card2 = rs.getString("crad_skil_2");
                String card3 = rs.getString("crad_skil_3");

                if (card1 != null && card1.equalsIgnoreCase(token)){
                    return "crad_skil_1";
                } else if (card2 != null && card2.equalsIgnoreCase(token)) {
                    return "crad_skil_2";
                } else if (card3 != null &&card3.equalsIgnoreCase(token)) {
                    return "crad_skil_3";
                }
            }
        }catch (SQLException e){ e.printStackTrace(); }

        return "";
    }

    public static void setCountCardSkill(){
        String token = HardwareID.getHWID();
        String sql = "SELECT crad_skil_1 , crad_skil_2 , crad_skil_3 FROM player WHERE token = ?";
        int i = 0;
        try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){


                for (int j = 1 ; j <= 3 ; j++){
                    String card = rs.getString("crad_skil_"+j);
                    if (card != null && !card.isEmpty()){
                        i++;
                    }
                }

                String sql2 = "UPDATE player SET card = ? WHERE token = ?";
                try(PreparedStatement up = DB_Base.getConnection().prepareStatement(sql2)){
                    up.setInt(1,i);
                    up.setString(2,token);
                    up.executeUpdate();
                }


            }
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void UpdataLastPokemon(String pokemon_name , int evo ){
        String room = DB_getData.getroom();
        String sql = "UPDATE rooms SET  last_Show_Pokemon_Time = ? , last_Show_Pokemon_Name = ? , last_Show_Pokemon_Evo = ? , last_Show_Pokemon_Pattern = ? , last_Show_Pokemon_Ishow = true WHERE room_id = ?";
        long trigger = System.currentTimeMillis();
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setLong(1, trigger);
            stmt.setString(2,pokemon_name);
            stmt.setInt(3, evo);
            stmt.setInt(4 , Gameconfig_Pokemon.getLast_Show_Pokemon_Pattern);
            stmt.setString(5 , room);
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void AddPokemonTouser(String pokemon){
        String token = HardwareID.getHWID();
        String Solat = ChackPokemonRemain(token);
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        if (Solat != null){
            String sql = "UPDATE player SET "+ Solat + " = ? WHERE token = ?";
            try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
                stmt.setString(1,uuidString);
                stmt.setString(2,token);
                stmt.executeUpdate();

                insertPokemonToUsers(uuidString,pokemon,token);
                ResetPokemonShow();
                DB_dbmanager.Update_Nextren();
            }catch (SQLException e){ e.printStackTrace(); }
        }
    }
    public static String[] SelectPokemonByToken(String token){
        String arr[] = new String[3];
        String sql = "SELECT Pokemon_Name , Max_Hp , hp FROM player_pokemon WHERE Pokemon_Token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1 ,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
               String name = rs.getString("Pokemon_Name");
               String maxHP = String.valueOf(rs.getInt("Max_Hp"));
               String hp = String.valueOf(rs.getInt("hp"));

               if(name != null && !name.isEmpty()) arr[0] = name;
               if(maxHP != null && !maxHP.isEmpty()) arr[1] = maxHP;
               if(hp != null && !hp.isEmpty()) arr[2] = hp;
            }
        }catch (SQLException e){ e.printStackTrace(); }
        return arr;
    }    public static void insertPokemonToUsers(String uuid , String pokemonname , String usertoken){
        String sql = "INSERT INTO player_pokemon(Player_name , Player_token , Pokemon_Name , Pokemon_Token , Max_Hp , hp) VALUES(? , ? , ? , ? , ? , ?)";
        String playername = DB_pokemon.GetnameUser(usertoken);
        String hp = Gameconfig_Pokemon.PokemonSkill_Hsm.get(pokemonname).get("hp");
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,playername);
            stmt.setString(2,usertoken);
            stmt.setString(3,pokemonname);
            stmt.setString(4,uuid);
            stmt.setInt(5,Integer.parseInt(hp));
            stmt.setInt(6,Integer.parseInt(hp));
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }
    }
    public static String ChackPokemonRemain(String user){
        String Allpokemon[] = GetAllPokemon(user);
        int i = 2;
        for(String a : Allpokemon){
            if (a == null || a.isEmpty()){
                return "pokemon_"+i;
            }
            i++;
        }
        return null;
    }

    public static void ResetPokemonShow(){
        String room = DB_getData.getroom();
        String sql = "UPDATE rooms SET last_Show_Pokemon_Name = null , last_Show_Pokemon_Evo = -1 , last_Show_Pokemon_Pattern = -1 , last_Show_Pokemon_Ishow = false WHERE room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1 , room);
            stmt.executeUpdate();
            Gameconfig_Pokemon.isShow_pokemon = false;
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static ArrayList<String> getAllPokemonInUser(String token){
        ArrayList<String> arr = new ArrayList<>();
        String sql = "SELECT pokemon , pokemon_2 , pokemon_3 , pokemon_4 , pokemon_5 FROM player WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                String pokemon1 , pokemon2 , pokemon3 , pokemon4 , pokemon5;
                pokemon1 = rs.getString("pokemon");
                pokemon2 = rs.getString("pokemon_2");
                pokemon3 = rs.getString("pokemon_3");
                pokemon4 = rs.getString("pokemon_4");
                pokemon5 = rs.getString("pokemon_5");
                if (pokemon1 != null && !pokemon1.isEmpty())arr.add(pokemon1);
                if (pokemon2 != null && !pokemon2.isEmpty())arr.add(pokemon2);
                if (pokemon3 != null && !pokemon3.isEmpty())arr.add(pokemon3);
                if (pokemon4 != null && !pokemon4.isEmpty())arr.add(pokemon4);
                if (pokemon5 != null && !pokemon5.isEmpty())arr.add(pokemon5);
            }
        }catch (SQLException e){ e.printStackTrace(); }

        return arr;
    }

    public static void SellPokemon(){
        String pokemon1;
        String user = HardwareID.getHWID();
        String []UserArrPokemon = DB_pokemon.GetAllPokemon(user);
        ArrayList<String> arr = Gameconfig_Pokemon.MoneySell;
        String mainPokemon = DB_pokemon.GetPokemon(user);
        String sql = "";


        for (int i = 0 ; i < arr.size() ; i++){
            pokemon1 = null;
            String pokemon = arr.get(i);
            String pokemonname = DB_pokemon.SelectPokemonByToken(arr.get(i))[0];
            if ( mainPokemon.equalsIgnoreCase(pokemon)){
                 sql = "UPDATE  player SET  pokemon = ? , conis = conis + ? WHERE token = ?";
                pokemon1 = Util_PokemonFuntion.SelectPokemonEvo(pokemonname,1);
                UUID uuid = UUID.randomUUID();
                String uuidString = uuid.toString();
                insertPokemonToUsers(uuidString,pokemon1,user);
                pokemon1 = uuidString;

            }else{
                for(int j = 0 ; j < 4 ; j++){
                    if (pokemon.equalsIgnoreCase(UserArrPokemon[j])){
                        int o = j + 2;
                        sql = "UPDATE  player SET  pokemon_" + o + " = ? , conis = conis + ? WHERE token = ?";

                        UserArrPokemon[j] = null;
                        break;
                    }
                }


            }
            if (sql != null && !sql.isEmpty() ){
                try (PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
                    stmt.setString(1,pokemon1);
                    stmt.setInt(2, Integer.parseInt(Gameconfig_Pokemon.PokemonSkill_Hsm.get(pokemonname).get("price")));
                    stmt.setString(3,user);
                    stmt.executeUpdate();
                }catch (SQLException e){ e.printStackTrace(); }
            }

        }


    }

    public static void Hatchingeggs(){
        String user = HardwareID.getHWID();
        String []UserArrPokemon = DB_pokemon.GetAllPokemon(user);

        if ( UserArrPokemon.length > 0){
            for (int i = 0 ; i < UserArrPokemon.length ; i++){
                String pokemonname = SelectPokemonByToken(UserArrPokemon[i])[0];
                String sql = "";
                String pokemon = Util_PokemonFuntion.RendomPokemon(1);
                if (i == 0 && pokemonname.equalsIgnoreCase("egg")){
                    sql = "UPDATE player SET pokemon = ? WHERE token = ?";
                    UserArrPokemon[0] = null;
                }else {
                    if (pokemonname.equalsIgnoreCase("egg")){
                        int o = i +2;
                        sql = "UPDATE player SET pokemon_" + o + " = ? WHERE token = ?";
                        UserArrPokemon[i] = null;
                    }

                }

                if (sql != null && !sql.isEmpty()){
                    UUID uuid = UUID.randomUUID();
                    String uuidString = uuid.toString();
                    try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
                        insertPokemonToUsers(uuidString,pokemon ,user);
                        stmt.setString(1,uuidString);
                        stmt.setString(2,user);
                        stmt.executeUpdate();

                    }catch (SQLException e){ e.printStackTrace(); }
                }
            }
        }


    }

    public static void UpdateBoxEvent(String room ,String box){
        String sql = "";
        String BoxEvent = "";
        if (box == null && box.isEmpty()){
            sql = "UPDATE rooms SET BoxEvent = ? WHERE room_id  = ?";
            BoxEvent = null;
        }else {
            sql = "UPDATE rooms SET BoxEvent = ? WHERE room_id  = ?";
            BoxEvent = box;
        }

        if (sql != null && !sql.isEmpty()){
            try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
                stmt.setString(1 , BoxEvent);
                stmt.setString(2,room);
                stmt.executeUpdate();
            }catch (SQLException e){ e.printStackTrace(); }
        }
    }

    public static void Updateis_Trens(boolean status){
        String room = DB_getData.getroom();
        String sql = "UPDATE  rooms SET is_Trens = ? WHERE room_id  = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setBoolean(1,status);
            stmt.setString(2, room);
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static void Update_break(String token , int break_count){
        String sql = "UPDATE player SET break = break + ? WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setInt(1, break_count);
            stmt.setString(2,token);
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static boolean Buypokeball(String hwid){
        String sql = "UPDATE player SET pokeball = pokeball + ? , conis = conis - ?  WHERE token = ?";
        int pokeball = Gameconfig_Event.Shop_CountPokeball;
        int price = pokeball * Gameconfig_Event.pokeball_Price;
        int component[] = DB_pokemon.GetAllComponet(hwid);
        int money = component[1];
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            if (price <= money){
                stmt.setInt(1,pokeball);
                stmt.setInt(2, price);
                stmt.setString(3,hwid);
                stmt.executeUpdate();
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){ e.printStackTrace(); }
        return false;
    }

    public static void UpdatePokeball(String hwid , int pokeball){
        String sql = "UPDATE player SET pokeball = pokeball + ? WHERE token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, pokeball);
            stmt.setString(2, hwid);
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }


    }

    public static void ResetPokemonDB(String hwid){
        ArrayList<String> arr = getAllPokemonInUser(hwid);
        ArrayList<String> PokemonToken = SelectPokemonDB_Bytoken_user(hwid);
        for (String a : PokemonToken){
            if (!ChackString2Array(a , arr)){
                DELETE_PokemonUserDB(a);
            }
        }
    }

    public static boolean ChackString2Array(String arr1, ArrayList<String> arr2){

            for (int j = 0 ; j < arr2.size() ; j++){
                if (arr1.equalsIgnoreCase(arr2.get(j))){
                    return true;
                }
            }

        return false;
    }

    public static void DELETE_PokemonUserDB(String pokemontoken){
        String sql = "DELETE FROM player_pokemon WHERE Pokemon_Token = ? ";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)) {
            stmt.setString(1,pokemontoken);
            stmt.executeUpdate();
        }catch (SQLException e){ e.printStackTrace(); }
    }

    public static ArrayList<String> SelectPokemonDB_Bytoken_user(String hwid){
        ArrayList<String> arr = new ArrayList<>();
        String sql  = "SELECT Pokemon_Token FROM player_pokemon WHERE Player_token = ?";
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setString(1,hwid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                String token = rs.getString("Pokemon_Token");
                if(token != null && !token.isEmpty())arr.add(token);

            }
        }catch (SQLException e){ e.printStackTrace(); }
        return arr;
    }

    public static void Heal_Pokemon(String pokemontoken , int hp){
        String sql = "UPDATE player_pokemon SET hp = ? WHERE Pokemon_Token = ?";
        String pokemondata[] = DB_pokemon.SelectPokemonByToken(pokemontoken);
        int oulHp = Integer.parseInt(pokemondata[2]);
        int maxHp = Integer.parseInt(pokemondata[1]);
        int newhp = oulHp + hp;
        if (newhp > maxHp){
            newhp = maxHp;
        }
        try(PreparedStatement stmt = DB_Base.getConnection().prepareStatement(sql)){
            stmt.setInt(1,newhp);
            stmt.setString(2,pokemontoken);
            stmt.executeUpdate();
        } catch (SQLException e){ e.printStackTrace(); }
    }

}
