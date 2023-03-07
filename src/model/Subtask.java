package model;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(Integer id, TypeOfTask type, String name, StatusOfTask status, String description, Integer epicId) {
        super(id, type, name, status, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}