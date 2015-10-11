package wanderbot;

import pl.gdan.elsy.tool.MatOld;

import java.awt.*;

public class World {
    public static final int SIZE = 200;
    private static final int OBSTACLES_NO = 40;
    private Player player;
    private Obstacle obstacles[];
    private int time;

    public World() {
        obstacles = new Obstacle[OBSTACLES_NO];
        for (int i = 0; i < obstacles.length; i++) {
            obstacles[i] = new Obstacle();
        }
        player = new Player(this);
        player.randomizePosition();
        time = 0;
    }

    public void count() {
        time++;
        player.count();
    }

    public Player getPlayer() {
        return player;
    }

    public Obstacle[] getObstacles() {
        return obstacles;
    }

    public boolean isCollision() {
        if (!MatOld.inside(player.x, SIZE - player.r) || !MatOld.inside(player.y, SIZE - player.r)) {
            return true;
        }
        for (int i = 0; i < obstacles.length; i++) {
            if (obstacles[i].circleCollides(player.x, player.y, player.r)) {
                obstacles[i].c = obstacles[i].c == Color.gray ? Color.yellow : Color.gray;
                return true;
            }
        }
        return false;
    }

    public boolean pointInObstacle(double x, double y) {
        if (!MatOld.inside(x, SIZE) || !MatOld.inside(y, SIZE)) {
            return true;
        }
        for (int i = 0; i < obstacles.length; i++) {
            if (obstacles[i].pointCollides(x, y)) {
                return true;
            }
        }
        return false;
    }

    public int getTime() {
        return time;
    }

}
