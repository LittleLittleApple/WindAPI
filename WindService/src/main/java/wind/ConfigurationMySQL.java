package wind;

import java.util.ResourceBundle;

public class ConfigurationMySQL {
	public final String url;
	public final String username;
	public final String password;

	public ConfigurationMySQL(ResourceBundle configuration) {
		url = getRequiredParameter(configuration, "mysql.url");
		username = getRequiredParameter(configuration, "mysql.username");
		password = getRequiredParameter(configuration, "mysql.password");

	}

	public static String getRequiredParameter(ResourceBundle configuration,
			String parameter) {
		if (!configuration.containsKey(parameter)) {
			throw new IllegalArgumentException(
					"missing parameter in config.properties: " + parameter);
		}
		return configuration.getString(parameter);
	}
}
