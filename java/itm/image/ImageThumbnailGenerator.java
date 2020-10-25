package itm.image;

/*******************************************************************************
    This file is part of the ITM course 2017
    (c) University of Vienna 2009-2017
*******************************************************************************/

import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


/**
    This class converts images of various formats to PNG thumbnails files.
    It can be called with 3 parameters, an input filename/directory, an output directory and a compression quality parameter.
    It will read the input image(s), grayscale and scale it/them and convert it/them to a PNG file(s) that is/are written to the output directory.

    If the input file or the output directory do not exist, an exception is thrown.
*/
public class ImageThumbnailGenerator 
{

    /**
        Constructor.
    */
    public ImageThumbnailGenerator()
    {
    }

    /**
        Processes an image directory in a batch process.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param overwrite indicates whether existing thumbnails should be overwritten or not
        @return a list of the created files
    */
    public ArrayList<File> batchProcessImages( File input, File output, boolean overwrite ) throws IOException
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

        ArrayList<File> ret = new ArrayList<File>();

        if ( input.isDirectory() ) {
            File[] files = input.listFiles();
            for ( File f : files ) {
                try {
                    File result = processImage( f, output, overwrite );
                    System.out.println( "converted " + f + " to " + result );
                    ret.add( result );
                } catch ( Exception e0 ) {
                    System.err.println( "Error converting " + input + " : " + e0.toString() );
                }
            }
        } else {
            try {
                File result = processImage( input, output, overwrite );
                System.out.println( "converted " + input + " to " + result );
                ret.add( result );
            } catch ( Exception e0 ) {
                System.err.println( "Error converting " + input + " : " + e0.toString() );
            }
        } 
        return ret;
    }  

    /**
        Processes the passed input image and stores it to the output directory.
        This function should not do anything if the outputfile already exists and if the overwrite flag is set to false.
        @param input a reference to the input image file
        @param output a reference to the output directory
        @param dimx the width of the resulting thumbnail
        @param dimy the height of the resulting thumbnail
        @param overwrite indicates whether existing thumbnails should be overwritten or not
    */
    protected File processImage( File input, File output, boolean overwrite ) throws IOException, IllegalArgumentException
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

        // create outputfilename and check whether thumb already exists
        File outputFile = new File( output, input.getName() + ".thumb.png" );
        if ( outputFile.exists() ) {
            if ( ! overwrite ) {
                return outputFile;
            }
        }

        // ***************************************************************
        //  Fill in your code here!
        // ***************************************************************

        BufferedImage image = null;


        // load the input image
        try{
            image = ImageIO.read(input);
        }catch(IOException e){
            System.out.println(e);
        }

        // rotate if needed

        if(image.getHeight() > image.getWidth()){
            BufferedImage rotatedImage = new BufferedImage(image.getHeight(),image.getWidth(),BufferedImage.TYPE_INT_RGB);


            AffineTransform transform = new AffineTransform();

            transform.setToTranslation(image.getHeight(),0);
            
            transform.rotate(Math.PI/2);  // pi/2 is a 90 degrees rotation


            Graphics2D graph2 = rotatedImage.createGraphics();
            graph2.drawImage(image,transform,null); //mit tranformiertem zeichnet er neues bild (anosnsten geht es nciht)

            image = rotatedImage;

        }

        // scale the image to a maximum of [ 200 w X 100 h ] pixels - do not distort!
        // if the image is smaller than [ 200 w X 100 h ] - print it on a [ dim X dim ] canvas!

        BufferedImage resultImage =null;  //canvas

        if(image.getWidth() > 200 || image.getHeight() > 100){  //should the height or width be bigger then 200 & 100 we need to scale down the pic

            double scaleFact =(double)200/image.getWidth();       //is the factor that the image needs to be scaled with -> 200px width
            
            //scalefactor

            AffineTransform scale = new AffineTransform();

            scale.scale(scaleFact,scaleFact);    // scale image

            resultImage = new BufferedImage((int)(image.getWidth()*scaleFact),(int)(image.getHeight()*scaleFact),BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = resultImage.createGraphics();

            g2d.drawImage(image,scale,null);  //draws the picture with the rotation we just appield



        } else  // shoudl it be less the 200px/100px
        {
            resultImage = new BufferedImage(200,100,BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = resultImage.createGraphics();

            g2d.drawImage(image,100-(image.getWidth()/2),50-(image.getHeight()/2),null);  //dafür da, damit es zentriet eingefügt wird

        }



        
        // add a watermark of your choice and paste it to the image
        // e.g. text or a graphic

        String waterMark = "Watermark";
        Graphics2D g =resultImage.createGraphics();

        g.setPaint(Color.BLUE);
        g.setFont(new Font("Arial",Font.PLAIN,18));  // setting font and size
        g.drawString(waterMark,18,18);                //for the position



        // encode and save the image  

        try{
            File outputfile = new File(output+"/"+input.getName()+".thumb.png");
            ImageIO.write(resultImage,"png",outputfile);           // filesource , format, filename

        }catch (IOException e){
            System.out.println("Couldn't save the image"+e);
        }



        return outputFile;


        /**
            ./ant.sh ImageThumbnailGenerator -Dinput=media/img/ -Doutput=test/ -Drotation=90
        */
    }

    /**
        Main method. Parses the commandline parameters and prints usage information if required.
    */
    public static void main( String[] args ) throws Exception
    {
        if ( args.length < 2 ) {
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-image> <output-directory>" );
            System.out.println( "usage: java itm.image.ImageThumbnailGenerator <input-directory> <output-directory>" );
            System.exit( 1 );
        }
        File fi = new File( args[0] );
        File fo = new File( args[1] );

        ImageThumbnailGenerator itg = new ImageThumbnailGenerator();
        itg.batchProcessImages( fi, fo, true );
    }    
}
