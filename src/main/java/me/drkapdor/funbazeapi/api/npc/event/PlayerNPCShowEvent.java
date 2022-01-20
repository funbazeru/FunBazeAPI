package me.drkapdor.funbazeapi.api.npc.event;

import me.drkapdor.funbazeapi.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * An event fired when a NPC is shown for a certain player.
 */
public class PlayerNPCShowEvent extends PlayerNPCEvent {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  /**
   * Constructs a new event instance.
   *
   * @param who The player who is now seeing the me.kapdor.rpmodule.api.npc
   * @param npc The me.kapdor.rpmodule.api.npc the player is now seeing
   */
  public PlayerNPCShowEvent(Player who, NPC npc) {
    super(who, npc);
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
   * {@inheritDoc}
   */
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
