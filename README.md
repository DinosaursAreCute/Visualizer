# Terminal Music Visualizer

A Java CLI application that creates beautiful visual representations of audio files directly in your terminal using ASCII art and ANSI colors.

## 🎵 Features

- **Waveform Visualization**: Display audio waveforms with colored amplitude indicators
- **Frequency Bar Charts**: Show frequency distribution across different bands
- **Terminal Colors**: Beautiful ANSI color support for enhanced visual experience
- **Cross-Platform**: Works on Windows, macOS, and Linux terminals
- **Lightweight**: No external dependencies for basic functionality
- **Extensible**: Clean architecture ready for additional visualization modes

## 📋 Requirements

- Java 11 or higher
- Maven 3.6 or higher
- Terminal with ANSI color support (most modern terminals)

## 🚀 Quick Start

### Building the Project

```bash
# Clone the repository
git clone https://github.com/DinosaursAreCute/Visualizer.git
cd Visualizer

# Build with Maven
mvn clean compile

# Run tests
mvn test
```

### Running the Application

```bash
# Run with demo visualization
mvn exec:java

# Run with your own audio file
mvn exec:java -Dexec.args="/path/to/your/audio.wav"

# Build and run JAR
mvn package
java -jar target/music-visualizer-1.0.0.jar [audio-file.wav]
```

## 📁 Project Structure

```
music-visualizer/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── dinosaur/
│   │   │           └── musicvisualizer/
│   │   │               ├── Main.java                    # CLI entry point
│   │   │               ├── audio/
│   │   │               │   ├── AudioFileReader.java     # Audio file processing
│   │   │               │   └── AudioVisualizer.java     # Visualization engine
│   │   │               └── utils/
│   │   │                   ├── Logger.java              # Logging utility
│   │   │                   └── TerminalUtils.java       # Terminal operations
│   │   └── resources/
│   │       └── demo.wav                                 # Sample audio file
│   └── test/
│       └── java/
│           └── com/
│               └── dinosaur/
│                   └── musicvisualizer/
│                       └── audio/
│                           └── AudioFileReaderTest.java # Unit tests
│
├── .gitignore                                          # Git ignore rules
├── README.md                                           # This file
└── pom.xml                                            # Maven configuration
```

## 🎨 Visualization Examples

### Waveform Display
```
🌊 Waveform Visualization

│████████████████████████████████████████████████████████████████████████████│
│██████████████████████████████████████████████████████████████████          │
│████████████████████████████████████████████                               │
│██████████████████████████████                                             │
│████████████                                                               │
│                                                                           │
│                                                                           │
│                                                                           │
│                                                                           │
└───────────────────────────────────────────────────────────────────────────┘
```

### Frequency Bars
```
📊 Frequency Bars

██████████████████████████████████████
██████████████████████████████████████
██████████████████████████████████████
██████████████████████████████        
██████████████████████████            
██████████████████████                
██████████████████                    
██████████████                        
██████████                            
──────────────────────────────────────
Low Freq        Mid Freq        High Freq
```

## 🛠️ Configuration

### Debug Mode
Enable debug logging:
```bash
java -Dmusic.visualizer.debug=true -jar music-visualizer.jar
```

### Custom Terminal Settings
The application automatically detects terminal capabilities, but you can override:
- `COLUMNS`: Terminal width
- `LINES`: Terminal height
- `TERM`: Terminal type for ANSI support detection

## 📝 Usage Examples

```bash
# Basic usage with demo
java -jar music-visualizer.jar

# Visualize your favorite song
java -jar music-visualizer.jar ~/Music/song.wav

# With debug output
java -Dmusic.visualizer.debug=true -jar music-visualizer.jar music.wav
```

## 🔧 Development

### Adding New Visualization Types

1. Extend the `AudioVisualizer` class with new visualization methods
2. Add configuration options in `Main.java`
3. Create corresponding tests in the test directory

### Supported Audio Formats

Currently supports:
- WAV files (PCM format)
- 8-bit and 16-bit samples
- Mono and stereo channels

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 🎯 Future Enhancements

- [ ] Support for MP3 and other audio formats
- [ ] Real-time audio input visualization
- [ ] Customizable color schemes
- [ ] Export visualizations to image files
- [ ] Interactive controls (pause, rewind, fast-forward)
- [ ] Frequency filtering and EQ visualization
- [ ] 3D ASCII visualization modes

## 📞 Support

If you encounter any issues or have questions:

1. Check the existing [Issues](https://github.com/DinosaursAreCute/Visualizer/issues)
2. Create a new issue with detailed information
3. Include your Java version, OS, and terminal type

## 🏆 Acknowledgments

- Inspired by terminal-based music visualizers like `cava`
- Built with ❤️ for the command-line music enthusiast community
- Thanks to all contributors and users who make this project better

---

**Enjoy visualizing your music in style! 🎵✨**