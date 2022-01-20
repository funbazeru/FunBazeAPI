package me.drkapdor.funbazeapi.api.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;
import com.comphenix.protocol.wrappers.EnumWrappers.Hand;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import me.drkapdor.funbazeapi.api.npc.event.PlayerNPCHideEvent;
import me.drkapdor.funbazeapi.api.npc.event.PlayerNPCInteractEvent;
import me.drkapdor.funbazeapi.api.npc.modifier.AnimationModifier;
import me.drkapdor.funbazeapi.api.npc.modifier.LabyModModifier;
import me.drkapdor.funbazeapi.api.npc.modifier.MetadataModifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NPCPool implements Listener {

  private static final Random RANDOM = new Random();

  private final Plugin plugin;

  private final double spawnDistance;
  private final double actionDistance;
  private final long tabListRemoveTicks;

  private final Map<Integer, NPC> npcMap = new ConcurrentHashMap<>();

  /**
   * Creates a new NPC pool which handles events, spawning and destruction of the NPCs for players.
   * Please use {@link #createDefault(Plugin)} instead, this constructor will be private in a
   * further release.
   *
   * @param plugin the instance of the plugin which creates this pool
   * @deprecated Use {@link #createDefault(Plugin)} instead
   */
  @Deprecated
  public NPCPool(Plugin plugin) {
    this(plugin, 50, 20, 30);
  }

  /**
   * Creates a new NPC pool which handles events, spawning and destruction of the NPCs for players.
   * Please use {@link #builder(Plugin)} instead, this constructor will be private in a further
   * release.
   *
   * @param plugin             the instance of the plugin which creates this pool
   * @param spawnDistance      the distance in which NPCs are spawned for players
   * @param actionDistance     the distance in which NPC actions are displayed for players
   * @param tabListRemoveTicks the time in ticks after which the NPC will be removed from the
   *                           players tab
   */
  @Deprecated
  public NPCPool(Plugin plugin, int spawnDistance, int actionDistance,
      long tabListRemoveTicks) {
    Preconditions.checkArgument(spawnDistance > 0 && actionDistance > 0, "Distance has to be > 0!");
    Preconditions.checkArgument(actionDistance <= spawnDistance,
        "Action distance cannot be higher than spawn distance!");

    this.plugin = plugin;

    // limiting the spawn distance to the Bukkit view distance to avoid NPCs not being shown
    this.spawnDistance = Math.min(
        spawnDistance * spawnDistance,
        Math.pow(Bukkit.getViewDistance() << 4, 2));
    this.actionDistance = actionDistance * actionDistance;
    this.tabListRemoveTicks = tabListRemoveTicks;

    Bukkit.getPluginManager().registerEvents(this, plugin);

    // communication with LabyMod
    String labyModPluginChannel = LabyModModifier.LABYMOD_PLUGIN_CHANNEL.getFullKey();
    // we might send messages on this channel
    Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, labyModPluginChannel);
    if (!Bukkit.getMessenger().isIncomingChannelRegistered(plugin, labyModPluginChannel)) {
      Bukkit.getMessenger().registerIncomingPluginChannel(plugin, labyModPluginChannel,
          (channel, player, message) -> {
            // we don't actually handle LabyMod messages, we just register
            // incoming messages to make sure minecraft:register is sent to the proxy,
            // so that it will forward our messages on the LabyMod channel to the player
          });
    }

    this.addInteractListener();
    this.npcTick();
  }

  /**
   * Creates a new me.kapdor.rpmodule.api.npc pool with the default values of a me.kapdor.rpmodule.api.npc pool. The default values of a builder
   * are {@code spawnDistance} to {@code 50}, {@code actionDistance} to {@code 20} and {@code
   * tabListRemoveTicks} to {@code 30}.
   *
   * @param plugin the instance of the plugin which creates this pool.
   * @return the created me.kapdor.rpmodule.api.npc pool with the default values of a pool.
   * @since 2.5-SNAPSHOT
   */
  public static NPCPool createDefault(Plugin plugin) {
    return NPCPool.builder(plugin).build();
  }

  /**
   * Creates a new builder for a me.kapdor.rpmodule.api.npc pool.
   *
   * @param plugin the instance of the plugin which creates the builder for the pool.
   * @return a new builder for creating a me.kapdor.rpmodule.api.npc pool instance.
   * @since 2.5-SNAPSHOT
   */
  public static Builder builder(Plugin plugin) {
    return new Builder(plugin);
  }

  /**
   * Adds a packet listener for listening to all use entity packets sent by a client.
   */
  protected void addInteractListener() {
    ProtocolLibrary.getProtocolManager()
        .addPacketListener(new PacketAdapter(this.plugin, PacketType.Play.Client.USE_ENTITY) {
          @Override
          public void onPacketReceiving(PacketEvent event) {
            PacketContainer packetContainer = event.getPacket();
            int targetId = packetContainer.getIntegers().read(0);
            if (NPCPool.this.npcMap.containsKey(targetId)) {
              NPC npc = NPCPool.this.npcMap.get(targetId);
              EntityUseAction action = packetContainer.getEntityUseActions().read(0);

              Hand hand = action == EntityUseAction.ATTACK
                  ? Hand.MAIN_HAND
                  : packetContainer.getHands().optionRead(0).orElse(Hand.MAIN_HAND);

              Bukkit.getScheduler().runTask(
                  NPCPool.this.plugin,
                  () -> Bukkit.getPluginManager().callEvent(
                      new PlayerNPCInteractEvent(
                          event.getPlayer(),
                          npc,
                          action,
                          hand))
              );
            }
          }
        });
  }

  /**
   * Starts the me.kapdor.rpmodule.api.npc tick.
   */
  protected void npcTick() {
    Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
      for (Player player : ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
        for (NPC npc : this.npcMap.values()) {
          if (!npc.getLocation().getWorld().equals(player.getLocation().getWorld())) {
            if (npc.isShownFor(player)) {
              npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.SPAWN_DISTANCE);
            }
            continue;
          }

          double distance = npc.getLocation().distanceSquared(player.getLocation());
          boolean inRange = distance <= this.spawnDistance;

          if ((npc.isExcluded(player) || !inRange) && npc.isShownFor(player)) {
            npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.SPAWN_DISTANCE);
          } else if ((!npc.isExcluded(player) && inRange) && !npc.isShownFor(player)) {
            npc.show(player, this.plugin, this.tabListRemoveTicks);
          }

          if (npc.isShownFor(player) && npc.isLookAtPlayer() && distance <= this.actionDistance) {
            npc.rotation().queueLookAt(player.getLocation()).send(player);
          }
        }
      }
    }, 20, 2);
  }

  /**
   * @return A free entity id which can be used for NPCs
   */
  protected int getFreeEntityId() {
    int id;

    do {
      id = RANDOM.nextInt(Integer.MAX_VALUE);
    } while (this.npcMap.containsKey(id));

    return id;
  }

  /**
   * Adds the given {@code me.kapdor.rpmodule.api.npc} to the list of handled NPCs of this pool.
   *
   * @param npc The me.kapdor.rpmodule.api.npc to add.
   * @see NPC#builder()
   */
  protected void takeCareOf(NPC npc) {
    this.npcMap.put(npc.getEntityId(), npc);
  }

  /**
   * Gets a specific me.kapdor.rpmodule.api.npc by the given {@code entityId}.
   *
   * @param entityId the entity id of the me.kapdor.rpmodule.api.npc to get.
   * @return The me.kapdor.rpmodule.api.npc or {@code null} if there is no me.kapdor.rpmodule.api.npc with the given entity id.
   * @deprecated Use {@link #getNpc(int)} instead.
   */
  @Deprecated
  public NPC getNPC(int entityId) {
    return this.npcMap.get(entityId);
  }

  /**
   * Gets a specific me.kapdor.rpmodule.api.npc by the given {@code entityId}.
   *
   * @param entityId the entity id of the me.kapdor.rpmodule.api.npc to get.
   * @return The me.kapdor.rpmodule.api.npc by the given {@code entityId}.
   * @since 2.5-SNAPSHOT
   */
  public Optional<NPC> getNpc(int entityId) {
    return Optional.ofNullable(this.npcMap.get(entityId));
  }

  /**
   * Gets a specific me.kapdor.rpmodule.api.npc by the given {@code uniqueId}.
   *
   * @param uniqueId the entity unique id of the me.kapdor.rpmodule.api.npc to get.
   * @return The me.kapdor.rpmodule.api.npc by the given {@code uniqueId}.
   * @since 2.5-SNAPSHOT
   */
  public Optional<NPC> getNpc(UUID uniqueId) {
    return this.npcMap.values().stream()
        .filter(npc -> npc.getProfile().getUniqueId().equals(uniqueId)).findFirst();
  }

  /**
   * Removes the given me.kapdor.rpmodule.api.npc by it's entity id from the handled NPCs of this pool.
   *
   * @param entityId the entity id of the me.kapdor.rpmodule.api.npc to get.
   */
  public void removeNPC(int entityId) {
    this.getNpc(entityId).ifPresent(npc -> {
      this.npcMap.remove(entityId);
      npc.getSeeingPlayers()
          .forEach(player -> npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.REMOVED));
    });
  }

  /**
   * Get an unmodifiable copy of all NPCs handled by this pool.
   *
   * @return a copy of the NPCs this pool manages.
   */
  public Collection<NPC> getNPCs() {
    return Collections.unmodifiableCollection(this.npcMap.values());
  }

  @EventHandler
  public void handleRespawn(PlayerRespawnEvent event) {
    Player player = event.getPlayer();

    this.npcMap.values().stream()
        .filter(npc -> npc.isShownFor(player))
        .forEach(npc -> npc.hide(player, this.plugin, PlayerNPCHideEvent.Reason.RESPAWNED));
  }

  @EventHandler
  public void handleQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    this.npcMap.values().stream()
        .filter(npc -> npc.isShownFor(player) || npc.isExcluded(player))
        .forEach(npc -> {
          npc.removeSeeingPlayer(player);
          npc.removeExcludedPlayer(player);
        });
  }

  @EventHandler
  public void handleSneak(PlayerToggleSneakEvent event) {
    Player player = event.getPlayer();

    this.npcMap.values().stream()
        .filter(npc -> npc.isImitatePlayer() && npc.isShownFor(player))
        .filter(npc -> npc.getLocation().getWorld().equals(player.getWorld())
            && npc.getLocation().distanceSquared(player.getLocation()) <= this.actionDistance)
        .forEach(npc -> npc.metadata()
            .queue(MetadataModifier.EntityMetadata.SNEAKING, event.isSneaking()).send(player));
  }

  @EventHandler
  public void handleClick(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    if (event.getAction() == Action.LEFT_CLICK_AIR
        || event.getAction() == Action.LEFT_CLICK_BLOCK) {
      this.npcMap.values().stream()
          .filter(npc -> npc.isImitatePlayer() && npc.isShownFor(player))
          .filter(npc -> npc.getLocation().getWorld().equals(player.getWorld())
              && npc.getLocation().distanceSquared(player.getLocation()) <= this.actionDistance)
          .forEach(npc -> npc.animation().queue(AnimationModifier.EntityAnimation.SWING_MAIN_ARM)
              .send(player));
    }
  }

  /**
   * A builder for a me.kapdor.rpmodule.api.npc pool.
   *
   * @since 2.5-SNAPSHOT
   */
  public static class Builder {

    /**
     * The instance of the plugin which creates this pool
     */
    private final Plugin plugin;

    /**
     * The distance in which NPCs are spawned for players
     */
    private int spawnDistance = 50;
    /**
     * The distance in which NPC actions are displayed for players
     */
    private int actionDistance = 20;
    /**
     * The time in ticks after which the NPC will be removed from the players tab
     */
    private long tabListRemoveTicks = 30;

    /**
     * Creates a new builder for a me.kapdor.rpmodule.api.npc pool.
     *
     * @param plugin The instance of the plugin which creates the builder.
     */
    private Builder(Plugin plugin) {
      this.plugin = Preconditions.checkNotNull(plugin, "plugin");
    }

    /**
     * Sets the spawn distance in which NPCs are spawned for players. Must be higher than {@code
     * 0}.
     *
     * @param spawnDistance the spawn distance in which NPCs are spawned for players.
     * @return The same instance of this class, for chaining.
     */
    public Builder spawnDistance(int spawnDistance) {
      Preconditions.checkArgument(spawnDistance > 0, "Spawn distance must be more than 0");
      this.spawnDistance = spawnDistance;
      return this;
    }

    /**
     * Sets the distance in which NPC actions are displayed for players. Must be higher than {@code
     * 0}.
     *
     * @param actionDistance the distance in which NPC actions are displayed for players.
     * @return The same instance of this class, for chaining.
     */
    public Builder actionDistance(int actionDistance) {
      Preconditions.checkArgument(actionDistance > 0, "Action distance must be more than 0");
      this.actionDistance = actionDistance;
      return this;
    }

    /**
     * Sets the distance in which NPC actions are displayed for players. A negative value indicates
     * that the me.kapdor.rpmodule.api.npc is never removed from the player list by default.
     *
     * @param tabListRemoveTicks the distance in which NPC actions are displayed for players.
     * @return The same instance of this class, for chaining.
     */
    public Builder tabListRemoveTicks(long tabListRemoveTicks) {
      this.tabListRemoveTicks = tabListRemoveTicks;
      return this;
    }

    /**
     * Creates a new me.kapdor.rpmodule.api.npc tool by the values passed to the builder.
     *
     * @return The created me.kapdor.rpmodule.api.npc pool.
     */
    public NPCPool build() {
      return new NPCPool(this.plugin, this.spawnDistance, this.actionDistance,
          this.tabListRemoveTicks);
    }
  }
}
