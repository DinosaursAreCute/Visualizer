package com.dinosaur.musicvisualizer.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AudioFileReader class.
 * Tests the audio file reading capabilities and error handling.
 */
class AudioFileReaderTest {
    
    private AudioFileReader audioFileReader;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        audioFileReader = new AudioFileReader();
    }

    @Test
    void testCanReadFile_WithNonExistentFile() {
        // Test with a file that doesn't exist
        String nonExistentFile = "/path/to/nonexistent/file.wav";
        
        boolean canRead = audioFileReader.canReadFile(nonExistentFile);
        
        assertFalse(canRead, "Should return false for non-existent files");
    }

    @Test
    void testCanReadFile_WithInvalidFile() throws IOException {
        // Create a temporary text file (not an audio file)
        File tempFile = tempDir.resolve("not_audio.wav").toFile();
        tempFile.createNewFile();
        
        // Write some non-audio content
        java.nio.file.Files.write(tempFile.toPath(), "This is not an audio file".getBytes());
        
        boolean canRead = audioFileReader.canReadFile(tempFile.getAbsolutePath());
        
        assertFalse(canRead, "Should return false for invalid audio files");
    }

    @Test
    void testReadAudioFile_WithNonExistentFile() {
        // Test reading a file that doesn't exist
        String nonExistentFile = "/path/to/nonexistent/file.wav";
        
        assertThrows(IOException.class, () -> {
            audioFileReader.readAudioFile(nonExistentFile);
        }, "Should throw IOException for non-existent files");
    }

    @Test
    void testGetAudioFileInfo_WithNonExistentFile() {
        // Test getting info for a file that doesn't exist
        String nonExistentFile = "/path/to/nonexistent/file.wav";
        
        String info = audioFileReader.getAudioFileInfo(nonExistentFile);
        
        assertNotNull(info, "Should return error message, not null");
        assertTrue(info.contains("Error"), "Should contain error message");
    }

    @Test
    void testGetAudioFileInfo_WithInvalidFile() throws IOException {
        // Create a temporary non-audio file
        File tempFile = tempDir.resolve("invalid.wav").toFile();
        tempFile.createNewFile();
        
        String info = audioFileReader.getAudioFileInfo(tempFile.getAbsolutePath());
        
        assertNotNull(info, "Should return error message, not null");
        assertTrue(info.contains("Error"), "Should contain error message for invalid audio file");
    }

    @Test
    void testAudioFileReaderInstantiation() {
        // Test that we can create an instance
        AudioFileReader reader = new AudioFileReader();
        
        assertNotNull(reader, "Should be able to create AudioFileReader instance");
    }

    @Test
    void testCanReadFile_WithNullPath() {
        // Test with null path - should handle gracefully
        boolean canRead = audioFileReader.canReadFile(null);
        
        assertFalse(canRead, "Should return false for null file path");
    }

    @Test
    void testCanReadFile_WithEmptyPath() {
        // Test with empty path
        boolean canRead = audioFileReader.canReadFile("");
        
        assertFalse(canRead, "Should return false for empty file path");
    }

    @Test
    void testReadAudioFile_WithNullPath() {
        // Test reading with null path
        assertThrows(Exception.class, () -> {
            audioFileReader.readAudioFile(null);
        }, "Should throw exception for null file path");
    }

    @Test
    void testReadAudioFile_WithEmptyPath() {
        // Test reading with empty path
        assertThrows(Exception.class, () -> {
            audioFileReader.readAudioFile("");
        }, "Should throw exception for empty file path");
    }

    /**
     * Integration test that would work with a real WAV file.
     * This test is disabled by default since we don't have a real audio file.
     * To enable this test, create a simple WAV file and update the path.
     */
    // @Test
    void testReadAudioFile_WithValidWavFile() {
        // This would test with an actual WAV file
        // String validWavFile = "path/to/test.wav";
        // 
        // try {
        //     if (audioFileReader.canReadFile(validWavFile)) {
        //         double[] audioData = audioFileReader.readAudioFile(validWavFile);
        //         
        //         assertNotNull(audioData, "Audio data should not be null");
        //         assertTrue(audioData.length > 0, "Audio data should contain samples");
        //         
        //         // Check that values are normalized
        //         for (double sample : audioData) {
        //             assertTrue(sample >= -1.0 && sample <= 1.0, 
        //                       "Audio samples should be normalized between -1.0 and 1.0");
        //         }
        //     }
        // } catch (Exception e) {
        //     fail("Should not throw exception for valid WAV file: " + e.getMessage());
        // }
    }
}