package itm.video;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import com.xuggle.xuggler.*;

import itm.model.MediaFactory;
import itm.model.VideoMedia;

import javax.crypto.spec.IvParameterSpec;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * This class creates JPEG thumbnails from from video frames grabbed from the
 * middle of a video stream It can be called with 2 parameters, an input
 * filename/directory and an output directory.
 * 
 * If the input file or the output directory do not exist, an exception is
 * thrown.
 */

public class VideoFrameGrabber {

	/**
	 * Constructor.
	 */
	public VideoFrameGrabber() {
	}

	/**
	 * Processes the passed input video file / video file directory and stores
	 * the processed files in the output directory.
	 * 
	 * @param input
	 *            a reference to the input video file / input directory
	 * @param output
	 *            a reference to the output directory
	 */
	public ArrayList<File> batchProcessVideoFiles(File input, File output) throws IOException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		ArrayList<File> ret = new ArrayList<File>();

		if (input.isDirectory()) {
			File[] files = input.listFiles();
			for (File f : files) {
				if (f.isDirectory())
					continue;

				String ext = f.getName().substring(f.getName().lastIndexOf(".") + 1).toLowerCase();
				if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv")
						|| ext.equals("mp4")) {
					File result = processVideo(f, output);
					System.out.println("converted " + f + " to " + result);
					ret.add(result);
				}

			}

		} else {
			String ext = input.getName().substring(input.getName().lastIndexOf(".") + 1).toLowerCase();
			if (ext.equals("avi") || ext.equals("swf") || ext.equals("asf") || ext.equals("flv") || ext.equals("mp4")) {
				File result = processVideo(input, output);
				System.out.println("converted " + input + " to " + result);
				ret.add(result);
			}
		}
		return ret;
	}

	/**
	 * Processes the passed audio file and stores the processed file to the
	 * output directory.
	 * 
	 * @param input
	 *            a reference to the input audio File
	 * @param output
	 *            a reference to the output directory
	 */
	protected File processVideo(File input, File output) throws IOException, IllegalArgumentException {
		if (!input.exists())
			throw new IOException("Input file " + input + " was not found!");
		if (input.isDirectory())
			throw new IOException("Input file " + input + " is a directory!");
		if (!output.exists())
			throw new IOException("Output directory " + output + " not found!");
		if (!output.isDirectory())
			throw new IOException(output + " is not a directory!");

		File outputFile = new File(output, input.getName() + "_thumb.jpg");
		// load the input video file

		// ***************************************************************
		// Fill in your code here!
		// ***************************************************************


		IContainer iContainer  = IContainer.make();
		IStreamCoder coder = null;
		iContainer.open(input.getPath(),IContainer.Type.READ,null);//containar format - null


		long frames =0;  // this will be the max amount of frames in the video stream
		int numStream = iContainer.getNumStreams();   //Total number of streams
		int videoStreamIndex = -1;  //Correct number of the video stream, -1 if none are found


		//Loop to find correct video stream, of not found an exception will be thrown. Also to extract the amount of frames from the stream

		for(int i=0; i<numStream; i++){

			IStream stream = iContainer.getStream(i);
			coder = stream.getStreamCoder();

			if(coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO){

				videoStreamIndex= i;
				frames=stream.getDuration();
				break;
			}

		}

		if(videoStreamIndex == -1)
			throw new IOException("The Inpoutfil"+ input + "does NOT have a VIDEOSTREAM!");


		int width = coder.getWidth();  //this is the video width
		int height= coder.getHeight();  // this is the video height



		// Create a resampler in case the video isn't in the right
		// format (BGR24)
		// Call the resampler with
		// .make with parameters: (targetWidth, targetHeight, targetPixelFormat, srcWidth, srcHeight, srcPixelFormat)

		//Converts IVideoPicture objects of a given width, height and format to a new width, height or format. 
		IVideoResampler resampler = IVideoResampler.make(width,height,IPixelFormat.Type.BGR24, width,height,coder.getPixelType());

		IPacket ipacket= IPacket.make(); // the packets that will be loaded and analysed later on
		
		//we have to get frame from the middle of the video
		// Jump the coorect place in the video stream( in this case halftime)
		iContainer.seekKeyFrame(
				videoStreamIndex						 //streamID
				,0                    		 			 //Starting point
				,frames/2			   	 				 //Point to seek to
				,frames								  	 //End point
				,0);								     // type of seeker (0= none/any)

		//videoPicture that will be decoded and saved if its complete
		IVideoPicture iVideoPicture = IVideoPicture.make(coder.getPixelType(),width,height);
		boolean success = false;  //making sure a Keyframe was found


		//read out the contents of the media file

		while (iContainer.readNextPacket(ipacket)>= 0){

			if(iVideoPicture.isComplete())   // finish the picture if it is complete
				break;

			if(ipacket.isKeyPacket() || success){
				if(ipacket.getStreamIndex() == videoStreamIndex){
					success= true;
					coder.open();

					int byteOffset = 0;  //bytes that have been decoded

					//Go through the whole package until its done or the picture is complete
					while(byteOffset < ipacket.getSize()){
						byteOffset += coder.decodeVideo(iVideoPicture,ipacket,byteOffset);


						if(iVideoPicture.isComplete()){
							// resample picture if its necessary and save it locally
							if(coder.getPixelType() != IPixelFormat.Type.BGR24){
								IVideoPicture resampled = IVideoPicture.make(
										resampler.getOutputPixelFormat(),
										width,
										height
								);

								resampler.resample(resampled,iVideoPicture);
								ImageIO.write(Utils.videoPictureToImage(resampled),"jpg",outputFile);
							}else  {
								ImageIO.write(Utils.videoPictureToImage(iVideoPicture),"jpg",outputFile);
							}
							break;

						}

					} //packet is done
					coder.close();  //clean it up
				}// if (videostream) end
			} // if end -> (ipacket.iskey);
		}

		// clean it up

		iContainer.close();
		iContainer = null;
		coder =null;


		return outputFile;

	}

	/**
	 * Main method. Parses the commandline parameters and prints usage
	 * information if required.
	 */
	public static void main(String[] args) throws Exception {

		// args = new String[] { "./media/video", "./test" };

		if (args.length < 2) {
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-videoFile> <output-directory>");
			System.out.println("usage: java itm.video.VideoFrameGrabber <input-directory> <output-directory>");
			System.exit(1);
		}
		File fi = new File(args[0]);
		File fo = new File(args[1]);
		VideoFrameGrabber grabber = new VideoFrameGrabber();
		grabber.batchProcessVideoFiles(fi, fo);
	}

}
