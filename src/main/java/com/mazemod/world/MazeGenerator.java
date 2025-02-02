package com.mazemod.world;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class MazeGenerator {

    public void generateMaze(Level world, BlockPos startPos) {
        // Пример простого алгоритма генерации лабиринта
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                if ((x + z) % 2 == 0) {
                    world.setBlock(startPos.offset(x, 0, z), Blocks.STONE.defaultBlockState(), 3);
                } else {
                    world.setBlock(startPos.offset(x, 0, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

    }
} 