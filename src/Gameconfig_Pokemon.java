
import java.awt.*;
import java.awt.image.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Gameconfig_Pokemon {
    public static String  User_Select_Pokemon  = "pikachu";

    //ShowPokemonAnime
    public static String[] Keymap_PokemonShowAnime_Hsm = { "l" , "r" , "Tren"};
    public static HashMap<String , HashMap<String , ArrayList<BufferedImage>>> PokemonShowAnime_Hsm = new HashMap<>();
    public static ArrayList<BufferedImage> PokemonShowAnime_ArrList = new ArrayList<>();
    public static BufferedImage PokemonShowAnime_Render ;
    public static String KeyLocation_PokemonShowAnime_name = null;
    public static int KeyLocation_PokemonShowAnime_Hsm = -1;
    public static long last_Show_Pokemon_Time;
    public static int last_Show_Pokemon_evo ;
    public static boolean isShow_pokemon ;
    public static int getLast_Show_Pokemon_Pattern;


    //pokemondetil
    public static String[] Keymap_pokemonskill_hsm = {"skill1", "skill2" ,"evo" , "toy" , "price" , "sword" , "hp"};
    public static HashMap<String , HashMap<String , String>> PokemonSkill_Hsm = new HashMap<>();

    //pokeball
    public static BufferedImage pokeball_img;
    public static BufferedImage coins_img;
    public static BufferedImage poke_card_img;
    public static BufferedImage pokeball_Nunull_img;

    //pikachu
    public static BufferedImage pikachu_img ;
    public static String pikachu = "pikachu";

    //snorlax
    public static BufferedImage snorlax_img;
    public static String snorlax = "snorlax";

    //cradskill
    public static ArrayList  AllCardSkill = DB_getData.getAllCard();
    public static BufferedImage BG_crad_skill_img;
    public static boolean iscardopen = false;
    public static String Cardtoken ;
    public static boolean isShowCardSkill = false;

    //bg
    public static BufferedImage BG_menu_img;
    public static BufferedImage Bg_mune_Join_AND_create_img;
    public static BufferedImage BG_menu_Witeingroom;
    public static BufferedImage BG_menu_GaneRuning;

    //player
    public static int toy = 0;
    public static BufferedImage jk ;


    //toy
    public static HashMap<Integer , ArrayList> hashmapdice = new HashMap<>();
    public static ArrayList<BufferedImage> RenderdiceArrayList = new ArrayList<>();

    public static long LaseDice_trigger ;

    //ui shop
    public static boolean IsShopSell = false;
    public static int[] IsShopSellArr = new int[5];
    public static ArrayList<String> MoneySell ;

    //TrensPokemon
    public static int Dice_TrensPokemon = 0;
    public static String Dice_TrensPokemon_name = "";
    public static boolean Is_Dice_TrensPokemon = false;
    public static boolean Is_Update_Dice_TrensPokemon = true;

    public static Graphics2D g2 ;


}
