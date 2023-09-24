package net.phoenix.wartracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.phoenix.wartracker.containers.Config;
import net.phoenix.wartracker.containers.War;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

public class WarTrackerClient implements ClientModInitializer {

    public static Pattern warBossBar = Pattern.compile("\\[([A-Z,a-z]{3,4})\\] ([A-Z,a-z ]*) - ❤ ([0-9]*) \\(([0-9,.,%]*)\\) - ☠ ([0-9]*)-([0-9]*) \\(([0-9,.]*)");
    public static War activeWar = null;
    public static BossBar warBoss = null;
    public static Config config = null;

    public static void onGameMessage(Text message, boolean overlay) {
        if (overlay) return;
        if (message.contains(Text.literal("You have been killed")) && activeWar != null) {
            activeWar.killed();
            activeWar.end(warBoss);
            warBoss = null;
        } else if (message.contains(Text.literal("- Captured"))) {
            activeWar.end(warBoss);
            warBoss = null;
        }
    }

    public static void sendWar() {
        String json = activeWar.toJson();
        activeWar = null;
        MinecraftClient client = MinecraftClient.getInstance();
        String username = client.player.getName().getString();
        String uuid = client.player.getUuidAsString();

        String url = config.get("url") + "/war";

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("X-Minecraft-Username", username)
                .header("X-Minecraft-UUID", uuid)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAcceptAsync((response) -> {
                    if (response.statusCode() != 200) {
                        client.execute(() -> {
                                    client.player.sendMessage(Text.literal("HTTP request was not successful. Please report this error with chat logs."));
                                }
                        );
                    }
                });
    }

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        config = Config.of("phoenix/wartracker").request();
    }
}
