package itm.model;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import itm.audio.AudioMetadataGenerator;
import itm.audio.AudioThumbGenerator;
import itm.image.ImageHistogramGenerator;
import itm.image.ImageMetadataGenerator;
import itm.image.ImageThumbnailGenerator;
import itm.video.VideoMetadataGenerator;
import itm.video.VideoThumbnailGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A very simple object factory for creating media objects.
 */
public class MediaFactory {

	/**
	 * A static cache of all loaded media objects
	 */
	protected static ArrayList<AbstractMedia> media = null;

	/**
	 * this directory should contain all the image files handled by this
	 * application.
	 */
	protected static File imageDir = null;

	/**
	 * this directory should contain all the audio files handled by this
	 * application.
	 */
	protected static File audioDir = null;

	/**
	 * this directory should contain all the video files handled by this
	 * application.
	 */
	protected static File videoDir = null;

	/**
	 * this directory should contain all the metadata files handled by this
	 * application.
	 */
	protected static File metadataDir = null;

	/**
	 * Indicates whether this factory was initialized or not.
	 */
	protected static boolean initialized = false;

	/**
	 * Initializes this media factory.
	 */
	public static void init(File imageDir, File audioDir, File videoDir,
			File metadataDir) {
		MediaFactory.imageDir = imageDir;
		MediaFactory.audioDir = audioDir;
		MediaFactory.videoDir = videoDir;
		MediaFactory.metadataDir = metadataDir;
		initialized = true;
	}

	/**
	 * Gets a list of all available media objects. The media is loaded only the
	 * first time, it is cached in memory for successive requests.
	 */
	public static ArrayList<AbstractMedia> getMedia() throws IOException {
		if (!initialized)
			throw new IOException(
					"error: mediafactory was not initialized yet!");
		if (media == null) // load media for the first time only
			media = loadMedia();
		return media;
	}

	/**
	 * Gets a list of all media objects that are tagged with the passed tag. The
	 * media is loaded only the first time, it is cached in memory for
	 * successive requests.
	 */
	public static ArrayList<AbstractMedia> getMedia(String tag)
			throws IOException {
		if (!initialized)
			throw new IOException(
					"error: mediafactory was not initialized yet!");
		if (media == null) // load media for the first time only
			media = loadMedia();
		ArrayList<AbstractMedia> ret = new ArrayList<AbstractMedia>();
		for (AbstractMedia am : media) {
			if (am.getTags().contains(tag))
				ret.add(am);
		}
		return ret;
	}

	/**
	 * Loads all media objects from the passed directory.
	 * 
	 * @param imageDir
	 *            this directory should contain all the image files handled by
	 *            this application.
	 * @param audioDir
	 *            this directory should contain all the audio files handled by
	 *            this application.
	 * @param videoDir
	 *            this directory should contain all the video files handled by
	 *            this application.
	 * @param metadataDir
	 *            this directory should contain all metadata files that describe
	 *            the media objects.
	 */
    protected static ArrayList<AbstractMedia> loadMedia() throws IOException
    {
        ArrayList<AbstractMedia> ret = new ArrayList<AbstractMedia>();
        
        // step 1.1: create image thumbnails, do not overwrite if not required
        ImageThumbnailGenerator itg = new ImageThumbnailGenerator();
        itg.batchProcessImages(imageDir, metadataDir, false);
        
        // step 1.2: create metadata, do not overwrite if not required
        ImageMetadataGenerator img = new ImageMetadataGenerator();
        ret.addAll(img.batchProcessImages(imageDir, metadataDir, false));

		// step 1.3: create histogram
		ImageHistogramGenerator histGenerator = new ImageHistogramGenerator();
		histGenerator.batchProcessImages(imageDir, metadataDir, 256);

        // step 2.1: create audio thumbnails (with a given length), do not
        // overwrite if not required
        AudioThumbGenerator atg = new AudioThumbGenerator(10);
        atg.batchProcessAudioFiles(audioDir, metadataDir);
        
        // step 2.3 create audio metadata
        AudioMetadataGenerator amg = new AudioMetadataGenerator();
        ret.addAll(amg.batchProcessAudio(audioDir, metadataDir, false));
        
        // step 3.1: create video thumbnails, do not overwrite if not required
        //VideoFrameGrabber vtg = new VideoFrameGrabber();
        VideoThumbnailGenerator vtg = new VideoThumbnailGenerator();
        vtg.batchProcessVideoFiles(videoDir, metadataDir,false,0);
        
        // step 3.2: create video metadata, do not overwrite if not required
        VideoMetadataGenerator vmg = new VideoMetadataGenerator();
        ret.addAll( vmg.batchProcessVideoFiles(videoDir, metadataDir, false) );
        
        return ret;
    }

	/**
	 * Factory method that creates an media object by reading a metadata file or
	 * by wrapping an image/video/audio file.
	 */
	public static AbstractMedia createMedia(File f) throws IOException {
		if (f == null)
			throw new IOException("Input file " + f + " was null!");
		if (!f.exists())
			throw new IOException("Input file " + f + " was not found!");

		String name = f.getName();
		if (name.indexOf(".") < 0)
			throw new IOException(
					"Could not determine what object to create from filename!");

		String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();

		// If passed file is a textfile: load metadata from file
		if (ext.equals("txt")) {
			if (name.startsWith("img_")) {
				AbstractMedia am = new ImageMedia();
				am.readFromFile(f);
				return am;
			} else if (name.startsWith("aud_")) {
				AbstractMedia am = new AudioMedia();
				am.readFromFile(f);
				return am;
			} else if (name.startsWith("vid_")) {
				AbstractMedia am = new VideoMedia();
				am.readFromFile(f);
			} else
				throw new IOException(
						"Could not determine media type of metadata file "
								+ name);
		}
		// If passed file is a jpg/gif/png: create a new image object
		if ((ext.equals("jpg") || ext.equals("gif") || ext.equals("png") || ext
				.equals("bmp")))
			return new ImageMedia(f);

		// If passed file is a wav/...: create a new audio object
		if (ext.equals("wav") || ext.equals("mp3") || ext.equals("ogg"))
			return new AudioMedia(f);

		// If passed file is a avi/...: create a new video object
		if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4"))
			return new VideoMedia(f);

		throw new IOException(
				"Could not determine what object to create from filename extension " + ext);
	}

	/**
	 * For debugging only.
	 */
	public static void main(String[] args) throws Exception {

		// args = new
		// String[]{"./webapps/itm/media/img","./webapps/itm/media/audio","./webapps/itm/media/md"};

		if (args.length < 4) {
			System.out.println("usage: java itm.model.MediaFactory <image-directory> <audio-directory> <video-directory> <metadata-directory>");
			System.exit(1);
		}
		File fimg = new File(args[0]);
		File faud = new File(args[1]);
		File fvid = new File(args[2]);
		File fmd = new File(args[3]);
		MediaFactory.init(fimg, faud, fvid, fmd);
		ArrayList<AbstractMedia> media = MediaFactory.loadMedia();
		for (AbstractMedia am : media)
			System.out.println("media: " + am);
	}

}
