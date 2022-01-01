package tankwar.entity;

import tankwar.model.Model;
import tankwar.utils.IoUtils;

import java.util.Random;

//服务器端的类
//因为只有一层对象,所以在这个类是一个静态变量
public class Level {
	public static int currentLevel = 0;
	public static int enemySpawnTime = 100;
	public static int enemyLeft = 20;
	public static int deathCount = 0;
	public static int maxNoEnemy = 3;
	public static int NoOfEnemy = 0;
	public static int[] enemySequence;

	//制作获胜场景所需的变量
	public static int winningCount;

	public static void loadLevel(Model gameModel){
		//增加关卡数量
		currentLevel++;

		//每次加载一个新的关卡将增加难度
		if(enemySpawnTime > 30)
			enemySpawnTime-=10;
		//if(maxNoEnemy < 5 && (currentLevel%2 == 0))
		//	maxNoEnemy++;

		//从上个关卡清除所有东西
		for(int i = 0; i <  400; i ++)
			gameModel.actors[i] = null;

		//启动时各关卡共享
		enemyLeft = 20;

		//加载基地，每个关卡都一样
		gameModel.actors[0] = new Wall(248, 498, 2, gameModel);
		gameModel.actors[1] = new Wall(273, 498, 3, gameModel);
		gameModel.actors[2] = new Wall(248, 473, 1, gameModel);
		gameModel.actors[3] = new Wall(273, 473, 1, gameModel);
		gameModel.actors[4] = new Base(gameModel);

		//加载一个关卡
		if(1+ (currentLevel-1)%8 == 1){
			enemySequence = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2};
			String[] level = IoUtils.readMap(1);
	       loadLevel(gameModel, level);
		}

		if(1+ (currentLevel-1)%8 == 2){
			enemySequence = new int[]{1,1,2,2,1,1,1,1,2,2,2,2,1,1,1,1,3,3,3,3};
			String[] level = IoUtils.readMap(2);
	       loadLevel(gameModel, level);
		}

		if(1+ (currentLevel-1)%8 == 3){
			enemySequence = new int[]{1,1,1,2,2,2,4,4,2,2,2,2,1,1,1,1,2,2,4,4};
			String[] level =IoUtils.readMap(3);
	       loadLevel(gameModel, level);
		}

		if(1+ (currentLevel-1)%8 == 4){
			enemySequence = new int[]{3,3,3,3,2,2,2,3,3,1,1,1,3,3,3,1,1,4,4,4};
			String[] level = IoUtils.readMap(4);
	       loadLevel(gameModel, level);
		}

		if(1+ (currentLevel-1)%8 == 5){
			enemySequence = new int[]{2,2,2,3,3,3,2,2,2,4,4,4,3,3,3,3,3,2,2,2};
			String[] level = IoUtils.readMap(5);
	       loadLevel(gameModel, level);
		}

		if(1+ (currentLevel-1)%8 == 6){
			enemySequence = new int[]{4,4,4,4,2,2,2,4,4,1,1,1,3,3,3,1,1,4,4,4};
			String[] level = IoUtils.readMap(6);
	       loadLevel(gameModel, level);
		}

		if(1+ (currentLevel-1)%8 == 7){
			enemySequence = new int[]{3,3,3,3,3,3,3,3,3,3,2,4,2,4,2,4,2,4,2,4};
			String[] level = IoUtils.readMap(7);
		}

		if(1+ (currentLevel-1)%8 == 8){
			enemySequence = new int[]{3,4,4,2,3,4,4,2,3,4,4,2,3,4,4,2,3,4,4,2};
			String[] level = IoUtils.readMap(8);
	       loadLevel(gameModel, level);
		}

		gameModel.addActor(gameModel.player);
	}

	public static void loadLevel(Model gameModel, String[] level){
		for(int i = 0; i < level.length; i++){
			if(level[i].equals("##"))
				gameModel.addActor(new Wall(23 + (i%20)*25,  23 + (i/20)*25,   gameModel));
           	if(level[i].equals("#0"))
				gameModel.addActor(new Wall(23 + (i%20)*25,  23 + (i/20)*25,  0,  gameModel));
			 if(level[i].equals("#1"))
				gameModel.addActor(new Wall(23 + (i%20)*25,  23 + (i/20)*25,  1,  gameModel));
			 if(level[i].equals("#2"))
				gameModel.addActor(new Wall(23 + (i%19)*25,  23 + (i/20)*25,  2,  gameModel));
			 if(level[i].equals("#3"))
				gameModel.addActor(new Wall(23 + (i%20)*25,  23 + (i/20)*25,  3,  gameModel));
		    if(level[i].equals("ss"))
				gameModel.addActor(new SteelWall(23 + (i%20)*25,  23 + (i/20)*25,  gameModel));
			if(level[i].equals("s0"))
				gameModel.addActor(new SteelWall(23 + (i%20)*25,  23 + (i/20)*25,  0,  gameModel));
			if(level[i].equals("s1"))
				gameModel.addActor(new SteelWall(23 + (i%20)*25,  23 + (i/20)*25,  1,  gameModel));
			if(level[i].equals("s2"))
				gameModel.addActor(new SteelWall(23 + (i%20)*25,  23 + (i/20)*25,  2,  gameModel));
			if(level[i].equals("s3"))
				gameModel.addActor(new SteelWall(23 + (i%20)*25,  23 + (i/20)*25,  3,  gameModel));
			if(level[i].equals("$$")){
				for(int j = 399; j >=0; j--){
					if(gameModel.actors[j] == null){
						gameModel.actors[j] = new Grass(23 + (i%20)*25,  23 + (i/20)*25);
						break;
					}
				}
			}
			if(level[i].equals("=="))
				gameModel.addActor(new River(23 + (i%20)*25,  23 + (i/20)*25, gameModel));
		}
	}

	public static void spawnEnemy(Model gameModel){
    	if(NoOfEnemy < maxNoEnemy && enemyLeft > 0 && (Model.gameFlow % enemySpawnTime == 0)){
			int xPos = 23 + new Random().nextInt(3)*238;
			boolean flashing = (enemyLeft%5 == 0);
			gameModel.addActor(new Enemy(enemySequence[20-enemyLeft], flashing, xPos, 23, gameModel));
			enemyLeft--;
			NoOfEnemy++;
		}
	}

	public static void reset(){
		currentLevel = 0;
		enemySpawnTime = 100;
		enemyLeft = 20;
		deathCount = 0;
		maxNoEnemy = 3;
		NoOfEnemy = 0;
	}
	public static void goOn(){
		currentLevel--;
		enemySpawnTime = 100;
		enemyLeft = 20;
		maxNoEnemy = 3;
	}

}