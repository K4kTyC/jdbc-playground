package org.example.util;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;

public class PoolStatistics {

    private PoolStatistics() {}

    private static final MBeanServer server;
    private static final ObjectName name;

    static {
        try {
            server = ManagementFactory.getPlatformMBeanServer();
            ObjectName filter = new ObjectName("org.mariadb.jdbc.pool:type=*");
            Set<ObjectName> objectNames = server.queryNames(filter, null);
            name = objectNames.iterator().next();
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getActiveConnections() {
        try {
            return server.getAttribute(name, "ActiveConnections");
        } catch (JMException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getTotalConnections() {
        try {
            return server.getAttribute(name, "TotalConnections");
        } catch (JMException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getConnectionRequests() {
        try {
            return server.getAttribute(name, "ConnectionRequests");
        } catch (JMException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getIdleConnections() {
        try {
            return server.getAttribute(name, "IdleConnections");
        } catch (JMException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printStats() {
        System.out.printf("Total: %s, Active: %s, Idle: %s, Waiting: %s\n", getTotalConnections(), getActiveConnections(), getIdleConnections(), getConnectionRequests());
    }
}
