package me.drkapdor.funbazeapi.api.npc;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.base.Preconditions;
import me.drkapdor.funbazeapi.api.npc.event.PlayerNPCHideEvent;
import me.drkapdor.funbazeapi.api.npc.event.PlayerNPCShowEvent;
import me.drkapdor.funbazeapi.api.npc.modifier.*;
import me.drkapdor.funbazeapi.api.npc.profile.NPCProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

public class NPC {

  private final Collection<Player> seeingPlayers = new CopyOnWriteArraySet<>();
  private final Collection<Player> excludedPlayers = new CopyOnWriteArraySet<>();

  private final NPCProfile npcProfile;
  private final int entityId;
  private final Location location;
  private final WrappedGameProfile gameProfile;
  private final SpawnCustomizer spawnCustomizer;

  private boolean lookAtPlayer;
  private boolean imitatePlayer;

  /**
   * Creates a new me.kapdor.rpmodule.api.npc instance.
   *
   * @param npcProfile         The NPCProfile of the me.kapdor.rpmodule.api.npc.
   * @param entityId        The entity id of the me.kapdor.rpmodule.api.npc.
   * @param location        The location of the me.kapdor.rpmodule.api.npc.
   * @param lookAtPlayer    If the me.kapdor.rpmodule.api.npc should always look in the direction of the player.
   * @param imitatePlayer   If the me.kapdor.rpmodule.api.npc should imitate the player.
   * @param spawnCustomizer The spawn customizer of the me.kapdor.rpmodule.api.npc.
   */
  private NPC(
      NPCProfile npcProfile,
      int entityId,
      Location location,
      boolean lookAtPlayer,
      boolean imitatePlayer,
      SpawnCustomizer spawnCustomizer) {
    this.npcProfile = npcProfile;
    this.gameProfile = this.convertProfile(npcProfile);
    this.entityId = entityId;

    this.location = location;
    this.lookAtPlayer = lookAtPlayer;
    this.imitatePlayer = imitatePlayer;
    this.spawnCustomizer = spawnCustomizer;
  }

  /**
   * Creates a new builder instance for a me.kapdor.rpmodule.api.npc.
   *
   * @return a new builder instance for a me.kapdor.rpmodule.api.npc.
   * @since 2.5-SNAPSHOT
   */
  public static NPC.Builder builder() {
    return new Builder();
  }

  /**
   * Shows this me.kapdor.rpmodule.api.npc to a player.
   *
   * @param player             The player to show this me.kapdor.rpmodule.api.npc to.
   * @param plugin             The plugin requesting the change.
   * @param tabListRemoveTicks The ticks before removing the player from the player list after
   *                           spawning. A negative value indicates that this me.kapdor.rpmodule.api.npc shouldn't get
   *                           removed from the player list.
   */
  protected void show(Player player, Plugin plugin, long tabListRemoveTicks) {
    this.seeingPlayers.add(player);

    VisibilityModifier visibilityModifier = new VisibilityModifier(this);
    visibilityModifier.queuePlayerListChange(EnumWrappers.PlayerInfoAction.ADD_PLAYER).send(player);

    Bukkit.getScheduler().runTaskLater(plugin, () -> {
      visibilityModifier.queueSpawn().send(player);
      this.spawnCustomizer.handleSpawn(this, player);

      if (tabListRemoveTicks >= 0) {
        // keeping the NPC longer in the player list, otherwise the skin might not be shown sometimes.
        Bukkit.getScheduler().runTaskLater(
            plugin,
            () -> visibilityModifier
                .queuePlayerListChange(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER).send(player),
            tabListRemoveTicks
        );
      }

      Bukkit.getPluginManager().callEvent(new PlayerNPCShowEvent(player, this));
    }, 10L);
  }

  /**
   * Hides this me.kapdor.rpmodule.api.npc from a player.
   *
   * @param player The player to hide the me.kapdor.rpmodule.api.npc for.
   * @param plugin The plugin requesting the change.
   * @param reason The reason why the me.kapdor.rpmodule.api.npc was hidden for the player.
   */
  protected void hide(
       Player player,
       Plugin plugin,
       PlayerNPCHideEvent.Reason reason) {
    new VisibilityModifier(this)
        .queuePlayerListChange(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER)
        .queueDestroy()
        .send(player);

    this.removeSeeingPlayer(player);

    Bukkit.getScheduler().runTask(plugin,
        () -> Bukkit.getPluginManager().callEvent(new PlayerNPCHideEvent(player, this, reason)));
  }

  /**
   * Converts a NPCProfile to a protocol lib NPCProfile wrapper.
   *
   * @param NPCProfile The NPCProfile to convert.
   * @return The protocol lib wrapper.
   * @since 2.5-SNAPSHOT
   */
  protected WrappedGameProfile convertProfile(NPCProfile NPCProfile) {
    WrappedGameProfile gameProfile = new WrappedGameProfile(NPCProfile.getUniqueId(),
        NPCProfile.getName());
    NPCProfile.getProperties().forEach(property -> gameProfile.getProperties().put(
        property.getName(),
        new WrappedSignedProperty(property.getName(), property.getValue(), property.getSignature())
    ));
    return gameProfile;
  }

  /**
   * Removes this player from the players that can see the me.kapdor.rpmodule.api.npc.
   *
   * @param player The player to remove.
   */
  protected void removeSeeingPlayer(Player player) {
    this.seeingPlayers.remove(player);
  }

  /**
   * Get an immutable copy of all players which can see this me.kapdor.rpmodule.api.npc.
   *
   * @return a copy of all players seeing this me.kapdor.rpmodule.api.npc.
   */
  public Collection<Player> getSeeingPlayers() {
    return Collections.unmodifiableCollection(this.seeingPlayers);
  }

  /**
   * Get if this me.kapdor.rpmodule.api.npc is shown for the given {@code player}.
   *
   * @param player The player to check.
   * @return If the me.kapdor.rpmodule.api.npc is shown for the given {@code player}.
   */
  public boolean isShownFor(Player player) {
    return this.seeingPlayers.contains(player);
  }

  /**
   * Adds a player which should be explicitly excluded from seeing this NPC
   *
   * @param player the player to be excluded
   */
  public void addExcludedPlayer(Player player) {
    this.excludedPlayers.add(player);
  }

  /**
   * Removes a player from being explicitly excluded from seeing this NPC
   *
   * @param player the player to be included again
   */
  public void removeExcludedPlayer(Player player) {
    this.excludedPlayers.remove(player);
  }

  /**
   * A modifiable collection of all players which are not allowed to see this player. Modifications
   * to the returned collection should be done using {@link #addExcludedPlayer(Player)} and {@link
   * #removeExcludedPlayer(Player)}.
   *
   * @return a collection of all players which are explicitly excluded from seeing this NPC.
   */
  public Collection<Player> getExcludedPlayers() {
    return this.excludedPlayers;
  }

  /**
   * Get if the specified {@code player} is explicitly not allowed to see this me.kapdor.rpmodule.api.npc.
   *
   * @param player The player to check.
   * @return if the specified {@code player} is explicitly not allowed to see this me.kapdor.rpmodule.api.npc.
   */
  public boolean isExcluded(Player player) {
    return this.excludedPlayers.contains(player);
  }

  /**
   * Creates a new animation modifier which serves methods to play animations on an NPC
   *
   * @return a animation modifier modifying this NPC
   */
  public AnimationModifier animation() {
    return new AnimationModifier(this);
  }

  /**
   * Creates a new rotation modifier which serves methods related to entity rotation
   *
   * @return a rotation modifier modifying this NPC
   */
  public RotationModifier rotation() {
    return new RotationModifier(this);
  }

  /**
   * Creates a new equipemt modifier which serves methods to change an NPCs equipment
   *
   * @return an equipment modifier modifying this NPC
   */
  public EquipmentModifier equipment() {
    return new EquipmentModifier(this);
  }

  /**
   * Creates a new metadata modifier which serves methods to change an NPCs metadata, including
   * sneaking etc.
   *
   * @return a metadata modifier modifying this NPC
   */
  public MetadataModifier metadata() {
    return new MetadataModifier(this);
  }

  /**
   * Creates a new visibility modifier which serves methods to change an NPCs visibility.
   *
   * @return a visibility modifier modifying this NPC
   * @since 2.5-SNAPSHOT
   */
  public VisibilityModifier visibility() {
    return new VisibilityModifier(this);
  }

  /**
   * Creates a new labymod modifier which serves methods to play emotes and stickers.
   *
   * @return a labymod modifier modifying this NPC
   * @since 2.5-SNAPSHOT
   */
  public LabyModModifier labymod() {
    return new LabyModModifier(this);
  }

  /**
   * Get the protocol lib NPCProfile wrapper for this me.kapdor.rpmodule.api.npc. To use this method {@code ProtocolLib} is
   * needed as a dependency of your project. If you don't want to do that, use {@link #getProfile()}
   * instead.
   *
   * @return the protocol lib NPCProfile wrapper for this me.kapdor.rpmodule.api.npc
   */
  public WrappedGameProfile getGameProfile() {
    return this.gameProfile;
  }

  /**
   * The NPCProfile of this me.kapdor.rpmodule.api.npc. The returned NPCProfile is mutable, however this has no effect to this
   * me.kapdor.rpmodule.api.npc.
   *
   * @return The NPCProfile of this me.kapdor.rpmodule.api.npc.
   * @since 2.5-SNAPSHOT
   */
  public NPCProfile getProfile() {
    return this.npcProfile;
  }

  /**
   * Get the entity id of this me.kapdor.rpmodule.api.npc.
   *
   * @return the entity id of this me.kapdor.rpmodule.api.npc.
   */
  public int getEntityId() {
    return this.entityId;
  }

  /**
   * Get the location where this me.kapdor.rpmodule.api.npc is located.
   *
   * @return the location where this me.kapdor.rpmodule.api.npc is located.
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Gets if this me.kapdor.rpmodule.api.npc should always look to the player.
   *
   * @return if this me.kapdor.rpmodule.api.npc should always look to the player.
   */
  public boolean isLookAtPlayer() {
    return this.lookAtPlayer;
  }

  /**
   * Sets if this me.kapdor.rpmodule.api.npc should always look to the player.
   *
   * @param lookAtPlayer if this me.kapdor.rpmodule.api.npc should always look to the player.
   */
  public void setLookAtPlayer(boolean lookAtPlayer) {
    this.lookAtPlayer = lookAtPlayer;
  }

  /**
   * Gets if this me.kapdor.rpmodule.api.npc should always imitate the player, including sneaking and hitting.
   *
   * @return if this me.kapdor.rpmodule.api.npc should always imitate the player.
   */
  public boolean isImitatePlayer() {
    return this.imitatePlayer;
  }

  /**
   * Sets if this me.kapdor.rpmodule.api.npc should always imitate the player, including sneaking and hitting.
   *
   * @param imitatePlayer if this me.kapdor.rpmodule.api.npc should always imitate the player.
   */
  public void setImitatePlayer(boolean imitatePlayer) {
    this.imitatePlayer = imitatePlayer;
  }

  /**
   * A builder for a me.kapdor.rpmodule.api.npc.
   */
  public static class Builder {

    private NPCProfile profile;

    private boolean lookAtPlayer = true;
    private boolean imitatePlayer = true;

    private Location location = new Location(Bukkit.getWorlds().get(0), 0D, 0D, 0D);
    private SpawnCustomizer spawnCustomizer = (npc, player) -> {
    };

    /**
     * Creates a new builder instance.
     */
    private Builder() {
    }

    /**
     * Creates a new instance of the NPC builder
     *
     * @param profile a player NPCProfile defining UUID, name and textures of the NPC
     * @deprecated Use {@link NPC#builder()} instead
     */
    @Deprecated
    public Builder(NPCProfile profile) {
      this.profile = profile;
    }

    /**
     * Sets the NPCProfile of the me.kapdor.rpmodule.api.npc, cannot be changed afterwards
     *
     * @param profile the NPCProfile
     * @return this builder instance
     */
    public Builder profile(NPCProfile profile) {
      this.profile = Preconditions.checkNotNull(profile, "NPCProfile");
      return this;
    }

    /**
     * Sets the location of the me.kapdor.rpmodule.api.npc, cannot be changed afterwards
     *
     * @param location the location
     * @return this builder instance
     */
    public Builder location(Location location) {
      this.location = Preconditions.checkNotNull(location, "location");
      return this;
    }

    /**
     * Enables/disables looking at the player, default is true
     *
     * @param lookAtPlayer if the NPC should look at the player
     * @return this builder instance
     */
    public Builder lookAtPlayer(boolean lookAtPlayer) {
      this.lookAtPlayer = lookAtPlayer;
      return this;
    }

    /**
     * Enables/disables imitation of the player, such as sneaking and hitting the player, default is
     * true
     *
     * @param imitatePlayer if the NPC should imitate players
     * @return this builder instance
     */
    public Builder imitatePlayer(boolean imitatePlayer) {
      this.imitatePlayer = imitatePlayer;
      return this;
    }

    /**
     * Sets an executor which will be called every time the NPC is spawned for a certain player.
     * Permanent NPC modifications should be done in this method, otherwise they will be lost at the
     * next respawn of the NPC.
     *
     * @param spawnCustomizer the spawn customizer which will be called on every spawn
     * @return this builder instance
     */
    public Builder spawnCustomizer(SpawnCustomizer spawnCustomizer) {
      this.spawnCustomizer = Preconditions.checkNotNull(spawnCustomizer, "spawnCustomizer");
      return this;
    }

    /**
     * Passes the NPC to a pool which handles events, spawning and destruction of this NPC for
     * players
     *
     * @param pool the pool the NPC will be passed to
     * @return this builder instance
     */
    public NPC build(NPCPool pool) {
      Preconditions.checkNotNull(this.profile, "A NPCProfile must be given");
      Preconditions
          .checkArgument(this.profile.isComplete(), "The provided NPCProfile has to be complete!");

      NPC npc = new NPC(
          this.profile,
          pool.getFreeEntityId(),
          this.location,
          this.lookAtPlayer,
          this.imitatePlayer,
          this.spawnCustomizer);
      pool.takeCareOf(npc);

      return npc;
    }
  }
}
