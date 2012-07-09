package com.cyrusinnovation.flatwiki;

public class WikiPage {
    private final String name;
    private final String source;

    public WikiPage(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getOutputFilename() {
        return name + ".html";
    }

    public String asHtml() {
        return String.format(
                "<h1>%s</h1>" +
                        "<div>%s</div>",
                name, source);
    }
}
