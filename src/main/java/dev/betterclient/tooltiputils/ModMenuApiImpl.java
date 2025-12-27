package dev.betterclient.tooltiputils;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            Option<Boolean> enableTooltips = Option.<Boolean>createBuilder()
                    .name(Component.literal("Enable Tooltips"))
                    .binding(
                            State.config.tooltipEnabled,
                            () -> State.config.tooltipEnabled,
                            v -> {
                                State.config.tooltipEnabled = v;
                                State.saveConfig();
                            }
                    )
                    .description(OptionDescription.of(Component.literal("Toggle the display of tooltips.")))
                    .controller(TickBoxControllerBuilder::create)
                    .build();

            Option<Float> tooltipScale = Option.<Float>createBuilder()
                    .name(Component.literal("Tooltip Scale"))
                    .binding(
                            State.config.tooltipScale,
                            () -> State.config.tooltipScale,
                            v -> {
                                State.config.tooltipScale = v;
                                State.saveConfig();
                            }
                    )
                    .description(OptionDescription.of(Component.literal("Adjust the scale of the tooltips.")))
                    .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.5f, 2.0f).step(0.1f))
                    .build();

            ButtonOption resetScale = ButtonOption
                    .createBuilder()
                    .name(Component.literal("Reset Scale"))
                    .action((yaclScreen, thisOption) -> {
                        State.config.tooltipScale = 1.0f;
                        State.saveConfig();
                    })
                    .text(Component.literal("Reset"))
                    .build();

            Option<Boolean> enableScrolling = Option.<Boolean>createBuilder()
                    .name(Component.literal("Enable Scrolling"))
                    .binding(
                            State.config.scrollEnabled,
                            () -> State.config.scrollEnabled,
                            v -> {
                                State.config.scrollEnabled = v;
                                State.saveConfig();
                            }
                    )
                    .description(OptionDescription.of(Component.literal("Toggle scrolling for long tooltips.")))
                    .controller(TickBoxControllerBuilder::create)
                    .build();

            Option<Integer> scrollSpeed = Option.<Integer>createBuilder()
                    .name(Component.literal("Scroll Speed"))
                    .binding(
                            State.config.scrollMultiplier,
                            () -> State.config.scrollMultiplier,
                            v -> {
                                State.config.scrollMultiplier = v;
                                State.saveConfig();
                            }
                    )
                    .description(OptionDescription.of(Component.literal("Adjust the speed of tooltip scrolling.")))
                    .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 50).step(1))
                    .build();

            Option<Boolean> expandWithShift = Option.<Boolean>createBuilder()
                    .name(Component.literal("Expand With Shift"))
                    .binding(
                            State.config.expandWithShift,
                            () -> State.config.expandWithShift,
                            v -> {
                                State.config.expandWithShift = v;
                                State.saveConfig();
                            }
                    )
                    .description(OptionDescription.of(Component.literal("Hold shift to expand tooltips.")))
                    .controller(TickBoxControllerBuilder::create)
                    .build();

            Option<Integer> cutoffLines = Option.<Integer>createBuilder()
                    .name(Component.literal("Cutoff Lines"))
                    .binding(
                            State.config.cutOffLines,
                            () -> State.config.cutOffLines,
                            v -> {
                                State.config.cutOffLines = v;
                                State.saveConfig();
                            }
                    )
                    .description(OptionDescription.of(Component.literal("Maximum number of lines before cutoff.")))
                    .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 25).step(1))
                    .build();

            Option<Boolean> lockTooltipPosition = Option.<Boolean>createBuilder()
                    .name(Component.literal("Lock Tooltip Position"))
                    .binding(
                            State.config.lockedTooltipPosition,
                            () -> State.config.lockedTooltipPosition,
                            v -> {
                                State.config.lockedTooltipPosition = v;
                                State.saveConfig();
                            }
                    )
                    .description(OptionDescription.of(Component.literal("Lock the position of the tooltip on screen.")))
                    .controller(TickBoxControllerBuilder::create)
                    .build();

            ButtonOption setLockPosition = ButtonOption
                    .createBuilder()
                    .name(Component.literal("Set Lock Position"))
                    .action((yaclScreen, thisOption) -> Minecraft.getInstance().setScreen(new PickPositionScreen(yaclScreen)))
                    .text(Component.literal("Set Position"))
                    .build();

            OptionGroup appearanceGroup = OptionGroup.createBuilder()
                    .name(Component.literal("Appearance"))
                    .option(enableTooltips)
                    .option(tooltipScale)
                    .option(resetScale)
                    .build();

            OptionGroup scrollingGroup = OptionGroup.createBuilder()
                    .name(Component.literal("Scrolling"))
                    .option(enableScrolling)
                    .option(scrollSpeed)
                    .build();

            OptionGroup contentGroup = OptionGroup.createBuilder()
                    .name(Component.literal("Content Control"))
                    .option(expandWithShift)
                    .option(cutoffLines)
                    .build();

            OptionGroup positionGroup = OptionGroup.createBuilder()
                    .name(Component.literal("Positioning"))
                    .option(lockTooltipPosition)
                    .option(setLockPosition)
                    .build();

            return YetAnotherConfigLib.createBuilder()
                    .title(Component.literal("TooltipUtils"))
                    .category(ConfigCategory
                            .createBuilder()
                            .name(Component.literal("General Settings"))
                            .group(appearanceGroup)
                            .group(scrollingGroup)
                            .group(contentGroup)
                            .group(positionGroup)
                            .build())
                    .save(State::saveConfig)
                    .build().generateScreen(parent);
        };
    }
}
