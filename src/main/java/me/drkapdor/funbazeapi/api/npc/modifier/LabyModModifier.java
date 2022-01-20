package me.drkapdor.funbazeapi.api.npc.modifier;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.drkapdor.funbazeapi.api.npc.NPC;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

/**
 * A modifier for modifying playing labymod emotes and stickers.
 *
 * @since 2.5-SNAPSHOT
 */
public class LabyModModifier extends NPCModifier {

  public static final MinecraftKey LABYMOD_PLUGIN_CHANNEL = new MinecraftKey("labymod3", "main");

  /**
   * Creates a new modifier.
   *
   * @param npc The me.kapdor.rpmodule.api.npc this modifier is for.
   * @see NPC#equipment()
   */
  public LabyModModifier(NPC npc) {
    super(npc);
  }

  /**
   * Queues the play of an emote or sticker.
   *
   * @param action             The action to play.
   * @param playbackIdentifier The identifier of the action (emote or sticker id)
   * @return The same instance of this class, for chaining.
   */
  public LabyModModifier queue(LabyModAction action, int playbackIdentifier) {
    PacketContainer container = super.newContainer(PacketType.Play.Server.CUSTOM_PAYLOAD, false);
    if (MINECRAFT_VERSION >= 13) {
      container.getMinecraftKeys().write(0, LABYMOD_PLUGIN_CHANNEL);
    } else {
      container.getStrings().write(0, LABYMOD_PLUGIN_CHANNEL.getFullKey());
    }

    ByteBuf content = this.createContent(action, playbackIdentifier);

    if (MinecraftReflection.is(MinecraftReflection.getPacketDataSerializerClass(), content)) {
      container.getModifier().withType(ByteBuf.class).write(0, content);
    } else {
      Object serializer = MinecraftReflection.getPacketDataSerializer(content);
      container.getModifier().withType(ByteBuf.class).write(0, serializer);
    }

    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(Iterable<? extends Player> players) {
    super.send(players, true);
  }

  /**
   * Creates the lmc message contents for write into a data serializer.
   *
   * @param action             The action to play.
   * @param playbackIdentifier The identifier of the action (emote or sticker id)
   * @return The lmc message contents written into a byte buf.
   */
  protected ByteBuf createContent(LabyModAction action, int playbackIdentifier) {
    ByteBuf byteBuf = Unpooled.buffer();
    this.writeString(byteBuf, action.messageKey);

    JsonArray array = new JsonArray();
    JsonObject data = new JsonObject();

    data.addProperty("uuid", super.npc.getProfile().getUniqueId().toString());
    data.addProperty(action.objectPropertyName, playbackIdentifier);

    array.add(data);
    this.writeString(byteBuf, array.toString());

    return byteBuf;
  }

  /**
   * Writes a string into the given byte buf.
   *
   * @param byteBuf The byte buf to write the data to.
   * @param string  The string to write into the byte buf.
   */
  protected void writeString(ByteBuf byteBuf, String string) {
    byte[] values = string.getBytes(StandardCharsets.UTF_8);
    this.writeVarInt(byteBuf, values.length);
    byteBuf.writeBytes(values);
  }

  /**
   * Writes a var int into the specified byte buf as defined by google in
   * <a href="https://developers.google.com/protocol-buffers/docs/encoding#varints">Base 128
   * Varints</a>.
   *
   * @param byteBuf The byte buf to write the int to.
   * @param value   The int value to write into the byte buf.
   */
  protected void writeVarInt(ByteBuf byteBuf, int value) {
    while ((value & -128) != 0) {
      byteBuf.writeByte(value & 127 | 128);
      value >>>= 7;
    }
    byteBuf.writeByte(value);
  }

  /**
   * A LabyMod action which can be played to a user using an me.kapdor.rpmodule.api.npc.
   */
  public enum LabyModAction {
    /**
     * Plays an emote to the client. For a overview of all emotes see
     * <a href="https://docs.labymod.net/pages/server/emote_api/">here</a>.
     */
    EMOTE("emote_api", "emote_id"),
    /**
     * Plays a sticker to the client. For a overview of all stickers see
     * <a href="https://dl.labymod.net/stickers.json">here</a>.
     */
    STICKER("sticker_api", "sticker_id");

    /**
     * The message key of the message send to the lmc channel.
     */
    private final String messageKey;
    /**
     * The property name of the action identifier in the backing json object.
     */
    private final String objectPropertyName;

    /**
     * Constructs a new laby mod action.
     *
     * @param messageKey         The message key of the message send to the lmc channel.
     * @param objectPropertyName The property name of the action identifier in the backing json
     *                           object.
     */
    LabyModAction(String messageKey, String objectPropertyName) {
      this.messageKey = messageKey;
      this.objectPropertyName = objectPropertyName;
    }

    /**
     * Get the message key of the message send to the lmc channel.
     *
     * @return the message key of the message send to the lmc channel.
     */
    public String getMessageKey() {
      return this.messageKey;
    }

    /**
     * Get he property name of the action identifier in the backing json object.
     *
     * @return the property name of the action identifier in the backing json object.
     */
    public String getObjectPropertyName() {
      return this.objectPropertyName;
    }
  }
}
