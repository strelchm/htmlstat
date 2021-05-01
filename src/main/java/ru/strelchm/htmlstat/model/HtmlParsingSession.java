package ru.strelchm.htmlstat.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "parsing_session")
public class HtmlParsingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime parsedTime;
    @OneToMany(mappedBy = "htmlParsingSession", cascade = {CascadeType.ALL})
    private List<Word> words;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getParsedTime() {
        return parsedTime;
    }

    public void setParsedTime(LocalDateTime parsedTime) {
        this.parsedTime = parsedTime;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
