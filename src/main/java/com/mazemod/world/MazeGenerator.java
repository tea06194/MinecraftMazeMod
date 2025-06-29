package com.mazemod.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MazeGenerator {
	private final Random random = new Random();

	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	private static final int LEVELS = 3;
	private static final int FLOOR_HEIGHT = 3; // Минимальная высота между этажами
												//
	// Добавляем поле для материала стен
	private Block wallMaterial;

	public void generateMaze(Level world, BlockPos startPos) {
		generateMaze(world, startPos, Blocks.STONE_BRICKS); // По умолчанию каменные кирпичи
	}

	public void generateMaze(Level world, BlockPos startPos, Block material) {
		this.wallMaterial = material;
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
					world.setBlock(startPos.offset(x, y, z), wallMaterial.defaultBlockState(), 3);
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
				if (newStart.isEmpty())
					break;
				current = newStart.get();
			}
		}
	}

	private List<BlockPos> getValidNeighbors(Level world, BlockPos pos, BlockPos startPos, Set<BlockPos> visited) {
		int[][] directions = { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };
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

	// private Optional<BlockPos> hunt(Level world, BlockPos startPos, Set<BlockPos>
	// visited) {
	// for (int x = 1; x < WIDTH; x += 2) {
	// for (int z = 1; z < HEIGHT; z += 2) {
	// BlockPos pos = startPos.offset(x, 0, z);
	// if (!visited.contains(pos)) {
	// for (BlockPos neighbor : getValidNeighbors(world, pos, startPos, visited)) {
	// carvePath(world, pos, neighbor);
	// visited.add(pos);
	// return Optional.of(pos);
	// }
	// }
	// }
	// }
	// return Optional.empty();
	// }

	// Исправленный метод hunt
	private Optional<BlockPos> hunt(Level world, BlockPos startPos, Set<BlockPos> visited) {
		for (int x = 1; x < WIDTH - 1; x += 2) {
			for (int z = 1; z < HEIGHT - 1; z += 2) {
				BlockPos pos = startPos.offset(x, 0, z);
				if (!visited.contains(pos)) {
					// Проверяем, есть ли рядом уже посещенные клетки
					List<BlockPos> visitedNeighbors = getVisitedNeighbors(world, pos, startPos,
							visited);
					if (!visitedNeighbors.isEmpty()) {
						// Соединяем с одним из посещенных соседей
						BlockPos neighbor = visitedNeighbors.get(random.nextInt(visitedNeighbors.size()));
						carvePath(world, pos, neighbor);
						visited.add(pos); // ВАЖНО: добавляем в visited
						return Optional.of(pos);
					}
				}
			}
		}
		return Optional.empty();
	}

	// Вспомогательный метод для поиска посещенных соседей
	private List<BlockPos> getVisitedNeighbors(Level world, BlockPos pos,
			BlockPos startPos, Set<BlockPos> visited) {
		int[][] directions = { { 2, 0 }, { -2, 0 }, { 0, 2 }, { 0, -2 } };
		List<BlockPos> neighbors = new ArrayList<>();

		for (int[] dir : directions) {
			BlockPos next = pos.offset(dir[0], 0, dir[1]);
			if (isInBounds(next, startPos) && visited.contains(next)) {
				neighbors.add(next);
			}
		}
		return neighbors;
	}

	private boolean isInBounds(BlockPos pos, BlockPos startPos) {
		int x = pos.getX() - startPos.getX();
		int z = pos.getZ() - startPos.getZ();
		return x >= 1 && x < WIDTH - 1 && z >= 1 && z < HEIGHT - 1;
	}

	// Улучшенный метод addStairs с полной проверкой верхнего этажа
	private void addStairs(Level world, BlockPos startPos, int level) {
		System.out.println("[addStairs] Start level=" + level);
		int numStairsToPlace = random.nextInt(6) + 3; // 3-8 лестниц
		System.out.println("[addStairs] Target stairs count = " + numStairsToPlace);

		int placed = 0;
		int attempts = 0;
		int maxAttempts = 500;
		Set<BlockPos> triedPositions = new HashSet<>();

		while (placed < numStairsToPlace && attempts < maxAttempts) {
			attempts++;

			// 1) Выбор случайной позиции
			BlockPos base = startPos.offset(
					random.nextInt(WIDTH - 4) + 2,
					0,
					random.nextInt(HEIGHT - 4) + 2);

			if (triedPositions.contains(base)) {
				continue;
			}
			triedPositions.add(base);

			System.out.println("[addStairs] Attempt #" + attempts + " at " + base);

			// 2) Проверяем, что базовая позиция подходит (воздух)
			if (!world.getBlockState(base).isAir()) {
				continue;
			}

			// 3) Пробуем каждую сторону
			boolean ladderPlaced = false;
			for (Direction dir : Direction.Plane.HORIZONTAL) {
				BlockPos support = base.relative(dir.getOpposite());

				// Проверяем опору на текущем этаже
				boolean hasSolidSupport = !world.getBlockState(support).isAir();
				boolean spaceAbove = world.getBlockState(base.above()).isAir();

				if (!hasSolidSupport || !spaceAbove) {
					continue;
				}

				// 4) КЛЮЧЕВАЯ ПРОВЕРКА: проверяем верхний этаж
				BlockPos upperBase = base.above(FLOOR_HEIGHT);
				if (!isValidUpperFloorExit(world, upperBase)) {
					System.out.println("[addStairs]   Upper floor blocked at " + upperBase + ", trying next direction");
					continue;
				}

				System.out.println(String.format("[addStairs]  Valid ladder position found: dir=%s, upper exit at %s",
						dir, upperBase));

				// 5) Прорубаем путь на верхнем этаже если нужно
				ensureUpperFloorAccess(world, upperBase);

				// 6) Размещаем лестницу
				BlockState ladderState = Blocks.LADDER.defaultBlockState()
						.setValue(LadderBlock.FACING, dir);

				for (int y = 0; y < FLOOR_HEIGHT; y++) {
					BlockPos ladderPos = base.above(y);
					world.setBlock(ladderPos, ladderState, 3);
				}

				placed++;
				System.out.println("[addStairs]   Placed ladder #" + placed + " at " + base + " facing " + dir);
				ladderPlaced = true;
				break;
			}

			if (!ladderPlaced) {
				System.out.println("[addStairs]   No suitable direction found for position " + base);
			}

			// Защита от зависания
			if (triedPositions.size() > (WIDTH * HEIGHT) / 4) {
				System.out.println("[addStairs] Too many positions tried, breaking early");
				break;
			}
		}

		System.out.println("[addStairs] Done: placed=" + placed + ", attempts=" + attempts);
	}

	// Проверяет, можно ли выйти с лестницы на верхнем этаже
	private boolean isValidUpperFloorExit(Level world, BlockPos upperBase) {
		// Проверяем саму позицию выхода
		if (!world.getBlockState(upperBase).isAir()) {
			return false;
		}

		// Проверяем высоту для игрока (2 блока)
		if (!world.getBlockState(upperBase.above()).isAir()) {
			return false;
		}

		// Проверяем, что рядом есть хотя бы один проход (не окружена стенами)
		int openSides = 0;
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			BlockPos adjacent = upperBase.relative(dir);
			if (world.getBlockState(adjacent).isAir() &&
					world.getBlockState(adjacent.above()).isAir()) {
				openSides++;
			}
		}

		return openSides > 0; // Есть хотя бы один проход
	}

	// Обеспечивает доступ на верхнем этаже
	private void ensureUpperFloorAccess(Level world, BlockPos upperBase) {
		// Убеждаемся, что место для выхода свободно
		if (!world.getBlockState(upperBase).isAir()) {
			world.setBlock(upperBase, Blocks.AIR.defaultBlockState(), 3);
			System.out.println("[addStairs]   Cleared upper floor at " + upperBase);
		}

		if (!world.getBlockState(upperBase.above()).isAir()) {
			world.setBlock(upperBase.above(), Blocks.AIR.defaultBlockState(), 3);
			System.out.println("[addStairs]   Cleared upper floor ceiling at " + upperBase.above());
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
