package nz.artedungeon.misc;

public interface GameConstants
{
    // Monsters
    int[] ASTEA_FROSTWEBS = {9972, 9973, 9974, 9975, 9976, 9976, 9977, 9978, 9979, 9980, 9981, 9982};
    int[] ICY_BONES = {10042, 10043, 10044, 10045};
    int[] ICE_FIENDS = {9913, 9914, 9915};
    int[] PLANE_FREEZERS = {9929, 9930};
    int[] BLOOD_CHILLERS = {10024, 1025, 1026};
    int[][] BOSSES = {ASTEA_FROSTWEBS, ICE_FIENDS, ICY_BONES, PLANE_FREEZERS, BLOOD_CHILLERS};
    int[] NONARGRESSIVE_NPCS = {11226,
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
    String[] BOSS_NAMES = {"Gluttonous behemoth", "Astea Frostweb", "Icy Bones", "Luminescent Icefiend",};
    String GLUTTONOUS = "Gluttonous behemoth";
    String ICE_FIEND = "Luminescent Icefiend";
    String ASTEA_FROSTWEB = "Astea Frostweb";
    // Keys
    int[] ORANGE_KEYS = {18202, 18204, 18206, 18208, 18210, 18212, 18214, 18216};
    int[] SILVER_KEYS = {18218, 18220, 18222, 18224, 18226, 18228, 18230, 18232};
    int[] YELLOW_KEYS = {18234, 18236, 18238, 18240, 18242, 18244, 18246, 18248};
    int[] GREEN_KEYS = {18250, 18252, 18254, 18256, 18258, 18260, 18262, 18264};
    int[] BLUE_KEYS = {18266, 18268, 18270, 18272, 18274, 18276, 18278, 18280};
    int[] PURPLE_KEYS = {18282, 18284, 18286, 18288, 18290, 18292, 18294, 18296};
    int[] CRIMSON_KEYS = {18298, 18300, 18302, 18304, 18306, 18308, 18310, 18312};
    int[] GOLD_KEYS = {18314, 18316, 18318, 18320, 18322, 18324, 18326, 18328};
    int[][] KEYS = {ORANGE_KEYS, SILVER_KEYS, YELLOW_KEYS, GREEN_KEYS, BLUE_KEYS, PURPLE_KEYS, CRIMSON_KEYS, GOLD_KEYS};
    // Doors
    int[] GUARDIAN_DOORS = {50346, 50347, 50348};
    int[] STANDARD_DOORS = {49306, 49335, 49336, 49337, 49338, 49375, 49625, 50342, 50343, 50344, 50311, 50275};
    int[][] BASIC_DOORS = {GUARDIAN_DOORS, STANDARD_DOORS};
    int[] ORANGE_DOORS = {50208, 50209, 50210, 50211, 50212, 50213, 50214, 50215};
    int[] SILVER_DOORS = {50216, 50217, 50218, 50219, 50220, 50221, 50222, 50223};
    int[] YELLOW_DOORS = {50224, 50225, 50226, 50227, 50228, 50229, 50230, 50231};
    int[] GREEN_DOORS = {50232, 50233, 50234, 50235, 50236, 50237, 50238, 50239};
    int[] BLUE_DOORS = {50240, 50241, 50242, 50243, 50244, 50245, 50246, 50247};
    int[] PURPLE_DOORS = {50248, 50249, 50250, 50251, 50252, 50253, 50254, 50255};
    int[] CRIMSON_DOORS = {50256, 50257, 50258, 50259, 50260, 50261, 50262, 50263};
    int[] GOLD_DOORS = {50264, 50265, 50266, 50267, 50268, 50269, 50270, 50271};
    int[] BOSS_DOORS = {50350, 50351, 50352};
    int[] FLAMMABLE_DEBRIS = {50314, 50315, 50316};
    int[] BROKEN_PULLY_DOOR = {50299, 50300, 50301};
    int[] BROKEN_KEY_DOOR = {50308, 50309, 50310};
    int[] DARK_SPIRIT = {50332, 50333, 50334};
    int[] WOODEN_BARRICADE = {50317, 50318, 50319};
    int[] RUNED_DOOR = {50278, 50279, 50280};
    int[] PILE_OF_ROCKS = {50305, 50306, 50307};
    int[] MAGICAL_BARRIER = {50329, 50330, 50331};
    int[] BARRED_DOOR = {50272, 50273, 50274};
    int[] LOCKED_DOOR = {50287, 50288, 50289};
    int[] PADLOCKED_DOOR = {50293, 50294, 50295};
    int[] RAMOKEE_EXILE = {50326, 50327, 50328};
    int[] VINE_COVERED_DOOR = {50323, 50324, 50325};
    int[] COLLAPSING_DOORFRAME = {50281, 50282, 50283};
    int[][] SKILL_DOORS = {FLAMMABLE_DEBRIS,
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
    int[][] DOORS = {ORANGE_DOORS,
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
    int[][] KEY_DOORS = {ORANGE_DOORS,
                         SILVER_DOORS,
                         YELLOW_DOORS,
                         GREEN_DOORS,
                         BLUE_DOORS,
                         PURPLE_DOORS,
                         CRIMSON_DOORS,
                         GOLD_DOORS};

    //Items
    int HEIM_CRAB = 18159, RED_EYE = 18161, DUSK_EEL = 18163, FLAT_FISH = 18165,
            SHORT_FIN = 18167, WEB_SNIPPER = 18169, BOULDA_BASS = 18171,
            SALVE_EEL = 18173, BLUE_CRAB = 18175, CAVE_MORAY = 18177;
    int[] FOODS = {HEIM_CRAB, RED_EYE,
                   DUSK_EEL, FLAT_FISH, SHORT_FIN, WEB_SNIPPER, BOULDA_BASS, SALVE_EEL, BLUE_CRAB, CAVE_MORAY};
    int[] NOOB_FOODS = {HEIM_CRAB, RED_EYE};
    int[] GOOD_FOODS = {DUSK_EEL, FLAT_FISH, SHORT_FIN, WEB_SNIPPER, BOULDA_BASS, SALVE_EEL, BLUE_CRAB, CAVE_MORAY,};
    int KINSHIP_RING = 15707;
    int COINS = 18201;
    int GGS = 18829;

    //Loot
    int[][] LOOT_ITEMS = {ORANGE_KEYS,
                          SILVER_KEYS,
                          YELLOW_KEYS,
                          GREEN_KEYS,
                          BLUE_KEYS,
                          PURPLE_KEYS,
                          CRIMSON_KEYS,
                          GOLD_KEYS,
                          GOOD_FOODS};
    int[] COMPLEXITY_LOOT = {COINS, GGS};

    //Ladders
    int[] FINISHLADDERS = {49695};
    int[] FINISHEDLADDERS = {49696, 49698, 49700};
    int[] EXITLADDERS = {50604, 51156, 51704};

    //Misc
    int ENTRANCE = 48496;
    int END_STAIRS = 50552;
    int WALL = 0x200000;
    int[] GGSPortal = {53124, 53125, 53126};

    //Boss releated IDs
    int[] BULWARK_AXE_ROCKS = {49295, 49296};
    int[] SKELETAL_TUNNELS = {49286, 49287, 49288};
    int[] SNOW = {49334};
    int LAKHRANZ_PILLERS = 51279;
    int[] STOMP_LODESTONE_RED = {49274, 49275};
    int[] STOMP_LODESTONE_GREEN = {49276, 49277};
    int[] STOMP_LODESTONE_BLUE = {49278, 49279};
    int[][] STOMP_LODESTONES = {STOMP_LODESTONE_RED, STOMP_LODESTONE_GREEN, STOMP_LODESTONE_BLUE};
    int STOMP_CRYSTAL_RED = 15752;
    int STOMP_CRYSTAL_GREEN = 15751;
    int STOMP_CRYSTAL_BLUE = 15750;
    int[] STOMP_CRYSTALS = {STOMP_CRYSTAL_RED, STOMP_CRYSTAL_BLUE, STOMP_CRYSTAL_GREEN};
    int[] STOMP_DEBRIS_SHADOWS = {49269};

    //Npc names
    String SMUGGLER = "Smuggler";

    //PaintComponent IDs
    int STORE_COMPONENT = 956;
    int STORE_SUB_COMPONENT = 24;
}