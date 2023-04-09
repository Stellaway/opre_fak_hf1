public class Task {
    char name;
    int start;
    int prio;
    int burst;
    int initialBurst;
    int waited;
    int nowRun;

    // Constructor


    public Task(char n, int p, int s, int b){
        name = n;
        start = s;
        prio = p;
        burst = b;
        initialBurst = b;
        nowRun = 0;
        waited = -1;        //has not run
    }

    //runs for 1 tick, returns if finished
    boolean run(int clock){
        burst--;
        nowRun++;
        if (burst == 0) {
            waited = clock-start-initialBurst+1;
            return true;
        }
        return false;
    }


    // Getters
    public char getName(){
        return name;
    }

    public int getStart(){
        return start;
    }

    public int getPrio(){
        return prio;
    }

    public int getBurst(){
        return burst;
    }

    public int getWaited(){
        return waited;
    }

    public int nowRun(){
        return nowRun;
    }
    public void nullRun() { nowRun = 0; }

    public boolean equals(Task t){
        return (name == t.getName()&&start == t.getStart()&&prio == t.getPrio()&&burst == t.getBurst());
    }
}

