package Application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles the saving and loading of game data.
 * <p>
 * The SaveLoad class is responsible for persisting and retrieving game data to and from JSON files.
 * It supports saving and loading data for the Duck, Inventory, and GameSettingsAndStats objects. Data for
 * game settings is stored in a separate file ("settings.json") while all other save data is stored in "allData.json".
 * </p>
 */
public class SaveLoad {

    /**
     * Saves all game data (Duck, Inventory, and Settings) to a JSON file.
     * <p>
     * This method creates a JSON object that represents the current state of the duck and inventory.
     * It loads any existing save data from "allData.json" if present, updates it with the current save slot,
     * and writes the result back to the file. Additionally, it saves the game settings separately.
     * </p>
     *
     * @param duck      the Duck object representing the current duck state
     * @param inventory the Inventory object representing the current inventory state
     * @param settings  the GameSettingsAndStats object representing the current game settings
     */
    public void saveAllData(Duck duck, Inventory inventory, GameSettingsAndStats settings) {
        // Create an ObjectMapper for JSON operations.
        ObjectMapper mapper = new ObjectMapper();
        // Create a JSON object to hold the current save slot data.
        JSONObject saveSlot = new JSONObject();

        // Save duck data if the duck object is not null.
        if (duck != null) {
            // Create a JSON object to store duck attributes.
            JSONObject duckJson = new JSONObject();
            duckJson.put("name", duck.getName());
            duckJson.put("health", duck.getHealth());
            duckJson.put("tiredness", duck.getTiredness());
            duckJson.put("hunger", duck.getHunger());
            duckJson.put("happiness", duck.getHappiness());
            duckJson.put("isDead", duck.isDead());
            duckJson.put("colour", duck.getColour());
            duckJson.put("day", duck.getDay());
            duckJson.put("coins", duck.getCoins());
            // Add the duck data to the save slot.
            saveSlot.put("Duck", duckJson);
        }

        // Save inventory data.
        JSONObject invJson = new JSONObject();
        invJson.put("Food", inventory.getFood());
        invJson.put("Toy", inventory.getToy());
        invJson.put("Night Cap", inventory.getNightCap());
        // Add the inventory data to the save slot.
        saveSlot.put("Inventory", invJson);

        // Load existing save data from "allData.json" if the file exists.
        JSONObject root = new JSONObject();
        try {
            File file = new File("allData.json");
            if (file.exists()) {
                JsonNode existing = mapper.readTree(file);
                root = new JSONObject(existing.toPrettyString());
            }
        } catch (Exception e) {
            // Print the stack trace if an error occurs during file reading.
            e.printStackTrace();
        }

        // Save the current save slot under the duck's name.
        root.put(duck.getName(), saveSlot);

        // Write the updated save data back to "allData.json".
        try (FileWriter writer = new FileWriter("allData.json")) {
            writer.write(root.toString(4)); // Use an indentation of 4 for pretty printing.
        } catch (IOException e) {
            // Print the stack trace if an error occurs during file writing.
            e.printStackTrace();
        }

        // Save the game settings separately.
        saveSettings(settings);
    }

    /**
     * Saves the game settings to a separate JSON file ("settings.json").
     * <p>
     * This method creates a JSON object with game settings (volume, level, time played, last played, time limit,
     * and parental lock status) and writes it to the file "settings.json".
     * </p>
     *
     * @param settings the GameSettingsAndStats object containing the current game settings
     */
    public void saveSettings(GameSettingsAndStats settings) {
        // Create a JSON object to hold settings data.
        JSONObject settingsJson = new JSONObject();
        settingsJson.put("Volume", settings.getVolume());
        settingsJson.put("Level", settings.getLevels());
        settingsJson.put("Time Played", settings.timePlayed());
        settingsJson.put("Last Played", settings.lastPlayed());
        settingsJson.put("Time Limit", settings.getTimeLimit());
        settingsJson.put("Parental Lock", settings.isParentalLockActive());
        settingsJson.put("Number of Sessions", settings.getSessions());

        // Write the settings data to "settings.json".
        try (FileWriter writer = new FileWriter("settings.json")) {
            writer.write(settingsJson.toString(4)); // Pretty print JSON with an indentation of 4.
        } catch (IOException e) {
            // Print the stack trace if an error occurs during file writing.
            e.printStackTrace();
        }
    }

    /**
     * Loads all saved game data from "allData.json" and returns them in a HashMap.
     * <p>
     * This method reads the "allData.json" file and iterates through each save slot keyed by the duck's name.
     * For each entry, it reconstructs the Duck and Inventory objects and also loads the shared game settings.
     * The results are stored in a HashMap where the key is the duck's name and the value is an array containing
     * the Duck, Inventory, and GameSettingsAndStats objects.
     * </p>
     *
     * @return a HashMap mapping duck names to an Object array containing {Duck, Inventory, GameSettingsAndStats}
     */
    public HashMap<String, Object[]> loadAllSaves() {
        // Create a HashMap to store the loaded save data.
        HashMap<String, Object[]> savesMap = new HashMap<>();
        File file = new File("allData.json");

        // Return an empty map if the save file does not exist.
        if (!file.exists()) return savesMap;

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Read the JSON data from the file.
            JsonNode root = mapper.readTree(file);

            // Iterate over each save slot (duck name) in the JSON.
            for (String duckName : (Iterable<String>) root::fieldNames) {
                JsonNode saveNode = root.get(duckName);
                // Skip entries that don't have both "Duck" and "Inventory" data.
                if (saveNode == null || !saveNode.has("Duck") || !saveNode.has("Inventory")) continue;

                // Extract the JSON nodes for duck and inventory.
                JsonNode duckJson = saveNode.get("Duck");
                JsonNode invJson = saveNode.get("Inventory");

                // Parse Duck data and create a new Duck object.
                Duck duck = new Duck(duckJson.get("name").asText());
                duck.setHealth(-(10 - duckJson.get("health").asInt()));
                duck.setTiredness(duckJson.get("tiredness").asInt());
                duck.setHunger(-(10 - duckJson.get("hunger").asInt()));
                duck.setHappiness(-(100 - duckJson.get("happiness").asInt()));
                duck.setDead(duckJson.get("isDead").asBoolean());
                duck.setColour(duckJson.get("colour").asText());
                duck.setDay(duckJson.get("day").asInt());
                duck.setCoins(duckJson.get("coins").asInt());

                // Parse Inventory data and create a new Inventory object.
                Inventory inventory = new Inventory();
                inventory.setFood(invJson.get("Food").asInt());
                inventory.setToy(invJson.get("Toy").asInt());
                inventory.setNightCap(invJson.get("Night Cap").asInt());

                // Load the shared game settings.
                GameSettingsAndStats settings = loadSettings();

                // Put the loaded objects into the HashMap using the duck's name as the key.
                savesMap.put(duckName, new Object[]{duck, inventory, settings});
            }
        } catch (Exception e) {
            // Print the stack trace if an error occurs during loading.
            e.printStackTrace();
        }

        return savesMap;
    }

    /**
     * Loads and returns the first available Duck object from "allData.json".
     * <p>
     * The method reads the "allData.json" file and iterates over the saves. For the first save entry found
     * with a "Duck" section, it reconstructs and returns a Duck object.
     * </p>
     *
     * @return the loaded Duck object, or null if no valid save is found or an error occurs
     */
    public Duck loadDuck() {
        try {
            // Read the JSON data from "allData.json".
            JsonNode root = new ObjectMapper().readTree(new File("allData.json"));
            // Iterate over each save entry.
            for (String duckName : (Iterable<String>) root::fieldNames) {
                JsonNode duckJson = root.get(duckName).get("Duck");
                if (duckJson != null) {
                    // Create a new Duck object using the saved name.
                    Duck duck = new Duck(duckJson.get("name").asText());
                    duck.setHealth(-(10 - duckJson.get("health").asInt()));
                    duck.setTiredness(duckJson.get("tiredness").asInt());
                    duck.setHunger(-(10 - duckJson.get("hunger").asInt()));
                    duck.setHappiness(-(100 - duckJson.get("happiness").asInt()));
                    duck.setDead(duckJson.get("isDead").asBoolean());
                    duck.setColour(duckJson.get("colour").asText());
                    duck.setDay(duckJson.get("day").asInt());
                    duck.setCoins(duckJson.get("coins").asInt());
                    // Return the first successfully loaded Duck.
                    return duck;
                }
            }
        } catch (IOException e) {
            // Print the stack trace if an error occurs while reading the file.
            e.printStackTrace();
        }
        return null; // Return null if no Duck is found.
    }

    /**
     * Loads and returns the first available Inventory object from "allData.json".
     * <p>
     * The method reads the "allData.json" file and iterates over the save entries. For the first save entry
     * found with an "Inventory" section, it reconstructs and returns an Inventory object.
     * </p>
     *
     * @return the loaded Inventory object, or null if no valid save is found or an error occurs
     */
    public Inventory loadInv() {
        try {
            // Read the JSON data from "allData.json".
            JsonNode root = new ObjectMapper().readTree(new File("allData.json"));
            // Iterate over each save entry.
            for (String duckName : (Iterable<String>) root::fieldNames) {
                JsonNode invJson = root.get(duckName).get("Inventory");
                if (invJson != null) {
                    // Create a new Inventory object and update it with saved values.
                    Inventory inventory = new Inventory();
                    inventory.setFood(invJson.get("Food").asInt());
                    inventory.setToy(invJson.get("Toy").asInt());
                    inventory.setNightCap(invJson.get("Night Cap").asInt());
                    // Return the first successfully loaded Inventory.
                    return inventory;
                }
            }
        } catch (IOException e) {
            // Print the stack trace if an error occurs during file reading.
            e.printStackTrace();
        }
        return null; // Return null if no Inventory is found.
    }

    /**
     * Loads and returns the game settings from "settings.json".
     * <p>
     * This method reads the "settings.json" file, creates a new GameSettingsAndStats object, updates its properties
     * with the values from the JSON, and returns it. If an error occurs, a new object with default settings is returned.
     * </p>
     *
     * @return the loaded GameSettingsAndStats object, or a new instance with default settings if loading fails
     */
    public GameSettingsAndStats loadSettings() {
        // Create an ObjectMapper for JSON operations.
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Read the JSON data from "settings.json".
            JsonNode json = mapper.readTree(new File("settings.json"));
            // Create a new GameSettingsAndStats object.
            GameSettingsAndStats settings = new GameSettingsAndStats();
            // Update settings properties from the JSON data.
            settings.setVolume(json.get("Volume").asInt());
            settings.setLevel(json.get("Level").asInt());
            settings.setTimePLayed(json.get("Time Played").asInt());
            settings.setLastPlayed(json.get("Last Played").asText());
            settings.setTimeLimit(json.get("Time Limit").asInt());
            settings.setParentalLockActive(json.get("Parental Lock").asBoolean());
            settings.updateNumSessions(json.get("Number of Sessions").asInt());
            // Return the updated settings object.
            return settings;
        } catch (IOException e) {
            // Print the stack trace if an error occurs during loading.
            e.printStackTrace();
        }
        // Fallback: return a new settings object with default values.
        return new GameSettingsAndStats();
    }
}
