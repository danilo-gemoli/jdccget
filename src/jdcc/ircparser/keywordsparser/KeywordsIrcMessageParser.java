package jdcc.ircparser.keywordsparser;

import jdcc.exceptions.NoSupportedLanguageException;
import jdcc.ircparser.IrcMessageParser;
import jdcc.ircparser.XdccMessageType;
import jdcc.utils.StringUtility;

import java.util.Hashtable;
import java.util.Map;

public class KeywordsIrcMessageParser implements IrcMessageParser {
    private String currentLanguage;
    private KeywordsDictionary currentDictionary;
    private Map<String, KeywordsDictionary> keywordsDictionaries;
    private boolean caseSensitive;

    public KeywordsIrcMessageParser() {
        keywordsDictionaries = new Hashtable<>();
        caseSensitive = false;
        initKeywordsDictionaries();
    }

    @Override
    public XdccMessageType parseXdccMessage(String message) {
        if (currentLanguage.equals(Languages.ALL_LANGUAGES)) {
            for (String lang : keywordsDictionaries.keySet()) {
                KeywordsDictionary dict = keywordsDictionaries.get(lang);
                XdccMessageType msgType = doParseXdccMessage(dict, message);
                if (msgType != XdccMessageType.UNKNOWN)
                    return msgType;
            }
            return XdccMessageType.UNKNOWN;
        }
        return doParseXdccMessage(currentDictionary, message);
    }

    @Override
    public int getPackNumberFromMessage(String message) {
        return 0;
    }

    @Override
    public void setLanguage(String lang) throws NoSupportedLanguageException {
        if (lang == null) {
            NoSupportedLanguageException e = new NoSupportedLanguageException();
            e.language = null;
            throw e;
        }
        if (lang.equals(Languages.ALL_LANGUAGES)) {
            currentDictionary = null;
        } else {
            KeywordsDictionary dict = keywordsDictionaries.get(lang);
            if (dict == null) {
                NoSupportedLanguageException e = new NoSupportedLanguageException();
                e.language = lang;
                throw e;
            }
            currentDictionary = dict;
        }
        currentLanguage = lang;
    }

    @Override
    public void setCaseSensitive(boolean value) {
        this.caseSensitive = value;
    }

    // METODI PRIVATI
    private void initKeywordsDictionaries() {
        keywordsDictionaries.put("ita", new ItaInMemoryKeywordsDictionary());
        keywordsDictionaries.put("eng", new EngInMemoryKeywordsDictionary());
        currentDictionary = keywordsDictionaries.get("eng");
    }

    private XdccMessageType doParseXdccMessage(KeywordsDictionary dict, String msg) {
        if (StringUtility.stringContainsWords(msg, dict.getDonwloadAvailableKeywords(), caseSensitive)) {
            return XdccMessageType.DOWNLOADING;
        } else if (StringUtility.stringContainsWords(msg, dict.getDonwloadInQueueKeywords(),
                caseSensitive)) {
            return XdccMessageType.IN_QUEUE;
        } else if (StringUtility.stringContainsWords(msg
                , dict.getDownloadQueueFullKeywords(), caseSensitive)) {
            return XdccMessageType.QUEUE_IS_FULL;
        } else if (StringUtility.stringContainsWords(msg
                , dict.getRemovedFromQueueKeywords(), caseSensitive)) {
            return XdccMessageType.REMOVED_FROM_QUEUE;
        }
        return XdccMessageType.UNKNOWN;
    }
}
