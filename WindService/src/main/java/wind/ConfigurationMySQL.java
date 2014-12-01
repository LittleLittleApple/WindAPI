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
		String value = configuration.getString(parameter);
//		if (value == null || value.isEmpty()) {
//			throw new IllegalArgumentException("missing parameter: "
//					+ parameter);
//		}
		return value;
	}
}
