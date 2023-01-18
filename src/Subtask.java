public class Subtask extends Task {
    public Integer epicId;

    public Subtask(String name, String description, Integer id, String status, Integer epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }
}