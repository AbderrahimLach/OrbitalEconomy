package xyz.directplan.orbitaleconomy.lib.config;

import java.util.HashSet;
import java.util.Set;

/**
 * @author DirectPlan
 */
public abstract class ConfigHandler {

    private final Set<ConfigurationAdapter> configurations = new HashSet<>();

    public void loadConfiguration(String fileName, Class<? extends ConfigEntry> entryClass) {
        loadConfigurations(new ConfigIdentifier(fileName, entryClass));
    }

    public void loadConfigurations(ConfigIdentifier... identifiers) {
        for(ConfigIdentifier identifier : identifiers) {
            ConfigurationAdapter adapter = loadConfiguration(identifier);
            configurations.add(adapter);
        }
    }

    public ConfigurationAdapter get(String config) {
        for(ConfigurationAdapter adapter : configurations) {
            if(adapter.getFile().getName().startsWith(config)) {
                return adapter;
            }
        }
        return null;
    }

    protected abstract ConfigurationAdapter loadConfiguration(ConfigIdentifier configIdentifier);

    public void saveConfigurations() {
        configurations.forEach(ca -> {
            ca.saveKeys();
            ca.saveConfiguration();
        });
    }

    public void reloadConfigurations() {
        configurations.forEach(ConfigurationAdapter::loadKeys);
    }
}
