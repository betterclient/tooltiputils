package dev.betterclient.tooltiputils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigurationScreen extends Screen {
    protected ConfigurationScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        int widgetWidth = 200;
        int widgetHeight = 20;
        int spacing = 24;

        int totalItems = 10;
        int totalContentHeight = (totalItems * spacing);

        int x = this.width / 2 - (widgetWidth / 2);
        int y = (this.height - totalContentHeight) / 2;

        StringWidget titleWidget = new StringWidget(x, y, widgetWidth, widgetHeight, Component.literal("TooltipUtils"), this.font);
        titleWidget.alignCenter();
        this.addRenderableWidget(titleWidget);
        y += spacing;

        this.addRenderableWidget(Checkbox
                .builder(Component.literal("Enable Tooltips"), this.font)
                .selected(State.config.tooltipEnabled)
                .onValueChange((checkbox, bl) -> {
                    State.config.tooltipEnabled = bl;
                    State.saveConfig();
                })
                .pos(x, y)
                .build()
        );
        y += spacing;

        OptionInstance<Integer> scaleOption = new OptionInstance<>(
                "Tooltip Scale",
                OptionInstance.noTooltip(),
                (text, value) -> Component.literal("Tooltip Scale: " + value/10f + "x"),
                new OptionInstance.IntRange(5, 15),
                (int) (State.config.tooltipScale * 10),
                intValue -> {
                    State.config.tooltipScale = intValue / 10f;
                    State.saveConfig();
                }
        );
        this.addRenderableWidget(scaleOption.createButton(Minecraft.getInstance().options, x, y, widgetWidth));
        y += spacing;

        this.addRenderableWidget(Button
                .builder(Component.literal("Reset Scale"), button -> {
                    State.config.tooltipScale = 1f;
                    State.saveConfig();
                    this.init(Minecraft.getInstance(), this.width, this.height);
                })
                .pos(x, y)
                .size(widgetWidth, widgetHeight)
                .build()
        );
        y += spacing;

        this.addRenderableWidget(Checkbox
                .builder(Component.literal("Enable Scrolling"), this.font)
                .selected(State.config.scrollEnabled)
                .onValueChange((checkbox, bl) -> {
                    State.config.scrollEnabled = bl;
                    State.saveConfig();
                })
                .pos(x, y)
                .build()
        );
        y += spacing;

        OptionInstance<Integer> scrollSpeedOption = new OptionInstance<>(
                "Scroll Speed",
                OptionInstance.noTooltip(),
                (text, value) -> Component.literal("Scroll Speed: " + value),
                new OptionInstance.IntRange(1, 50),
                (int) State.config.scrollMultiplier,
                intValue -> {
                    State.config.scrollMultiplier = (float) intValue;
                    State.saveConfig();
                }
        );
        this.addRenderableWidget(scrollSpeedOption.createButton(Minecraft.getInstance().options, x, y, widgetWidth));
        y += spacing;

        this.addRenderableWidget(Checkbox
                .builder(Component.literal("Expand only with Shift"), this.font)
                .selected(State.config.expandWithShift)
                .onValueChange((checkbox, bl) -> {
                    State.config.expandWithShift = bl;
                    State.saveConfig();
                })
                .pos(x, y)
                .build()
        );
        y += spacing;

        OptionInstance<Integer> cutoffOption = new OptionInstance<>(
                "Cutoff Lines",
                OptionInstance.noTooltip(),
                (text, value) -> Component.literal("Max Lines: " + value),
                new OptionInstance.IntRange(1, 50),
                State.config.cutOffLines,
                intValue -> {
                    State.config.cutOffLines = intValue;
                    State.saveConfig();
                }
        );
        this.addRenderableWidget(cutoffOption.createButton(Minecraft.getInstance().options, x, y, widgetWidth));
        y += spacing;

        this.addRenderableWidget(Checkbox
                .builder(Component.literal("Lock Tooltip Position"), this.font)
                .selected(State.config.lockedTooltipPosition)
                .onValueChange((checkbox, bl) -> {
                    State.config.lockedTooltipPosition = bl;
                    State.saveConfig();
                })
                .pos(x, y)
                .build()
        );
        y += spacing;

        this.addRenderableWidget(Button
                .builder(Component.literal("Set Locked Position"), button -> Minecraft.getInstance().setScreen(new PickPositionScreen(this)))
                .pos(x, y)
                .size(widgetWidth, widgetHeight)
                .build()
        );
    }
}
