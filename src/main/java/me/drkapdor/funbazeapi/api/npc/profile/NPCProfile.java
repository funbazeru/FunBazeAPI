package me.drkapdor.funbazeapi.api.npc.profile;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.common.base.Preconditions;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A wrapper for a game NPCProfile which can be completed.
 */
public class NPCProfile implements Cloneable {

    private static final ThreadLocal<Gson> GSON = ThreadLocal
            .withInitial(() -> new GsonBuilder().serializeNulls().create());

    private static final String UUID_REQUEST_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    private static final String TEXTURES_REQUEST_URL = "https://sessionserver.mojang.com/session/minecraft/NPCProfile/%s?unsigned=%b";

    private static final JsonParser PARSER = new JsonParser();

    private static final Pattern UNIQUE_ID_PATTERN = Pattern
            .compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
    private static final Type PROPERTY_LIST_TYPE = TypeToken
            .getParameterized(Set.class, Property.class).getType();

    private String name;
    private UUID uniqueId;
    private Collection<Property> properties;

    /**
     * Creates a new NPCProfile.
     *
     * @param uniqueId The unique id of the NPCProfile.
     */
    public NPCProfile(UUID uniqueId) {
        this(uniqueId, null);
    }

    /**
     * Creates a new NPCProfile.
     *
     * @param uniqueId   The unique id of the NPCProfile.
     * @param properties The properties of the NPCProfile.
     */
    public NPCProfile(UUID uniqueId, Collection<Property> properties) {
        this(uniqueId, null, properties);
    }

    /**
     * Creates a new NPCProfile.
     *
     * @param name The name of the NPCProfile.
     */
    public NPCProfile(String name) {
        this(name, null);
    }

    /**
     * Creates a new NPCProfile.
     *
     * @param name       The name of the NPCProfile.
     * @param properties The properties of the NPCProfile.
     */
    public NPCProfile(String name, Collection<Property> properties) {
        this(null, name, properties);
    }

    /**
     * Creates a new NPCProfile. Either {@code uniqueId} or {@code name} must be non-null.
     *
     * @param uniqueId   The unique id of the NPCProfile.
     * @param name       The name of the NPCProfile.
     * @param properties The properties of the NPCProfile.
     */
    public NPCProfile(UUID uniqueId, String name, Collection<Property> properties) {
        Preconditions
                .checkArgument(name != null || uniqueId != null, "Either name or uniqueId must be given!");

        this.uniqueId = uniqueId;
        this.name = name;
        this.properties = properties;
    }

    /**
     * Checks if this NPCProfile is complete. Complete does not mean, that the NPCProfile has textures.
     *
     * @return if this NPCProfile is complete (has unique id and name)
     */
    public boolean isComplete() {
        return this.uniqueId != null && this.name != null;
    }

    /**
     * Checks if this NPCProfile has textures. That does not mean, that this NPCProfile has a name and
     * unique id.
     *
     * @return if this NPCProfile has textures.
     * @since 2.5-SNAPSHOT
     */
    public boolean hasTextures() {
        return this.getProperty("textures").isPresent();
    }

    /**
     * Checks if this NPCProfile has properties.
     *
     * @return if this NPCProfile has properties
     */
    public boolean hasProperties() {
        return this.properties != null && !this.properties.isEmpty();
    }

    /**
     * Fills this profiles with all missing attributes
     *
     * @return if the NPCProfile was successfully completed
     */
    public boolean complete() {
        return this.complete(true);
    }

    /**
     * Fills this profiles with all missing attributes
     *
     * @param propertiesAndName if properties and name should be filled for this NPCProfile
     * @return if the NPCProfile was successfully completed
     */
    public boolean complete(boolean propertiesAndName) {
        if (this.isComplete() && this.hasProperties()) {
            return true;
        }

        if (this.uniqueId == null) {
            JsonElement identifierElement = this.makeRequest(String.format(UUID_REQUEST_URL, this.name));
            if (identifierElement == null || !identifierElement.isJsonObject()) {
                return false;
            }

            JsonObject jsonObject = identifierElement.getAsJsonObject();
            if (jsonObject.has("id")) {
                this.uniqueId = UUID.fromString(
                        UNIQUE_ID_PATTERN.matcher(jsonObject.get("id").getAsString())
                                .replaceAll("$1-$2-$3-$4-$5"));
            } else {
                return false;
            }
        }

        if ((this.name == null || this.properties == null) && propertiesAndName) {
            JsonElement profileElement = this.makeRequest(
                    String.format(TEXTURES_REQUEST_URL, this.uniqueId.toString().replace("-", ""), false));
            if (profileElement == null || !profileElement.isJsonObject()) {
                return false;
            }

            JsonObject object = profileElement.getAsJsonObject();
            if (object.has("name") && object.has("properties")) {
                this.name = this.name == null ? object.get("name").getAsString() : this.name;
                this.getProperties()
                        .addAll(GSON.get().fromJson(object.get("properties"), PROPERTY_LIST_TYPE));
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Makes a request to the given url, accepting only application/json.
     *
     * @param apiUrl The api url to make the request to.
     * @return The json element parsed from the result stream of the site.
     * @since 2.5-SNAPSHOT
     */
    protected JsonElement makeRequest(String apiUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setUseCaches(true);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                try (Reader reader = new InputStreamReader(connection.getInputStream(),
                        StandardCharsets.UTF_8)) {
                    return PARSER.parse(reader);
                }
            }
            return null;
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if this NPCProfile has a unique id.
     *
     * @return if this NPCProfile has a unique id.
     * @since 2.5-SNAPSHOT
     */
    public boolean hasUniqueId() {
        return this.uniqueId != null;
    }

    /**
     * Get the unique id of this NPCProfile. May be null when this NPCProfile was created using a name and
     * is not complete. Is never null when {@link #hasUniqueId()} is {@code true}.
     *
     * @return the unique id of this NPCProfile.
     */
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    /**
     * Sets the unique of this NPCProfile. To re-request the NPCProfile textures/uuid of this NPCProfile, make
     * sure the properties are clear.
     *
     * @param uniqueId the new unique of this NPCProfile.
     * @return the same NPCProfile instance, for chaining.
     */
    public NPCProfile setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    /**
     * Check if this NPCProfile has a name.
     *
     * @return if this NPCProfile has a name.
     * @since 2.5-SNAPSHOT
     */
    public boolean hasName() {
        return this.name != null;
    }

    /**
     * Get the name of this NPCProfile. May be null when this NPCProfile was created using a unique id and
     * is not complete. Is never null when {@link #hasName()} ()} is {@code true}.
     *
     * @return the name of this NPCProfile.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this NPCProfile. To re-request the NPCProfile textures/uuid of this NPCProfile, make
     * sure the properties are clear.
     *
     * @param name the new name of this NPCProfile.
     * @return the same NPCProfile instance, for chaining.
     */
    public NPCProfile setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the properties of this NPCProfile.
     *
     * @return the properties of this NPCProfile.
     */
    public Collection<Property> getProperties() {
        if (this.properties == null) {
            this.properties = ConcurrentHashMap.newKeySet();
        }
        return this.properties;
    }

    /**
     * Sets the properties of this NPCProfile.
     *
     * @param properties The new properties of this NPCProfile.
     */
    public void setProperties(Collection<Property> properties) {
        this.properties = properties;
    }

    /**
     * Adds the given {@code property} to this NPCProfile.
     *
     * @param property the property to add.
     * @return the same NPCProfile instance, for chaining.
     * @since 2.5-SNAPSHOT
     */
    public NPCProfile setProperty(Property property) {
        this.getProperties().add(property);
        return this;
    }

    /**
     * Get a specific property by its name.
     *
     * @param name the name of the property.
     * @return the property.
     * @since 2.5-SNAPSHOT
     */
    public Optional<Property> getProperty(String name) {
        return this.getProperties().stream().filter(property -> property.getName().equals(name))
                .findFirst();
    }

    /**
     * Clears the properties of this NPCProfile.
     *
     * @since 2.5-SNAPSHOT
     */
    public void clearProperties() {
        this.getProperties().clear();
    }

    /**
     * Get the properties of this NPCProfile as a protocol lib wrapper. This is not recommended to use as
     * it creates a copy of all properties and requires protocol lib as a dependency. Use {@link
     * #getProperties()} instead.
     *
     * @return the properties of this NPCProfile as a protocol lib wrapper.
     * @deprecated Use {@link #getProperties()} instead.
     */
    @Deprecated
    public Collection<WrappedSignedProperty> getWrappedProperties() {
        return this.getProperties().stream().map(Property::asWrapped).collect(Collectors.toList());
    }

    /**
     * Converts this NPCProfile to a protocol lib wrapper. This method requires protocol lib a dependency
     * of your project and is not the point of this class. It will be removed in a further release.
     *
     * @return this NPCProfile as a protocol lib wrapper.
     * @deprecated No longer supported for public use, convert it yourself when needed.
     */
    @Deprecated
    public WrappedGameProfile asWrapped() {
        return this.asWrapped(true);
    }

    /**
     * Converts this NPCProfile to a protocol lib wrapper. This method requires protocol lib a dependency
     * of your project and is not the point of this class. It will be removed in a further release.
     *
     * @param withProperties If the properties of this wrapper should get copied.
     * @return this NPCProfile as a protocol lib wrapper.
     * @deprecated No longer supported for public use, convert it yourself when needed.
     */
    @Deprecated
    public WrappedGameProfile asWrapped(boolean withProperties) {
        WrappedGameProfile NPCProfile = new WrappedGameProfile(this.getUniqueId(), this.getName());

        if (withProperties) {
            this.getProperties().forEach(
                    property -> NPCProfile.getProperties().put(property.getName(), property.asWrapped()));
        }

        return NPCProfile;
    }

    /**
     * Creates a clone of this NPCProfile.
     *
     * @return the cloned NPCProfile.
     */
    @Override
    public NPCProfile clone() {
        try {
            return (NPCProfile) super.clone();
        } catch (CloneNotSupportedException exception) {
            return new NPCProfile(this.uniqueId, this.name,
                    this.properties == null ? null : new HashSet<>(this.properties));
        }
    }

    /**
     * A property a NPCProfile can contain. A property must be immutable.
     */
    public static class Property {

        private final String name;
        private final String value;
        private final String signature;

        public Property(String name, String value, String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }

        /**
         * Get the name of this property.
         *
         * @return the name of this property.
         */
        public String getName() {
            return this.name;
        }

        /**
         * The value of this property.
         *
         * @return the value of this property.
         */
        public String getValue() {
            return this.value;
        }

        /**
         * Get the signature of this NPCProfile. It might be null, but must never be null when {@link
         * #isSigned()} is {@code true}.
         *
         * @return the signature of this NPCProfile.
         */
        public String getSignature() {
            return this.signature;
        }

        /**
         * Get if this property has a signature.
         *
         * @return if this property has a signature.
         */
        public boolean isSigned() {
            return this.signature != null;
        }

        /**
         * Converts this property to a protocol lib wrapper. This method is no longer supported for
         * public use and will be removed in a further release as its requiring protocol lib as a
         * project dependency which is not the point of this wrapper class.
         *
         * @return this property to a protocol lib wrapper
         * @deprecated No longer supported for public use, convert it yourself when needed.
         */
        @Deprecated
        public WrappedSignedProperty asWrapped() {
            return new WrappedSignedProperty(this.getName(), this.getValue(), this.getSignature());
        }
    }
}
