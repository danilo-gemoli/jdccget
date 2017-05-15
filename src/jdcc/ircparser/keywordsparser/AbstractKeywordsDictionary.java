package jdcc.ircparser.keywordsparser;

public abstract class AbstractKeywordsDictionary implements KeywordsDictionary {
    protected String[] inQueueKeywords;
    protected String[] queueFullKeywords;
    protected String[] removedQueueKeywords;
    protected String[] downloadAvailableKeywords;

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
}
