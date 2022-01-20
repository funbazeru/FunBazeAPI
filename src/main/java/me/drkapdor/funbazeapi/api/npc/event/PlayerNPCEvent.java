package me.drkapdor.funbazeapi.api.npc.event;

import me.drkapdor.funbazeapi.api.npc.NPC;
import me.drkapdor.funbazeapi.api.npc.modifier.NPCModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

/**
 * Represents an event fired when an action between a player and a me.kapdor.rpjobs.api.npc occurs.
 */
public abstract class PlayerNPCEvent extends PlayerEvent {

  /**
   * The me.kapdor.rpjobs.api.npc involved in this event.
   */
  private final NPC npc;

  /**
   * Constructs a new event instance.
   *
   * @param who The player involved in this event
   * @param npc The me.kapdor.rpjobs.api.npc involved in this event
   */
  public PlayerNPCEvent(Player who, NPC npc) {
    super(who);
    this.npc = npc;
  }

  /**
   * Sends the queued data in the provided {@link NPCModifier}s to the player involved in this
   * event.
   *
   * @param npcModifiers The {@link NPCModifier}s whose data should be send
   */
  public void send(NPCModifier... npcModifiers) {
    for (NPCModifier npcModifier : npcModifiers) {
      npcModifier.send(super.getPlayer());
    }
  }

  /**
   * @return The me.kapdor.rpjobs.api.npc involved in this event
   */
  public NPC getNPC() {
    return this.npc;
  }
}
