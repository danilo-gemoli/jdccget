package jdcc.ircparser.keywordsparser;

public interface KeywordsDictionary {
    String[] getDonwloadInQueueKeywords();

    String[] getDownloadQueueFullKeywords();

    String[] getDonwloadAvailableKeywords();

    String[] getRemovedFromQueueKeywords();

    // TODO: aggiungere keywords per "ripresa supportata" del download.
}
