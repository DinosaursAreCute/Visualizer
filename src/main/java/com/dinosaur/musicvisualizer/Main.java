package com.dinosaur.musicvisualizer;

import com.dinosaur.musicvisualizer.audio.AudioFileReader;
import com.dinosaur.musicvisualizer.audio.AudioVisualizer;
import com.dinosaur.musicvisualizer.utils.Logger;
import com.dinosaur.musicvisualizer.utils.TerminalUtils;

/**
 * Main entry point for the Terminal Music Visualizer CLI application.
 * This application reads audio files and displays visual representations in the terminal.
 */
public class Main {
    private static final Logger logger = new Logger();

    public static void main(String[] args) {
        // Clear terminal and show welcome message
        TerminalUtils.clearScreen();
        TerminalUtils.printColoredText("🎵 Welcome to Terminal Music Visualizer! 🎵", TerminalUtils.Color.CYAN);
        System.out.println();

        try {
            // Check if audio file path is provided
            if (args.length == 0) {
                logger.info("No audio file specified. Using demo file...");
                visualizeDemo();
            } else {
                String audioFilePath = args[0];
                logger.info("Loading audio file: " + audioFilePath);
                visualizeAudioFile(audioFilePath);
            }
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage());
            showUsage();
        }
    }

    /**
     * Visualizes the demo audio file included with the application.
     */
    private static void visualizeDemo() {
        try {
            String demoPath = "src/main/resources/demo.wav";
            AudioFileReader reader = new AudioFileReader();
            AudioVisualizer visualizer = new AudioVisualizer();

            logger.info("Reading demo audio file...");
            if (reader.canReadFile(demoPath)) {
                double[] audioData = reader.readAudioFile(demoPath);
                visualizer.visualize(audioData);
            } else {
                logger.warn("Demo file not found. Generating sample visualization...");
                visualizer.visualizeSample();
            }
        } catch (Exception e) {
            logger.error("Failed to visualize demo: " + e.getMessage());
        }
    }

    /**
     * Visualizes the specified audio file.
     * 
     * @param filePath Path to the audio file to visualize
     */
    private static void visualizeAudioFile(String filePath) {
        try {
            AudioFileReader reader = new AudioFileReader();
            AudioVisualizer visualizer = new AudioVisualizer();

            if (!reader.canReadFile(filePath)) {
                logger.error("Cannot read audio file: " + filePath);
                return;
            }

            logger.info("Processing audio file...");
            double[] audioData = reader.readAudioFile(filePath);
            visualizer.visualize(audioData);
            
            logger.info("Visualization complete!");
        } catch (Exception e) {
            logger.error("Failed to visualize audio file: " + e.getMessage());
        }
    }

    /**
     * Displays usage information for the application.
     */
    private static void showUsage() {
        System.out.println();
        TerminalUtils.printColoredText("Usage:", TerminalUtils.Color.YELLOW);
        System.out.println("  java -jar music-visualizer.jar [audio-file]");
        System.out.println();
        System.out.println("  audio-file: Path to WAV audio file (optional)");
        System.out.println("              If not provided, demo visualization will be shown");
        System.out.println();
        TerminalUtils.printColoredText("Examples:", TerminalUtils.Color.GREEN);
        System.out.println("  java -jar music-visualizer.jar");
        System.out.println("  java -jar music-visualizer.jar /path/to/song.wav");
    }
}