package net.phoenix.wartracker.mixin;

import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.phoenix.wartracker.client.WarTrackerClient;
import net.phoenix.wartracker.containers.War;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarMixin {
    @Inject(method = "renderBossBar(Lnet/minecraft/client/util/math/MatrixStack;IILnet/minecraft/entity/boss/BossBar;)V",
            at = @At("HEAD"))
    private void onRender(MatrixStack matrices, int x, int y, BossBar bossBar, CallbackInfo ci) {
        if (!bossBar.getName().contains(Text.literal("Tower"))) return;
        if (WarTrackerClient.activeWar == null) WarTrackerClient.activeWar = War.of(bossBar);
        WarTrackerClient.warBoss = bossBar;
    }

}