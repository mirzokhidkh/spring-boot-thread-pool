package uz.mirzokhidkh.springbootthreads.repository;

import uz.mirzokhidkh.springbootthreads.domain.Client;
import uz.mirzokhidkh.springbootthreads.payload.ApiResponse;
import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientStateDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientDTO;

import java.sql.SQLException;
import java.util.List;

public interface ClientDAO {

    List<Client> getAllClients() throws SQLException;

    ApiResponse getClientById(Integer id);

    ApiResponse saveClient(ClientDTO clientDTO);

    ApiResponse updateClientState(ClientStateDTO clientStateDTO);

    ApiResponse updateClientBalance(ClientBalanceDTO clientBalanceDTO);

    List<Integer> getAllIntegers();
}
