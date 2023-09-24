package net.phoenix.wartracker.containers;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.phoenix.wartracker.client.WarTrackerClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class War {

    private List<String> warMembersUUID = new ArrayList<>();
    private String territoryName = "";
    private String territoryOwner = "";
    private final TerritoryDefenses start;
    private TerritoryDefenses end;
    private final long startTime;
    private boolean killed = false;

    public War(List<String> warMembersUUID, String territoryName, String territoryOwner, TerritoryDefenses start, long startTime) {
        this.warMembersUUID = warMembersUUID;
        this.territoryName = territoryName;
        this.territoryOwner = territoryOwner;
        this.start = start;
        this.startTime = startTime;
    }

    public static War of(BossBar bossBar) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;


        int searchRadius = 150;
        List<PlayerEntity> newPlayers = new ArrayList<>();
        if (player != null && client.world != null) {
            newPlayers = MinecraftClient.getInstance().world.getEntitiesByClass(PlayerEntity.class, new Box(
                    player.getX() - searchRadius, player.getY() - searchRadius, player.getZ() - searchRadius,
                    player.getX() + searchRadius, player.getY() + searchRadius, player.getZ() + searchRadius
            ), playerEntity -> playerEntity != null && playerEntity.getName().getString().matches("[A-Za-z_0-9]+"));
        }
        List<String> warMembersUUID = new ArrayList<>();
        for (PlayerEntity entity : newPlayers) {
            warMembersUUID.add(entity.getUuidAsString());
        }

        Matcher pattern = WarTrackerClient.warBossBar.matcher(bossBar.getName().toString());

        String territoryName = pattern.group(2);
        String territoryOwner = pattern.group(1);

        TerritoryDefenses start = new TerritoryDefenses(
                Long.parseLong(pattern.group(3)),
                Integer.parseInt(pattern.group(5)),
                Integer.parseInt(pattern.group(6)),
                Float.parseFloat(pattern.group(4)),
                Float.parseFloat(pattern.group(7))
        );

        long startTime = Instant.now().toEpochMilli();

        return new War(
                warMembersUUID,
                territoryName,
                territoryOwner,
                start,
                startTime
        );
    }

    public void end(BossBar bossBar) {
        Matcher pattern = WarTrackerClient.warBossBar.matcher(bossBar.getName().toString());

        this.end = new TerritoryDefenses(
                Long.parseLong(pattern.group(3)),
                Integer.parseInt(pattern.group(5)),
                Integer.parseInt(pattern.group(6)),
                Float.parseFloat(pattern.group(4)),
                Float.parseFloat(pattern.group(7))
        );
        WarTrackerClient.sendWar();
    }

    public void killed() {
        this.killed = true;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static class TerritoryDefenses {
        public long health;
        public int attackLow;
        public int attackHigh;
        public float defense;
        public float atckSpeed;

        public TerritoryDefenses(long health, int attackLow, int attackHigh, float defense, float atckSpeed) {
            this.health = health;
            this.attackLow = attackLow;
            this.attackHigh = attackHigh;
            this.defense = defense;
            this.atckSpeed = atckSpeed;
        }

    }


}
