import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class CPU {

    int clock;

    ArrayList<Task> notRunning;
    ArrayList<Task> queuedRR;
    ArrayList<Task> queuedSRTF;
    ArrayList<Task> finished;
    ArrayList<Character> runOrder;

    public CPU(){
        notRunning = new ArrayList<>();
        queuedRR = new ArrayList<>();
        queuedSRTF = new ArrayList<>();
        finished = new ArrayList<>();
        runOrder = new ArrayList<>();
        clock = 0;
    }

    public void runTasks(){
        while(!notRunning.isEmpty() || !queuedRR.isEmpty() || !queuedSRTF.isEmpty()){
            tick();
            System.out.println("notRunning is empty: " + notRunning.isEmpty());
            System.out.println("queuedRR is empty: " + queuedRR.isEmpty());
            System.out.println("queuedSRTF is empty: " + queuedSRTF.isEmpty());

        }
    }

    public void addTask(char name, int start, int prio, int burst){
        Task t = new Task(name, start, prio, burst);

        notRunning.add(t);
        notRunning.sort(Comparator.comparing(Task::getStart));

    }

    public void tick(){
        System.out.println("Clock: " + clock);

        ArrayList<Task> tempQueuedRR = new ArrayList<>();           //for alphabetical order among now-starting RRs
        ArrayList<Task> toRemove = new ArrayList<>();            //for removing now-starting tasks from notRunning
        for(Task t : notRunning){                                   //now-starting tasks
            if(t.getStart() == clock){

                toRemove.add(t);

                if(t.getPrio() == 0) {                              //SRTF, put at its place

                    queuedSRTF.add(t);

                }
                else {                                              //RR, put at the end of RRs
                    tempQueuedRR.add(t);
                }

            }
        }
        tempQueuedRR.sort(Comparator.comparing(Task::getName));     //adding RRs in alphabetical order, not ruining round-robin order
        queuedRR.addAll(tempQueuedRR);
        queuedSRTF.sort(Comparator.comparing(Task::getBurst).thenComparing(Task::getStart).thenComparing(Task::getName));

        for(Task t : toRemove){                                      //remove now-starting tasks from notRunning
            notRunning.remove(t);
        }

        Task nowServed = null;
        boolean isRR = false;
        if(!queuedRR.isEmpty()){
            isRR = true;
            nowServed = queuedRR.get(0);            //get the first RR
        }else if(!queuedSRTF.isEmpty()){
            nowServed = queuedSRTF.get(0);          //get the first SRTF
        }
        if(nowServed == null){                      //nothing to run, return from this tick
            clock++;
            return;
        }


        runOrder.add(nowServed.getName());          //add to run order
        if(nowServed.run(clock)) {                  //run task
            if(isRR) queuedRR.remove(nowServed);    //remove from RR if isRR
                else queuedSRTF.remove(nowServed);  //
            finished.add(nowServed);                //
            clock++;                                //if finished
            return;                                 //
        }


        if(isRR){                           //round-robin algorithm
            if (!nowServed.nowRun()){       //not now run => run last time => drained its timeslice => move to the end
                queuedRR.remove(nowServed);
                queuedRR.add(nowServed);
            }
        }

        if(!isRR){                          //shortest remaining time first algorithm
            //nothing to do
        }


        clock++;
    }

    public ArrayList<Task> getFinished(){
        return finished;
    }

    public void printRunOrder(){
        for(char c : runOrder){
            System.out.print(c);
        }
    }

    public void printWaitTimes(){
        ArrayList<Task> temp = new ArrayList<>(finished);
        temp.sort(Comparator.comparing(Task::getName));
        for(Task t : temp){
            System.out.print(t.getName() + ":" + t.getWaited()+ ",");
        }

    }

    public void print(){
        printRunOrder();
        System.out.println();
        printWaitTimes();
    }

}
