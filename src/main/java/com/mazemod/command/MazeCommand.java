package com.mazemod.command;

import com.mazemod.world.MazeGenerator;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import com.mazemod.item.MazeItem;
import net.minecraft.world.item.Items;
import com.mazemod.MazeMod;

public class MazeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("genmaze")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                BlockPos pos = player.blockPosition();
                new MazeGenerator().generateMaze(player.getLevel(), pos.above());
                return 1;
            }));

        dispatcher.register(Commands.literal("getmazeitem")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ItemStack mazeItemStack = new ItemStack(MazeMod.MAZE_ITEM.get());
                player.getInventory().add(mazeItemStack);
                return 1;
            }));
    }
}
