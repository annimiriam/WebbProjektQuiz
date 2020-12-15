package YouTube;

/**
 * This class represents a YouTube video
 *
 * @author Anni Johansson
 */
public class YouTubeVideos {
    public String kind = "";
    public String etag = ""; // Etag-objekt?
    public Id id = new Id();
    public Snippet snippet = new Snippet();


    public class Id {
        public String kind = "";
        public String videoId = "";
        public String channelId = "";
        public String playlistId = "";
    }

    public class Snippet {
        public String publishedAt = ""; //datetime
        public String channelid = "";
        public String title = "";
        public String description = "";
        public Thumbnails thumbnails = new Thumbnails();
        public String channelTitle = "";
        public String liveBroadcastContent = "";

        public class Thumbnails {

            public ThumbnailObjects default_ = new ThumbnailObjects(); // Egentligen default, men det är reserverat ord
            public ThumbnailObjects medium = new ThumbnailObjects();
            public ThumbnailObjects high = new ThumbnailObjects();
            public ThumbnailObjects standard = new ThumbnailObjects();
            public ThumbnailObjects maxres = new ThumbnailObjects();

            public class ThumbnailObjects {
                public String url = "";
                public int width = 0;
                public int height = 0;

            }
        }
    }
}
