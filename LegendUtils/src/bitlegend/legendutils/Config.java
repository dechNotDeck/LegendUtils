package bitlegend.legendutils;

import java.io.File;
import java.io.IOException;
//import java.util.List;

//import org.bukkit.util.config.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private final LegendUtils plugin;
	public String directory = "plugins" + File.separator + LegendUtils.class.getSimpleName();
	File file = new File(directory + File.separator + "config.yml");

	public Config(LegendUtils instance) {
		plugin = instance;
	}

	public void configCheck() {
		new File(directory).mkdir();
		if (!file.exists()) {
			try {
				file.createNewFile();
				addDefaults();
			} catch (Exception e) {
				System.out.println(plugin.getDescription().getName()
						+ ": Unable to create config file.");
			}
		} else {
			loadKeys();
		}
	}
	
	public YamlConfiguration load() {
		try {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			return config;
		} catch (Exception e) {
			System.out.println(plugin.getDescription().getName() +
					": Unable to load config file.");
		}
		return null;
	}
	
	private void addDefaults() {
		System.out.println("Generating Config file...");
		write(plugin.enableOnStart, true);
		write("DB_User", "root");
		write("DB_Name", "minecraft");
		write("DB_Pass", "password");
		write("DB_Host", "localhost");
		write("Enderman_Blocks", "0,103"); //Separate IDs by commas, no spaces. eg: 1,2,103,5,20
		write("XP_Modifier", 1.0);
		write("Debug_Mode", false); //Set this to false or even completely remove this line
	}
	
	private void loadKeys() {
		System.out.println("Loading Config File...");
		plugin.enabled = readBoolean(plugin.enableOnStart);
	}
	
	public void write(String root, Object x) {
		YamlConfiguration config = load();
		config.set(root, x);
		try {
			config.save(file);
		} catch (IOException e) {
			System.out.println("There was an error saving configuration to file " + file.getName());
		}
	}
	
	public Boolean readBoolean(String root) {
		YamlConfiguration config = load();
		return config.getBoolean(root, true);
	}
	
	public Double readDouble(String root) {
		YamlConfiguration config = load();
		return config.getDouble(root, 0);
	}
	
	public String readString(String root) {
		YamlConfiguration config = load();
		return config.getString(root);
	}
	
	/*
	public List<String> readStringList(String root) {
		YamlConfiguration config = load();
		return config.getKeys(root);
	}
	*/
}
