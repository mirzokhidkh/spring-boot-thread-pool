package uz.mirzokhidkh.springbootthreads.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mirzokhidkh.springbootthreads.domain.Client;
import uz.mirzokhidkh.springbootthreads.payload.ApiResponse;
import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientStateDTO;
import uz.mirzokhidkh.springbootthreads.service.AsynchService;
import uz.mirzokhidkh.springbootthreads.repository.ClientDAO;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final ClientDAO clientDao;
    private final AsynchService asynchService;

    public ClientController(ClientDAO clientDao,
                            AsynchService asynchService) {
        this.clientDao = clientDao;
        this.asynchService = asynchService;
    }


    @GetMapping("/client/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Integer id) {
        ApiResponse apiResponse = clientDao.getClientById(id);
        return ResponseEntity.status(apiResponse.getCode() == 1 ? 200 : 400).body(apiResponse);
    }


    @GetMapping("/clients")
    public List<Client> getAllClients() throws SQLException {
        long s = System.currentTimeMillis();
        List<Client> aLlClients = clientDao.getAllClients();
        System.out.println((System.currentTimeMillis() - s)/1000);
        return aLlClients;
    }

    @PostMapping("/clients/save")
    public ResponseEntity<?> saveClient(@RequestBody ClientDTO clientDTO) {
        ApiResponse apiResponse = clientDao.saveClient(clientDTO);
        return ResponseEntity.status(apiResponse.getCode() == 1 ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/clients/updateState")
    public ResponseEntity<?> updateClientState(@RequestBody ClientStateDTO clientStateDTO) {
        ApiResponse apiResponse = clientDao.updateClientState(clientStateDTO);
        return ResponseEntity.status(apiResponse.getCode() == 1 ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/clients/updateBalance")
    public ResponseEntity<?> updateBalance(@RequestBody ClientBalanceDTO clientBalanceDTO) {
        Instant start = Instant.now();

        ApiResponse apiResponse = clientDao.updateClientBalance(clientBalanceDTO);

        System.out.println("Total time: " + Duration.between(start, Instant.now()).toMillis());

        return ResponseEntity.status(apiResponse.getCode() == 1 ? 200 : 400).body(apiResponse);

    }

    @PostMapping("/clients/update-all-clients-balance")
    public ResponseEntity<?> updateAllBalanceAsync(@RequestParam Integer balance) {
        Instant start = Instant.now();

        asynchService.executeAsynchronously(balance);

//        Util.getStatisticForEachThread();
//        System.out.println("Total tasks : " + Util.getSum());

        long toMillis = Duration.between(start, Instant.now()).toMillis();
        System.out.println("Total time: " + toMillis);

        return ResponseEntity.ok("Saved : " +toMillis+" millisecund");

    }

    @GetMapping("/clients/list/id")
    public List<Integer> getAllIntegers(){
        return clientDao.getAllIntegers();
    }


}
