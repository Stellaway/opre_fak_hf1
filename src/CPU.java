import java.util.ArrayList;
import java.util.Comparator;

public class CPU {

    int clock;

    ArrayList<Task> notRunning;
    ArrayList<Task> queuedRR;
    ArrayList<Task> queuedSRTF;
    ArrayList<Task> finished;
    ArrayList<Character> runOrder;
    ArrayList<Task> allTasks;

    public CPU(){
        notRunning = new ArrayList<>();
        queuedRR = new ArrayList<>();
        queuedSRTF = new ArrayList<>();
        finished = new ArrayList<>();
        runOrder = new ArrayList<>();
        clock = 0;
    }

    public void runTasks(){
        allTasks = new ArrayList<>(notRunning); // copy every task

        while(!notRunning.isEmpty() || !queuedRR.isEmpty() || !queuedSRTF.isEmpty()){
            tick();

        }
        getOutConsecutiveDuplicates(runOrder);
    }

    public void addTask(char name, int prio, int start, int burst){
        Task t = new Task(name, prio, start, burst);

        notRunning.add(t);
        notRunning.sort(Comparator.comparing(Task::getStart));

    }

    public void tick(){

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


        clock++;
    }

    public void getOutConsecutiveDuplicates(ArrayList<Character> list){
        for(int i = 0; i < list.size()-1; i++){
            if(list.get(i) == list.get(i+1)){
                list.remove(i);
                i--;
            }
        }
    }


    public void printRunOrder(){
        for(char c : runOrder){
            System.out.print(c);
        }
    }

    public void printWaitTimes(){
        for(Task t : allTasks){
            System.out.print(t.getName() + ":" + t.getWaited());
            if(t != allTasks.get(allTasks.size()-1)) System.out.print(",");
        }

    }

    public void print(){
        printRunOrder();
        System.out.println();
        printWaitTimes();
    }

}
