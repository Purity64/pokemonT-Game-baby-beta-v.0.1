import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Util_PokemonFuntion {
    public static String  RendomPokemon(int evo){
        ArrayList<String> arrpokemon = DB_getData.getAll_NamePoekmon_evo(evo);
        Random rand = new Random();
        int dice = rand.nextInt(arrpokemon.size()) ;
        return arrpokemon.get(dice);
    }

    public static BufferedImage getPokemonImg(String pokemon , int from){
        String[] keyform = Gameconfig_Pokemon.Keymap_PokemonShowAnime_Hsm;
        try {
            BufferedImage img = Gameconfig_Pokemon.PokemonShowAnime_Hsm.get(pokemon).get(keyform[from]).get(39);
            return img;
        } catch (NullPointerException e) {

        }
        return null;

    }
    public static String PokemonMap[][] = {
            {"munchlax","snorlax"},
            {"pichu","pikachu" , "raichu"},
            {"paras","parasect"}
    };

    public static String SelectPokemonEvo(String pokemon ,int evo){
        String poke = null;
        for(int i = 0 ; i < PokemonMap.length ; i++){
            for (int j = 0 ; j < PokemonMap[i].length ; j++){
                if (PokemonMap[i][j].equalsIgnoreCase(pokemon)){
                    if(j != 0){
                        evo = evo - 1;
                        poke = PokemonMap[i][evo];
                        return poke;
                    }else {
                        poke = pokemon;
                        return poke;
                    }

                }
            }
        }
        return poke;
    }
}
