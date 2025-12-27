package dev.betterclient.tooltiputils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class PickPositionScreen extends Screen {
    public Screen parent;

    protected PickPositionScreen(Screen parent) {
        super(Component.empty());
        this.parent = parent;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        State.config.tooltipX = (int) mouseX;
        State.config.tooltipY = (int) mouseY;
        Minecraft.getInstance().setScreen(parent);
        State.saveConfig();
        return true;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);

        //render example tooltip at mouse
        guiGraphics.renderTooltip(
                this.font,
                List.of(
                        tooltipComponent("Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
                        tooltipComponent("Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
                        tooltipComponent("Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris"),
                        tooltipComponent("nisi ut aliquip ex ea commodo consequat."),
                        tooltipComponent("Duis aute irure dolor in reprehenderit in voluptate velit esse"),
                        tooltipComponent("cillum dolore eu fugiat nulla pariatur."),
                        tooltipComponent("Excepteur sint occaecat cupidatat non proident, sunt in culpa"),
                        tooltipComponent("qui officia deserunt mollit anim id est laborum."),
                        tooltipComponent("Curabitur pretium tincidunt lacus. Nulla gravida orci a odio."),
                        tooltipComponent("Nullam varius, turpis et commodo pharetra, est eros bibendum elit,"),
                        tooltipComponent("nec luctus magna felis sollicitudin mauris."),
                        tooltipComponent("Integer in mauris eu nibh euismod gravida."),
                        tooltipComponent("Duis ac tellus et risus vulputate vehicula."),
                        tooltipComponent("Donec lobortis risus a elit."),
                        tooltipComponent("Etiam tempor. Ut ullamcorper, ligula eu tempor congue, eros est"),
                        tooltipComponent("euismod turpis, id tincidunt sapien risus a quam."),
                        tooltipComponent("Maecenas fermentum consequat mi. Donec fermentum."),
                        tooltipComponent("Pellentesque malesuada nulla a mi. Duis sapien sem, aliquet nec,"),
                        tooltipComponent("commodo eget, consequat quis, neque.")
                ),
                i, j,
                DefaultTooltipPositioner.INSTANCE,
                null
        );
    }

    private static ClientTooltipComponent tooltipComponent(String text) {
        return ClientTooltipComponent.create(FormattedCharSequence.forward(text, Style.EMPTY));
    }
}
