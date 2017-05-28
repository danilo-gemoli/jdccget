package jdcc.ircparser.keywordsparser;

public class EngInMemoryKeywordsDictionary extends AbstractKeywordsDictionary {
    public EngInMemoryKeywordsDictionary() {
        inQueueKeywords = new String[] {
            "added", "position", "remove"
        };
        queueFullKeywords = new String[] {
            "queue", "full"
        };
        downloadAvailableKeywords = new String[] {
            "sending"
        };
        removedQueueKeywords = new String[] {
            "removed", "queue"
        };
        bandwidthLimitKeywords = new String[] {
            "bandwidth", "limit"
        };
        downloadResumeSupportedKeywords = new String[] {
            "resume", "supported"
        };
    }
}
