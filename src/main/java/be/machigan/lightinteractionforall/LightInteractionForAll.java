package be.machigan.lightinteractionforall;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Light;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class LightInteractionForAll extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractWithLight(PlayerInteractEvent e)
    {
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!e.getMaterial().equals(Material.LIGHT)) return;
        if (e.getPlayer().isSneaking()) return;
        if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;

        Block block = e.getClickedBlock();
        if (!block.getType().equals(Material.LIGHT)) return;

        if (e.getPlayer().isOp() || !e.getPlayer().hasPermission("light4all.use")) return;

        BlockPlaceEvent event = new BlockPlaceEvent(
                block,
                block.getState(),
                block,
                new ItemStack(Material.AIR),
                e.getPlayer(),
                true,
                EquipmentSlot.HAND);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        Light light = (Light) block.getBlockData();
        light.setLevel(
                light.getLevel() == light.getMaximumLevel()
                        ? 0
                        : light.getLevel() + 1
        );
        block.setBlockData(light);
    }
}
