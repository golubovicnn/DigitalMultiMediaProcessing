package itm.image;



/*******************************************************************************
    This file is part of the ITM course 2017
    (c) University of Vienna 2009-2017
*******************************************************************************/


// Yngve Skorge 1506252


import itm.model.ImageMedia;

import itm.model.MediaFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
    This class reads images of various formats and stores some basic image meta data to text files.
    It can be called with 3 parameters, an input filename/directory, an output directory and an "overwrite" flag.
    It will read the input image(s), retrieve some meta data and write it to a text file in the output directory.
    The overwrite flag indicates whether the resulting output file should be overwritten or not.
    
    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageMetadataGenerator 
{

    /**
        Constructor.
    */
    public ImageMetadataGenerator()
    {
    }
   

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image directory
        @param output a reference to the output directory
        @param overwrite indicates whether existing metadata files should be overwritten or not
        @return a list of the created media objects (images)
    */
    public ArrayList<ImageMedia> batchProcessImages( File input, File output, boolean overwrite ) throws IOException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        ArrayList<ImageMedia> ret = new ArrayList<ImageMedia>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    ImageMedia result = processImage( f, output, overwrite );
                    System.out.println( "converted " + f + " to " + output );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        } else {
                try {
                    ImageMedia result = processImage( input, output, overwrite );
                    System.out.println( "converted " + input + " to " + output );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
        }
        return ret;
    }    
    
    /**
        Processes the passed input image and stores the extracted metadata to a textfile in the output directory.
        @param input a reference to the input image
        @param output a reference to the output directory
        @param overwrite indicates whether existing metadata files should be overwritten or not
        @return the created image media object
    */
    protected ImageMedia processImage( File input, File output, boolean overwrite ) throws IOException, IllegalArgumentException
    {
        if ( ! input.exists() ) {
            throw new IOException( "Input file " + input + " was not found!" );
        }
        if ( input.isDirectory() ) {
            throw new IOException( "Input file " + input + " is a directory!" );
        }
        if ( ! output.exists() ) {
            throw new IOException( "Output directory " + output + " not found!" );
        }
        if ( ! output.isDirectory() ) {
            throw new IOException( output + " is not a directory!" );
        }

        // create outputfilename and check whether thumb already exists. All image 
        // metadata files have to start with "img_" -  this is used by the mediafactory!
        File outputFile = new File( output, "img_" + input.getName() + ".txt" );
        if ( outputFile.exists() ) {
            if ( ! overwrite ) {
                // load from file
                ImageMedia media = new ImageMedia();
                media.readFromFile( outputFile );
                return media;
                }
        }


        // get metadata and store it to media object
        ImageMedia media = (ImageMedia) MediaFactory.createMedia( input );

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************
        
        // load the input image

        BufferedImage image = ImageIO.read(input);
        String extension;


       
        // set width and height of the image

        media.setWidth(image.getWidth());
        media.setHeight(image.getHeight());


        // add a tag "image" to the media


        media.addTag("image");

        // add a tag corresponding to the filename extension of the file to the media


        if(input.getName().toLowerCase().endsWith(".jpg")){
            extension="JPG";
        }
        else if(input.getName().toLowerCase().endsWith(".png")){
            extension="PNG";
        }
        else if(input.getName().toLowerCase().endsWith(".bmp")){
            extension="BMP";
        }
        else if(input.getName().toLowerCase().endsWith(".jpeg")){
            extension="JPEG";
        }
        else if(input.getName().toLowerCase().endsWith(".gif")){
            extension="GIF";
        }else
        {
            extension="unknown";
        }

        media.addTag(extension);

        // if there is a colormodel:
        

        // set color space type
        media.setColorSpaceType(image.getColorModel().getColorSpace().getType());

        // set pixel size
        media.setPixelSize(image.getColorModel().getPixelSize());

        // set transparency
        media.setTransparancy(image.getTransparency());

        // set number of (color) components

        media.setComponentsNo(image.getColorModel().getNumComponents());
        media.setColorComponentsNo(image.getColorModel().getNumColorComponents());

        // set orientation  0 ist landscape 1 ist portrait
        if(image.getHeight() < image.getWidth()){
            media.setOrientation(0);
            
        }
        
        else if(image.getHeight() > image.getWidth()){
            media.setOrientation(1);
        }



        //DOMINANT COLOR
        //store

        String dominantColor = getDominantColors(image);

        //check what tagg

        if(dominantColor.equals("red"))
            media.addTag(dominantColor);

        else if(dominantColor.equals("green"))
            media.addTag(dominantColor);

        else if(dominantColor.equals("blue"))
            media.addTag(dominantColor);

        else
             media.addTag(dominantColor);



        media.writeToFile(outputFile);
        return media;

    }

    public String getDominantColors(BufferedImage img){

            int redBucket = 0;
            int blueBucket = 0;
            int greenBucket = 0;

            ColorModel colormodel = ColorModel.getRGBdefault();
            //made an array of pixels
            int[] pixel = img.getRGB(0,0,img.getWidth(), img.getHeight(), null, 0, img.getWidth());
            //going through each pixel
            for(int i = 0; i < pixel.length; i++) {

                int onePixelRed = colormodel.getRed(pixel[i]);
                int onePixelBlue = colormodel.getBlue(pixel[i]);
                int onePixelGreen = colormodel.getGreen(pixel[i]);

                //compare the current pixels, highest value? increment this value
                if(onePixelRed > onePixelBlue && onePixelRed > onePixelGreen) {
                    redBucket++;
                } else if(onePixelBlue > onePixelRed && onePixelBlue > onePixelGreen) {
                    blueBucket++;
                } else if(onePixelGreen > onePixelRed && onePixelGreen > onePixelBlue) {
                    greenBucket++;
                }
            }

            if(redBucket > blueBucket && redBucket > greenBucket) {
                return "red";
            } else if(blueBucket > redBucket && blueBucket > greenBucket) {
                return "blue";
            } else if(greenBucket > redBucket && greenBucket > blueBucket) {
                return "green";
            } else {
                return "grey";
            }
        }







//        int sumRed =0;
//        int sumGreen =0;
//        int sumBlue =0;
//
//        for(int i=0;i < img.getHeight();i++){
//
//            for(int j=0;j < img.getWidth(); j++){
//
//                Color c = new Color(img.getRGB(i,j));
//
//                int redBucket=c.getRed();
//                int greenBucket=c.getGreen();
//                int blueBucket=c.getBlue();
//
//                if(redBucket > greenBucket && redBucket > blueBucket){
//                   sumRed++;
//                }
//                if(greenBucket > redBucket && greenBucket > blueBucket){
//                    sumGreen++;
//                }
//                if(blueBucket > greenBucket && redBucket < blueBucket){
//                    sumBlue++;
//                }
//
//            }
//        }
//
//
//        if (sumRed > sumGreen && sumRed > sumBlue) {
//            return "red";
//
//        } else if (sumGreen > sumRed && sumGreen > sumBlue) {
//
//            return "green";
//
//        } else if(sumBlue> sumRed && sumBlue > sumGreen){
//            return "blue";
//        }
//        else return "grey";
//
//    }



    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 2 ) {
            System.out.println( "usage: java itm.image.ImageMetadataGenerator <input-image> <output-directory>" );
            System.out.println( "usage: java itm.image.ImageMetadataGenerator <input-directory> <output-directory>" );
            System.exit( 1 );
        }
        File fi = new File( args[0] );
        File fo = new File( args[1] );
        ImageMetadataGenerator img = new ImageMetadataGenerator();
        img.batchProcessImages( fi, fo, true );        
    }    
}
