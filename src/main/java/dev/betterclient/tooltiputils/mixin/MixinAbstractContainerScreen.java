package dev.betterclient.tooltiputils.mixin;

import dev.betterclient.tooltiputils.State;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
    @Shadow @Nullable protected Slot hoveredSlot;

    protected MixinAbstractContainerScreen(Component component) {
        super(component);
        throw new AssertionError("hi");
    }

    @Inject(method = "extractTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;getItem()Lnet/minecraft/world/item/ItemStack;"))
    public void onGetItem(GuiGraphicsExtractor graphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (hoveredSlot != null && hoveredSlot.hasItem()) {
            State.notifyItem(hoveredSlot.getItem());
        }
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true)
    public void onScroll(double x, double y, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {
        if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            State.doScroll(hoveredSlot.getItem(), scrollY);
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
