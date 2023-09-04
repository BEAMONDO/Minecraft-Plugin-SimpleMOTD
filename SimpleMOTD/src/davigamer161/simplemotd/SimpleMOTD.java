package davigamer161.simplemotd;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import davigamer161.simplemotd.comandos.ComandoPrincipal;
import davigamer161.simplemotd.utils.MOTD;
import davigamer161.simplemotd.utils.UpdateChecker;

public class SimpleMOTD extends JavaPlugin implements Listener{
	
	private FileConfiguration messages = null;
    private File messagesFile = null;
    public String rutaConfig;
    public String rutaMessages;
	PluginDescriptionFile pdffile;
    public String version;
    public String latestversion;
    public String nombre;	
	
	public SimpleMOTD(){
	      this.pdffile = this.getDescription();
	      this.version = this.pdffile.getVersion();
	      this.nombre = ChatColor.RED+"["+ChatColor.YELLOW+this.pdffile.getName()+ChatColor.RED+"]"+ChatColor.WHITE;
	    }
	//---------------------Para cuando se activa el plugin----------------------------------//
    //------------------------------Desde aqui-----------------------------//
    public void onEnable(){
      Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE+"<------------------------------------>");
	  Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.WHITE+" Enabled, ("+ChatColor.GREEN+"Version: "+ChatColor.AQUA+version+ChatColor.WHITE+")");
	  Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.GOLD+" Thanks for use my plugin :)");
	  Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.YELLOW+" Made by "+ChatColor.LIGHT_PURPLE+"davigamer161");
      Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE+"<------------------------------------>");
      registrarComandos();
      registrarEventos();
      registrarConfig();
      registrarMensajes();
      checkearMesages();
      comprobarActualizaciones();
      File newFolder = new File(this.getDataFolder() + File.separator + "ServerIcons");
      if (!newFolder.exists()) {
         newFolder.mkdir();
      }
      this.getServer().getPluginManager().registerEvents(this, this);
    }
    //------------------------------Hasta aqui-----------------------------//
	
	

  //------------------Para cuando se desactiva el plugin----------------------------------//
    //------------------------------Desde aqui-----------------------------//
    public void onDisable(){
	    Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.WHITE+"Disabled, ("+ChatColor.GREEN+"Version: "+ChatColor.AQUA+version+ChatColor.WHITE+")");
    }
    //------------------------------Hasta aqui-----------------------------//

    

    //-----------------------Para registrar comandos----------------------------------------//
    //------------------------------Desde aqui-----------------------------//
    public void registrarComandos(){
        this.getCommand("simplemotd").setExecutor(new ComandoPrincipal(this));
      }
    //------------------------------Hasta aqui-----------------------------//



    //-----------------------Para registrar eventos----------------------------------------//
    //------------------------------Desde aqui-----------------------------//
    public void registrarEventos(){
      PluginManager pm = getServer().getPluginManager();
      pm.registerEvents(new UpdateChecker(this), this);
      pm.registerEvents(new MOTD(this), this);
    }
  //------------------------------Hasta aqui-----------------------------//
    
    

  //--------------------------Para crear config.yml--------------------------------------//
    //------------------------------Desde aqui-----------------------------//
    public void registrarConfig(){
        File config = new File(this.getDataFolder(),"config.yml");
        rutaConfig = config.getPath();
        if(!config.exists()){
          this.getConfig().options().copyDefaults(true);
          saveConfig();
        }
      }
    //------------------------------Hasta aqui-----------------------------//
    
    
    
  //-------------------------Para crear messages.yml---------------------------------------//
    //------------------------------Desde aqui-----------------------------//
    public FileConfiguration getMessages(){
      if(messages == null){
        reloadMessages();
      }
      return messages;
    }
    public void reloadMessages(){
      if(messages == null){
        messagesFile = new File(getDataFolder(),"messages.yml");
      }
      messages = YamlConfiguration.loadConfiguration(messagesFile);
      Reader defConfigStream;
      try{
        defConfigStream = new InputStreamReader(this.getResource("messages.yml"),"UTF8");
        if(defConfigStream != null){
          YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
          messages.setDefaults(defConfig);
        }
      }catch(UnsupportedEncodingException e){
        e.printStackTrace();
      }
    }
    public void saveMessages(){
      try{
        messages.save(messagesFile);
      }catch(IOException e){
          e.printStackTrace();
        }
      }
      public void registrarMensajes(){
        messagesFile = new File(this.getDataFolder(),"messages.yml");
        rutaMessages = messagesFile.getPath();
        if(!messagesFile.exists()){
          this.getMessages().options().copyDefaults(true);
          saveMessages();
        }
      }
      public void checkearMesages() {
    	  Path archivo = Paths.get(rutaMessages);
    	  try {
          String texto = new String(Files.readAllBytes(archivo));
          if(!texto.contains("update-checker:")) {
            getMessages().set("Messages.update-checker", "%plugin% &bThere is a new version &e(&f%latestversion%&e)&b. Download it here: &7https://www.spigotmc.org/resources/112452/");
            saveMessages();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      //------------------------------Hasta aqui-----------------------------//
      
      
      
    //-------------------------------Para auto actualizar--------------------------------------//
      //------------------------------Desde aqui-----------------------------//
      public void comprobarActualizaciones(){		  
  		  try {
  			  HttpURLConnection con = (HttpURLConnection) new URL(
  	          "https://api.spigotmc.org/legacy/update.php?resource=112452").openConnection();
  	          int timed_out = 1250;
  	          con.setConnectTimeout(timed_out);
  	          con.setReadTimeout(timed_out);
  	          latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
  	          if (latestversion.length() <= 7) {
  	        	  if(!version.equals(latestversion)){
  	        		  Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.AQUA+" There is a new version available. "+ChatColor.YELLOW+
  	        				  "("+ChatColor.GRAY+latestversion+ChatColor.YELLOW+")");
  	        		  Bukkit.getConsoleSender().sendMessage(nombre+ChatColor.AQUA+" You can download it at: "+ChatColor.WHITE+"https://www.spigotmc.org/resources/112452/");  
  	        	  }      	  
  	          }
  	      } catch (Exception ex) {
  	    	  Bukkit.getConsoleSender().sendMessage(nombre + ChatColor.RED +"Error while checking update.");
  	      }
  	  }
      public String getVersion(){
      	return this.version;
      }
      public String getLatestVersion(){
      	return this.latestversion;	
      }
      //------------------------------Hasta aqui-----------------------------//
      
      
      
    //------------------------------Evento cambiar icono-----------------------------//
      //------------------------------Desde aqui-----------------------------//
     @EventHandler
     public void change(ServerListPingEvent evento) {
        try {
           if (this.chooseIcon() != null) {
              evento.setServerIcon(Bukkit.loadServerIcon(this.chooseIcon()));
           }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
     }

     public File chooseIcon(){
		FileConfiguration messages = this.getMessages();
		File carpeta = new File(this.getDataFolder() + File.separator + "ServerIcons");
        File[] listaDeArchivos = carpeta.listFiles();
        ArrayList<File> ArchivosRandom = new ArrayList();
        int numeroDeArchivos;
			if(listaDeArchivos != null){
				File[] cantidad = listaDeArchivos;
				int variable = listaDeArchivos.length;
				for(numeroDeArchivos = 0; numeroDeArchivos < variable; ++numeroDeArchivos){
					File archivo = cantidad[numeroDeArchivos];
					if (archivo.getName().contains(".png")) {
						try{
							BufferedImage imagen = ImageIO.read(new File(carpeta + File.separator + archivo.getName()));
							int ancho = imagen.getWidth();
							int alto = imagen.getHeight();
							if(ancho == 64 && alto == 64){
								ArchivosRandom.add(archivo);
							}else if (!archivo.isHidden()){
								String mensaje = messages.getString("Messages.icon-error.invalid-image");
								Bukkit.getServer().getLogger().log(Level.WARNING, mensaje);
							}
						}catch (IOException error2){
							error2.printStackTrace();
						}
					}else if(!archivo.isHidden()){
						String mensaje = messages.getString("Messages.icon-error.invalid-image");
						Bukkit.getServer().getLogger().log(Level.WARNING, mensaje);
					}
				}
			}else {
        String mensaje = messages.getString("Messages.icon-error.retry");
        Bukkit.getServer().getLogger().log(Level.SEVERE, mensaje);
        carpeta.mkdir();
     }if (ArchivosRandom.isEmpty()){
            	return null;
        	}else{
				Random random = new Random();
				numeroDeArchivos = random.nextInt(ArchivosRandom.size());
				return (File)ArchivosRandom.get(numeroDeArchivos);
        	}
	}
     //------------------------------Hasta aqui-----------------------------//
}