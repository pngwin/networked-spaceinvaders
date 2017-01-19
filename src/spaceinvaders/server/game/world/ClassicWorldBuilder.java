package spaceinvaders.server.game.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import spaceinvaders.game.Entity;
import spaceinvaders.game.EntityEnum;
import spaceinvaders.game.GameConfig;

/**
 * Assembles parts of the {@link World}.
 */
public class ClassicWorldBuilder implements WorldBuilder {
  private final GameConfig config = GameConfig.getInstance();
  private final World world = new World();

  @Override
  public void buildInvaders() {
    final int frameW = config.frame().getWidth();
    final int invaderW = config.invader().getWidth();
    final int invaderH = config.invader().getHeight();
    final int invaderCols = config.getInvaderCols();
    final int invaderRows = config.getInvaderRows();
    final int jumpX = invaderW + config.playerBullet().getWidth() * 3;
    final int jumpY = invaderH + invaderH / 2;
    final int witdthOffset = (frameW - invaderCols * jumpX + Math.abs(invaderW - jumpX)) / 2;
    final int heightOffset = invaderH;

    if (witdthOffset <= 0 || heightOffset <= 0) {
      throw new AssertionError();
    }

    int offsetX = witdthOffset;
    int offsetY = heightOffset;
    List<LogicEntity> invaders = new ArrayList<>(invaderRows * invaderCols);
    for (int row = 0; row < invaderRows; ++row) {
      for (int col = 0; col < invaderCols; ++col) {
        invaders.add(new Invader(offsetX,offsetY));
        offsetX += jumpX;
      }
      offsetX = witdthOffset;
      offsetY += jumpY;
    }
    world.setEntities(EntityEnum.INVADER,invaders);
  }

  @Override
  public void buildPlayers(int teamSize) {
    if (teamSize <= 0) {
      throw new AssertionError();
    }

    final int frameW = config.frame().getWidth();
    final int frameH = config.frame().getHeight();
    final int playerW = config.player().getWidth();
    final int playerH = config.player().getHeight();
    final int jumpX = playerW * 3;
    final int witdthOffset = (frameW - teamSize * jumpX + Math.abs(playerW - jumpX)) / 2;
    final int heightOffset = frameH - playerH - playerH / 2;

    if (witdthOffset <= 0) {
      throw new AssertionError();
    }

    int offsetX = witdthOffset;
    List<LogicEntity> players = new ArrayList<>(teamSize);
    for (int player = 0; player < teamSize; ++player) {
      players.add(new Player(offsetX,heightOffset));
      offsetX += witdthOffset;
    }
    world.setEntities(EntityEnum.PLAYER,players);
  }

  /**
   * <b>Warning:</b>
   * Must be called after the players have been built.
   *
   * @throws NullPointerException - if the players have not been buit.
   */
  @Override
  public void buildShields() {
    Iterator<LogicEntity> it = world.getIterator(EntityEnum.PLAYER);
    if (it == null) {
      throw new NullPointerException();
    }

    final int shieldW = config.shield().getWidth();
    final int shieldH = config.shield().getHeight();
    final int playerW = config.player().getWidth();
    final int playerH = config.player().getHeight();
    final int shieldsPerPlayer = config.getShieldsPerPlayer();
    final int widthOffset = playerW / 2 - shieldW / 2 - shieldW * shieldsPerPlayer / 2;
    final int heightOffset = playerH - shieldH * 2;

    List<LogicEntity> shields = new ArrayList<>();
    while (it.hasNext()) {
      LogicEntity player = it.next();
      int offsetX = player.getX() + widthOffset;
      for (int shield = 0; shield < shieldsPerPlayer; ++shield) {
        shields.add(new Shield(offsetX,heightOffset));
        offsetX += shieldW;
      }
    }
    world.setEntities(EntityEnum.SHIELD,shields);
  }

  @Override
  public void buildBullets() {
    world.setEntities(EntityEnum.PLAYER_BULLET,new ArrayList<LogicEntity>());
    world.setEntities(EntityEnum.INVADER_BULLET,new ArrayList<LogicEntity>());
  }

  @Override
  public World getWorld() {
    return world;
  }
}