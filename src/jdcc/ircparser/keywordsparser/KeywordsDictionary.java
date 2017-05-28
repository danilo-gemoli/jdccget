package jdcc.ircparser.keywordsparser;

public interface KeywordsDictionary {
    String[] getDonwloadInQueueKeywords();

    String[] getDownloadQueueFullKeywords();

    String[] getDonwloadAvailableKeywords();

    String[] getRemovedFromQueueKeywords();

    String[] getBandwidthLimitKeywords();

    String[] getDownloadResumeSupportedKeywords();
}
