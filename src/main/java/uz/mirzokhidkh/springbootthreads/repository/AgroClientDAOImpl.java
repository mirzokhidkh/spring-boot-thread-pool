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
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.Transaction;

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


    public List<Transaction> getActiveAgroTransactions() {
        List<Transaction> list = new ArrayList<>();

        String QUERY = "select t.* from agro_leads t";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(QUERY);
            Transaction transaction = new Transaction();
            while (rs.next()) {
                int opDc = rs.getInt("op_dc");

                transaction.setDocNum(Integer.parseInt(rs.getString("doc_numb")));
                transaction.setDDate(rs.getString("doc_date"));
                transaction.setBankCl(rs.getString("cl_mfo"));

                String client = null;
                if (opDc == 1) {
                    client = rs.getString("cl_acc");
                } else {
                    client = rs.getString("co_acc");
                }
                transaction.setClient(client.substring(7));
                transaction.setAccCl(rs.getNString("cl_acc"));
                transaction.setNameCl(rs.getString("cl_name"));
                transaction.setAccCo(rs.getString("co_acc"));
                transaction.setBankCo(rs.getString("co_mfo"));
                transaction.setNameCo(rs.getString("co_name"));
                transaction.setPurpose(rs.getString("pay_purpose"));
                transaction.setPurposeCode(rs.getString("sym_id"));
                transaction.setSumma(rs.getLong("sum_pay"));
                transaction.setCurrency(rs.getString("code_currency"));
                transaction.setTypeDoc(rs.getInt("trans_id"));
                transaction.setVDate(rs.getDate("curr_day"));
                transaction.setPdc(opDc == 1 ? "D" : "C");
                transaction.setId(rs.getLong("id"));

                list.add(transaction);
            }

            return list;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void moveTransactionToHistory(Long leadId) {

        String runSP = "{call AGRO_PLAT_API.Move_Trans_To_Archive(?)}";
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall(runSP);) {
            conn.setAutoCommit(true);
            cs.setLong(1, leadId);
            cs.execute();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
