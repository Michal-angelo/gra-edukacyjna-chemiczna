package pl.edu.pg.jpwp.gra_edukacyjna_chemiczna.Common;

public class DevModeSettings {
    private static final boolean DEV_MODE = true;//Master switch


    public static boolean DEV_LEVEL_CREATOR = DEV_MODE && false;//back button = save particle
    public static boolean DEV_LEVEL_LOADER = DEV_MODE && false;//back button = load particle
    public static boolean DEV_LOADRANDOM = DEV_MODE && true;


    public static boolean DEV_POPULATE_ATOMS = DEV_MODE && false;
    public static boolean DEV_INSTANT_PLAY = DEV_MODE && true;//jump to PlayActivity -> GameView after app start
    public static int DEV_LOAD_LEVEL_ID = 0;//...and load level
    public static Enums.Difficulty DEV_DIFFICULTY = Enums.Difficulty.Easy;
}
