package nz.artedungeon.misc;

import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.dungeon.MyPlayer;

public interface GameConstants
{
    // Monsters
    int[]    ASTEA_FROSTWEBS      = {9972, 9973, 9974, 9975, 9976, 9976, 9977, 9978, 9979, 9980, 9981, 9982};
    int[]    ICY_BONES            = {10042, 10043, 10044, 10045, 10046};
    int[]    ICE_FIENDS           = {9913, 9914, 9915};
    int[]    PLANE_FREEZERS       = {9929, 9930};
    int[]    BLOOD_CHILLERS       = {10024, 1025, 1026};
    int[][]  BOSSES               = {ASTEA_FROSTWEBS, ICE_FIENDS, ICY_BONES, PLANE_FREEZERS, BLOOD_CHILLERS};
    int[]    NONARGRESSIVE_NPCS   = {11226,
                                     12166,
                                     12174,
                                     12171,
                                     12170,
                                     12168,
                                     10978,
                                     10956,
                                     10954,
                                     10955,
                                     10957,
                                     10944,
                                     10942,
                                     10943,
                                     10945,
                                     11090};
    String[] BOSS_NAMES           = {"Gluttonous behemoth", "Astea Frostweb", "Icy Bones", "Luminescent Icefiend",};
    String   GLUTTONOUS           = "Gluttonous behemoth";
    String   ICE_FIEND            = "Luminescent Icefiend";
    String   ASTEA_FROSTWEB       = "Astea Frostweb";
    // Keys
    int[]    ORANGE_KEYS          = {18202, 18204, 18206, 18208, 18210, 18212, 18214, 18216};
    int[]    SILVER_KEYS          = {18218, 18220, 18222, 18224, 18226, 18228, 18230, 18232};
    int[]    YELLOW_KEYS          = {18234, 18236, 18238, 18240, 18242, 18244, 18246, 18248};
    int[]    GREEN_KEYS           = {18250, 18252, 18254, 18256, 18258, 18260, 18262, 18264};
    int[]    BLUE_KEYS            = {18266, 18268, 18270, 18272, 18274, 18276, 18278, 18280};
    int[]    PURPLE_KEYS          = {18282, 18284, 18286, 18288, 18290, 18292, 18294, 18296};
    int[]    CRIMSON_KEYS         = {18298, 18300, 18302, 18304, 18306, 18308, 18310, 18312};
    int[]    GOLD_KEYS            = {18314, 18316, 18318, 18320, 18322, 18324, 18326, 18328};
    int[][]  KEYS                 = {ORANGE_KEYS,
                                     SILVER_KEYS,
                                     YELLOW_KEYS,
                                     GREEN_KEYS,
                                     BLUE_KEYS,
                                     PURPLE_KEYS,
                                     CRIMSON_KEYS,
                                     GOLD_KEYS};
    // Doors
    int[]    GUARDIAN_DOORS       = {50346, 50347, 50348};
    int[]    STANDARD_DOORS       = {49306,
                                     49335,
                                     49336,
                                     49337,
                                     49338,
                                     49375,
                                     49625,
                                     50342,
                                     50343,
                                     50344,
                                     50311,
                                     50275};
    int[][]  BASIC_DOORS          = {GUARDIAN_DOORS, STANDARD_DOORS};
    int[]    ORANGE_DOORS         = {50208, 50209, 50210, 50211, 50212, 50213, 50214, 50215};
    int[]    SILVER_DOORS         = {50216, 50217, 50218, 50219, 50220, 50221, 50222, 50223};
    int[]    YELLOW_DOORS         = {50224, 50225, 50226, 50227, 50228, 50229, 50230, 50231};
    int[]    GREEN_DOORS          = {50232, 50233, 50234, 50235, 50236, 50237, 50238, 50239};
    int[]    BLUE_DOORS           = {50240, 50241, 50242, 50243, 50244, 50245, 50246, 50247};
    int[]    PURPLE_DOORS         = {50248, 50249, 50250, 50251, 50252, 50253, 50254, 50255};
    int[]    CRIMSON_DOORS        = {50256, 50257, 50258, 50259, 50260, 50261, 50262, 50263};
    int[]    GOLD_DOORS           = {50264, 50265, 50266, 50267, 50268, 50269, 50270, 50271};
    int[]    BOSS_DOORS           = {50350, 50351, 50352, 53950};
    int[]    FLAMMABLE_DEBRIS     = {50314, 50315, 50316};
    int[]    BROKEN_PULLY_DOOR    = {50299, 50300, 50301};
    int[]    BROKEN_KEY_DOOR      = {50308, 50309, 50310};
    int[]    DARK_SPIRIT          = {50332, 50333, 50334};
    int[]    WOODEN_BARRICADE     = {50317, 50318, 50319};
    int[]    RUNED_DOOR           = {50278, 50279, 50280};
    int[]    PILE_OF_ROCKS        = {50305, 50306, 50307};
    int[]    MAGICAL_BARRIER      = {50329, 50330, 50331};
    int[]    BARRED_DOOR          = {50272, 50273, 50274};
    int[]    LOCKED_DOOR          = {50287, 50288, 50289};
    int[]    PADLOCKED_DOOR       = {50293, 50294, 50295};
    int[]    RAMOKEE_EXILE        = {50326, 50327, 50328};
    int[]    VINE_COVERED_DOOR    = {50323, 50324, 50325};
    int[]    COLLAPSING_DOORFRAME = {50281, 50282, 50283};
    int[][]  SKILL_DOORS          = {FLAMMABLE_DEBRIS,
                                     BROKEN_PULLY_DOOR,
                                     BROKEN_KEY_DOOR,
                                     DARK_SPIRIT,
                                     WOODEN_BARRICADE,
                                     RUNED_DOOR,
                                     PILE_OF_ROCKS,
                                     MAGICAL_BARRIER,
                                     BARRED_DOOR,
                                     LOCKED_DOOR,
                                     PADLOCKED_DOOR,
                                     RAMOKEE_EXILE,
                                     VINE_COVERED_DOOR,
                                     COLLAPSING_DOORFRAME};
    int[][]  DOORS                = {ORANGE_DOORS,
                                     SILVER_DOORS,
                                     YELLOW_DOORS,
                                     GREEN_DOORS,
                                     BLUE_DOORS,
                                     PURPLE_DOORS,
                                     CRIMSON_DOORS,
                                     GOLD_DOORS,
                                     GUARDIAN_DOORS,
                                     STANDARD_DOORS,
                                     FLAMMABLE_DEBRIS,
                                     BROKEN_PULLY_DOOR,
                                     BROKEN_KEY_DOOR,
                                     DARK_SPIRIT,
                                     WOODEN_BARRICADE,
                                     RUNED_DOOR,
                                     PILE_OF_ROCKS,
                                     MAGICAL_BARRIER,
                                     BARRED_DOOR,
                                     LOCKED_DOOR,
                                     PADLOCKED_DOOR,
                                     RAMOKEE_EXILE,
                                     VINE_COVERED_DOOR,
                                     COLLAPSING_DOORFRAME};
    int[][]  KEY_DOORS            = {ORANGE_DOORS,
                                     SILVER_DOORS,
                                     YELLOW_DOORS,
                                     GREEN_DOORS,
                                     BLUE_DOORS,
                                     PURPLE_DOORS,
                                     CRIMSON_DOORS,
                                     GOLD_DOORS};

    //Items
    int HEIM_CRAB = 18159, RED_EYE = 18161, DUSK_EEL = 18163, FLAT_FISH = 18165, SHORT_FIN = 18167, WEB_SNIPPER = 18169, BOULDA_BASS = 18171, SALVE_EEL = 18173, BLUE_CRAB = 18175, CAVE_MORAY = 18177;
    int[] FOODS        = {HEIM_CRAB,
                          RED_EYE,
                          DUSK_EEL,
                          FLAT_FISH,
                          SHORT_FIN,
                          WEB_SNIPPER,
                          BOULDA_BASS,
                          SALVE_EEL,
                          BLUE_CRAB,
                          CAVE_MORAY};
    int[] NOOB_FOODS   = {HEIM_CRAB, RED_EYE};
    int[] GOOD_FOODS   = {DUSK_EEL, FLAT_FISH, SHORT_FIN, WEB_SNIPPER, BOULDA_BASS, SALVE_EEL, BLUE_CRAB, CAVE_MORAY,};
    int   KINSHIP_RING = 15707;
    int   COINS        = 18201;
    int   GGS          = 18829;

    //Loot
    int[][] LOOT_ITEMS      = {ORANGE_KEYS,
                               SILVER_KEYS,
                               YELLOW_KEYS,
                               GREEN_KEYS,
                               BLUE_KEYS,
                               PURPLE_KEYS,
                               CRIMSON_KEYS,
                               GOLD_KEYS,
                               GOOD_FOODS};
    int[]   COMPLEXITY_LOOT = {COINS, GGS};

    //Ladders
    int[] FINISHLADDERS   = {49695};
    int[] FINISHEDLADDERS = {49696, 49698, 49700};
    int[] EXITLADDERS     = {50604, 51156, 51704};

    //Misc
    int   ENTRANCE   = 48496;
    int   END_STAIRS = 50552;
    int   WALL       = 0x200000;
    int[] GGSPortal  = {53124, 53125, 53126};

    //Boss releated IDs
    int[]   BULWARK_AXE_ROCKS     = {49295, 49296};
    int[]   SKELETAL_TUNNELS      = {49286, 49287, 49288};
    int[]   SNOW                  = {49334};
    int     LAKHRANZ_PILLERS      = 51279;
    int[]   STOMP_LODESTONE_RED   = {49274, 49275};
    int[]   STOMP_LODESTONE_GREEN = {49276, 49277};
    int[]   STOMP_LODESTONE_BLUE  = {49278, 49279};
    int[][] STOMP_LODESTONES      = {STOMP_LODESTONE_RED, STOMP_LODESTONE_GREEN, STOMP_LODESTONE_BLUE};
    int     STOMP_CRYSTAL_RED     = 15752;
    int     STOMP_CRYSTAL_GREEN   = 15751;
    int     STOMP_CRYSTAL_BLUE    = 15750;
    int[]   STOMP_CRYSTALS        = {STOMP_CRYSTAL_RED, STOMP_CRYSTAL_BLUE, STOMP_CRYSTAL_GREEN};
    int[]   STOMP_DEBRIS_SHADOWS  = {49269};

    //Npc names
    String SMUGGLER = "Smuggler";

    //PaintComponent IDs
    int STORE_COMPONENT     = 956;
    int STORE_SUB_COMPONENT = 24;

    int MAX_FLOOR = 29;

    //PUZZLE SHIT
    final int POLTERGEIST = 11245, UNHAPPY_GHOST = 11246, CARVE_BLOCK = 17415, FISHING_FERRET = 11007, HUNTER_FERRET = 11010, POND_SKATER = 12089, MAZE_BARRIER = 49341;
    final int[]   SUSPICOUS_GROOVES_ROW_1 = {49390,
                                             49392,
                                             49394,
                                             49396,
                                             49414,
                                             49416,
                                             49418,
                                             49420,
                                             49438,
                                             49440,
                                             49442,
                                             49444,
                                             54336,
                                             54338,
                                             54340,
                                             54342};
    final int[]   SUSPICOUS_GROOVES_ROW_2 = {49398,
                                             49400,
                                             49402,
                                             49404,
                                             49422,
                                             49424,
                                             49426,
                                             49428,
                                             49446,
                                             49448,
                                             49450,
                                             49452,
                                             54344,
                                             54346,
                                             54348,
                                             54350};
    final int[]   SUSPICOUS_GROOVES_ROW_3 = {49406,
                                             49408,
                                             49410,
                                             49412,
                                             49430,
                                             49432,
                                             49434,
                                             49436,
                                             49454,
                                             49456,
                                             49458,
                                             49460,
                                             54352,
                                             54354,
                                             54356,
                                             54358};
    final int[][] SUSPICOUS_GROOVES       = {SUSPICOUS_GROOVES_ROW_1, SUSPICOUS_GROOVES_ROW_2, SUSPICOUS_GROOVES_ROW_3};
    int[] BARCRATE_CAMP  = {49528, 49529, 49530};
    int[] LOGCRATE_CAMP  = {49534, 49535, 49536};
    int[] FISHCRATE_CAMP = {49522, 49523, 49524};
    final int[] GHOSTS                   = {10981, 10983, 10985, 10987, 10989, 10991, 10993, 10995, 10997, 10999};
    final int[] FTL_STATUES              = {10966, 10967, 10968, 12114};
    final int[] LEVERS                   = {49381, 49382, 49383, 54333};
    final int[] SLIDERS                  = {12125,
                                            12126,
                                            12127,
                                            12128,
                                            12129,
                                            12130,
                                            12131,
                                            12132,
                                            12133,
                                            12134,
                                            12135,
                                            12136,
                                            12137,
                                            12138,
                                            12139,
                                            12140,
                                            12141,
                                            12142,
                                            12143,
                                            12144,
                                            12145,
                                            12146,
                                            12147,
                                            12148,
                                            12149,
                                            12150,
                                            12151,
                                            12152,
                                            12153,
                                            12154,
                                            12155,
                                            12156};
    final int[] COLORED_FERRETS          = {12167, 12165, 12171, 12169, 12173};
    final int[] ALL_UNWINCHED_BRIDGES    = {39912, 39913, 39920, 39921, 39929, 39930, 39931};
    final int[] WINCHED_BRIDGES          = {39915, 39924, 39935, 54643};
    final int[] REPAIRED_BRIDGES         = {54190, 54191, 54192, 54193};
    final int[] FINISHED_STATUE_BRIDGES  = {40010, 40011, 54614};
    final int[] FINISHED_BRIDGES         = {54013, 54014, 54015, 54016};
    final int[] UNGRAPPLED_CHASMS        = {54237, 54238, 54239, 54240};
    final int[] GRAPPLED_CHASMS          = {54241, 54242, 54243, 54244, 54245, 54246, 54247, 54248};
    final int[] ICY_PRESSURE_PADS        = {49320, 49321, 49322, 49323};
    final int[] SMALL_DEBRIS             = {49615, 49616, 49617, 49618};
    final int[] HEADING_STATUES          = {10942,
                                            10943,
                                            10944,
                                            10945,
                                            10946,
                                            10947,
                                            10948,
                                            10949,
                                            10950,
                                            10951,
                                            10952,
                                            10953,
                                            12117,
                                            12118,
                                            12119,
                                            12120};
    final int[] SLIDING_STATUES          = {10954,
                                            10955,
                                            10956,
                                            10957,
                                            10958,
                                            10959,
                                            10960,
                                            10961,
                                            10962,
                                            10963,
                                            10964,
                                            10965,
                                            12121,
                                            12122,
                                            12123,
                                            12124};
    final int[] THREE_STATUES            = {11036,
                                            11037,
                                            11038,
                                            11039,
                                            11040,
                                            11041,
                                            11042,
                                            11043,
                                            11044,
                                            12094,
                                            12095,
                                            12096};
    final int[] TEN_STATUES              = {11027, 11028, 11029, 11030, 11031, 11032, 11033, 11034, 11035};
    final int[] LARGE_CRYSTALS           = {49507, 49508, 49509, 49510, 49511, 49512, 54261};
    final int[] TRASH                    = {49315, 49316, 49317, 49318};
    final int[] POWER_LODESTONES         = {49570, 49571, 49572, 54235};
    final int[] RECESS_FOUNTAINS         = {54502, 54544, 54621};
    final int[] CLOSED_SARCOPHAGUS       = {54078, 54079, 54080, 54081};
    final int[] UNREPAIRED_STATUE_BRIDGE = {39991, 40002, 40003};
    final int[] BARREL_PIPES             = {49688, 49690, 54287};
    final int[] AGILITY_DOORS            = {49693};
    final int[] DRY_BLOOD_FOUNTAIN       = {54110, 54111, 54112, 54113};
    final int[] FREMMY_CRATES            = {49522, 49523, 49524, 49528, 49529, 49530, 49534, 49535, 49536};
    final int[] CENTER_FLOWERS           = {35507, 35520, 35523, 35525, 35562, 35568, 35569, 35576};
    final int[] BOOKCASES                = {54419, 54420, 54421, 54422};
    final int[] ACTIVE_PADS              = {52206, 52207, 54282};
    final int[] AGILITY_GROOVES          = {49665, 49666, 49667, 54311};
    final int[] WALL_TRAPS               = {49656, 49657, 49658, 54308};

    final int[] BLACKLISTED_FLAGS = {35655688,
                                     1876420,
                                     10486786,
                                     1075970304,
                                     6291969,
                                     539033728,
                                     270565440,
                                     136331296,
                                     2097152,
                                     6921224,
                                     1210204448};
    final int   DOOR_FLAG         = 2097152;

    final int TELEPORT_FAILSAFE = 50;

    Filter<Npc> INROOM_ENEMY_FILTER = new Filter<Npc>()
    {

        public boolean accept(Npc npc) {
            return npc != null && MyPlayer.currentRoom().contains(npc);
        }
    };
}