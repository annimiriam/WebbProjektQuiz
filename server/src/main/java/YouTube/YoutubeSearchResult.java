package YouTube;

import java.util.List;

/**
 * This class represents a search result from YouTube
 * @author Fredrik Jeppsson
 */
public class YoutubeSearchResult {
    public String kind = "";
    public String etag = "";
    public String nextPageToken = "";
    public String regionCode = "";

    public class PageInfo {
        public int totalResults;
        public int resultsPerPage;

        @Override
        public String toString() {
            return "PageInfo{" +
                    "totalResults=" + totalResults +
                    ", resultsPerPage=" + resultsPerPage +
                    '}';
        }
    }

    public PageInfo pageInfo;
    public List<VideoItem> items;

    @Override
    public String toString() {
        return "YoutubeSearchResult{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", nextPageToken='" + nextPageToken + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", pageInfo=" + pageInfo +
                ", items=" + items +
                '}';
    }
}
