package me.drkapdor.funbazeapi.api.npc;

import org.bukkit.entity.Player;

/**
 * A spawn customizer for modifying a me.drkapdor.api.npc at spawn time for a specific player.
 */
@FunctionalInterface
public interface SpawnCustomizer {

  /**
   * Being called when a NPC was spawned for a certain player. Permanent NPC modifications should be
   * done in this method, otherwise they will be lost at the next respawn of the NPC. Modifications
   * should only be sent to this one player.
   *
   * @param npc    the NPC that has been spawned
   * @param player the player the NPC has been spawned for
   */
  void handleSpawn(NPC npc, Player player);
}
