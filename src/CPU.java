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
        ArrayList<Task> tempQueuedSRTF = new ArrayList<>();         //for alphabetical order among now-starting SRTFs
        ArrayList<Task> toRemove = new ArrayList<>();               //for removing now-starting tasks from notRunning
        for(Task t : notRunning){                                   //now-starting tasks
            if(t.getStart() == clock){

                toRemove.add(t);

                if(t.getPrio() == 0) {                              //SRTF, put at its place

                    tempQueuedSRTF.add(t);

                }
                else {                                              //RR, put at the end of RRs
                    tempQueuedRR.add(t);
                }

            }
        }
        tempQueuedRR.sort(Comparator.comparing(Task::getName));     //adding RRs in alphabetical order, not ruining round-robin order
        tempQueuedSRTF.sort(Comparator.comparing(Task::getName));   //adding SRTFs in alphabetical order, not ruining round-robin order



        queuedRR.addAll(tempQueuedRR);
        queuedSRTF.addAll(tempQueuedSRTF);
        tempQueuedSRTF.addAll(queuedSRTF);                          //adding SRTFs to the beginning of the queue
        //queuedSRTF= new ArrayList<>(tempQueuedSRTF);

        queuedSRTF.sort(Comparator.comparing(Task::getBurst));

        notRunning.removeAll(toRemove);             //remove now-starting tasks from notRunning

        Task nowServed = null;
        boolean isRR = false;
        if(!queuedRR.isEmpty()){
            isRR = true;
            nowServed = queuedRR.get(0);            //get the first RR
            while (nowServed.nowRun()==2){          //if ran for 2 ticks
                queuedRR.remove(nowServed);
                queuedRR.add(queuedRR.size(), nowServed);
                nowServed.nullRun();
                nowServed = queuedRR.get(0);
            }
        }else if(!queuedSRTF.isEmpty()){
            nowServed = queuedSRTF.get(0);          //get the first SRTF
            /*for(Task t : queuedSRTF){               //search for the shortest burst task
                if(t.getBurst() <= nowServed.getBurst()){
                    nowServed = t;
                }
            }*/
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
