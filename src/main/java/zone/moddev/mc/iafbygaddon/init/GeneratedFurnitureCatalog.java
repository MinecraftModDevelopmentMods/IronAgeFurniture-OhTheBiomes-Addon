package zone.moddev.mc.iafbygaddon.init;

/** Generated, immutable description of the supported BYG wood families. */
public final class GeneratedFurnitureCatalog {
    public static final Wood[] WOODS = new Wood[] {
        new Wood("aspen", "aspen_planks", "aspen_log"),
        new Wood("baobab", "baobabplanks", "baobablog"),
        new Wood("blue_enchanted", "enchantedplanks", "enchantedlog"),
        new Wood("cherry", "cherryplanks", "cherrylog"),
        new Wood("cika", "cikaplanks", "cikalog"),
        new Wood("cypress", "cypressplanks", "cypresslog"),
        new Wood("ebony", "ebonyplanks", "ebonylog"),
        new Wood("fir", "firplanks", "firlog"),
        new Wood("frozen_oak", "frozenoakplanks", "frozenoaklog"),
        new Wood("great_oak", "greatoakplanks", "greatoaklog"),
        new Wood("green_enchanted", "greenenchantedplanks", "greenenchantedlog"),
        new Wood("hawthorn", "hawthornplanks", "hawthornlog"),
        new Wood("holly", "hollyplanks", "hollylog"),
        new Wood("ironwood", "ironwoodplanks", "ironwoodlog"),
        new Wood("jacaranda", "jacarandaplanks", "jacarandalog"),
        new Wood("mahogany", "mahogany_planks", "mahogany_log"),
        new Wood("mangrove", "mangroveplanks", "mangrovelog"),
        new Wood("maple", "mapleplanks", "maplelog"),
        new Wood("palm", "palm_planks", "palm_log"),
        new Wood("pine", "pineplanks", "pinelog"),
        new Wood("rainbow_eucalyptus", "rainboweucalyptusplanks", "rainboweucalyptuslog"),
        new Wood("redwood", "redwoodplanks", "redwoodlog"),
        new Wood("rowan", "rowanplanks", "rowanlog"),
        new Wood("skyris", "skyrisplanks", "skyrislog"),
        new Wood("willow", "willowplanks", "willowlog"),
        new Wood("witch_hazel", "witchhazelplanks", "witchhazellog"),
        new Wood("zelkova", "zelkovaplanks", "zelkovalog")
    };

    private GeneratedFurnitureCatalog() { throw new IllegalAccessError("This class cannot be instantiated"); }

    public static final class Wood {
        public final String id;
        public final String planks;
        public final String log;
        private Wood(String id, String planks, String log) {
            this.id = id; this.planks = planks; this.log = log;
        }
    }
}
