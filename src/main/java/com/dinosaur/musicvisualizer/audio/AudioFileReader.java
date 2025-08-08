package com.dinosaur.musicvisualizer.audio;

import com.dinosaur.musicvisualizer.utils.Logger;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Handles reading audio files and extracting audio data for visualization.
 * Currently supports WAV format files.
 */
public class AudioFileReader {
    private static final Logger logger = new Logger();

    /**
     * Checks if the specified audio file can be read by this reader.
     * 
     * @param filePath Path to the audio file
     * @return true if the file can be read, false otherwise
     */
    public boolean canReadFile(String filePath) {
        try {
            if (filePath == null || filePath.trim().isEmpty()) {
                logger.warn("Audio file path is null or empty");
                return false;
            }
            
            File file = new File(filePath);
            if (!file.exists()) {
                logger.warn("Audio file does not exist: " + filePath);
                return false;
            }

            // Try to get audio input stream to validate the file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            audioStream.close();
            return true;
            
        } catch (UnsupportedAudioFileException e) {
            logger.error("Unsupported audio file format: " + filePath);
            return false;
        } catch (IOException e) {
            logger.error("IO error reading file: " + filePath);
            return false;
        }
    }

    /**
     * Reads audio data from the specified file and converts it to a double array.
     * 
     * @param filePath Path to the audio file
     * @return Array of audio sample values normalized to [-1.0, 1.0]
     * @throws IOException if there's an error reading the file
     * @throws UnsupportedAudioFileException if the file format is not supported
     */
    public double[] readAudioFile(String filePath) throws IOException, UnsupportedAudioFileException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IOException("Audio file path cannot be null or empty");
        }
        
        File audioFile = new File(filePath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        
        AudioFormat format = audioStream.getFormat();
        logger.info("Audio format: " + format.toString());
        
        // Calculate the number of samples
        long frameLength = audioStream.getFrameLength();
        int frameSize = format.getFrameSize();
        int sampleSizeInBits = format.getSampleSizeInBits();
        
        // Read the audio data
        byte[] audioData = new byte[(int) (frameLength * frameSize)];
        int bytesRead = audioStream.read(audioData);
        audioStream.close();
        
        logger.info("Read " + bytesRead + " bytes of audio data");
        
        // Convert bytes to double array
        return convertBytesToDoubles(audioData, sampleSizeInBits, format.isBigEndian());
    }

    /**
     * Converts raw audio bytes to normalized double values.
     * 
     * @param audioData Raw audio data bytes
     * @param sampleSizeInBits Size of each sample in bits (8 or 16)
     * @param bigEndian Whether the data is in big-endian format
     * @return Normalized audio samples as double array
     */
    private double[] convertBytesToDoubles(byte[] audioData, int sampleSizeInBits, boolean bigEndian) {
        int bytesPerSample = sampleSizeInBits / 8;
        int numSamples = audioData.length / bytesPerSample;
        double[] samples = new double[numSamples];
        
        for (int i = 0; i < numSamples; i++) {
            int sampleValue = 0;
            
            if (sampleSizeInBits == 16) {
                // 16-bit samples
                int byteIndex = i * 2;
                if (bigEndian) {
                    sampleValue = (audioData[byteIndex] << 8) | (audioData[byteIndex + 1] & 0xFF);
                } else {
                    sampleValue = (audioData[byteIndex + 1] << 8) | (audioData[byteIndex] & 0xFF);
                }
                
                // Convert to signed short
                if (sampleValue > 32767) {
                    sampleValue -= 65536;
                }
                
                // Normalize to [-1.0, 1.0]
                samples[i] = sampleValue / 32768.0;
                
            } else if (sampleSizeInBits == 8) {
                // 8-bit samples
                sampleValue = audioData[i] & 0xFF;
                
                // Convert to signed and normalize
                samples[i] = (sampleValue - 128) / 128.0;
            }
        }
        
        logger.info("Converted " + numSamples + " audio samples");
        return samples;
    }

    /**
     * Gets information about an audio file without reading all the data.
     * 
     * @param filePath Path to the audio file
     * @return String containing audio file information
     */
    public String getAudioFileInfo(String filePath) {
        try {
            if (filePath == null || filePath.trim().isEmpty()) {
                return "Error: Audio file path is null or empty";
            }
            
            File file = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioStream.getFormat();
            
            StringBuilder info = new StringBuilder();
            info.append("File: ").append(file.getName()).append("\n");
            info.append("Format: ").append(format.getEncoding()).append("\n");
            info.append("Sample Rate: ").append(format.getSampleRate()).append(" Hz\n");
            info.append("Channels: ").append(format.getChannels()).append("\n");
            info.append("Sample Size: ").append(format.getSampleSizeInBits()).append(" bits\n");
            info.append("Frame Length: ").append(audioStream.getFrameLength()).append("\n");
            
            double durationInSeconds = audioStream.getFrameLength() / format.getFrameRate();
            info.append("Duration: ").append(String.format("%.2f", durationInSeconds)).append(" seconds");
            
            audioStream.close();
            return info.toString();
            
        } catch (Exception e) {
            return "Error reading file information: " + e.getMessage();
        }
    }
}