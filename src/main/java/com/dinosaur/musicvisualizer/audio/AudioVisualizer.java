package com.dinosaur.musicvisualizer.audio;

import com.dinosaur.musicvisualizer.utils.Logger;
import com.dinosaur.musicvisualizer.utils.TerminalUtils;

/**
 * Creates visual representations of audio data in the terminal.
 * Supports various visualization styles including waveforms and bar charts.
 */
public class AudioVisualizer {
    private static final Logger logger = new Logger();
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 20;

    /**
     * Visualizes the given audio data in the terminal.
     * 
     * @param audioData Array of normalized audio samples
     */
    public void visualize(double[] audioData) {
        if (audioData == null || audioData.length == 0) {
            logger.warn("No audio data to visualize");
            return;
        }

        logger.info("Visualizing " + audioData.length + " audio samples");
        
        // Show different visualization styles
        visualizeWaveform(audioData);
        System.out.println();
        visualizeFrequencyBars(audioData);
    }

    /**
     * Creates a waveform visualization of the audio data.
     * 
     * @param audioData Array of audio samples
     */
    public void visualizeWaveform(double[] audioData) {
        TerminalUtils.printColoredText("🌊 Waveform Visualization", TerminalUtils.Color.BLUE);
        System.out.println();

        int width = DEFAULT_WIDTH;
        int height = DEFAULT_HEIGHT;
        
        // Downsample the audio data to fit the terminal width
        double[] downsampled = downsampleAudio(audioData, width);
        
        // Create the waveform display
        for (int row = height - 1; row >= 0; row--) {
            System.out.print("│");
            
            for (int col = 0; col < width; col++) {
                double normalizedValue = (downsampled[col] + 1.0) / 2.0; // Convert from [-1,1] to [0,1]
                double threshold = (double) row / (double) height;
                
                if (normalizedValue >= threshold) {
                    if (normalizedValue > 0.8) {
                        TerminalUtils.printColoredText("█", TerminalUtils.Color.RED);
                    } else if (normalizedValue > 0.6) {
                        TerminalUtils.printColoredText("█", TerminalUtils.Color.YELLOW);
                    } else {
                        TerminalUtils.printColoredText("█", TerminalUtils.Color.GREEN);
                    }
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        
        // Draw the bottom border
        System.out.print("└");
        for (int i = 0; i < width; i++) {
            System.out.print("─");
        }
        System.out.println();
    }

    /**
     * Creates a frequency bar visualization of the audio data.
     * 
     * @param audioData Array of audio samples
     */
    public void visualizeFrequencyBars(double[] audioData) {
        TerminalUtils.printColoredText("📊 Frequency Bars", TerminalUtils.Color.MAGENTA);
        System.out.println();

        int numBars = 20;
        int maxBarHeight = 15;
        
        // Simple frequency analysis - divide audio into frequency bands
        double[] frequencyBands = analyzeFrequencyBands(audioData, numBars);
        
        // Draw bars from top to bottom
        for (int row = maxBarHeight; row > 0; row--) {
            for (int bar = 0; bar < numBars; bar++) {
                double barHeight = frequencyBands[bar] * maxBarHeight;
                
                if (barHeight >= row) {
                    // Color based on frequency band
                    if (bar < numBars / 3) {
                        TerminalUtils.printColoredText("██", TerminalUtils.Color.RED);    // Low frequencies
                    } else if (bar < 2 * numBars / 3) {
                        TerminalUtils.printColoredText("██", TerminalUtils.Color.YELLOW); // Mid frequencies  
                    } else {
                        TerminalUtils.printColoredText("██", TerminalUtils.Color.CYAN);   // High frequencies
                    }
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
        
        // Draw labels
        for (int bar = 0; bar < numBars; bar++) {
            System.out.print("──");
        }
        System.out.println();
        
        System.out.println("Low Freq        Mid Freq        High Freq");
    }

    /**
     * Generates a sample visualization for demonstration purposes.
     */
    public void visualizeSample() {
        logger.info("Generating sample visualization...");
        
        // Generate sample sine wave data
        double[] sampleData = generateSampleAudio();
        visualize(sampleData);
    }

    /**
     * Downsamples audio data to fit the specified width.
     * 
     * @param audioData Original audio data
     * @param targetWidth Desired number of samples
     * @return Downsampled audio data
     */
    private double[] downsampleAudio(double[] audioData, int targetWidth) {
        if (audioData.length <= targetWidth) {
            return audioData;
        }
        
        double[] downsampled = new double[targetWidth];
        double samplesPerBin = (double) audioData.length / targetWidth;
        
        for (int i = 0; i < targetWidth; i++) {
            int startIndex = (int) (i * samplesPerBin);
            int endIndex = (int) ((i + 1) * samplesPerBin);
            
            // Average the samples in this bin
            double sum = 0;
            int count = 0;
            for (int j = startIndex; j < endIndex && j < audioData.length; j++) {
                sum += Math.abs(audioData[j]);
                count++;
            }
            
            downsampled[i] = count > 0 ? sum / count : 0;
        }
        
        return downsampled;
    }

    /**
     * Performs simple frequency analysis by dividing audio into frequency bands.
     * 
     * @param audioData Audio samples to analyze
     * @param numBands Number of frequency bands to create
     * @return Array of frequency band magnitudes
     */
    private double[] analyzeFrequencyBands(double[] audioData, int numBands) {
        double[] bands = new double[numBands];
        int samplesPerBand = audioData.length / numBands;
        
        for (int band = 0; band < numBands; band++) {
            double sum = 0;
            int startIndex = band * samplesPerBand;
            int endIndex = Math.min((band + 1) * samplesPerBand, audioData.length);
            
            for (int i = startIndex; i < endIndex; i++) {
                sum += Math.abs(audioData[i]);
            }
            
            bands[band] = samplesPerBand > 0 ? sum / samplesPerBand : 0;
        }
        
        return bands;
    }

    /**
     * Generates sample audio data for demonstration.
     * 
     * @return Array of sample audio data
     */
    private double[] generateSampleAudio() {
        int sampleRate = 1000;
        double duration = 2.0; // 2 seconds
        int numSamples = (int) (sampleRate * duration);
        double[] sampleData = new double[numSamples];
        
        // Generate a mix of sine waves at different frequencies
        for (int i = 0; i < numSamples; i++) {
            double time = (double) i / sampleRate;
            
            // Mix of frequencies to create interesting visualization
            double sample = 0.3 * Math.sin(2 * Math.PI * 100 * time)  // 100 Hz
                          + 0.2 * Math.sin(2 * Math.PI * 300 * time)  // 300 Hz
                          + 0.1 * Math.sin(2 * Math.PI * 800 * time); // 800 Hz
            
            // Add some envelope to make it more interesting
            double envelope = Math.exp(-time * 0.5) * (1 + 0.5 * Math.sin(2 * Math.PI * 5 * time));
            sampleData[i] = sample * envelope;
        }
        
        return sampleData;
    }
}