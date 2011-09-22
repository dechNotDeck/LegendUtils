package bitlegend.legendutils;

import java.io.File;
import java.util.List;

import org.bukkit.util.config.Configuration;

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

	public void write(String root, Object x) {
		Configuration config = load();
		config.setProperty(root, x);
		config.save();
	}

	public Boolean readBoolean(String root) {
		Configuration config = load();
		return config.getBoolean(root, true);
	}

	public Double readDouble(String root) {
		Configuration config = load();
		return config.getDouble(root, 0);
	}

	public List<String> readStringList(String root) {
		Configuration config = load();
		return config.getKeys(root);
	}

	public String readString(String root) {
		Configuration config = load();
		return config.getString(root);
	}

	private Configuration load() {
		try {
			Configuration config = new Configuration(file);
			config.load();
			return config;

		} catch (Exception e) {
			System.out.println(plugin.getDescription().getName()
					+ ": Unable to load config file.");
		}
		return null;
	}

	private void addDefaults() {
		System.out.println("Generating Config File...");
		write(plugin.enableOnStart, true);
		write("DB_User", "root");
		write("DB_Name", "minecraft");
		write("DB_Pass", "password");
		write("DB_Host", "localhost");
		write("Enderman_Blocks", "0,103"); //Separate IDs by commas, no spaces. eg: 1,2,103,5,20
		write("XP_Modifier", 1.0);
		write("Debug_Mode", false); //Set this to false or even completely remove this line
		loadKeys();
	}

	private void loadKeys() {
		System.out.println("Loading Config File...");
		plugin.enabled = readBoolean(plugin.enableOnStart);
	}
}
