package com.mazemod.item;

import com.mazemod.MazeMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Items;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS
            = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MazeMod.MODID);


    public static final RegistryObject<CreativeModeTab> MAZE_TAB = CREATIVE_MODE_TABS.register("maze_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mazemod")) // Название вкладки
                    .icon(() -> new ItemStack(Items.STONE)) // Иконка вкладки
                    .displayItems((parameters, output) -> {
                        output.accept(MazeMod.MAZE_ITEM.get()); // Добавляем предмет в вкладку
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
