import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subtaskId;

    public Epic(String name, String description, Integer id, String status, ArrayList<Integer> subtaskId) {
        super(name, description, id, status);
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtaskId=" + subtaskId +
                '}';
    }
}