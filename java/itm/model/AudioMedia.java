package itm.model;

import java.awt.image.BufferedImage;

/*******************************************************************************
 This file is part of the ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class AudioMedia extends AbstractMedia {

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	int channels;
	int bitrate;
	double duration;
	String encoding;
	String author;
	String title;
	String date;
	String comment;
	String album;
	String track;
	String composer;
	String genre;
	float frequency;

	private final String PROPNAME_channels= "channels";
	private final String PROPNAME_bitrate= "bitrate";
	private final String PROPNAME_duration= "duration";
	private final String PROPNAME_encoding= "encoding";
	private final String PROPNAME_author= "author";
	private final String PROPNAME_title= "title";
	private final String PROPNAME_date= "date";
	private final String PROPNAME_comment= "comment";
	private final String PROPNAME_album= "album";
	private final String PROPNAME_track= "track";
	private final String PROPNAME_composer= "composer";
	private final String PROPNAME_genre= "genre";
	private final String PROPNAME_frequency= "frequency";
	private final String PROPNAME_seperator=": ";


	public int getChannels() {
		return channels;
	}

	public void setChannels(int channels) {
		this.channels = channels;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	/**
	 * Constructor.
	 */
	public AudioMedia() {
		super();
	}

	/**
	 * Constructor.
	 */
	public AudioMedia(File instance) {
		super(instance);
	}

	/* GET / SET methods */

	// ***************************************************************
	// Fill in your code here!
	// ***************************************************************

	/* (de-)serialization */

	/**
	 * Serializes this object to the passed file.
	 *
	 */
	@Override
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("type: audio");
		StringBuffer sup = super.serializeObject();
		out.print(sup);

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************

		// print properties
		out.println(PROPNAME_album+PROPNAME_seperator+getAlbum());
		out.println(PROPNAME_author+PROPNAME_seperator+getAuthor());
		out.println(PROPNAME_bitrate+PROPNAME_seperator+getBitrate());
		out.println(PROPNAME_channels+PROPNAME_seperator+getChannels());
		out.println(PROPNAME_comment+PROPNAME_seperator+getComment());
		out.println(PROPNAME_composer+PROPNAME_seperator+getComposer());
		out.println(PROPNAME_date+PROPNAME_seperator+getDate());
		out.println(PROPNAME_duration+PROPNAME_seperator+getDuration());
		out.println(PROPNAME_encoding+PROPNAME_seperator+getEncoding());
		out.println(PROPNAME_frequency+PROPNAME_seperator+getFrequency());
		out.println(PROPNAME_genre+PROPNAME_seperator+getGenre());
		out.println(PROPNAME_title+PROPNAME_seperator+getTitle());
		out.println(PROPNAME_track+PROPNAME_seperator+getTrack());


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

			// ***************************************************************
			// Fill in your code here!
			// ***************************************************************
			if ( line.startsWith( PROPNAME_channels ) ) {
				setChannels( Integer.parseInt(line.substring( (PROPNAME_channels+PROPNAME_seperator).length() )) );
			}
			else if ( line.startsWith( PROPNAME_bitrate ) ) {
				setBitrate( Integer.parseInt(line.substring( (PROPNAME_bitrate+PROPNAME_seperator).length() )) );
			}
			else if ( line.startsWith( PROPNAME_duration ) ) {
				setDuration( Integer.parseInt(line.substring( (PROPNAME_duration+PROPNAME_seperator).length() )) );
			}
			else if ( line.startsWith( PROPNAME_album ) ) {
				setAlbum(line.substring( (PROPNAME_album+PROPNAME_seperator).length() ) );
			}
			else if ( line.startsWith( PROPNAME_author ) ) {
				setAuthor(line.substring( (PROPNAME_author+PROPNAME_seperator).length() ) );
			}
			else  if ( line.startsWith( PROPNAME_comment ) ) {
				setComment(line.substring( (PROPNAME_comment+PROPNAME_seperator).length() ) );
			}
			else  if ( line.startsWith( PROPNAME_composer ) ) {
				setComposer(line.substring( (PROPNAME_composer+PROPNAME_seperator).length() ) );
			}
			else  if ( line.startsWith( PROPNAME_date ) ) {
				setDate(line.substring( (PROPNAME_date+PROPNAME_seperator).length() ) );
			}
			else  if ( line.startsWith( PROPNAME_encoding ) ) {
				setEncoding(line.substring( (PROPNAME_encoding+PROPNAME_seperator).length() ) );
			}
			else  if ( line.startsWith( PROPNAME_frequency ) ) {
				setFrequency(Float.parseFloat(line.substring( (PROPNAME_frequency+PROPNAME_seperator).length()) ) );
			}
			else  if ( line.startsWith( PROPNAME_genre ) ) {
				setGenre(line.substring( (PROPNAME_genre+PROPNAME_seperator).length() ) );
			}
			else  if ( line.startsWith( PROPNAME_title ) ) {
				setTitle(line.substring( (PROPNAME_title+PROPNAME_seperator).length() ) );
			}
			else  if ( line.startsWith( PROPNAME_track ) ) {
				setTrack(line.substring( (PROPNAME_track+PROPNAME_seperator).length() ) );
			}

		}
	}

}