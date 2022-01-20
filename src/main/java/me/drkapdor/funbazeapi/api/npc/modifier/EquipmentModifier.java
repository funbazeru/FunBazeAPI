package me.drkapdor.funbazeapi.api.npc.modifier;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import me.drkapdor.funbazeapi.api.npc.NPC;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

/**
 * A modifier for modifying the equipment of a player.
 */
public class EquipmentModifier extends NPCModifier {

  /**
   * The id of the main hand item slot.
   *
   * @since 2.5-SNAPSHOT
   */
  public static final int MAINHAND = 0;
  /**
   * The id of the off hand item slot.
   *
   * @since 2.5-SNAPSHOT
   */
  public static final int OFFHAND = 1;
  /**
   * The id of the feet armor item slot.
   *
   * @since 2.5-SNAPSHOT
   */
  public static final int FEET = 2;
  /**
   * The id of the legs armor item slot.
   *
   * @since 2.5-SNAPSHOT
   */
  public static final int LEGS = 3;
  /**
   * The id of the chest armor item slot.
   *
   * @since 2.5-SNAPSHOT
   */
  public static final int CHEST = 4;
  /**
   * The id of the head armor item slot.
   *
   * @since 2.5-SNAPSHOT
   */
  public static final int HEAD = 5;

  /**
   * Creates a new modifier.
   *
   * @param npc The me.kapdor.rpmodule.api.npc this modifier is for.
   * @see NPC#equipment()
   */
  public EquipmentModifier(NPC npc) {
    super(npc);
  }

  /**
   * Queues the change of an item slot using the protocol lib item slot enum wrapper directly. If
   * you don't want to use protocol lib as a dependency, use {@link #queue(int, ItemStack)} with the
   * item slot numbers defined at the top of this class.
   *
   * @param itemSlot  The item slot the modification should take place.
   * @param equipment The item which should be placed at the specific slot.
   * @return The same instance of this class, for chaining.
   */
  public EquipmentModifier queue(
       EnumWrappers.ItemSlot itemSlot,
       ItemStack equipment) {
    PacketContainer packetContainer = super.newContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);

    if (MINECRAFT_VERSION < 16) {
      packetContainer.getItemSlots().write(MINECRAFT_VERSION < 9 ? 1 : 0, itemSlot);
      packetContainer.getItemModifier().write(0, equipment);
    } else {
      packetContainer.getSlotStackPairLists()
          .write(0, Collections.singletonList(new Pair<>(itemSlot, equipment)));
    }

    return this;
  }

  /**
   * Queues the change of an item slot using the specified slot number.
   *
   * @param itemSlot  The item slot the modification should take place.
   * @param equipment The item which should be placed at the specific slot.
   * @return The same instance of this class, for chaining.
   */
  public EquipmentModifier queue(int itemSlot, ItemStack equipment) {
    PacketContainer packetContainer = super.newContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);

    if (MINECRAFT_VERSION < 16) {
      packetContainer.getIntegers().write(MINECRAFT_VERSION < 9 ? 1 : 0, itemSlot);
      packetContainer.getItemModifier().write(0, equipment);
    } else {
      for (EnumWrappers.ItemSlot slot : EnumWrappers.ItemSlot.values()) {
        if (slot.ordinal() == itemSlot) {
          packetContainer.getSlotStackPairLists()
              .write(0, Collections.singletonList(new Pair<>(slot, equipment)));
          break;
        }
      }
    }

    return this;
  }
}
