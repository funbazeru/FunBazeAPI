package me.drkapdor.funbazeapi.api.npc;

import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.api.npc.profile.NPCProfile;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCManager {

    private final NPCPool pool = NPCPool.builder(ApiPlugin.getInstance()).spawnDistance(16).actionDistance(8).tabListRemoveTicks(20).build();

    public NPC createNPC(Location location, NPCProfile profile, boolean mimic, boolean lookAtPlayer) {
        return NPC.builder().location(location).profile(profile).imitatePlayer(mimic).lookAtPlayer(lookAtPlayer).build(pool);
    }

    public NPCProfile newProfile(UUID uuid, String name, String skinValue, String skinSignature) {
        List<NPCProfile.Property> properties = new ArrayList<>();
        properties.add(new NPCProfile.Property("textures", skinValue, skinSignature));
        return new NPCProfile(uuid, name, properties);
    }

    public void remove(int id) {
        pool.removeNPC(id);
    }
}
