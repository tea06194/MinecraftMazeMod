package com.mazemod.world;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import java.util.*;

public class MazeGenerator {
    private final Random random = new Random();

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private static final int LEVELS = 3;
    private static final int FLOOR_HEIGHT = 3; // Минимальная высота между этажами

    public void generateMaze(Level world, BlockPos startPos) {
        for (int level = 0; level < LEVELS; level++) {
            BlockPos levelStart = startPos.offset(0, level * FLOOR_HEIGHT, 0);
            carveWalls(world, levelStart);
            generateMazeHuntAndKill(world, levelStart);
            addTorches(world, levelStart);
            if (level < LEVELS - 1) {
                addStairs(world, levelStart, level); // добавляем переходы между этажами
            }
        }
    }

    private void carveWalls(Level world, BlockPos startPos) {
        for (int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < HEIGHT; z++) {
                for (int y = 0; y < 3; y++) { // Высота стен - минимум 3 блока
                    world.setBlock(startPos.offset(x, y, z), Blocks.STONE_BRICKS.defaultBlockState(), 3);
                }
            }
        }
    }

    private void generateMazeHuntAndKill(Level world, BlockPos startPos) {
        Set<BlockPos> visited = new HashSet<>();
        BlockPos current = startPos.offset(1, 0, 1);

        while (true) {
            List<BlockPos> neighbors = getValidNeighbors(world, current, startPos, visited);
            if (!neighbors.isEmpty()) {
                BlockPos next = neighbors.get(random.nextInt(neighbors.size()));
                carvePath(world, current, next);
                visited.add(next);
                current = next;
            } else {
                Optional<BlockPos> newStart = hunt(world, startPos, visited);
                if (newStart.isEmpty()) break;
                current = newStart.get();
            }
        }
    }

    private List<BlockPos> getValidNeighbors(Level world, BlockPos pos, BlockPos startPos, Set<BlockPos> visited) {
        int[][] directions = {{2, 0}, {-2, 0}, {0, 2}, {0, -2}};
        List<BlockPos> neighbors = new ArrayList<>();

        for (int[] dir : directions) {
            BlockPos next = pos.offset(dir[0], 0, dir[1]);
            if (isInBounds(next, startPos) && !visited.contains(next)) {
                neighbors.add(next);
            }
        }
        return neighbors;
    }

    private void carvePath(Level world, BlockPos from, BlockPos to) {
        BlockPos mid = new BlockPos((from.getX() + to.getX()) / 2, from.getY(), (from.getZ() + to.getZ()) / 2);
        clearBlock(world, from);
        clearBlock(world, mid);
        clearBlock(world, to);
    }

    private void clearBlock(Level world, BlockPos pos) {
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        world.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3);
    }

    private Optional<BlockPos> hunt(Level world, BlockPos startPos, Set<BlockPos> visited) {
        for (int x = 1; x < WIDTH; x += 2) {
            for (int z = 1; z < HEIGHT; z += 2) {
                BlockPos pos = startPos.offset(x, 0, z);
                if (!visited.contains(pos)) {
                    for (BlockPos neighbor : getValidNeighbors(world, pos, startPos, visited)) {
                        carvePath(world, pos, neighbor);
                        visited.add(pos);
                        return Optional.of(pos);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private boolean isInBounds(BlockPos pos, BlockPos startPos) {
        int x = pos.getX() - startPos.getX();
        int z = pos.getZ() - startPos.getZ();
        return x >= 1 && x < WIDTH - 1 && z >= 1 && z < HEIGHT - 1;
    }

    // Добавляем лестницы для переходов между этажами
    private void addStairs(Level world, BlockPos startPos, int level) {
        int numStairs = random.nextInt(3) + 5; // Количество переходов от 5 до 7

        for (int i = 0; i < numStairs; i++) {
            BlockPos pos = startPos.offset(random.nextInt(WIDTH - 2) + 1, level * FLOOR_HEIGHT, random.nextInt(HEIGHT - 2) + 1);
            world.setBlock(pos, Blocks.LADDER.defaultBlockState(), 3);
            world.setBlock(pos.above(), Blocks.LADDER.defaultBlockState(), 3);
        }
    }

    // Добавляем только факела (без стекол)
    private void addTorches(Level world, BlockPos startPos) {
        for (int x = 1; x < WIDTH - 1; x += random.nextInt(5) + 3) {
            for (int z = 1; z < HEIGHT - 1; z += random.nextInt(5) + 3) {
                // Расставляем факела
                if (random.nextBoolean()) {
                    BlockPos torchPos = startPos.offset(x, 1, z);
                    world.setBlock(torchPos, Blocks.TORCH.defaultBlockState(), 3);
                }
            }
        }
    }
}
