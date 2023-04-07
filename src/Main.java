import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CPU cpu = new CPU();

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if(parts.length != 4)
                continue;
            cpu.addTask(parts[0].charAt(0), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }

        cpu.runTasks();


        cpu.print();



    }
}