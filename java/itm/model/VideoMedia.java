package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class VideoMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* video format metadata */
	String videoCodec;
	String videoCodecID;
	double videoFrameRate;
	long videoLength; //seconds
	int videoHeight;//pixel
	int videoWidth;//pixel

	/* audio format metadata */

	String audioCodec;
	String audioCodecID;
	int audioChannels;//number of channels
	int audioSampleRate;// Hz
	int audioBitRate;// kb/s

	/**
	 * Constructor.
	 */
	public VideoMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public VideoMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************




	private final String PROPNAME_videoCodec= "videoCodec";
	private final String PROPNAME_videoCodecID= "videoCodecID";
	private final String PROPNAME_videoFrameRate= "videoFrameRate";
	private final String PROPNAME_videoLength= "videoLength";
	private final String PROPNAME_videoHeight= "videoHeight";
	private final String PROPNAME_videoWidth= "videoWidth";
	private final String PROPNAME_audioCodec= "audioCodec";
	private final String PROPNAME_audioCodecID= "audioCodecID";
	private final String PROPNAME_audioChannels= "audioChannels";
	private final String PROPNAME_audioSampleRate= "audioSampleRate";
	private final String PROPNAME_audioBitRate= "audioBitRate";
	private final String PROPNAME_seperator=": ";

	public String getVideoCodec() {
		return videoCodec;
	}

	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}

	public String getVideoCodecID() {
		return videoCodecID;
	}

	public void setVideoCodecID(String videoCodecID) {
		this.videoCodecID = videoCodecID;
	}

	public double getVideoFrameRate() {
		return videoFrameRate;
	}

	public void setVideoFrameRate(double videoFrameRate) {
		this.videoFrameRate = videoFrameRate;
	}

	public long getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(long videoLength) {
		this.videoLength = videoLength;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public String getAudioCodec() {
		return audioCodec;
	}

	public void setAudioCodec(String audioCodec) {
		this.audioCodec = audioCodec;
	}

	public String getAudioCodecID() {
		return audioCodecID;
	}

	public void setAudioCodecID(String audioCodecID) {
		this.audioCodecID = audioCodecID;
	}

	public int getAudioChannels() {
		return audioChannels;
	}

	public void setAudioChannels(int audioChannels) {
		this.audioChannels = audioChannels;
	}

	public int getAudioSampleRate() {
		return audioSampleRate;
	}

	public void setAudioSampleRate(int audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}

	public int getAudioBitRate() {
		return audioBitRate;
	}

	public void setAudioBitRate(int audioBitRate) {
		this.audioBitRate = audioBitRate;
	}


	/* (de-)serialization */

	/**
	 * Serializes this object to the passed file.
	 * 
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: video");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		/* video fields */

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		out.println(PROPNAME_audioBitRate + PROPNAME_seperator + getAudioBitRate());
		out.println(PROPNAME_audioChannels + PROPNAME_seperator + getAudioChannels());
		out.println(PROPNAME_audioCodec + PROPNAME_seperator + getAudioCodec());
		out.println(PROPNAME_audioCodecID + PROPNAME_seperator + getAudioCodecID());
		out.println(PROPNAME_audioSampleRate + PROPNAME_seperator + getAudioSampleRate());
		out.println(PROPNAME_videoCodec + PROPNAME_seperator + getVideoCodec());
		out.println(PROPNAME_videoCodecID + PROPNAME_seperator + getVideoCodecID());
		out.println(PROPNAME_videoFrameRate + PROPNAME_seperator + getVideoFrameRate());
		out.println(PROPNAME_videoHeight + PROPNAME_seperator + getVideoHeight());
		out.println(PROPNAME_videoLength + PROPNAME_seperator + getVideoLength());
		out.println(PROPNAME_videoWidth + PROPNAME_seperator + getVideoWidth());

		return data.getBuffer();
	}

	/**
	 * Deserializes this object from the passed string buffer.
	 */
	@Override
	public void deserializeObject(String data) throws IOException {
		super.deserializeObject(data);

		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {

			/* video fields */
			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************

			if ( line.startsWith( PROPNAME_audioBitRate ) ) {
				setAudioBitRate( Integer.parseInt(line.substring( (PROPNAME_audioBitRate+PROPNAME_seperator).length() )) );
			}
			else if ( line.startsWith( PROPNAME_audioChannels ) ) {
				setAudioChannels( Integer.parseInt(line.substring( (PROPNAME_audioChannels+PROPNAME_seperator).length() )) );
			}
			else if ( line.startsWith( PROPNAME_audioCodec ) ) {
				setAudioCodec( line.substring( (PROPNAME_audioCodec+PROPNAME_seperator).length() ) );
			}
			else if ( line.startsWith( PROPNAME_audioCodecID ) ) {
				setAudioCodecID( line.substring( (PROPNAME_audioCodecID+PROPNAME_seperator).length() ) );
			}
			else if ( line.startsWith( PROPNAME_audioSampleRate ) ) {
				setAudioSampleRate ( Integer.parseInt(line.substring( (PROPNAME_audioSampleRate+PROPNAME_seperator).length()) ) );
			}
			else if ( line.startsWith( PROPNAME_videoCodec ) ) {
				setVideoCodec( line.substring( (PROPNAME_videoCodec+PROPNAME_seperator).length() ) );
			}
			else if ( line.startsWith( PROPNAME_videoCodecID ) ) {
				setVideoCodecID( line.substring( (PROPNAME_videoCodecID+PROPNAME_seperator).length() ) );
			}
			else if ( line.startsWith( PROPNAME_videoFrameRate ) ) {
				setVideoFrameRate( Double.parseDouble(line.substring( (PROPNAME_videoFrameRate+PROPNAME_seperator).length() )) );
			}
			else if ( line.startsWith( PROPNAME_videoHeight ) ) {
				setVideoHeight( Integer.parseInt(line.substring( (PROPNAME_videoHeight+PROPNAME_seperator).length() )) );
			}
			else if ( line.startsWith( PROPNAME_videoLength ) ) {
				setVideoLength( Long.parseLong(line.substring( (PROPNAME_videoLength+PROPNAME_seperator).length())) );
			}
			else if ( line.startsWith( PROPNAME_videoWidth ) ) {
				setVideoWidth( Integer.parseInt(line.substring( (PROPNAME_videoWidth+PROPNAME_seperator).length() )) );
			}
		}
	}

}
