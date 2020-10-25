<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="itm.image.*" %>
<%@ page import="itm.model.*" %>
<%@ page import="itm.util.*" %>
<!--
/*******************************************************************************
 This file is part of the WM.II.ITM course 2016
 (c) University of Vienna 2009-2016
 *******************************************************************************/
-->
<%

%>
<html>
    <head>
    </head>
    <body>


        <%

            String tag = null;

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************

            // get "tag" parameter

            tag = request.getParameter("tag");

            // if no param was passed, forward to index.jsp (using jsp:forward)

            if(tag == null){
                %>
                <jsp:forward page="index.jsp" />
                <%
            }
                    else if (tag.equals("")) {
                %>
                <jsp.forward page="index.jsp" />
                 <%
            }






        %>

        <h1>Media that is tagged with <%= tag %></h1>
        <a href="index.jsp">back</a>

        <%

            // ***************************************************************
            //  Fill in your code here!
            // ***************************************************************

            // get all media objects that are tagged with the passed tag

            ArrayList<AbstractMedia> media = MediaFactory.getMedia();
            int c=0; // counter for rowbreak after 3 thumbnails.

            // iterate over all available media objects and display them

            String basePath = getServletConfig().getServletContext().getRealPath( "media" );
            if ( basePath == null )
                throw new NullPointerException( "could not determine base path of media directory! please set manually in JSP file!" );
            File base = new File( basePath );
            File imageDir = new File( basePath, "img");
            File audioDir = new File( basePath, "audio");
            File videoDir = new File( basePath, "video");
            File metadataDir = new File( basePath, "md");
            MediaFactory.init( imageDir, audioDir, videoDir, metadataDir );

            // iterate over all available media objects
            for ( AbstractMedia medium : media ) {
                c++;
        %>
        <div style="width:400px;height:400px;padding:20px;float:left;">
            <%

                boolean matchedTag = false;

                for ( String t : medium.getTags() )
                    if (t.toLowerCase().equals(tag.toLowerCase()))
                        matchedTag = true;



                // handle images
                if ( medium instanceof ImageMedia && matchedTag ) {

                    // ***************************************************************
                    //  Fill in your code here!
                    // ***************************************************************

                    ImageMedia img = (ImageMedia) medium;
            %>
            <div style="width:200px;height:200px;padding:10px;">
                <a href="media/img/<%= img.getInstance().getName()%>">
                    <img src="media/md/<%= img.getInstance().getName() %>.thumb.png" border="0"/>
                </a>
            </div>
            <div>
                Name: <%= img.getName() %><br/>

                Dimensions: <%= img.getWidth() %>x<%= img.getHeight() %>px<br/>

                Size: <%= img.getSize() %> bytes<br/>

                Pixelsize: <%= img.getPixelSize()%> <br/>

                Number of Image Components: <%= img.getComponentsNo()%> <br/>

                Number of Image Colorcomponents: <%= img.getColorComponentsNo()%> <br/>

                Transparency: <%= img.getTransparancy()%> <br/>

                Orientation: <%= img.getOrientation()%> <br/>

                Tags: <% for ( String t : img.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>


            </div>
            <%
            } else
            if ( medium instanceof AudioMedia && matchedTag) {
                // display audio thumbnail and metadata
                AudioMedia audio = (AudioMedia) medium;
            %>
            <div style="width:200px;height:200px;padding:10px;">
                <br/><br/><br/><br/>
                <embed src="media/md/<%= audio.getInstance().getName() %>.wav" autostart="false" width="150" height="30" />
                <br/>
                <a href="media/audio/<%= audio.getInstance().getName()%>">
                    Download <%= audio.getInstance().getName()%>
                </a>
            </div>
            <div>
                Name: <%= audio.getName() %><br/>
                Title: <%= audio.getTitle() %> <br/>
                Duration: <%= audio.getDuration() %><br/>
                Size: <%= audio.getSize() %> bytes <br/>
                Album: <%= audio.getAlbum() %> <br/>
                Author: <%= audio.getAuthor() %> <br/>
                Date: <%= audio.getDate() %> <br/>
                Composer: <%= audio.getComposer() %> <br/>
                Genre: <%= audio.getGenre() %> <br/>
                comment: <%= audio.getComment() %> <br/>
                Track: <%= audio.getTrack() %> <br/>
                Bitrate: <%= audio.getBitrate() %> <br/>
                Channels: <%= audio.getChannels() %> <br/>
                Encoding: <%= audio.getEncoding() %> <br/>
                Frequency: <%= audio.getFrequency() %> <br/>

                Tags: <% for ( String t : audio.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>
            </div>
            <%
            } else
            if ( medium instanceof VideoMedia && matchedTag ) {
                // handle videos thumbnail and metadata...
                VideoMedia video = (VideoMedia) medium;
            %>
            <div style="width:200px;height:200px;padding:10px;">
                <a href="media/video/<%= video.getInstance().getName()%>">

                    <%--<object width="200" height="200">--%>
                        <%--<param name="movie" value="media/md/<%= video.getInstance().getName() %>_thumb.avi">--%>
                        <%--<embed src="media/md/<%= video.getInstance().getName() %>_thumb.avi" width="200" height="200">--%>
                        <%--</embed>--%>
                    <%--</object>--%>

                </a>
            </div>
            <div>
                Name: <a href="media/video/<%= video.getInstance().getName()%>"><%= video.getName() %></a><br/>
                Tags: <% for ( String t : video.getTags() ) { %><a href="tags.jsp?tag=<%= t %>"><%= t %></a> <% } %><br/>
                Size: <%= video.getSize() %> bytes <br/>
                Dimensions: <%= video.getVideoWidth() %>x<%= video.getVideoHeight() %>px<br/>
                Video Length: <%= video.getVideoLength() %> sec <br/>
                Video Framerate: <%= (int) (video.getVideoFrameRate() + 0.5) %> <br/>
                Audio Bitrate: <%= video.getAudioBitRate() %> <br/>
                Audio Channels: <%= video.getAudioChannels() %> <br/>
                Audio Codec: <%= video.getAudioCodec() %> <br/>
                Audio CodecID: <%= video.getAudioCodecID() %> <br/>
                Audio Samplerate: <%= video.getAudioSampleRate() %> <br/>
                Video Codec: <%= video.getVideoCodec() %> <br/>
                Video CodecID: <%= video.getVideoCodecID() %> <br/>
            </div>
            <%
                } else {
                }

                if(matchedTag) {
                    %>
                     </div>
                    <%
                }

            if ( c % 3 == 0 ) {
             %>
                <div style="clear:left"/>
            <%
                }

            } // for


        %>

    </body>
</html>
