package dev.betterclient.tooltiputils.mixin;

import dev.betterclient.tooltiputils.PickPositionScreen;
import dev.betterclient.tooltiputils.State;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.joml.Vector2i;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultTooltipPositioner.class)
public class MixinDefaultTooltipPositioner {
    @Inject(method = "positionTooltip(IILorg/joml/Vector2i;II)V", at = @At(value = "FIELD", target = "Lorg/joml/Vector2i;y:I", remap = false, opcode = Opcodes.GETFIELD), cancellable = true)
    public void onMax(int screenWidth, int screenHeight, Vector2i pos, int tooltipWidth, int tooltipHeight, CallbackInfo ci) {
        int totalHeight = tooltipHeight + 3;
        if (State.config.lockedTooltipPosition && !(Minecraft.getInstance().screen instanceof PickPositionScreen)) {
            pos.x = State.config.tooltipX;
            pos.y = State.config.tooltipY;
        }

        if (pos.y + totalHeight > screenHeight) {
            pos.y = (int) (screenHeight - totalHeight - State.scrollAmount);
        }

        ci.cancel();
    }
}
