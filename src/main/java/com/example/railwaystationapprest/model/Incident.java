package com.example.railwaystationapprest.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 1000, nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;
    private Timestamp time;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "journey_id")
    private Journey journey;

    public Incident() {
    }

    public Incident(String description, IncidentStatus status, User user, Journey journey) {
        this.description = description;
        this.status = status;
        this.time = new Timestamp(System.currentTimeMillis());
        this.user = user;
        this.journey = journey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }
}
