package com.busyhill.remind.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "rm_task")
public class RemindTask {
    @Id
    private Long id;
    private String label;
    private LocalDateTime addedOn;
    private String location;
    private LocalDateTime taskTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(LocalDateTime taskTime) {
        this.taskTime = taskTime;
    }

    public RemindTask() {
    }

    public RemindTask(Long id, String label, LocalDateTime addedOn, String location, LocalDateTime taskTime) {
        this.id = id;
        this.label = label;
        this.addedOn = addedOn;
        this.location = location;
        this.taskTime = taskTime;
    }
}
