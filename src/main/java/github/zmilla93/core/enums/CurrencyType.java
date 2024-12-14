package github.zmilla93.core.enums;

import github.zmilla93.core.poe.Game;
import github.zmilla93.core.utility.ZUtil;
import github.zmilla93.gui.buttons.IIcon;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Acts as a reference to a specific currency type in either game.
 * Handles converting untranslated currency names and tags into icons, and can be used as a savable reference to a currency type.
 */
public class CurrencyType implements IIcon {

    /// Static mapping of currency names in all languages to their respective CurrencyTypes.
    private static final HashMap<String, CurrencyType> currencyMapPoe1 = new HashMap<>();
    private static final HashMap<String, CurrencyType> currencyMapPoe2 = new HashMap<>();
    private static final HashMap<String, CurrencyType> currencyNameMap = new HashMap<>();
    private static final ArrayList<CurrencyType> commonCurrencyTypes = new ArrayList<>();

    /// ID is the only thing saved. It is the english name of the currency, which is also the file name.
    public final String ID;
    // FIXME: Words don't actually need to be stored unless some form of translation is added,
    //        which isn't relevant unless info is added from the trade site for items without icons.
    public transient final String[] words;
    private transient String path;

    /// Static class, use CurrencyType.getCurrencyType().
    private CurrencyType(String line) {
        words = line.split(",");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].trim();
        }
        ID = words[0];
    }

    /**
     * Converts a currency name in any language ("Orb of Alchemy", "Orbe d'alchimie"), or a currency tag ("alch"), into a CurrencyType.
     */
    public static @Nullable CurrencyType getCurrencyType(String currency, Game game) {
        if (game.isPoe1()) return currencyMapPoe1.get(currency);
        else return currencyMapPoe2.get(currency);
    }

    @Deprecated
    // FIXME : Should add a getCurrencyId() function to replace contexts where you only care about ID and not icon data, like when saving.
    public static CurrencyType getCurrencyType(String currency) {
        if (currencyNameMap.containsKey(currency)) {
            return currencyNameMap.get(currency);
        }
        return null;
    }

    /**
     * Adds aliases for partial words
     */
    private static void addCustomTags() {
        // TODO : Add all common tags
        addAlias("Orb of Alchemy", "alch");
        addAlias("Chaos Orb", "chaos");
        addAlias("Exalted Orb", "exalted");
        addAlias("Divine Orb", "divine");
    }

    /**
     * Builds a hashmap of all currency types and translations using currency.txt
     */
    public static void initIconList() {
        // FIXME : Temp remap
        createCurrencyMaps();
        currencyNameMap.clear();
        try {
            BufferedReader reader = ZUtil.getBufferedReader("/text/currency.txt", true);
            while (reader.ready()) {
                String line = reader.readLine().trim();
                if (line.startsWith("//")) continue;
                if (line.isEmpty()) continue;
                addCSV(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addCustomTags();
        buildCommonCurrencyList();
    }

    public static void createCurrencyMaps() {
        createCurrencyMap(currencyMapPoe1, Paths.get("/poe1/translations.csv"));
        createCurrencyMap(currencyMapPoe2, Paths.get("/poe2/translations.csv"));
    }

    private static void createCurrencyMap(HashMap<String, CurrencyType> map, Path csvPath) {
        map.clear();
        try {
            BufferedReader reader = ZUtil.getBufferedReader(csvPath, true);
            while (reader.ready()) {
                String line = reader.readLine().trim();
                if (line.startsWith("//")) continue;
                if (line.isEmpty()) continue;
                parseCsvLineIntoMap(map, line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create currency map for path: " + csvPath);
        }
    }

    private static void parseCsvLineIntoMap(HashMap<String, CurrencyType> map, String line) {
        CurrencyType currency = new CurrencyType(line);
        for (String word : currency.words) {
            map.put(word, currency);
        }
    }

    private static void addCSV(String line) {
        CurrencyType currency = new CurrencyType(line);
        for (String word : currency.words) {
            currencyNameMap.put(word, currency);
        }
    }

    private static void buildCommonCurrencyList() {
        commonCurrencyTypes.clear();
        /// These are the currency types that appear when you list an item in a stash tab
        String[] commonCurrencyNames = new String[]{
                "Blessed Orb",
                "Cartographer's Chisel",
                "Chaos Orb",
                "Chromatic Orb",
                "Divine Orb",
                "Engineer's Orb",
                "Exalted Orb",
                "Gemcutter's Prism",
                "Glassblower's Bauble",
                "Jeweller's Orb",
                "Mirror of Kalandra",
                "Orb of Alchemy",
                "Orb of Alteration",
                "Orb of Chance",
                "Orb of Fusing",
                "Orb of Regret",
                "Orb of Scouring",
                "Orb of Transmutation",
                "Regal Orb",
                "Vaal Orb",
        };
        for (String currencyName : commonCurrencyNames) {
            commonCurrencyTypes.add(getCurrencyType(currencyName));
        }

//        commonCurrencyTypes.add(getCurrencyType("Blessed Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Cartographer's Chisel"));
//        commonCurrencyTypes.add(getCurrencyType("Chaos Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Chromatic Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Divine Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Engineer's Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Exalted Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Gemcutter's Prism"));
//        commonCurrencyTypes.add(getCurrencyType("Glassblower's Bauble"));
//        commonCurrencyTypes.add(getCurrencyType("Jeweller's Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Mirror of Kalandra"));
//        commonCurrencyTypes.add(getCurrencyType("Orb of Alchemy"));
//        commonCurrencyTypes.add(getCurrencyType("Orb of Alteration"));
//        commonCurrencyTypes.add(getCurrencyType("Orb of Chance"));
//        commonCurrencyTypes.add(getCurrencyType("Orb of Fusing"));
//        commonCurrencyTypes.add(getCurrencyType("Orb of Regret"));
//        commonCurrencyTypes.add(getCurrencyType("Orb of Scouring"));
//        commonCurrencyTypes.add(getCurrencyType("Orb of Transmutation"));
//        commonCurrencyTypes.add(getCurrencyType("Regal Orb"));
//        commonCurrencyTypes.add(getCurrencyType("Vaal Orb"));
    }

    public static void addAlias(String existingTag, String alias) {
        CurrencyType image = getCurrencyType(existingTag);
        if (image == null) return;
        currencyNameMap.put(alias, image);
    }

    public static String getIconPath(String currency) {
        if (currencyNameMap.containsKey(currency)) {
            return currencyNameMap.get(currency).path();
        }
        System.err.println("Currency not found: " + currency);
        return null;
    }

    public static ArrayList<CurrencyType> getCommonCurrencyTypes() {
        return commonCurrencyTypes;
    }

    @Override
    public String path() {
        if (path == null) {
            String fileName = words[0].replaceAll(" ", "_").replaceAll(":", "") + ".png";
            path = "/currency/" + fileName;
        }
        return path;
    }

    @Override
    public String toString() {
        return words[0];
    }

}
