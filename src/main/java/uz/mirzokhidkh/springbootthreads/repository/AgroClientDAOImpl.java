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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        String QUERY = "select t.* from agro_client_leads_v t";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()
        ) {
            ResultSet rs = stmt.executeQuery(QUERY);
            Transaction transaction;
            while (rs.next()) {

                String consent = rs.getString("consent");

                //mijoz rozi bo'lsagina listga qo'shiladi
                if (Objects.equals(consent, "Y")) {
                    transaction = new Transaction();

                    String docNumb = rs.getString("doc_numb");
                    Date docDate = rs.getDate("doc_date");
                    String clMfo = rs.getString("cl_mfo");
                    String client = rs.getString("client");
                    String clAcc = rs.getNString("cl_acc");
                    String clName = rs.getString("cl_name");
                    String coAcc = rs.getString("co_acc");
                    String coMfo = rs.getString("co_mfo");
                    String coName = rs.getString("co_name");
                    String payPurpose = rs.getString("pay_purpose");
                    long sumPay = rs.getLong("sum_pay");
                    String codeCurrency = rs.getString("code_currency");
                    String codeDocument = rs.getString("code_document");
                    Date currDay = rs.getDate("curr_day");
                    int opDc = rs.getInt("op_dc");
                    long id = rs.getLong("id");

                    transaction.setDocNum(Integer.parseInt(docNumb));
                    transaction.setDDate(docDate);
                    transaction.setBankCl(clMfo);
                    //client 20 talik hisob raqamidan 9-indexdan 17-indexgacha Client Code hisoblandi.
                    //masalan : '20208000004389063001'[9:17] -> 04389063
                    String clientCode = client.substring(9, 17);
                    transaction.setClient(clientCode);
                    transaction.setAccCl(clAcc.substring(7));
                    transaction.setNameCl(clName);
                    transaction.setAccCo(coAcc.substring(7));
                    transaction.setBankCo(coMfo);
                    transaction.setNameCo(coName);
                    transaction.setPurpose(payPurpose);
//                transaction.setPurposeCode(rs.getString("sym_id"));
                    transaction.setSumma(sumPay);
                    transaction.setCurrency(codeCurrency);
                    transaction.setTypeDoc(codeDocument);
                    transaction.setVDate(currDay);
                    //opDc = 1 bo'lsa 'D'(Debit) , 0 bo'lsa 'C'(Credit)
                    transaction.setPdc(opDc == 1 ? "D" : "C");
                    transaction.setId(id);


                    list.add(transaction);
                }
            }
            rs.close();


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
