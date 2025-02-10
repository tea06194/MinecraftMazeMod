package com.mazemod;

import com.mazemod.command.MazeCommand;
import com.mazemod.item.MazeItem;
import com.mazemod.item.ModCreativeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod(MazeMod.MODID)
public class MazeMod {

    public static final String MODID = "mazemod";

    // Регистрация предметов
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> MAZE_ITEM = ITEMS.register("maze_item",
        () -> new MazeItem(new Item.Properties()
            .setId(ITEMS.key("maze_item"))
        )
    );

    public MazeMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        ITEMS.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MazeCommand.register(event.getDispatcher());
    }
}
