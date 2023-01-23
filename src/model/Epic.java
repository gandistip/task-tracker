package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, Integer id, String status, ArrayList<Integer> subtaskId) {
        super(name, description, id, status);
        this.subtaskId = subtaskId;
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtaskId=" + subtaskId +
                '}';
    }
}