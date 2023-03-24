package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId;

    public Epic(Integer id, TypeOfTask type, String name, StatusOfTask status, String description,
                LocalDateTime startTime, Duration duration, ArrayList<Integer> subtaskId) {
        super(id, type, name, status, description, startTime, duration);
        this.subtaskId = subtaskId;
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                ", subtaskId=" + subtaskId +
                '}';
    }
}