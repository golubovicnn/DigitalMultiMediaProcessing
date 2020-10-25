package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import itm.util.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class describes an abstract media object. It stores a reference to the
 * wrapped media file in its "instance" property and may hold metadata
 * associated to this media object like semantic tags or similar.
 */
public abstract class AbstractMedia {

	/**
	 * Reference to the media file.
	 */
	protected File instance = null;

	/**
	 * The name of this media object.
	 */
	protected String name = null;

	/**
	 * A list of "semantic tags" (in this simple implementation, a list of
	 * Strings)
	 */
	protected ArrayList<String> tags = null;

	/**
	 * Constructor.
	 */
	public AbstractMedia() {
		this.tags = new ArrayList<String>();
	}

	/**
	 * Constructor.
	 * 
	 * @param instance
	 *            a reference to the media file.
	 */
	public AbstractMedia(File instance) {
		this();
		this.instance = instance;
	}

	/**
	 * @return the size of the media object in bytes
	 */
	public long getSize() {
		if (instance == null)
			return -1L;

		if (!instance.exists())
			return -1L;

		return instance.length();
	}

	/**
	 * @return a list of tags associated to this media object.
	 */
	public ArrayList<String> getTags() {
		return tags;
	}

	/**
	 * Adds the passed tag to the list of tags associated to this media object.
	 */
	public void addTag(String tag) {
		if (tag != null)
			tags.add(tag);
	}

	/**
	 * Removes the passed tag from the list of tags associated to this media
	 * object.
	 */
	public void removeTag(String tag) {
		if (tag != null)
			tags.remove(tag);
	}

	/**
	 * @return the name of the media object (if not set, the name of the
	 *         instance file is returned or null if not available)
	 */
	public String getName() {
		if (name == null) {
			if (instance == null)
				return null;
			return instance.getName();
		}
		return name;
	}

	/**
	 * Sets the name of the media object.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return a reference to the media file.
	 */
	public File getInstance() {
		return instance;
	}

	/**
	 * Sets the reference to the media file.
	 */
	public void setInstance(File instance) {
		this.instance = instance;
	}

	/**
	 * Serializes this object to a string buffer.
	 */
	public StringBuffer serializeObject() throws IOException {
		StringWriter data = new StringWriter();
		PrintWriter out = new PrintWriter(data);
		out.println("file: " + getInstance());
		out.println("name: " + getName());
		out.println("size: " + getSize() + " bytes (" + getSize() / 1000 + "kB)");
		out.print("tags: ");
		for (String tag : tags)
			out.print(tag + ",");
		out.println();
		return data.getBuffer();
	}

	/**
	 * Deserializes this object from the passed string buffer.
	 */
	public void deserializeObject(String data) throws IOException {
		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("file: ")) {
				File f = new File(line.substring("file: ".length()));
				setInstance(f);
			} else if (line.startsWith("name: "))
				setName(line.substring("name: ".length()));
			else if (line.startsWith("size: "))
				;
			// no need to set size - will be calculated from media file directly
			else if (line.startsWith("tags: ")) {
				StringTokenizer st = new StringTokenizer(line.substring("tags: ".length()), ",");
				while (st.hasMoreTokens()) {
					String tag = st.nextToken().trim();
					addTag(tag);
				}
			}
		}
	}

	/**
	 * Serializes this object to the passed file.
	 */
	public void writeToFile(File outputFile) throws IOException {
		IOUtil.writeFile(serializeObject(), outputFile);
	}

	/**
	 * Deserializes this object from the passed file.
	 */
	public void readFromFile(File inputFile) throws IOException {
		String data = IOUtil.readFile(inputFile);
		deserializeObject(data);
	}

	/**
	 * For debugging purposes only
	 */
	@Override
	public String toString() {
		String ret = "-------------------------\n";
		try {
			ret += serializeObject().toString();
		} catch (IOException e0) {
			ret += "error: " + e0;
		}
		ret += "-------------------------\n";
		return ret;
	}

}
