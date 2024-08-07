package info.preva1l.staffaio.staffchat;

import info.preva1l.staffaio.StaffAIO;
import info.preva1l.staffaio.logging.User;
import info.preva1l.staffaio.utils.TaskManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffChatListener implements Listener {
    private final StaffAIO plugin;

    public StaffChatListener(StaffAIO plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        TaskManager.Async.run(() -> {
            User user = User.adapt(event.getPlayer());
            if (plugin.getStaffChatManager().isToggled(user)) {
                event.setCancelled(true);
                plugin.getStaffChatManager().sendMessage(user, event.getMessage());
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        TaskManager.Async.run(() -> {
            User user = User.adapt(event.getPlayer());
            if (!event.getPlayer().hasPermission("staffaio.staffchat")) return;
            plugin.getStaffChatManager().sendJoin(user);
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent event) {
        TaskManager.Async.run(() -> {
            User user = User.adapt(event.getPlayer());
            if (!event.getPlayer().hasPermission("staffaio.staffchat")) return;
            plugin.getStaffChatManager().sendLeave(user);
        });
    }
}
