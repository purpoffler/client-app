import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class DataType implements Runnable {
    ArrayList<String> inputData = new ArrayList<>();
    private final BlockingQueue<ArrayList<String>> dataQueue;
    static Scanner sc = new Scanner(System.in);

    public DataType(BlockingQueue dataQueue){
        this.dataQueue = dataQueue;
    }

    @Override
    public void run() {
        chooseDataType();
        System.out.println("Введите данные (не более 200 символов):");
        inputData();
        try {
            dataQueue.put(inputData);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private void chooseDataType(){
        while (true) {
            if (sc.hasNext()) {
                String str = sc.nextLine();
                if (str.equalsIgnoreCase("CONSOLE") || str.equalsIgnoreCase("PLAIN") || str.equalsIgnoreCase("JSON")) {
                    System.out.println(String.format("Окей, тогда собираем %s", str));
                    inputData.add(str);
                    return;
                }
            }
            System.out.println("Упс, неправильный формат данных");
        }
    }

    private void inputData(){
        if (sc.hasNext()) {
            String data = sc.nextLine();
            while (true) {
                if (data.length() < 200) {
                    System.out.println("кайф!");
                    inputData.add(data);
                    break;
                }
                System.out.println("Упс, количество символов больше 200");
            }
        }
    }
}
