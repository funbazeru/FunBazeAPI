package me.drkapdor.funbazeapi.utility.block;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.drkapdor.funbazeapi.ApiPlugin;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockUtil {

    public static BlockModel getBlockModel(Block block) {
        if (block.getType().toString().contains("STAIRS"))
            return BlockModel.STAIRS;
        else if (block.getType().toString().contains("SLAB") ||
                block.getType().toString().contains("STEP") ||
                block.getType().toString().contains("BED"))
            return BlockModel.HALF;
        else if (block.getType().toString().contains("SNOW") &&
                !block.getType().toString().contains("BLOCK"))
            return BlockModel.SNOW;
        else if (block.getType().toString().contains("PLATE") ||
                block.getType().toString().contains("CARPET"))
            return BlockModel.PLATE;
        else if (block.getType().toString().contains("TRAPDOOR"))
            return BlockModel.TRAPDOOR;
        else if (block.getType().toString().contains("GATE"))
            return BlockModel.GATE;
        else if (block.getType().toString().contains("BUTTON") ||
                block.getType().toString().contains("LEVER"))
            return BlockModel.BUTTON;
        else if (block.getType().toString().contains("TORCH"))
            return BlockModel.TORCH;
        else if (block.getType().toString().contains("SKULL") ||
                block.getType().toString().contains("FLOWER_POT"))
            return BlockModel.SKULL;
        else if (block.getType().toString().contains("GRASS") ||
                block.getType().toString().contains("FLOWER") )
            return BlockModel.GRASS;
        else return BlockModel.OTHER;
    }

    public static boolean hasRegion(Block block, String regionName) {
        for (ProtectedRegion region : ApiPlugin.getRegionManager().getApplicableRegions(block.getLocation()))
            if (region.getId().equals(regionName))
                return true;
        return false;
    }

    public static Collection<String> getRegions(Block block) {
        List<String> names = new ArrayList<>();
        for (ProtectedRegion region : ApiPlugin.getRegionManager().getApplicableRegions(block.getLocation()))
            names.add(region.getId());
        return names;
    }
}
