package jdcc.ircparser.keywordsparser;

public class ItaInMemoryKeywordsDictionary extends AbstractKeywordsDictionary {
    public ItaInMemoryKeywordsDictionary() {
        inQueueKeywords = new String[] {
            "aggiunto", "posizione"
        };
        queueFullKeywords = new String[] {
            "piena"
        };
        downloadAvailableKeywords = new String[] {
            "sto inviando"
        };
        removedQueueKeywords = new String[] {
            "rimosso", "coda"
        };
        bandwidthLimitKeywords = new String[] {
            "limite", "banda", "pack"
        };
        downloadResumeSupportedKeywords = new String[] {
            "ripresa", "supportata"
        };
    }
}
