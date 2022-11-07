package uz.mirzokhidkh.springbootthreads.component;

import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.service.ClientService;

//@Data
//@Component
//@Scope("prototype")
public class MyRunnableTask implements Runnable {

    //    private final Integer id;
//    private final Integer balance;
    private final ClientBalanceDTO clientBalanceDTO;
    private final ClientService clientService;

    public MyRunnableTask(ClientBalanceDTO clientBalanceDTO, ClientService clientService) {
        this.clientBalanceDTO = clientBalanceDTO;
        this.clientService = clientService;
    }

//    public MyRunnableTask(ClientService clientService,Integer id, Integer balance) {
//        this.clientService = clientService;
//        this.id = id;
//        this.balance = balance;
//    }


    @Override
    public void run() {
        System.out.println("MyRunnableTask is running..." + Thread.currentThread().getName());

//        ClientBalanceDTO clientBalanceDTO = new ClientBalanceDTO(id, balance);
        clientService.updateClientBalance(clientBalanceDTO);


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
