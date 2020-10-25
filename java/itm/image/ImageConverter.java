package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2017
    (c) University of Vienna 2009-2017
*******************************************************************************/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import javax.imageio.stream.*;
import javax.imageio.ImageWriter;

/**
    This class converts images into various image formats (BMP, PNG, ...).
    It can be called with 3 parameters, an input filename/directory, an output directory and a target format.
    It will read the input image(s) and convert it/them to the target format and write them to the output directory.
    
    If the input file/dir or the output directory do not exist, an exception is thrown.
*/
public class ImageConverter 
{

    public final static String BMP = "bmp";
    public final static String PNG = "png";
    public final static String JPEG = "jpeg";
    public final static String JPG = "jpg";
   
    /**
        Constructor.
    */
    public ImageConverter()
    {
    }

    /**
        Processes the passed input image / image directory and stores the processed files to the output directory.
        @param input a reference to the input image / input directory
        @param output a reference to the output directory
      	@param targetFormat bmp, png or jpeg
      	@param quality a number between 0 (minimum quality) and 1 (max quality)  
    */
    public ArrayList<File> batchProcessImages( File input, File output, String targetFormat, float quality ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );
        if ( ( ! targetFormat.equalsIgnoreCase( BMP ) ) &&
             ( ! targetFormat.equalsIgnoreCase( PNG ) ) &&
             ( ! targetFormat.equalsIgnoreCase( JPEG ) ) )
            throw new IllegalArgumentException( "Unknown target format: " + targetFormat );
             

        ArrayList<File> ret = new ArrayList<File>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, targetFormat, quality  );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + f + " : " + e0.toString() );
                    }
                 }
            } else {
            try {
                File result = processImage( input, output, targetFormat, quality );
                System.out.println( "converted " + input + " to " + result );
                ret.add( result );
            } catch ( Exception e0 ) {
                System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        return ret;
    }    
    
    /**
        Processes the passed input image and stores the processed file to the output directory.
        @param input a reference to the input image
        @param output a reference to the output directory
      	@param targetFormat bmp, png or jpeg
      	@param quality a number between 0 (minimum quality) and 1 (max quality)  
    */
	protected File processImage( File input, File output, String targetFormat, float quality ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) 
            throw new IOException( "Input file " + input + " was not found!" );
        if ( input.isDirectory() ) 
            throw new IOException( "Input file " + input + " is a directory!" );
        if ( ! output.exists() ) 
            throw new IOException( "Output directory " + output + " not found!" );
        if ( ! output.isDirectory() ) 
            throw new IOException( output + " is not a directory!" );
        if ( ( ! targetFormat.equalsIgnoreCase( BMP ) ) &&
                ( ! targetFormat.equalsIgnoreCase( PNG ) ) &&
                ( ! targetFormat.equalsIgnoreCase( JPEG ) ) )
               throw new IllegalArgumentException( "Unknown target format: " + targetFormat );

        File outputFile = null;
        
        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        // load the input image
       
        // encode and save the image #


        BufferedImage image = null;        // this is the intern fileformat for java for storing  /image objekt

        try {
            image = ImageIO.read(input);  //  reading the image and then converting to BufferedImage -> image
        } catch (IOException e) {
            System.out.println(e);
        }



        if(targetFormat.equals(BMP) || targetFormat.equals(PNG)){    // for "BMP" & "PNG" you may use the standart writer, for JPEG

            try{
               outputFile= new File(output+"/"+input.getName()+"."+targetFormat);
                ImageIO.write(image,targetFormat,outputFile);

            }catch (IOException e){
                System.out.println("Could NOT be SAVED!"+e);
            }

        }

        else if(quality >= 0 && quality <=1){

            outputFile = new File(output+"/"+input.getName()+"-"+quality+"."+targetFormat); // + quality, because its "jpeg" not the other 2 jormats

            Iterator iterator = ImageIO.getImageWritersByFormatName("jpeg");            //writer for "jpeg"
            ImageWriter writer = (ImageWriter)iterator.next();                           //with writer going through all pixels, picture get written new
            ImageWriteParam IWP= writer.getDefaultWriteParam(); //damit du werte setzten kannst


            IWP.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);  // for quality (zugriff)

            IWP.setCompressionQuality(quality); // sets the compression quality 0-1

            FileImageOutputStream outputStream = new FileImageOutputStream(outputFile);
            writer.setOutput(outputStream);
            IIOImage outputimage = new IIOImage(image,null,null);
            writer.write(null, outputimage,IWP);
            writer.dispose(); //close writer
        }
        else
            throw new IllegalArgumentException("Quality-parameter not valid, must be between 0 and 1");


        return outputFile;
    }
     
    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
    	if ( args.length < 3 ) {
            System.out.println( "usage: java itm.image.ImageConverter <input-image> <output-directory> <format> [<quality>]" );
            System.out.println( "usage: java itm.image.ImageConverter <input-directory> <output-directory> <format> [<quality>]" );
            System.out.println( "");
            System.out.println( "formats: bmp, png, jpeg" );
            System.out.println( "quality: only for jpeg, 0: low quality, 1: highest quality" );
            System.exit( 1 );
            }
        // read params
        File fi = new File( args[0] ); //inputfile
        File fo = new File( args[1] ); //outputfile
        String targetFormat = args[2]; //format
        float quality = 1.0f;
        if ( args.length > 3 ) {
        	quality = Float.parseFloat( args[3] ); // quality
        }

        System.out.println( "converting " + fi.getAbsolutePath() + " to " + fo.getAbsolutePath() );
        ImageConverter converter = new ImageConverter();
        converter.batchProcessImages( fi, fo, targetFormat, quality );        
    }    
}
