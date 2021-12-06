import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer {
    private List<Integer> vector1;
    private List<Integer> vector2;
    LinkedList<Integer> list = new LinkedList<>();
    int capacity = 3;

    public ProducerConsumer(){
        vector1 = new ArrayList<>();
        vector2 = new ArrayList<>();

        Random random = new Random();
        int i;
        for (i=0;i<3;i++){
            this.vector1.add(random.nextInt(100));
            this.vector2.add(random.nextInt(100));
        }
        System.out.println("Vector one: "+vector1);
        System.out.println("Vector two: "+vector2);
    }

    // Function called by the producer thread
    // The first thread (producer) will compute the products of pairs of elements - one from each vector - and will feed the second thread.
    public void produce() throws InterruptedException {
        int i=0;
        while (i<3){
            synchronized (this){
                // producer thread waits while list is full
                while (list.size()==capacity)
                    wait();

                // to insert the product in the list
                list.add(vector1.get(i)*vector2.get(i));
                i++;

                notify();
                Thread.sleep(1000);
            }
        }
    }

    //  The second thread (consumer) will sum up the products computed by the first one.
    public void consume() throws InterruptedException{
        int product = 0;
        int i =0 ;
        while (i<3){
            synchronized (this){
                while(list.size()==0)
                    wait();

                int val = list.removeFirst();
                product += val;
                i++;
                System.out.println("Current product: "+product);

                // wake up producer thread
                notify();

                Thread.sleep(1000);
            }
        }
    }

}
