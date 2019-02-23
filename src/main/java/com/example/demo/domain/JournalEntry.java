package com.example.demo.domain;

import com.example.demo.jsonserializer.JsonLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "entry")
public class JournalEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private LocalDate created;
    private String summary;

    @Transient
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public JournalEntry(String title, String summary, String created) {
        this.title = title;
        this.created = LocalDate.parse(created, format);
        this.summary = summary;
    }

    public JournalEntry() {
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", summary='" + summary + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonSerialize(using = JsonLocalDateSerializer.class)
    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonIgnore
    public String getCreatedAsShort() {
        return format.format(created);
    }
}
