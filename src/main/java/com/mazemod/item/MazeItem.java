package com.mazemod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

import com.mazemod.world.MazeGenerator;

import net.minecraft.core.BlockPos;

public class MazeItem extends Item {
    public MazeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!world.isClientSide) {
            new MazeGenerator().generateMaze(world, pos.above());
        }
        return InteractionResult.SUCCESS;
    }
}
