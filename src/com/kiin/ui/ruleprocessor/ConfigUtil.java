package com.kiin.ui.ruleprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
* @author Kailashnath Kutti - kailashnath.kutti@gmail.com
*
*/
public class ConfigUtil {
static Properties properties = new Properties();
static {
ClassLoader loader = Thread.currentThread().getContextClassLoader();
InputStream stream = loader.getResourceAsStream("droolsconnection.properties");

try {
properties.load(stream);
} catch (IOException e) {
e.printStackTrace();
}
}
public static String getConfigBasePath() {
return properties.getProperty("configBasePath");
}
public static String getPropertyByName(String name) {
return properties.getProperty(name);
}
}