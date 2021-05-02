package ru.strelchm.htmlstat.db.model;

import javax.persistence.*;

@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private HtmlParsingSession htmlParsingSession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HtmlParsingSession getHtmlParsingSession() {
        return htmlParsingSession;
    }

    public void setHtmlParsingSession(HtmlParsingSession htmlParsingSession) {
        this.htmlParsingSession = htmlParsingSession;
    }
}
