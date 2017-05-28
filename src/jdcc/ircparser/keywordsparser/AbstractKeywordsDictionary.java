package jdcc.ircparser.keywordsparser;

public abstract class AbstractKeywordsDictionary implements KeywordsDictionary {
    protected String[] inQueueKeywords;
    protected String[] queueFullKeywords;
    protected String[] removedQueueKeywords;
    protected String[] downloadAvailableKeywords;
    protected String[] bandwidthLimitKeywords;
    protected String[] downloadResumeSupportedKeywords;

    @Override
    public String[] getDonwloadAvailableKeywords() {
        return downloadAvailableKeywords;
    }

    @Override
    public String[] getDonwloadInQueueKeywords() {
        return inQueueKeywords;
    }

    @Override
    public String[] getDownloadQueueFullKeywords() {
        return queueFullKeywords;
    }

    @Override
    public String[] getRemovedFromQueueKeywords() {
        return removedQueueKeywords;
    }

    @Override
    public String[] getBandwidthLimitKeywords() { return bandwidthLimitKeywords; }

    @Override
    public String[] getDownloadResumeSupportedKeywords() {
        return downloadResumeSupportedKeywords;
    }
}
