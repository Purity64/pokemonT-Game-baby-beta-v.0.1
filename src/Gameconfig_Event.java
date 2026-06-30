import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class Gameconfig_Event {
    public static BufferedImage test ;

    //eventBox
    public static String[] EventBoxAyyaykeyForder = {"boxCave" , "boxCity" , "boxEven"  , "boxGym" , "boxLegenPokemon" , "boxNomalBlack" , "boxNomalBlue", "boxNomalGreen" , "boxNomalRed" , "boxPokemonCenter" , "boxRocket" , "boxStart" , "boxEgg"};
    public static HashMap<String , ArrayList> RenderimgBoxeventHashMap = new HashMap<>();
    public static ArrayList<BufferedImage> RenderingBoxeventArray = new ArrayList<>();
    public static BufferedImage RenderimgBoxevent;
    public static String KeyRenderimgBoxevent = "";
    public static boolean IsEvent = false;

    //shopEvent
    public static boolean isShopOpen = false;
    public static final int pokeball_Price = 5;
    public static int Shop_CountPokeball = 1;
    public static int Shop_CountMoney = Shop_CountPokeball * pokeball_Price;

    //pokemoncenter Event
    public static boolean isPokemoncenterOpen = false;
    public static String isSelect_Pokemoncenter = null;
}
