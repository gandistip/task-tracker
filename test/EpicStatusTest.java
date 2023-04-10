import exceptions.InputException;
import model.Epic;
import model.Subtask;
import model.TypeOfTask;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static model.StatusOfTask.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {
    TaskManager taskManager = Managers.getDefault();
    Epic e1 = new Epic(
            0, TypeOfTask.EPIC,"e1",NEW,"d",
            null,null,
            new ArrayList<>());
    int e1Id = taskManager.addEpic(e1);

    public EpicStatusTest() throws IOException, InterruptedException {
    }

    @Test
    public void statusEpicEmpty() {
        assertEquals(NEW, e1.getStatus(), "Статусы не совпадают.");
    }

    @Test
    public void statusEpicNewNew() throws InputException {
        Subtask s1 = new Subtask(
            0,TypeOfTask.SUBTASK,"s1",NEW,"d",
            LocalDateTime.of(2023,01,1,3,0), Duration.ofMinutes(1),
            e1Id);
        int s1Id = taskManager.addSubtask(s1);
        Subtask s2 = new Subtask(
            0,TypeOfTask.SUBTASK,"s2",NEW,"d",
            LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1),
            e1Id);
        int s2Id = taskManager.addSubtask(s2);

        assertEquals(NEW, e1.getStatus(), "Статусы не совпадают.");
    }

    @Test
    public void statusEpicDoneDone() throws InputException {
        Subtask s1 = new Subtask(
                0,TypeOfTask.SUBTASK,"s1",DONE,"d",
                LocalDateTime.of(2023,01,1,3,0), Duration.ofMinutes(1),
                e1Id);
        int s1Id = taskManager.addSubtask(s1);
        Subtask s2 = new Subtask(
                0,TypeOfTask.SUBTASK,"s2",DONE,"d",
                LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1),
                e1Id);
        int s2Id = taskManager.addSubtask(s2);

        assertEquals(DONE, e1.getStatus(), "Статусы не совпадают.");
    }

    @Test
    public void statusEpicNewDone() throws InputException {
        Subtask s1 = new Subtask(
                0,TypeOfTask.SUBTASK,"s1",NEW,"d",
                LocalDateTime.of(2023,01,1,3,0), Duration.ofMinutes(1),
                e1Id);
        int s1Id = taskManager.addSubtask(s1);
        Subtask s2 = new Subtask(
                0,TypeOfTask.SUBTASK,"s2",DONE,"d",
                LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1),
                e1Id);
        int s2Id = taskManager.addSubtask(s2);

        assertEquals(IN_PROGRESS, e1.getStatus(), "Статусы не совпадают.");
    }

    @Test
    public void statusEpicProgressProgress() throws InputException {
        Subtask s1 = new Subtask(
                0,TypeOfTask.SUBTASK,"s1",IN_PROGRESS,"d",
                LocalDateTime.of(2023,01,1,3,0), Duration.ofMinutes(1),
                e1Id);
        int s1Id = taskManager.addSubtask(s1);
        Subtask s2 = new Subtask(
                0,TypeOfTask.SUBTASK,"s2",IN_PROGRESS,"d",
                LocalDateTime.of(2023,01,1,4,0),Duration.ofMinutes(1),
                e1Id);
        int s2Id = taskManager.addSubtask(s2);

        assertEquals(IN_PROGRESS, e1.getStatus(), "Статусы не совпадают.");
    }



}
