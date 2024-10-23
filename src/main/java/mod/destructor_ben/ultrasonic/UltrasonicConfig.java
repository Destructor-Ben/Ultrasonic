package mod.destructor_ben.ultrasonic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import mod.destructor_ben.ultrasonic.mixin.GameOptionsScreenAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

// TODO: finish populating the config screen, the default values, the fields, and the saving and loading logic
// TODO: maybe abstract this a little bit
public class UltrasonicConfig {
    private static final int CONFIG_VERSION = 1;
    private static File file;

    // Defaults
    private static final boolean DEFAULT_DISPLAY_MUSIC_MESSAGE = true;
    private static final boolean DEFAULT_ALWAYS_PLAY_MUSIC = true;

    // Json ids
    private static final String JSON_DISPLAY_MUSIC_MESSAGE = "displayMusicMessage";
    private static final String JSON_ALWAYS_PLAY_MUSIC = "alwaysPlayMusic";

    // Values
    public static boolean displayMusicMessage = DEFAULT_DISPLAY_MUSIC_MESSAGE;
    public static boolean alwaysPlayMusic = DEFAULT_ALWAYS_PLAY_MUSIC;

    // TODO: integrate these properly
    public static boolean modifyAvailableMusic = true;

    public static void initialize()
    {
        file = FabricLoader.getInstance().getConfigDir().resolve(Ultrasonic.MOD_ID + ".json").toFile();
        load();
        save();
    }

    public static void modifyMusicScreen()
    {
        var minecraft = MinecraftClient.getInstance();
        var screen = (GameOptionsScreen)minecraft.currentScreen;
        if (screen == null)
            return;

        var accessor = (GameOptionsScreenAccessor)screen;
        var body = accessor.getBody();

        var musicOptionsButton = ButtonWidget.builder(
            Text.translatable("option.ultrasonic.title"),
            button -> minecraft.setScreen(UltrasonicConfig.getConfigScreen(minecraft.currentScreen))
        ).width(310).build();

        body.addWidgetEntry(musicOptionsButton, null);
    }

    public static Screen getConfigScreen(Screen parent) {
        var builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.translatable("option.ultrasonic.title"));

        var entryBuilder = builder.entryBuilder();

        // Music options
        var music = builder.getOrCreateCategory(Text.translatable("option.ultrasonic.category.music"));

        music.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.ultrasonic.display_music_message"), displayMusicMessage)
            .setDefaultValue(DEFAULT_DISPLAY_MUSIC_MESSAGE)
            .setTooltip(Text.translatable("option.ultrasonic.display_music_message.tooltip"))
            .setSaveConsumer(newValue -> displayMusicMessage = newValue)
            .build());

        music.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.ultrasonic.always_play_music"), alwaysPlayMusic)
            .setDefaultValue(DEFAULT_ALWAYS_PLAY_MUSIC)
            .setTooltip(Text.translatable("option.ultrasonic.always_play_music.tooltip"))
            .setSaveConsumer(newValue -> alwaysPlayMusic = newValue)
            .build());

        // Saving logic
        builder.setSavingRunnable(() -> {
            save();
            load();
        });

        return builder.build();
    }

    public static void load()
    {
        try {
            loadInternal();
        }
        catch (Exception e) {
            Ultrasonic.LOGGER.error("Config failed to load - {}", e.getMessage(), e);
        }
    }

    public static void save()
    {
        try {
            saveInternal();
        }
        catch (Exception e) {
            Ultrasonic.LOGGER.error("Config failed to save - {}", e.getMessage(), e);
        }
    }

    private static void loadInternal() throws IOException
    {
        // Don't parse it if it doesn't exist
        if (!file.exists())
        {
            Ultrasonic.LOGGER.info("Skipping loading config because it doesn't exist");
            return;
        }

        // Read the file
        var stream = new FileInputStream(file);
        var bytes = stream.readAllBytes();
        stream.close();

        // Parse json
        var contents = new String(bytes);
        var json = JsonParser.parseString(contents).getAsJsonObject();
        var version = json.get("configVersion");
        if (version == null)
            throw new IOException("No config version specified");

        // Read the config
        displayMusicMessage = json.get(JSON_DISPLAY_MUSIC_MESSAGE).getAsBoolean();
        alwaysPlayMusic = json.get(JSON_ALWAYS_PLAY_MUSIC).getAsBoolean();

        Ultrasonic.LOGGER.info("Loaded config file");
    }

    private static void saveInternal() throws IOException
    {
        // Setup json
        var json = new JsonObject();
        json.addProperty("configVersion", CONFIG_VERSION);

        // Convert the config into json
        json.addProperty(JSON_DISPLAY_MUSIC_MESSAGE, displayMusicMessage);
        json.addProperty(JSON_ALWAYS_PLAY_MUSIC, alwaysPlayMusic);

        // Write to the file
        var stream = new FileOutputStream(file, false);
        stream.write(json.toString().getBytes());
        stream.close();

        Ultrasonic.LOGGER.info("Saved config file");
    }
}
