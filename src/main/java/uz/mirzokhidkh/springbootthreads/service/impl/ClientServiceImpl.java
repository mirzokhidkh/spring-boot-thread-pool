package uz.mirzokhidkh.springbootthreads.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.mirzokhidkh.springbootthreads.domain.Client;
import uz.mirzokhidkh.springbootthreads.payload.ApiResponse;
import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientStateDTO;
import uz.mirzokhidkh.springbootthreads.repository.ClientRepository;
import uz.mirzokhidkh.springbootthreads.service.ClientService;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final DataSource dataSource;
    private final ClientRepository clientRepository;

    public ClientServiceImpl(DataSource dataSource, ClientRepository clientRepository) {
        this.dataSource = dataSource;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.getAllClients();
    }

    @Override
    public ApiResponse getClientById(Integer id) {
        Client client = clientRepository.findById(id).orElse(null);
        ApiResponse apiResponse;
        if (client == null) {
            apiResponse = new ApiResponse("Client ID '" + id + "' not found");
        } else {
            apiResponse = new ApiResponse("Success", 1, client);
        }
        return apiResponse;
    }

    @Override
    public ApiResponse saveClient(ClientDTO clientDTO) {

        String runSP = "{call Clients_Info_API.Set_Client(?,?,?,?,?,?,?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP);) {
            conn.setAutoCommit(true);

            cs.setString(1, clientDTO.getName());
            cs.setString(2, clientDTO.getAddress());
            cs.setInt(3, clientDTO.getBalance());
            cs.setInt(4, clientDTO.getState());
            cs.setString(5, clientDTO.getActive());
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.registerOutParameter(7, Types.INTEGER);
            cs.execute();

            String o_msg = cs.getString(6);
            int o_code = cs.getInt(7);

            return new ApiResponse(o_msg, o_code);

        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), 0);
        }

    }

    @Override
    public ApiResponse updateClientState(ClientStateDTO clientStateDTO) {
        String runSP = "{call Clients_Info_API.Update_Client_State(?,?,?,?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP)) {
            conn.setAutoCommit(true);

            cs.setInt(1, clientStateDTO.getId());
            cs.setInt(2, clientStateDTO.getState());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();

            String o_msg = cs.getString(3);
            int o_code = cs.getInt(4);

            return new ApiResponse(o_msg, o_code);

        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), 0);
        }
    }

    @Override
    @Async
    public ApiResponse updateClientBalance(ClientBalanceDTO clientBalanceDTO) {
        String runSP = "{call Clients_Info_API.Update_Balance(?,?,?,?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP)) {
            conn.setAutoCommit(true);

            cs.setInt(1, clientBalanceDTO.getId());
            cs.setInt(2, clientBalanceDTO.getBalance());
            cs.registerOutParameter(3, Types.VARCHAR);
            cs.registerOutParameter(4, Types.INTEGER);
            cs.execute();

//            String o_msg = cs.getString("o_msg");
//            int o_code = cs.getInt("o_code");
            String o_msg = cs.getString(3);
            int o_code = cs.getInt(4);

            return new ApiResponse(o_msg, o_code);

        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), 0);
        }
    }

    //    @Override
    public ApiResponse updateClientBalance2(ClientBalanceDTO clientBalanceDTO) {
        String runSP = "{? = call Clients_Info_API.Update_Balance_Function(?,?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP)) {
            conn.setAutoCommit(true);

            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setInt(2, clientBalanceDTO.getId());
            cs.setInt(3, clientBalanceDTO.getBalance());
            cs.execute();

//            String o_msg = cs.getString("o_msg");
//            int o_code = cs.getInt("o_code");
            String o_msg = cs.getString(1);
//            int o_code = cs.getInt(4);

            return new ApiResponse(o_msg, 1);

        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), 0);
        }
    }


    public List<Integer> getAllIntegers() {
        return clientRepository.findAllIntegers();
    }

}
