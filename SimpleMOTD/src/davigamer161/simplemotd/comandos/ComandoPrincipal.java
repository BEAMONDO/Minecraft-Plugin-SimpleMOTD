package davigamer161.simplemotd.comandos;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import davigamer161.simplemotd.SimpleMOTD;

public class ComandoPrincipal implements CommandExecutor{
	
	private SimpleMOTD plugin;
    public ComandoPrincipal(SimpleMOTD plugin){
        this.plugin = plugin;
    }

	public boolean onCommand(CommandSender sender, Command comando, String label, String[] args) {
		if(!(sender instanceof Player)){
            FileConfiguration messagess = plugin.getMessages();
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("reload")){
                    plugin.reloadConfig();
                    plugin.reloadMessages();
                    String mensaje = messagess.getString("Messages.reload");
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
                }     
            }else{
                FileConfiguration messages = plugin.getMessages();
                String mensaje = messages.getString("Messages.console-error");
                Bukkit.getConsoleSender().sendMessage(mensaje);
            }
        }
        else if(sender instanceof Player){
            Player jugador = (Player) sender;
            FileConfiguration config = plugin.getConfig();
            FileConfiguration messages = plugin.getMessages();
            String path = "Config.no-perm-message";
            if(args.length > 0){
//-------------------------------------Comando version----------------------------------------------------------//
//----------------------------------------Desde aqui---------------------------------------//
				if(args[0].equalsIgnoreCase("version")){
					if(sender instanceof Player && (jugador.hasPermission("simplemotd.version"))){
						String mensaje = messages.getString("Messages.version");
						jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
						return true;
					}if(sender instanceof Player && !(jugador.hasPermission("simplemotd.version"))){
						if(config.getString(path).equals("true")){
							String mensaje = messages.getString("Messages.no-perm");
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
						}
					}
				}
//----------------------------------------Hasta aqui---------------------------------------//
		    
		                

//---------------------------------------Comando help--------------------------------------------------------//
//----------------------------------------Desde aqui---------------------------------------//
				else if(args[0].equalsIgnoreCase("help")){
					if(sender instanceof Player && (jugador.hasPermission("simplemotd.help"))){
							List<String> mensaje = messages.getStringList("Messages.help");
							for(int i=0;i<mensaje.size();i++){
								String texto = mensaje.get(i);
								jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', texto.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
							}
						return true;
					}if(sender instanceof Player && !(jugador.hasPermission("simplemotd.help"))){
						if(config.getString(path).equals("true")){
							String mensaje = messages.getString("Messages.no-perm");
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
						}
					}
				}
//----------------------------------------Hasta aqui---------------------------------------//

		                

//--------------------------------------Comando reload---------------------------------------------------------//
//----------------------------------------Desde aqui---------------------------------------//
				else if(args[0].equalsIgnoreCase("reload")){
					if(sender instanceof Player && (jugador.hasPermission("simplemotd.reload"))){
						plugin.reloadConfig();
						plugin.reloadMessages();
						String mensaje = messages.getString("Messages.reload");
						jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
						return true;
					}if(sender instanceof Player && !(jugador.hasPermission("simplemotd.reload"))){
						if(config.getString(path).equals("true")){
							String mensaje = messages.getString("Messages.no-perm");
							jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
						} 
					}
				} 
//----------------------------------------Hasta aqui---------------------------------------//



//---------------------------------------Comando plugin--------------------------------------------------------//
//----------------------------------------Desde aqui---------------------------------------//
		        else if(args[0].equalsIgnoreCase("plugin")){
		            if(sender instanceof Player && (jugador.hasPermission("simplemotd.plugin"))){
		                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.nombre+" &ehttps://www.spigotmc.org/resources/112452/"));
	                    return true;
                    }if(sender instanceof Player && !(jugador.hasPermission("simplemotd.plugin"))){
	                    if(config.getString(path).equals("true")){
		                    String mensaje = messages.getString("Messages.no-perm");
		                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
		                }
		            }
	            }
//----------------------------------------Hasta aqui---------------------------------------//
		    }else{
		        if(sender instanceof Player && (jugador.hasPermission("simplemotd.help"))){
	                String mensaje = messages.getString("Messages.command-no-argument");
                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
	            	return true;
		        }else if(sender instanceof Player && !(jugador.hasPermission("simplemotd.help"))){
		            if(config.getString(path).equals("true")){
	                    String mensaje = messages.getString("Messages.no-perm");
	                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.replaceAll("%plugin%", plugin.nombre).replaceAll("%version%", plugin.version)));
	                }
	            }
		    }
		}
		return false;
	}
}