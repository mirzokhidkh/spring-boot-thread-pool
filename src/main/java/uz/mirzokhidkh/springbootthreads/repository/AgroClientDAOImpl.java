package uz.mirzokhidkh.springbootthreads.repository;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.mirzokhidkh.springbootthreads.domain.Client;
import uz.mirzokhidkh.springbootthreads.payload.ApiResponse;
import uz.mirzokhidkh.springbootthreads.payload.ClientBalanceDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientDTO;
import uz.mirzokhidkh.springbootthreads.payload.ClientStateDTO;
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.AgroLogModel;
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.NewOrganization;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgroClientDAOImpl {

    private final DataSource dataSource;
//    private final ClientRepository clientRepository;

    public AgroClientDAOImpl(DataSource dataSource
    ) {
        this.dataSource = dataSource;
    }


    //    PROCEDURE Set_Client(
//            i_query_id   VARCHAR2,
//            i_query_date DATE,
//            i_inn        VARCHAR2,
//            i_client_branch   VARCHAR2,
//            i_client     VARCHAR2,
//            i_dial_d     DATE,
//            i_dial_num   NUMBER,
//            i_offer_agr_file_url  VARCHAR2,
//            o_msg   OUT VARCHAR2,
//            o_code  OUT NUMBER
//    );
    public ApiResponse saveClient(NewOrganization newOrganization) {

        String runSP = "{call AGRO_PLAT_API.Set_Client(?,?,?,?,?,?,?,?,?,?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP);) {
            conn.setAutoCommit(true);

            cs.setInt(1, newOrganization.getQueryId());
            cs.setDate(2, Date.valueOf(newOrganization.getQueryDate()));
            cs.setString(3, newOrganization.getInn());
            cs.setString(4, newOrganization.getClientBranch());
            cs.setString(5, newOrganization.getClient());
            cs.setDate(6, Date.valueOf(newOrganization.getDialD()));
            cs.setInt(7, newOrganization.getDialNum());

            String offerAgreementFileUrl = newOrganization.getOfferAgreementFileUrl();
//            if (offerAgreementFileUrl != null)
            cs.setString(8, offerAgreementFileUrl != null ? offerAgreementFileUrl : "");

            cs.registerOutParameter(9, Types.VARCHAR);
            cs.registerOutParameter(10, Types.INTEGER);
            cs.execute();

            String o_msg = cs.getString(9);
            int o_code = cs.getInt(10);

            return new ApiResponse(o_msg, o_code);

        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), 0);
        }

    }

    //                     i_method_name agro_logs.method_name%TYPE,
//                     i_status_code agro_logs.status_code%TYPE,
//                     i_msg         agro_logs.msg%TYPE,
//                     i_request_v   agro_logs.request_v%TYPE,
//                     i_response_v  agro_logs.response_v%TYPE,
//                     o_msg         OUT VARCHAR2,
//                     o_code        OUT NUMBER
    public void saveLog(AgroLogModel agroLogModel) {

        String runSP = "{call AGRO_PLAT_API.Save_Log(?,?,?,?,?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP);) {
            conn.setAutoCommit(true);

            cs.setString(1, agroLogModel.getMethodName());
            cs.setInt(2, agroLogModel.getStatusCode());
            cs.setString(3, agroLogModel.getMsg());
            cs.setString(4, agroLogModel.getRequestV());
            cs.setString(5, agroLogModel.getResponseV());
            cs.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ApiResponse updateClientApprovedState(int queryId) {

        String runSP = "{call AGRO_PLAT_API.Update_Cl_Approved(?,?,?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP);) {
            conn.setAutoCommit(true);
            cs.setInt(1, queryId);
            cs.registerOutParameter(2, Types.VARCHAR);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.execute();

            String o_msg = cs.getString(2);
            int o_code = cs.getInt(3);

            return new ApiResponse(o_msg, o_code);
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), 0);
        }

    }


}
