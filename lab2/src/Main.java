public class Main {
    public static void main(String[] args) throws InterruptedException{
        final ProducerConsumer pc = new ProducerConsumer();

        // create the producer thread
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    pc.produce();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        // create the consumer thread
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    pc.consume();
                }
                catch (InterruptedException e){e.printStackTrace();}
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }
}
