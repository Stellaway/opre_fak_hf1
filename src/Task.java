public class Task {
    char name;
    int start;
    int prio;
    int burst;

    // Constructor


    public Task(char n, int s, int p, int b){
        name = n;
        start = s;
        prio = p;
        burst = b;
    }

    void run(){
        burst--;
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


}
