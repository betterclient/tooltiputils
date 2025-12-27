package dev.betterclient.tooltiputils.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import dev.betterclient.tooltiputils.State;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mixin(GuiGraphics.class)
public class MixinGuiGraphics {
    @Shadow @Final private Matrix3x2fStack pose;
    @Unique private int rememberX = 0, rememberY = 0, rememberWidth = 0, rememberHeight = 0;

    @Redirect(method = "renderTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/TooltipRenderUtil;renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIILnet/minecraft/resources/ResourceLocation;)V"))
    public void onAfterPush(GuiGraphics guiGraphics, int x, int y, int width, int height, ResourceLocation sprite) {
        //scale
        this.pose.translate(x, y);
        this.pose.scale(State.config.tooltipScale, State.config.tooltipScale);
        this.pose.translate(-x, -y);
        State.preRender = false;

        rememberY = y;
        rememberX = x;
        rememberWidth = width;
        rememberHeight = height;
        TooltipRenderUtil.renderTooltipBackground(guiGraphics, x, y, width, height, sprite);
    }

    //modify height counter's list
    @Redirect(method = "renderTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    public <E> Iterator<E> onGetIteratorForLines(List<E> instance) {
        return modifyList(instance).iterator();
    }

    //modify renderer's counter list
    @Inject(method = "renderTooltip", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 1), cancellable = true)
    public void cancelNormalRendering(Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, ResourceLocation background, CallbackInfo ci) {
        ci.cancel();
        int p = rememberY;
        components = modifyList(components);

        GuiGraphics thiz = (GuiGraphics)(Object)this;
        for(int q = 0; q < components.size(); ++q) {
            ClientTooltipComponent clientTooltipComponent2 = components.get(q);
            clientTooltipComponent2.renderText(thiz, font, rememberX, p);
            p += clientTooltipComponent2.getHeight(font) + (q == 0 ? 2 : 0);
        }

        p = rememberY;

        for(int q = 0; q < components.size(); ++q) {
            ClientTooltipComponent clientTooltipComponent2 = components.get(q);
            clientTooltipComponent2.renderImage(font, rememberX, p, rememberWidth, rememberHeight, thiz);
            p += clientTooltipComponent2.getHeight(font) + (q == 0 ? 2 : 0);
        }

        this.pose.popMatrix();
    }


    @Unique
    @SuppressWarnings("unchecked")
    private <E> List<E> modifyList(List<E> instance) {
        if (!State.config.expandWithShift) {
            return instance;
        }

        if (InputConstants.isKeyDown(
                Minecraft.getInstance().getWindow(),
                InputConstants.KEY_LSHIFT
        )) {
            return instance;
        }

        if (instance.size() <= (State.config.cutOffLines + 1)) {
            return instance;
        }

        List<E> es = new ArrayList<>(instance.subList(0, State.config.cutOffLines));

        es.add((E) ClientTooltipComponent.create(
                FormattedCharSequence.forward(
                        "Press Shift to expand",
                        Style.EMPTY.withColor(ChatFormatting.YELLOW)
                )
        ));

        return es;
    }

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    public void onRenderStart(CallbackInfo callbackInfo) {
        if (!State.config.tooltipEnabled) callbackInfo.cancel();

        State.preRender = true;
    }
}
