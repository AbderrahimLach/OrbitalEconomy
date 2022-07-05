package xyz.directplan.orbitaleconomy.lib.storage.misc;

import lombok.Data;

/**
 * @author DirectPlan
 */

@Data
public class ConnectionData {

    private final String host, username, password, database;
    private final int port, maximumPoolSize;
}
