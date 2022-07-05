package xyz.directplan.orbitaleconomy.lib.config;

import lombok.Data;

/**
 * @author DirectPlan
 */
@Data
public class ConfigIdentifier {
    private final String fileName;
    private final Class<? extends ConfigEntry> entryClass;
}
