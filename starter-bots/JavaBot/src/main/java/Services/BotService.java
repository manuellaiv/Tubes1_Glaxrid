package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class BotService {
    private GameObject bot;
    private Position centerp = new Position();
    private GameObject center = new GameObject(null, null, null, 0, centerp, null);
    private PlayerAction playerAction;
    private GameState gameState;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
    }


    public GameObject getBot() {
        return this.bot;
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
    }

    public PlayerAction getPlayerAction() {
        return this.playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public void computeNextPlayerAction(PlayerAction playerAction) {
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = new Random().nextInt(360);

        if (!gameState.getGameObjects().isEmpty()) {
            var foodList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
            var playerList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.PLAYER)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
            var superfoodList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
            var gascloudList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());
            var asteroidfieldList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            // List <Double> priolist = new ArrayList<>();
            // if (!foodList.isEmpty()) {priolist.add(prio(foodList.get(0)));} 
            // if (!playerList.isEmpty()) {priolist.add(prio(playerList.get(0)));} 
            // if (!superfoodList.isEmpty()) {priolist.add(prio(superfoodList.get(0)));}
            // if (!gascloudList.isEmpty()) {priolist.add(prio(gascloudList.get(0)));}
            // if (!asteroidfieldList.isEmpty()) {priolist.add(prio(asteroidfieldList.get(0)));}
            // Collections.sort(priolist, Collections.reverseOrder());

            // if(!playerList.isEmpty() && prio(playerList.get(0)) == -999999){
            //     playerAction.heading = getHeadingBetween(playerList.get(0)) - 180;
            // }
            // if(!gascloudList.isEmpty() && getDistanceBetween(bot, gascloudList.get(0)) < bot.getSize()){
            //     playerAction.heading = getHeadingBetween(center);
            // }
            // if(!asteroidfieldList.isEmpty() && getDistanceBetween(bot, asteroidfieldList.get(0)) < bot.getSize()){
            //     playerAction.heading = getHeadingBetween(asteroidfieldList.get(0))-60;
            // }
            // else if(!foodList.isEmpty() && priolist.get(0) == prio(foodList.get(0))){
            //     playerAction.heading = getHeadingBetween(foodList.get(0));
            // }
            // else if(!playerList.isEmpty() && priolist.get(0) == prio(playerList.get(0))){
            //     playerAction.heading = getHeadingBetween(playerList.get(0));
            // }
            // else if(!superfoodList.isEmpty() && priolist.get(0) == prio(superfoodList.get(0))){
            //     playerAction.heading = getHeadingBetween(superfoodList.get(0));
            // }

            // Defaultnya arahkan ke makanan
            playerAction.heading = getHeadingBetween(bot, foodList.get(0));
            // Jika ada superfood arahkan ke superfood
            if (!superfoodList.isEmpty() && getDistanceBetween(bot, superfoodList.get(0)) < getDistanceBetween(bot, foodList.get(0))){
                playerAction.heading = getHeadingBetween(bot, superfoodList.get(0));
            }
            // Menjauh jika ada player lain yang berpotensi memakan
            if (!playerList.isEmpty() && nearestPlayer(playerList.get(0)) == 1){
                playerAction.heading = getHeadingBetween(bot, playerList.get(0)) - 180;
            }
            // Jika terlalu dekat ke ujung
            if (getDistanceBetween(center, bot) + bot.getSize() + 5 > gameState.getWorld().getRadius()){
                playerAction.heading = getHeadingBetween(bot, center);
            }
            // Mendekat jika ada player yang bisa dimakan
            else if(!playerList.isEmpty() && nearestPlayer(playerList.get(0)) == 2){
                playerAction.heading = getHeadingBetween(bot, playerList.get(0));
            }
            // Menjauhi gascloud
            else if (!gascloudList.isEmpty() && nearGasCloud(gascloudList.get(0))){
                playerAction.heading = playerAction.heading - 50;
            }
            // Menjauhi asteroidfield
            else if (!asteroidfieldList.isEmpty() && nearAsteroidfield(asteroidfieldList.get(0))){
                playerAction.heading = playerAction.heading - 50;
            }
        }

        this.playerAction = playerAction;
    }

    public boolean nearGasCloud(GameObject gascloud){
        return bot.getSize() + gascloud.getSize() > getDistanceBetween(bot, gascloud);
    }

    public boolean nearAsteroidfield(GameObject asteroidfield){
        return bot.getSize() + asteroidfield.getSize() > getDistanceBetween(bot, asteroidfield);
    }

    public int nearestPlayer(GameObject player){
        if (player.getSize() > bot.getSize() + 5 && getDistanceBetween(bot, player) < player.getSpeed()){
            return 1; // bahaya player lain
        }
        else if (player.getSize() < bot.getSize() + 5 && getDistanceBetween(bot, player) < bot.getSpeed()){
            return 2; // bisa dikejar
        }
        else{
            return 3; // tidak ada apa-apa
        }
    }

    // public double prio(GameObject gameObject){
    //     if(gameObject.getGameObjectType() == ObjectTypes.FOOD || gameObject.getGameObjectType() == ObjectTypes.SUPERFOOD || gameObject.getGameObjectType() == ObjectTypes.TELEPORTER){
    //         return gameObject.getSize() - getDistanceBetween(bot, gameObject);
    //     }
    //     else if(gameObject.getGameObjectType() == ObjectTypes.PLAYER){
    //         if (gameObject.getSize() > bot.getSize() && getDistanceBetween(bot, gameObject) < gameObject.getSpeed()){
    //             return -999999;
    //         }
    //         else{
    //             return (double)gameObject.getSize()/(getDistanceBetween(bot, gameObject));
    //         }
    //     }
    //     else if(gameObject.getGameObjectType() == ObjectTypes.GASCLOUD || gameObject.getGameObjectType() == ObjectTypes.ASTEROIDFIELD){
    //         return -999999;
    //     }
    //     else{
    //         return -999999;
    //     }
    // }


    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateSelfState();
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

    private double getDistanceBetween(GameObject object1, GameObject object2) {
        var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    private int getHeadingBetween(GameObject obj1, GameObject obj2) {
        var direction = toDegrees(Math.atan2(obj2.getPosition().y - obj1.getPosition().y,
                obj2.getPosition().x - obj1.getPosition().x));
        return (direction + 360) % 360;
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }


}
