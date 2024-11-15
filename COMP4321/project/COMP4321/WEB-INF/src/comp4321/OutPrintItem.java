package comp4321;

import java.io.Serializable;

enum OutPrintItemType {
    SCORE,
    TITLE,
    URL,
    KEYWORD,
    DATE,
    LINEBREAK,
    PREFIX,
    INDENT,
    TEXT,
    HLINE,
    PREVIOUS
}

public class OutPrintItem implements Serializable {
    private String text;
    private OutPrintItemType type;
    OutPrintItem(String text, OutPrintItemType type) {
        this.text = text;
        this.type = type;
    }

    public String getHTML() {
        switch (type) {
            case TITLE:
                return "<span style=\"font-weight: 700\">" + text + "</span>";
            case URL:
                return "<a href=\"" + text + "\">" + text + "</a>";
            case LINEBREAK:
                return "<br>";
            case PREFIX:
                return "";
            case SCORE:
                return "<span class=\"pageScore\">" + text + "</span>";
            case HLINE:
                return "";
            case PREVIOUS:
                return "<span class=\"previous_query\" onclick=\"search_previous('" + text + "\')\">" + text + "</span>";
            default:
                return "<span>" + text + "</span>";
        }
    }

    public String getText(String prefix, String indent) {
        switch (type) {
            case LINEBREAK:
                return "\n";
            case PREFIX:
                return prefix;
            case INDENT:
                return indent;
            default:
                return text;
        }
    }
}

