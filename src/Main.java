public class Main {
    public static void main(String[] args) {
        CPU cpu = new CPU();

        cpu.addTask('A', 0, 0, 6);
        cpu.addTask('B', 0, 1, 5);
        cpu.addTask('C', 1, 5, 2);
        cpu.addTask('D', 1, 10, 1);

        cpu.runTasks();


        cpu.print();



    }
}