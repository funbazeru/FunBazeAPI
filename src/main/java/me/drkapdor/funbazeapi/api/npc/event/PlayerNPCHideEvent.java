package me.drkapdor.funbazeapi.api.npc.event;

import me.drkapdor.funbazeapi.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * An event fired when a me.dekapdor.api.npc is hidden for a certain player.
 */
public class PlayerNPCHideEvent extends PlayerNPCEvent {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  /**
   * The reason why the me.drkapdor.api.npc was hidden.
   */
  private final Reason reason;

  /**
   * Constructs a new event instance.
   *
   * @param who    The player who is no longer seeing the me.kapdor.rpmodule.api.npc
   * @param npc    The me.kapdor.rpmodule.api.npc the player is no longer seeing
   * @param reason The reason why the me.kapdor.rpmodule.api.npc was hidden
   */
  public PlayerNPCHideEvent(Player who, NPC npc, Reason reason) {
    super(who, npc);
    this.reason = reason;
  }

  /**
   * Get the handlers for this event.
   *
   * @return the handlers for this event.
   */
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  /**
   * @return The reason why the me.drkapdor.api.npc was hidden
   */
  public Reason getReason() {
    return this.reason;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  /**
   * Represents a reason why a me.kapdor.rpmodule.api.npc was hidden for a player.
   */
  public enum Reason {
    /**
     * The player has manually been excluded from seeing the me.kapdor.rpmodule.api.npc.
     */
    EXCLUDED,
    /**
     * The distance from me.kapdor.rpmodule.api.npc and player is now higher than the configured spawn distance.
     */
    SPAWN_DISTANCE,
    /**
     * The me.drkapdor.rpmodule.api.npc was removed from the pool.
     */
    REMOVED,
    /**
     * The player seeing the me.kapdor.rpmodule.api.npc respawned.
     */
    RESPAWNED
  }
}
