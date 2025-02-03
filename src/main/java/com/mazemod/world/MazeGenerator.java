package com.mazemod.world;

import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class MazeGenerator {

    private final Random random = new Random();
    private static final Logger logger = Logger.getLogger(MazeGenerator.class.getName());

    static {
        try {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // Set up file handler to write logs to the root of the project
            FileHandler fileHandler = new FileHandler("./maze_generator.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.setUseParentHandlers(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateMaze(Level world, BlockPos startPos) {
        int y = 0;
        logger.info("-- Initializing maze with walls --");
        for (int x = 0; x < 30; x++) {
            for (int z = 0; z < 30; z++) {
                world.setBlock(startPos.offset(x, y, z), Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
            }
        }

        // Ensure the starting position is within bounds and on a wall
        BlockPos initialPos = new BlockPos(startPos.getX() + 1, startPos.getY(), startPos.getZ() + 1);
        logger.info("-- Starting maze generation from position: " + initialPos + " --");
        generateMazeDFS(world, initialPos);
    }

    private void generateMazeDFS(Level world, BlockPos startPos) {
        Stack<BlockPos> stack = new Stack<>();
        stack.push(startPos);
        world.setBlock(startPos, Blocks.AIR.defaultBlockState(), 3);
        logger.info("-- Starting position: " + startPos + " --");

        // Directions are now only positive
        int[][] directions = {{2, 0}, {0, 2}};

        while (!stack.isEmpty()) {
            BlockPos current = stack.peek();
            boolean moved = false;
            logger.info("-- Current position: " + current + " --");

            // Shuffle directions for randomness
            for (int i = 0; i < directions.length; i++) {
                int[] temp = directions[i];
                int randomIndex = random.nextInt(directions.length);
                directions[i] = directions[randomIndex];
                directions[randomIndex] = temp;
            }

            for (int[] direction : directions) {
                BlockPos newPos = current.offset(direction[0], 0, direction[1]);
                logger.info("-- Checking new position: " + newPos + " --");
                if (isInBounds(newPos, startPos) && isWall(world, newPos)) {
                    logger.info("-- Carving through wall and path to: " + newPos + " --");
                    // Carve through the wall
                    world.setBlock(newPos, Blocks.AIR.defaultBlockState(), 3);
                    // Carve the path
                    world.setBlock(current.offset(direction[0] / 2, 0, direction[1] / 2), Blocks.AIR.defaultBlockState(), 3);
                    stack.push(newPos);
                    moved = true;
                    break;
                }
            }

            if (!moved) {
                logger.info("-- Returning to previous position --");
                stack.pop();
            }
        }
    }

    private boolean isInBounds(BlockPos pos, BlockPos startPos) {
        int x = pos.getX() - startPos.getX();
        int z = pos.getZ() - startPos.getZ();
        logger.info("-- Checking bounds for position: (" + pos.getX() + ", " + pos.getZ() + ") --");
        logger.info("-- Calculated x: " + x + ", z: " + z + " --");
        boolean inBounds = x >= 0 && x < 29 && z >= 0 && z < 29;
        logger.info("-- Position " + pos + " is within bounds: " + inBounds + " --");
        return inBounds;
    }

    private boolean isWall(Level world, BlockPos pos) {
        logger.info("-- Checking block at position: " + pos + " --");
        var block = world.getBlockState(pos).getBlock();
        logger.info("-- Block at position " + pos + " is: " + block + " --");
        boolean isWall = block == Blocks.DIAMOND_BLOCK;
        logger.info("-- Block at position " + pos + " is a wall: " + isWall + " --");
        return isWall;
    }
}