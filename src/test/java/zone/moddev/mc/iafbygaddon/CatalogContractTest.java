package zone.moddev.mc.iafbygaddon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;

/** Checks the committed data without booting Minecraft or rewriting the catalog. */
public class CatalogContractTest {
    private static final Path ROOT = Paths.get("src/main/resources/assets/iafbygaddon");
    private static List<Path> children(Path directory) throws Exception {
        try (Stream<Path> files = Files.list(directory)) {
            return files.collect(Collectors.toList());
        }
    }
    private static JsonObject json(Path path) throws Exception {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return new JsonParser().parse(reader).getAsJsonObject();
        }
    }
    @Test public void furnitureInventoriesAreComplete() throws Exception {
        assertEquals(432, children(ROOT.resolve("blockstates")).size());
        assertEquals(1458, children(ROOT.resolve("models/block")).size());
        assertEquals(405, children(ROOT.resolve("models/item")).size());
        assertEquals(2943, children(ROOT.resolve("recipes")).size());
        assertEquals(2943, children(ROOT.resolve("advancements/recipes")).size());
        assertEquals(17, children(ROOT.resolve("lang")).size());
    }
    @Test public void allVanillaBedColoursHaveExplicitMappings() throws Exception {
        String[] colours = {"red","orange","magenta","light_blue","yellow","lime","pink","gray",
                "light_gray","cyan","purple","blue","brown","green","white","black"};
        JsonObject catalog = json(Paths.get("gradle/furniture-catalog.json"));
        for (com.google.gson.JsonElement entry : catalog.getAsJsonArray("woods")) {
            String wood = entry.getAsJsonObject().get("wood").getAsString();
            for (int meta = 0; meta < colours.length; ++meta) {
                String id = "bed_wood_foot_byg_" + wood + (meta == 0 ? "" : "_" + colours[meta]);
                JsonObject recipe = json(ROOT.resolve("recipes/" + id + ".json"));
                assertEquals(meta == 0 ? 14 : meta == 14 ? 0 : meta,
                        recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject().get("data").getAsInt());
                assertEquals(meta, recipe.getAsJsonObject("result").get("data").getAsInt());
                assertEquals("{Color:\"" + colours[meta] + "\"}", recipe.getAsJsonObject("result").get("nbt").getAsString());
            }
        }
    }
    @Test public void recipeBookNeverGroupsDifferentOutputs() throws Exception {
        Map<String, String> groups = new HashMap<String, String>();
        for (Path path : children(ROOT.resolve("recipes"))) {
            JsonObject recipe = json(path), output = recipe.getAsJsonObject("result");
            String identity = output.get("item").getAsString() + "/" + (output.has("data") ? output.get("data").getAsInt() : 0);
            String previous = groups.put(recipe.get("group").getAsString(), identity);
            if (previous != null) assertEquals(path.toString(), previous, identity);
        }
    }
    @Test public void everyAdvancementTestsItsOwnRecipeAndIngredient() throws Exception {
        for (Path path : children(ROOT.resolve("advancements/recipes"))) {
            String id = path.getFileName().toString().replace(".json", "");
            JsonObject advancement = json(path);
            JsonObject criteria = advancement.getAsJsonObject("criteria");
            JsonObject ingredient = criteria.getAsJsonObject("has_ingredient").getAsJsonObject("conditions")
                    .getAsJsonArray("items").get(0).getAsJsonObject();
            assertTrue(path.toString(), ingredient.has("item"));
            assertNotEquals("minecraft:crafting_table", ingredient.get("item").getAsString());
            assertEquals("iafbygaddon:" + id, criteria.getAsJsonObject("has_the_recipe")
                    .getAsJsonObject("conditions").get("recipe").getAsString());
            assertEquals("iafbygaddon:" + id, advancement.getAsJsonObject("rewards").getAsJsonArray("recipes").get(0).getAsString());
        }
    }
    @Test public void phaseThreeRegistryPathsStillExist() throws Exception {
        String[] forms = {"classic", "shield", "stool_short", "stool_tall", "bench_single",
                "bench_log_single", "bench_back_single", "bench_padded_single", "bench_back_padded_single"};
        for (com.google.gson.JsonElement wood : json(Paths.get("gradle/furniture-catalog.json")).getAsJsonArray("woods"))
            for (String form : forms) {
                String id = "chair_wood_ironage_" + form + "_byg_" + wood.getAsJsonObject().get("wood").getAsString();
                assertTrue(Files.isRegularFile(ROOT.resolve("blockstates/" + id + ".json")));
                assertTrue(Files.isRegularFile(ROOT.resolve("models/item/" + id + ".json")));
            }
    }
}
