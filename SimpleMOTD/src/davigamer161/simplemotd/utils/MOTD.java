package davigamer161.simplemotd.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import davigamer161.simplemotd.SimpleMOTD;

public class MOTD implements Listener{

	private SimpleMOTD plugin;
	public MOTD(SimpleMOTD plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void on(ServerListPingEvent event){
		FileConfiguration config = plugin.getConfig();
		FileConfiguration messages = plugin.getMessages();
		String motdlinea1 = messages.getString("Messages.motd.line1");
		String motdlinea2 = messages.getString("Messages.motd.line2");
		String mantenimientolinea1 = messages.getString("Messages.maintenance.line1");
		String mantenimientolinea2 = messages.getString("Messages.maintenance.line2");
		String path = "Config.maintenance-mode";
		if(config.getString(path).equals("true")){
	    	event.setMotd(mantenimientolinea1 + "\n" + mantenimientolinea2);
	    }else{
	    	event.setMotd(motdlinea1 + "\n" + motdlinea2);
	    }
	}
	@EventHandler
    public void on(PlayerJoinEvent event){
		FileConfiguration config = plugin.getConfig();
		FileConfiguration messages = plugin.getMessages();
		Player jugador = event.getPlayer();
		String path = "Config.maintenance-mode";
	    if(config.getString(path).equals("true")){
		    if(!(jugador.hasPermission("simplemotd.bypass"))){
				String poth = "Config.maintenance-mode-kick";
				String mensajo = messages.getString("Messages.maintenance.kick-bypass");
				jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensajo));
				if(config.getString(poth).equals("true")){
					String mensaje = messages.getString("Messages.maintenance.kick-message");
					jugador.kickPlayer(ChatColor.translateAlternateColorCodes('&', mensaje));
				}
	        }else if(jugador.hasPermission("simplemotd.bypass")){
				String mensajo = messages.getString("Messages.maintenance.kick-bypass");
				jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensajo));
			}
	    }
	}
}