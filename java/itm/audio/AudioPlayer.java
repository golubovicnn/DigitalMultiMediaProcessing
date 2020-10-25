package itm.audio;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

/**
 * Plays an audio file using the system's default sound output device
 * 
 */
public class AudioPlayer {

	/**
	 * Constructor
	 */
	public AudioPlayer() {

	}

	/**
	 * Plays audio data from a given input file to the system's default sound
	 * output device
	 * 
	 * @param input
	 *            the audio file
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	protected void playAudio(File input) throws IOException {

		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");

		AudioInputStream audio = null;
		try {
			audio = openAudioInputStream(input);
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("could not open audio file " + input
					+ ". Encoding / file format not supported");
		}

		try {
			rawplay(audio);
		} catch (LineUnavailableException e) {
			throw new IOException("Error when playing sound from file "
					+ input.getName() + ". Sound output device unavailable");
		}

		audio.close();

	}

	/**
	 * Decodes an encoded audio file and returns a PCM input stream
	 * 
	 * Supported encodings: MP3, OGG (requires SPIs to be in the classpath)
	 * 
	 * @param input
	 *            a reference to the input audio file
	 * @return a PCM AudioInputStream
	 * @throws UnsupportedAudioFileException
	 *             an audio file's encoding is not supported
	 * @throws IOException
	 *             general error when accessing audio file
	 */
	private AudioInputStream openAudioInputStream(File input)
			throws UnsupportedAudioFileException, IOException {

		AudioInputStream din = null;
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// open audio stream
		try {
			din = AudioSystem.getAudioInputStream(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get format
		AudioFormat format = din.getFormat();
		//DataLine.Info provides additional information specific to data lines. 
		//This information includes:
		//the audio formats supported by the data line
		//the minimum and maximum sizes of its internal buffer 
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);     //get dataline info

		// get decoded format
		if(!AudioSystem.isLineSupported(info))    //audiosystem is an "entrypoint" for all kinds of functions for manipulating
		{	//AudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian)
			format = new AudioFormat(format.getSampleRate(),16,format.getChannels(),true,false);
		}

		// get decoded audio input stream
		if(!format.matches(din.getFormat())){

			if(!AudioSystem.isConversionSupported(format,din.getFormat())){

				throw new UnsupportedAudioFileException();
			}


			din = AudioSystem.getAudioInputStream(format,din);   //convert inputstream to correct format, the one recently set
		}

		return din;
	}

	/**
	 * Writes audio data from an AudioInputStream to a SourceDataline
	 * 0
	 * @param audio
	 *            the audio data
	 * @throws IOException
	 *             error when writing audio data to source data line
	 * @throws LineUnavailableException
	 *             system's default source data line is not available
	 */
	private void rawplay(AudioInputStream audio) throws IOException,
			LineUnavailableException {

		
		
		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// get audio format
		AudioFormat format = audio.getFormat();
		
		// get a source data line
		SourceDataLine line = null; //is a line we can write to

		DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);

		try {
			line = (SourceDataLine)AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}



		// read samples from audio and write them to the data line
		line.open();
		line.start();

		int read;

		byte[] buffer = new byte[line.getBufferSize()];

		while ((read = audio.read(buffer,0,buffer.length))!= -1)
		{
			line.write(buffer,0,read);    //write the file to line -> file is being played
		}

		// properly close the line!
		line.stop();
		line.drain();
		line.close();
		line = null;
	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.out
					.println("usage: java itm.audio.AudioPlayer <input-audioFile>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		AudioPlayer player = new AudioPlayer();
		player.playAudio(fi);
		System.exit(0);

	}

}
