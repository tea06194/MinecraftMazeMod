package com.mazemod.command;

import com.mazemod.MazeMod;
import com.mazemod.world.MazeGenerator;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class MazeCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("genmaze")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					BlockPos pos = player.blockPosition();
					new MazeGenerator().generateMaze(player.serverLevel(), pos.above());
					return 1;
				}));

		// Стеклянный лабиринт
		dispatcher.register(Commands.literal("genglassmaze")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					BlockPos pos = player.blockPosition();
					new MazeGenerator().generateMaze(player.serverLevel(), pos.above(), Blocks.GLASS);
					return 1;
				}));

		dispatcher.register(Commands.literal("getmazeitem")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					ItemStack mazeItemStack = new ItemStack(MazeMod.MAZE_ITEM.get());
					player.getInventory().add(mazeItemStack);
					return 1;
				}));

		// Получить стеклянный предмет
		dispatcher.register(Commands.literal("getglassmazeitem")
				.executes(context -> {
					ServerPlayer player = context.getSource().getPlayerOrException();
					ItemStack glassMazeItemStack = new ItemStack(MazeMod.GLASS_MAZE_ITEM.get());
					player.getInventory().add(glassMazeItemStack);
					return 1;
				}));
	}
}
