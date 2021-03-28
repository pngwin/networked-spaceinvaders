package spaceinvaders.command.server;

import static spaceinvaders.command.ProtocolEnum.UDP;

import spaceinvaders.command.Command;
import spaceinvaders.server.game.GameLoop;

/** Move a player one step left. */
public class ToggleCheatCommand extends Command {
  private transient GameLoop executor;
  private Integer id;

  ToggleCheatCommand() {
    super(ToggleCheatCommand.class.getName(),UDP);
  }

  /**
   * @param id player id.
   */
  public ToggleCheatCommand(int id) {
    this();
    this.id = id;
  }

  @Override
  public void execute() {
    executor.toggleCheat();
  }

  @Override
  public void setExecutor(Object executor) {
    if (executor instanceof GameLoop) {
      this.executor = (GameLoop) executor;
    } else {
      // This should never happen.
      throw new AssertionError();
    }
  }
}
