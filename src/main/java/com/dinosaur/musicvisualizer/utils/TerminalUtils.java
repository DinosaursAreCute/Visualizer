package com.dinosaur.musicvisualizer.utils;

/**
 * Utility class for terminal operations including ANSI colors, 
 * screen clearing, and other terminal-specific functionality.
 */
public class TerminalUtils {
    
    // ANSI color codes
    public enum Color {
        RESET("\033[0m"),
        BLACK("\033[0;30m"),
        RED("\033[0;31m"),
        GREEN("\033[0;32m"),
        YELLOW("\033[0;33m"),
        BLUE("\033[0;34m"),
        MAGENTA("\033[0;35m"),
        CYAN("\033[0;36m"),
        WHITE("\033[0;37m"),
        
        // Bright colors
        BRIGHT_BLACK("\033[0;90m"),
        BRIGHT_RED("\033[0;91m"),
        BRIGHT_GREEN("\033[0;92m"),
        BRIGHT_YELLOW("\033[0;93m"),
        BRIGHT_BLUE("\033[0;94m"),
        BRIGHT_MAGENTA("\033[0;95m"),
        BRIGHT_CYAN("\033[0;96m"),
        BRIGHT_WHITE("\033[0;97m"),
        
        // Background colors
        BG_BLACK("\033[40m"),
        BG_RED("\033[41m"),
        BG_GREEN("\033[42m"),
        BG_YELLOW("\033[43m"),
        BG_BLUE("\033[44m"),
        BG_MAGENTA("\033[45m"),
        BG_CYAN("\033[46m"),
        BG_WHITE("\033[47m");

        private final String code;

        Color(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    // ANSI escape sequences
    private static final String CLEAR_SCREEN = "\033[2J\033[H";
    private static final String CLEAR_LINE = "\033[K";
    private static final String CURSOR_HOME = "\033[H";
    private static final String HIDE_CURSOR = "\033[?25l";
    private static final String SHOW_CURSOR = "\033[?25h";
    
    // Check if ANSI colors are supported
    private static final boolean ANSI_SUPPORTED = checkAnsiSupport();

    /**
     * Clears the terminal screen and moves cursor to home position.
     */
    public static void clearScreen() {
        if (ANSI_SUPPORTED) {
            System.out.print(CLEAR_SCREEN);
            System.out.flush();
        } else {
            // Fallback: print empty lines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Clears the current line.
     */
    public static void clearLine() {
        if (ANSI_SUPPORTED) {
            System.out.print(CLEAR_LINE);
        }
    }

    /**
     * Moves the cursor to the home position (top-left).
     */
    public static void moveCursorHome() {
        if (ANSI_SUPPORTED) {
            System.out.print(CURSOR_HOME);
        }
    }

    /**
     * Hides the terminal cursor.
     */
    public static void hideCursor() {
        if (ANSI_SUPPORTED) {
            System.out.print(HIDE_CURSOR);
        }
    }

    /**
     * Shows the terminal cursor.
     */
    public static void showCursor() {
        if (ANSI_SUPPORTED) {
            System.out.print(SHOW_CURSOR);
        }
    }

    /**
     * Prints colored text to the terminal.
     * 
     * @param text The text to print
     * @param color The color to use
     */
    public static void printColoredText(String text, Color color) {
        if (ANSI_SUPPORTED) {
            System.out.print(color.getCode() + text + Color.RESET.getCode());
        } else {
            System.out.print(text);
        }
    }

    /**
     * Prints colored text with a newline.
     * 
     * @param text The text to print
     * @param color The color to use
     */
    public static void printColoredLine(String text, Color color) {
        printColoredText(text, color);
        System.out.println();
    }

    /**
     * Prints text with both foreground and background colors.
     * 
     * @param text The text to print
     * @param fgColor The foreground color
     * @param bgColor The background color
     */
    public static void printColoredText(String text, Color fgColor, Color bgColor) {
        if (ANSI_SUPPORTED) {
            System.out.print(bgColor.getCode() + fgColor.getCode() + text + Color.RESET.getCode());
        } else {
            System.out.print(text);
        }
    }

    /**
     * Creates a colored string without printing it.
     * 
     * @param text The text to color
     * @param color The color to apply
     * @return Colored string if ANSI is supported, plain string otherwise
     */
    public static String colorize(String text, Color color) {
        if (ANSI_SUPPORTED) {
            return color.getCode() + text + Color.RESET.getCode();
        } else {
            return text;
        }
    }

    /**
     * Prints a horizontal line separator.
     * 
     * @param width Width of the line
     * @param character Character to use for the line
     * @param color Color of the line
     */
    public static void printSeparator(int width, char character, Color color) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            sb.append(character);
        }
        printColoredLine(sb.toString(), color);
    }

    /**
     * Prints a progress bar in the terminal.
     * 
     * @param progress Progress value (0.0 to 1.0)
     * @param width Width of the progress bar
     */
    public static void printProgressBar(double progress, int width) {
        progress = Math.max(0.0, Math.min(1.0, progress));
        int filledWidth = (int) (progress * width);
        
        System.out.print("[");
        
        // Filled portion
        for (int i = 0; i < filledWidth; i++) {
            printColoredText("█", Color.GREEN);
        }
        
        // Empty portion
        for (int i = filledWidth; i < width; i++) {
            System.out.print("░");
        }
        
        System.out.print("] ");
        System.out.printf("%.1f%%", progress * 100);
    }

    /**
     * Gets the terminal width (best effort).
     * 
     * @return Estimated terminal width, defaults to 80
     */
    public static int getTerminalWidth() {
        // Try to get terminal width from system properties or environment
        String columns = System.getenv("COLUMNS");
        if (columns != null) {
            try {
                return Integer.parseInt(columns);
            } catch (NumberFormatException e) {
                // Fall through to default
            }
        }
        
        // Default width
        return 80;
    }

    /**
     * Gets the terminal height (best effort).
     * 
     * @return Estimated terminal height, defaults to 24
     */
    public static int getTerminalHeight() {
        // Try to get terminal height from environment
        String lines = System.getenv("LINES");
        if (lines != null) {
            try {
                return Integer.parseInt(lines);
            } catch (NumberFormatException e) {
                // Fall through to default
            }
        }
        
        // Default height
        return 24;
    }

    /**
     * Pauses execution for the specified number of milliseconds.
     * 
     * @param milliseconds Number of milliseconds to sleep
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks if ANSI escape sequences are supported in the current terminal.
     * 
     * @return true if ANSI is likely supported, false otherwise
     */
    private static boolean checkAnsiSupport() {
        // Check common indicators for ANSI support
        String term = System.getenv("TERM");
        String os = System.getProperty("os.name").toLowerCase();
        
        // Most Unix-like systems support ANSI
        if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return true;
        }
        
        // Windows 10+ supports ANSI in newer terminals
        if (os.contains("win")) {
            String termProgram = System.getenv("TERM_PROGRAM");
            return termProgram != null || (term != null && !term.equals("dumb"));
        }
        
        // Check for terminal type
        return term != null && !term.equals("dumb") && !term.equals("unknown");
    }

    /**
     * Prints a banner with the application title.
     * 
     * @param title The title to display
     */
    public static void printBanner(String title) {
        int width = Math.max(title.length() + 4, 50);
        
        // Top border
        printSeparator(width, '═', Color.CYAN);
        
        // Title line
        int padding = (width - title.length() - 2) / 2;
        System.out.print("║");
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }
        printColoredText(title, Color.BRIGHT_YELLOW);
        for (int i = 0; i < width - title.length() - padding - 2; i++) {
            System.out.print(" ");
        }
        System.out.println("║");
        
        // Bottom border
        printSeparator(width, '═', Color.CYAN);
    }
}