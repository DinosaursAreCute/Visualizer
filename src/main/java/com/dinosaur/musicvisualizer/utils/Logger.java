package com.dinosaur.musicvisualizer.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logging utility for the music visualizer application.
 * Provides methods for different log levels with colored output.
 */
public class Logger {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private boolean debugEnabled = false;

    /**
     * Creates a new Logger instance.
     */
    public Logger() {
        // Check if debug mode is enabled via system property
        String debugProperty = System.getProperty("music.visualizer.debug");
        debugEnabled = "true".equalsIgnoreCase(debugProperty);
    }

    /**
     * Logs an informational message.
     * 
     * @param message The message to log
     */
    public void info(String message) {
        log("INFO", message, TerminalUtils.Color.GREEN);
    }

    /**
     * Logs a warning message.
     * 
     * @param message The message to log
     */
    public void warn(String message) {
        log("WARN", message, TerminalUtils.Color.YELLOW);
    }

    /**
     * Logs an error message.
     * 
     * @param message The message to log
     */
    public void error(String message) {
        log("ERROR", message, TerminalUtils.Color.RED);
    }

    /**
     * Logs a debug message (only if debug mode is enabled).
     * 
     * @param message The message to log
     */
    public void debug(String message) {
        if (debugEnabled) {
            log("DEBUG", message, TerminalUtils.Color.CYAN);
        }
    }

    /**
     * Enables or disables debug logging.
     * 
     * @param enabled True to enable debug logging, false to disable
     */
    public void setDebugEnabled(boolean enabled) {
        this.debugEnabled = enabled;
    }

    /**
     * Checks if debug logging is enabled.
     * 
     * @return True if debug logging is enabled, false otherwise
     */
    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Logs a message with a timestamp, level, and color formatting.
     * 
     * @param level The log level (INFO, WARN, ERROR, DEBUG)
     * @param message The message to log
     * @param color The color to use for the level indicator
     */
    private void log(String level, String message, TerminalUtils.Color color) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        System.out.print("[" + timestamp + "] ");
        TerminalUtils.printColoredText("[" + level + "]", color);
        System.out.println(" " + message);
    }

    /**
     * Logs a formatted message with arguments (similar to String.format).
     * 
     * @param level The log level method to use (info, warn, error, debug)
     * @param format The format string
     * @param args The arguments for the format string
     */
    public void logf(String level, String format, Object... args) {
        String message = String.format(format, args);
        switch (level.toLowerCase()) {
            case "info":
                info(message);
                break;
            case "warn":
                warn(message);
                break;
            case "error":
                error(message);
                break;
            case "debug":
                debug(message);
                break;
            default:
                info(message);
        }
    }

    /**
     * Logs an exception with its stack trace.
     * 
     * @param message A descriptive message about the exception
     * @param exception The exception to log
     */
    public void exception(String message, Exception exception) {
        error(message + ": " + exception.getMessage());
        if (debugEnabled) {
            System.err.println("Stack trace:");
            exception.printStackTrace();
        }
    }

    /**
     * Logs a separator line for visual organization.
     * 
     * @param title Optional title for the separator
     */
    public void separator(String title) {
        System.out.println();
        if (title != null && !title.trim().isEmpty()) {
            TerminalUtils.printColoredText("=== " + title + " ===", TerminalUtils.Color.MAGENTA);
        } else {
            System.out.println("=====================================");
        }
        System.out.println();
    }

    /**
     * Logs application startup information.
     * 
     * @param appName Name of the application
     * @param version Version of the application
     */
    public void logStartup(String appName, String version) {
        separator("Application Startup");
        info("Starting " + appName + " v" + version);
        info("Java version: " + System.getProperty("java.version"));
        info("Operating system: " + System.getProperty("os.name"));
        if (debugEnabled) {
            debug("Debug logging is enabled");
        }
        separator(null);
    }
}