package net.phoenix.wartracker.mixin;

import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import net.phoenix.wartracker.client.WarTrackerClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public class ChatHandlerMixin {
    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void gameMessage(Text message, boolean overlay, CallbackInfo ci) {
        WarTrackerClient.onGameMessage(message, overlay);
    }
}
