package com.mazemod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import com.mazemod.item.MazeItem;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.mazemod.command.MazeCommand;
import net.minecraftforge.common.MinecraftForge;

@Mod(MazeMod.MOD_ID)
public class MazeMod {
    public static final String MOD_ID = "mazemod";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> MAZE_ITEM = ITEMS.register("maze_item", 
        () -> new MazeItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public MazeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MazeCommand.register(event.getDispatcher());
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Initialization code
    }
} 