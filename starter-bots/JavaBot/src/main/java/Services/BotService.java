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
            var wormholeList = gameState.getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.WORMHOLE)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(bot, item)))
                    .collect(Collectors.toList());

            // Defaultnya arahkan ke makanan
            playerAction.heading = getHeadingBetween(bot, foodList.get(0));
            // Jika ada superfood arahkan ke superfood
            if (!superfoodList.isEmpty() && getDistanceBetween(bot, superfoodList.get(0)) < getDistanceBetween(bot, foodList.get(0))){
                playerAction.heading = getHeadingBetween(bot, superfoodList.get(0));
            }
            // Menjauh jika ada player lain yang berpotensi memakan
            if (!playerList.isEmpty() && nearestPlayer(playerList.get(0)) == 1){
                if(isLeft(bot,playerList.get(0),playerAction.heading)){
                    playerAction.heading = playerAction.heading - 160;
                }
                else{
                    playerAction.heading = playerAction.heading + 160;
                }
            }
            // Jika terlalu dekat ke ujung
            if (getDistanceBetween(center, bot) + 1.7*bot.getSize() > gameState.getWorld().getRadius()){
                playerAction.heading = getHeadingBetween(bot, center) - 40;
            }
            // Mendekat jika ada player yang bisa dimakan
            else if(!playerList.isEmpty() && nearestPlayer(playerList.get(0)) == 2){
                playerAction.heading = getHeadingBetween(bot, playerList.get(0));
            }
            // Menjauhi gascloud
            else if (!gascloudList.isEmpty() && nearGasCloud(gascloudList.get(0))){
                if(isLeft(bot,gascloudList.get(0),playerAction.heading)){
                    playerAction.heading = playerAction.heading - 70;
                }
                else{
                    playerAction.heading = playerAction.heading + 70;
                }
            }
            // Menjauhi asteroidfield
            else if (!asteroidfieldList.isEmpty() && nearAsteroidfield(asteroidfieldList.get(0))){
                if(isLeft(bot,asteroidfieldList.get(0),playerAction.heading)){
                    playerAction.heading = playerAction.heading - 70;
                }
                else{
                    playerAction.heading = playerAction.heading + 70;
                }
            }
            // Menjauhi wormhole
            else if (!wormholeList.isEmpty() && nearWormhole(wormholeList.get(0))){
                if(isLeft(bot,wormholeList.get(0),playerAction.heading)){
                    playerAction.heading = playerAction.heading - 70;
                }
                else{
                    playerAction.heading = playerAction.heading + 70;
                }
            }
        }

        this.playerAction = playerAction;
    }

    public boolean nearGasCloud(GameObject gascloud){
        return 1.2*bot.getSize() + 1.2*gascloud.getSize() > getDistanceBetween(bot, gascloud);
    }

    public boolean nearAsteroidfield(GameObject asteroidfield){
        return 1.2*bot.getSize() + asteroidfield.getSize() > getDistanceBetween(bot, asteroidfield);
    }

    public boolean nearWormhole(GameObject wormhole){
        return 1.2*bot.getSize() + wormhole.getSize() > getDistanceBetween(bot, wormhole);
    }

    public int nearestPlayer(GameObject player){
        if (0.5*player.getSize() > 2.7*bot.getSize() && 2.6*getDistanceBetween(bot, player) < 0.7*player.getSpeed()){
            return 1; // bahaya player lain
        }
        else if (0.5*player.getSize() < 2.7*bot.getSize() && 2.6*getDistanceBetween(bot, player) < 0.7*bot.getSpeed()){
            return 2; // bisa dikejar
        }
        else{
            return 3; // tidak ada apa-apa
        }
    }

    public boolean isLeft(GameObject obj1, GameObject obj2, int heading){
        int dif = getHeadingBetween(obj1, obj2);
        if (heading > dif){
            dif = dif + 360;
        }
        if(dif-heading <= 180){
            return true;
        }
        else{
            return false;
        }
    }

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