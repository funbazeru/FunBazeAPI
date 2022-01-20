package me.drkapdor.funbazeapi.api.npc.modifier;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.drkapdor.funbazeapi.api.npc.NPC;
import org.bukkit.Location;

/**
 * A modifier for modifying the rotation of a player.
 */
public class RotationModifier extends NPCModifier {

  /**
   * Creates a new modifier.
   *
   * @param npc The me.kapdor.rpjobs.api.npc this modifier is for.
   * @see NPC#rotation()
   */
  public RotationModifier(NPC npc) {
    super(npc);
  }

  /**
   * Queues the change of the current rotation of the wrapped me.kapdor.rpjobs.api.npc.
   *
   * @param yaw   The yaw of the target rotated location.
   * @param pitch The pitch of the target rotated location.
   * @return The same instance of this class, for chaining.
   */
  public RotationModifier queueRotate(float yaw, float pitch) {
    byte yawAngle = (byte) (yaw * 256F / 360F);
    byte pitchAngle = (byte) (pitch * 256F / 360F);

    PacketContainer entityHeadLookContainer = super
        .newContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
    entityHeadLookContainer.getBytes().write(0, yawAngle);

    PacketContainer bodyRotateContainer;
    if (MINECRAFT_VERSION < 9) {
      bodyRotateContainer = super.newContainer(PacketType.Play.Server.ENTITY_TELEPORT);

      Location location = super.npc.getLocation();
      bodyRotateContainer.getIntegers()
          .write(1, (int) Math.floor(location.getX() * 32.0D))
          .write(2, (int) Math.floor(location.getY() * 32.0D))
          .write(3, (int) Math.floor(location.getZ() * 32.0D));
    } else {
      bodyRotateContainer = super.newContainer(PacketType.Play.Server.ENTITY_LOOK);
    }

    bodyRotateContainer.getBytes()
        .write(0, yawAngle)
        .write(1, pitchAngle);
    bodyRotateContainer.getBooleans().write(0, true);

    return this;
  }

  /**
   * Queues the change of the current rotation of the wrapped me.kapdor.rpjobs.api.npc.
   *
   * @param location the target location the me.kapdor.rpjobs.api.npc should look to.
   * @return The same instance of this class, for chaining.
   */
  public RotationModifier queueLookAt(Location location) {
    double xDifference = location.getX() - super.npc.getLocation().getX();
    double yDifference = location.getY() - super.npc.getLocation().getY();
    double zDifference = location.getZ() - super.npc.getLocation().getZ();

    double r = Math
        .sqrt(Math.pow(xDifference, 2) + Math.pow(yDifference, 2) + Math.pow(zDifference, 2));

    float yaw = (float) (-Math.atan2(xDifference, zDifference) / Math.PI * 180D);
    yaw = yaw < 0 ? yaw + 360 : yaw;

    float pitch = (float) (-Math.asin(yDifference / r) / Math.PI * 180D);

    return this.queueRotate(yaw, pitch);
  }
}
