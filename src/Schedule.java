import java.util.ArrayList;
import java.util.Comparator;

public class Schedule {

    ArrayList<Task> tasks;

    public Schedule(){
        tasks = new ArrayList<Task>();

    }

    public void addTask(Task t) {
        tasks.add(t);
        tasks.sort(Comparator.comparing(Task::getStart).thenComparing(Task::getPrio));
    }

    void run(){
        //TODO
    }


}
