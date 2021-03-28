package spaceinvaders.game;

import static java.util.logging.Level.SEVERE;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import spaceinvaders.Config;
import spaceinvaders.game.EntityEnum;

/** Configuration for game entities. */
public class GameConfig {
  private static final transient Logger LOGGER = Logger.getLogger(GameConfig.class.getName());
  private static transient GameConfig singleton;

  private static boolean cheatsOn = false;
  private final int playerSpeedHackMultiplier = 5;
  private final int bulletSpeedHackMultiplier = 3;

  private static int defaultPlayerSpeed;
  private static int defaultBulletSpeed;

  private Map<EntityEnum,EntityConfig> entityMap;
  private Boolean predictable;
  private Integer invaderRows;
  private Integer invaderCols;
  private Integer invadersShootingFactor;
  private Integer shieldsPerPlayer;
  private FrameConfig frame;
  private Speed speed;

  private GameConfig() {}

  /** Get the single instance. */
  public static GameConfig getInstance() {
    if (singleton == null) {
      try {
        singleton = readConfig();

        // grab config values on load-up
        defaultPlayerSpeed = singleton.speed.player.distance;
        defaultBulletSpeed = singleton.speed.bullet.distance;

        //System.out.println("Default Player Speed: " + defaultPlayerSpeed + "\nDefault Bullet Speed: " + defaultBulletSpeed);
      } catch (IOException ioExcepiton) {
        LOGGER.log(SEVERE,ioExcepiton.toString(),ioExcepiton);
      }
    }
    return singleton;
  }

  /**
   * Reads the configuration from a json.
   *
   * @throws IOException if an error occurs while reading the configuration file.
   */
  private static GameConfig readConfig() throws IOException {
    final Config config = Config.getInstance();
    return config.getJsonResource(config.getGameConfigFile(),GameConfig.class);
  }

  public boolean isPredictable() {
    return predictable;
  }

  public FrameConfig frame() {
    return frame;
  }

  public int getDefaultPlayerSpeed()
  {
    return defaultPlayerSpeed;
  }

  public int getDefaultBulletSpeed()
  {
    return defaultBulletSpeed;
  }

  public Speed speed() {
    return speed;
  }

  public int getInvaderRows() {
    return invaderRows;
  }

  public int getInvaderCols() {
    return invaderCols;
  }

  public int getInvadersShootingFactor() {
    return invadersShootingFactor;
  }

  public int getShieldsPerPlayer() {
    return shieldsPerPlayer;
  }

  public EntityConfig invader() {
    return getFromMap(EntityEnum.INVADER);
  }

  public EntityConfig player() {
    return getFromMap(EntityEnum.PLAYER);
  }

  public EntityConfig shield() {
    return getFromMap(EntityEnum.SHIELD);
  }

  public EntityConfig invaderBullet() {
    return getFromMap(EntityEnum.INVADER_BULLET);
  }

  public EntityConfig playerBullet() {
    return getFromMap(EntityEnum.PLAYER_BULLET);
  }

  // Operates directly on game configuration file to enable cheating
  public void toggleCheat()
  {
    if (!cheatsOn){
      cheatsOn = true;

      // increase player and projectile speed
      speed.player.setDistance(new Integer(speed.player.getDistance() * playerSpeedHackMultiplier));
      speed.bullet.setDistance(new Integer(speed.bullet.getDistance() * bulletSpeedHackMultiplier));
    }
    else
    {
      cheatsOn = false;

      // revert player and bullet speed cheats
      speed.player.setDistance(new Integer(speed.player.getDistance() / playerSpeedHackMultiplier));
      speed.bullet.setDistance(new Integer(speed.bullet.getDistance() / bulletSpeedHackMultiplier));
    }
  }

  private EntityConfig getFromMap(EntityEnum type) {
    EntityConfig conf = entityMap.get(type);
    if (conf == null) {
      throw new AssertionError();
    }
    return conf;
  }

  public class FrameConfig {
    private Integer width;
    private Integer height;

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }
  }

  public class EntityConfig {
    private Integer width;
    private Integer height;

    public int getWidth() {
      return width;
    }

    public int getHeight() {
      return height;
    }
  }

  public class SpeedConfig {
    private Integer distance;
    private Integer rate;

    public int getDistance() {
      return distance;
    }

    public int getRate() {
      return rate;
    }

    public void setDistance(Integer d){
      distance = d;
    }

    public void setRate(Integer r){
      rate = r;
    }
  }

  public class Speed {
    private SpeedConfig invader;
    private SpeedConfig player;
    private SpeedConfig bullet;

    public SpeedConfig invader() {
      return invader;
    }

    public SpeedConfig player() {
      return player;
    }

    public SpeedConfig bullet() {
      return bullet;
    }
  }
}
