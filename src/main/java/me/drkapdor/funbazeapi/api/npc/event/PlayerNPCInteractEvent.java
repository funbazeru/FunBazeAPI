package me.drkapdor.funbazeapi.api.npc.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import me.drkapdor.funbazeapi.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * An event called when a player interacts with a me.kapdor.rpmodule.api.npc.
 */
public class PlayerNPCInteractEvent extends PlayerNPCEvent {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  /**
   * The action type of the interact.
   */
  private final EntityUseAction action;

  /**
   * The player hand used for the interact.
   */
  private final Hand hand;

  /**
   * Constructs a new event instance.
   *
   * @param who    The player who interacted with the me.kapdor.rpmodule.api.npc.
   * @param npc    The me.kapdor.rpmodule.api.npc with whom the player has interacted.
   * @param action The action type of the interact.
   */
  public PlayerNPCInteractEvent(
          Player who,
          NPC npc,
          EnumWrappers.EntityUseAction action) {
    this(who, npc, EntityUseAction.fromHandle(action), Hand.MAIN_HAND);
  }

  /**
   * Constructs a new event instance.
   *
   * @param who    The player who interacted with the me.kapdor.rpmodule.api.npc.
   * @param npc    The me.kapdor.rpmodule.api.npc with whom the player has interacted.
   * @param action The action type of the interact.
   * @param hand   The player hand used for the interact.
   */
  public PlayerNPCInteractEvent(
      Player who,
      NPC npc,
      EnumWrappers.EntityUseAction action,
      EnumWrappers.Hand hand) {
    this(who, npc, EntityUseAction.fromHandle(action), Hand.fromHandle(hand));
  }

  /**
   * Constructs a new event instance.
   *
   * @param who    The player who interacted with the me.kapdor.rpmodule.api.npc.
   * @param npc    The me.kapdor.rpmodule.api.npc with whom the player has interacted.
   * @param action The action type of the interact.
   */
  public PlayerNPCInteractEvent(
      Player who,
      NPC npc,
      EntityUseAction action) {
    this(who, npc, action, Hand.MAIN_HAND);
  }

  /**
   * Constructs a new event instance.
   *
   * @param who    The player who interacted with the me.kapdor.rpmodule.api.npc.
   * @param npc    The me.kapdor.rpmodule.api.npc with whom the player has interacted.
   * @param action The action type of the interact.
   * @param hand   The player hand used for the interact.
   */
  public PlayerNPCInteractEvent(
      Player who,
      NPC npc,
      EntityUseAction action,
      Hand hand) {
    super(who, npc);
    this.action = action;
    this.hand = hand;
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
   * Gets the interact action as a protocol lib wrapper. This is not recommended to use, the
   * alternative is {@link #getUseAction()}.
   *
   * @return the interact action as a protocol lib wrapper.
   */
  @Deprecated
  public EnumWrappers.EntityUseAction getAction() {
    return this.action.handle;
  }

  /**
   * Gets the interact action with the associated me.kapdor.rpmodule.api.npc.
   *
   * @return the interact action with the associated me.kapdor.rpmodule.api.npc.
   * @since 2.5-SNAPSHOT
   */
  public EntityUseAction getUseAction() {
    return this.action;
  }

  /**
   * Gets the hand which the player used to interact with the associated me.kapdor.rpmodule.api.npc.
   *
   * @return The hand the player used to interact
   */
  public Hand getHand() {
    return this.hand;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  /**
   * A wrapper for the interact action with a me.kapdor.rpmodule.api.npc.
   *
   * @since 2.5-SNAPSHOT
   */
  public enum EntityUseAction {
    /**
     * A normal interact. (right click)
     */
    INTERACT(EnumWrappers.EntityUseAction.INTERACT),
    /**
     * An attack. (left click)
     */
    ATTACK(EnumWrappers.EntityUseAction.ATTACK),
    /**
     * A normal interact to a specific entity. (right click)
     */
    INTERACT_AT(EnumWrappers.EntityUseAction.INTERACT_AT);

    /**
     * All values of the action, to prevent a copy.
     */
    private static final EntityUseAction[] VALUES = values();
    /**
     * The entity use action as the protocol lib wrapper.
     */
    private final EnumWrappers.EntityUseAction handle;

    /**
     * Constructs an instance of the interact action.
     *
     * @param handle The protocol lib association with the action.
     */
    EntityUseAction(EnumWrappers.EntityUseAction handle) {
      this.handle = handle;
    }

    /**
     * Converts the protocol lib wrapper to the associated action.
     *
     * @param action The protocol lib wrapper of the association.
     * @return The association with the protocol lib wrapper action.
     * @throws IllegalArgumentException When no association was found.
     */
    private static EntityUseAction fromHandle(EnumWrappers.EntityUseAction action) {
      for (EntityUseAction value : VALUES) {
        if (value.handle == action) {
          return value;
        }
      }
      throw new IllegalArgumentException("No use action for handle: " + action);
    }
  }

  /**
   * A wrapper for the hand used for interacts.
   */
  public enum Hand {
    /**
     * Main hand of the player.
     */
    MAIN_HAND(EnumWrappers.Hand.MAIN_HAND),
    /**
     * Off hand of the player.
     */
    OFF_HAND(EnumWrappers.Hand.OFF_HAND);

    /**
     * All hand enum values, to prevent a copy.
     */
    private static final Hand[] VALUES = values();

    /**
     * The hand as the protocol lib wrapper.
     */
    private final EnumWrappers.Hand handle;

    /**
     * @param handle The hand as the protocol lib wrapper.
     */
    Hand(EnumWrappers.Hand handle) {
      this.handle = handle;
    }

    /**
     * Converts the protocol lib wrapper to the associated hand.
     *
     * @param hand The protocol lib wrapper of the association.
     * @return The association with the protocol lib wrapper hand.
     * @throws IllegalArgumentException When no association was found.
     */
    private static Hand fromHandle(EnumWrappers.Hand hand) {
      for (Hand value : VALUES) {
        if (value.handle == hand) {
          return value;
        }
      }
      throw new IllegalArgumentException("No hand for handle: " + hand);
    }
  }
}
