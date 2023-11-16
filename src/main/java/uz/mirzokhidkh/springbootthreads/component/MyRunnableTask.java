package uz.mirzokhidkh.springbootthreads.component;

import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.repository.ClientDAO;

//@Data
//@Component
//@Scope("prototype")
public class MyRunnableTask implements Runnable {

    //    private final Integer id;
//    private final Integer balance;
    private final ClientBalanceDTO clientBalanceDTO;
    private final ClientDAO clientDao;

    public MyRunnableTask(ClientBalanceDTO clientBalanceDTO, ClientDAO clientDao) {
        this.clientBalanceDTO = clientBalanceDTO;
        this.clientDao = clientDao;
    }

//    public MyRunnableTask(ClientService clientService,Integer id, Integer balance) {
//        this.clientService = clientService;
//        this.id = id;
//        this.balance = balance;
//    }


    @Override
    public void run() {
//        System.out.println("MyRunnableTask is running..." + Thread.currentThread().getName());

//        ClientBalanceDTO clientBalanceDTO = new ClientBalanceDTO(id, balance);
        clientDao.updateClientBalance(clientBalanceDTO);
//        String threadName = Thread.currentThread().getName();


//        Util.putToMap(threadName);

        sleepThread();
    }

    private void sleepThread(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);
//        List<List<Integer>> subSets = Lists.partition(intList, 3);
//        System.out.println(subSets);
//    }

//    public static setClientBalanceDTO(ClientBalanceDTO clientBalanceDTO) {
//        this.clientBalanceDTO = clientBalanceDTO;
//    }


}
