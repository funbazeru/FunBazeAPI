package me.drkapdor.funbazeapi.api.npc;

import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.npc.profile.NPCProfile;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCManager {

    private static final NPCPool pool = NPCPool.builder(ApiPlugin.getInstance()).spawnDistance(16).actionDistance(8).tabListRemoveTicks(20).build();

    public static NPC createNPC(Location location, NPCProfile profile, boolean mimic, boolean lookAtPlayer) {
        return NPC.builder().location(location).profile(profile).imitatePlayer(mimic).lookAtPlayer(lookAtPlayer).build(pool);
    }

    public static NPCProfile createProfile(UUID uuid, String name, String skinValue, String skinSignature) {
        List<NPCProfile.Property> properties = new ArrayList<>();
        properties.add(new NPCProfile.Property("textures", skinValue, skinSignature));
        return new NPCProfile(uuid, name, properties);
    }

    public static void removeNPC(int id) {
        pool.removeNPC(id);
    }
}
