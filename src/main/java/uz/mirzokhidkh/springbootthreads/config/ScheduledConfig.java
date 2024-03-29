package uz.mirzokhidkh.springbootthreads.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import uz.mirzokhidkh.springbootthreads.payload.ApiResponse;
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.*;
import uz.mirzokhidkh.springbootthreads.repository.AgroClientDAOImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//@Configuration
//@EnableScheduling
@Component
//@EnableAsync
public class ScheduledConfig {

    private static final Logger log = LoggerFactory.getLogger(ScheduledConfig.class);

    private static int i = 0;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final WebClient webClient;

    private final AgroClientDAOImpl agroClientDAO;

    private final ObjectMapper objectMapper;

    @Value("${agro.token}")
    private String agroToken;

    @Value("${agro.url.get-new-org}")
    private String urlGetNewOrg;

    @Value("${agro.url.got-new-org}")
    private String urlGotNewOrg;

    @Value("${agro.url.send-trans}")
    private String urlSendTransaction;

    public ScheduledConfig(WebClient webClient, AgroClientDAOImpl agroClientDAO, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.agroClientDAO = agroClientDAO;
        this.objectMapper = objectMapper;
    }

    /**
     * to check cron expression
     * <a href="https://crontab.cronhub.io/">click me</a>
     */

    //    @Async
//@Scheduled(fixedDelay = 1000)
//    @Scheduled(fixedRate = 1000)
    //<second> <minute> <hour> <day-of-month> <month> <day-of-week>
//    @Scheduled(cron = "0 * 9-17 * * MON-FRI") //Every minute, between 09:00 AM and 05:59 PM, Monday through Friday
//    @Scheduled(cron = "0 * 1 * * MON-FRI") //Every minute, between 09:00 AM and 05:59 PM, Monday through Friday
    @Scheduled(cron = "0/5 * * * * MON-FRI") //Every minute, between 09:00 AM and 05:59 PM, Monday through Friday
//    @Scheduled(cron = "${cron.expression}")
    public void scheduleGetNewTransaction() throws InterruptedException, JsonProcessingException {
        Date date = new Date();
        log.info("The time is now {}", dateFormat.format(date));

//        System.out.println("Fixed delay task - " + System.currentTimeMillis() / 1000);
//        System.out.println("Fixed delay task - " + System.currentTimeMillis());
//        System.out.println(new Date());
//    System.out.println("Started : " + ++i);
//    Thread.sleep(10000);
//    System.out.println("Finished : " + i);

//        String urlGetNewOrg = "http://localhost:8243/api/company/get-new-organization/";
//        String urlGotNewOrg = "http://localhost:8243/api/company/got-new-organization/";


        NewOrganization newOrganization = null;
        int code;
        String text;

//        boolean isHasNew = true;
        AgroLogModel agroLogModel1 = new AgroLogModel();
        agroLogModel1.setMethodName("method-1");
        agroLogModel1.setMsg("Msg");
        agroLogModel1.setRequestV("");
        while (true) {

//            try {
            newOrganization = webClient.get()
                    .uri(urlGetNewOrg)
                    .header(HttpHeaders.AUTHORIZATION, "Token AGRO-TEST-1 " + agroToken)
                    .retrieve()
//                    .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(), response -> {
//                        agroLogModel1.setStatusCode(response.rawStatusCode());
//                        return response.createException();
//                    })
                    .bodyToMono(NewOrganization.class)
                    .block();

//            } catch (WebClientException e) {
//                log.info(e.getMessage());
//                //'WebClientResponseException' exceptioni statusCode 4xx yoki 5xx holatda qaytadi
//                if (e instanceof WebClientResponseException) {
//                    WebClientResponseException clientEx = (WebClientResponseException) e;
//                    agroLogModel1.setStatusCode(clientEx.getRawStatusCode());
//                    agroLogModel1.setResponseV(clientEx.getResponseBodyAsString());
//                } else {
//                    agroLogModel1.setStatusCode(-1);
//                    agroLogModel1.setResponseV(e.getMessage());
//                }
//                agroClientDAO.saveLog(agroLogModel1);
//                System.out.println("LOG-1");
//            }


//            if (newOrganization != null) {

            System.out.println("1-URL => " + newOrganization);


            String valueAsString = objectMapper.writeValueAsString(newOrganization);
            agroLogModel1.setResponseV(valueAsString);

            agroLogModel1.setStatusCode(200);
            agroClientDAO.saveLog(agroLogModel1);
            System.out.println("LOG-1 ");

            code = newOrganization.getCode();
            text = newOrganization.getText();

            //        if (code != 0 && text == null) {
            if (text == null) {

                ApiResponse apiResponse = agroClientDAO.saveClient(newOrganization);
                int codeSavedNewOrg = apiResponse.getCode();
                System.out.println(apiResponse);

                int queryId = newOrganization.getQueryId();
                GotNewOrganization gotNewOrganization = new GotNewOrganization(queryId);


                // code = 1 bo'lgandagina yangi tashkilot olinganligi haqida ma'lumot yuboriladi
                if (codeSavedNewOrg == 1) {
                    AgroResponse agroResponse1 = webClient.post()
                            .uri(urlGotNewOrg)
                            .bodyValue(gotNewOrganization)
                            .header(HttpHeaders.AUTHORIZATION, "Token AGRO-TEST-2 " + agroToken)
                            .retrieve()
                            .bodyToMono(AgroResponse.class)
                            .block();

                    System.out.println("2-URL => " + agroResponse1);

                    int code2 = agroResponse1.getCode();
                    if (code2 == 1) {
//                        ApiResponse apiResponse1 = agroClientDAO.updateClientApprovedState(1);
                        ApiResponse apiResponse1 = agroClientDAO.updateClientApprovedState(queryId);
                        System.out.println("Approve :" + apiResponse1);
                    }
                    AgroLogModel agroLogModel2 = new AgroLogModel();
                    agroLogModel2.setMethodName("method-2");
                    agroLogModel2.setStatusCode(200);
                    agroLogModel2.setMsg("OK");
                    valueAsString = objectMapper.writeValueAsString(gotNewOrganization);
                    agroLogModel2.setRequestV(valueAsString);
                    valueAsString = objectMapper.writeValueAsString(agroResponse1);
                    agroLogModel2.setResponseV(valueAsString);

                    agroClientDAO.saveLog(agroLogModel2);
                    System.out.println("LOG-2");
                } else {
                    break;
                }
                System.out.println("---------------------------------------");

            } else {
//                isHasNew = false/;
                break;
            }
//            } else {
//                break;
//            }

        }

        System.out.println("=============================================");
//        System.out.println(Thread.currentThread().getName());

    }

    //<second> <minute> <hour> <day-of-month> <month> <day-of-week>
    @Scheduled(cron = "0/15 * * * * MON-FRI") //Every minute, between 09:00 AM and 05:59 PM, Monday through Friday
//    @Scheduled(cron = "0 0/3 * * * MON-FRI") //Every 3 minutes, between 09:00 AM and 05:59 PM, Monday through Friday
//    @Scheduled(cron = "0/10 * * * * MON-FRI") //Every 10 minutes, between 09:00 AM and 05:59 PM, Monday through Friday
    public void scheduleCheckNewTransaction() throws JsonProcessingException {
        Date date = new Date();
        log.info("The time is now {}", dateFormat.format(date));

//        String urlSendTransaction = "http://localhost:8243/api/company/transaction/";
        AtomicReference<String> jsonObj = new AtomicReference<>();

        List<Transaction> agroTransactions = agroClientDAO.getActiveAgroTransactions();
//         for (Transaction transaction : agroTransactions) {

        agroTransactions.forEach(transaction -> {

            try {
                jsonObj.set(objectMapper.writeValueAsString(transaction));
                log.info(jsonObj.get().toString());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }



//            Transaction transaction = getTransaction();
            AgroResponse agroResponse2 = webClient.post()
                    .uri(urlSendTransaction)
                    .bodyValue(transaction)
                    .header(HttpHeaders.AUTHORIZATION, "Token AGRO-TEST-3 " + agroToken)
                    .retrieve()
                    .bodyToMono(AgroResponse.class)
                    .block();

            System.out.println(agroResponse2);

            int code = agroResponse2.getCode();

            if (code == 1) {
                agroClientDAO.moveTransactionToHistory(transaction.getId());
            }

            AgroLogModel agroLogModel3 = new AgroLogModel();
            agroLogModel3.setMethodName("method-3");
            agroLogModel3.setStatusCode(200);
            agroLogModel3.setMsg("OK");
            String valueAsString = null;
            try {
                valueAsString = objectMapper.writeValueAsString(transaction);
                agroLogModel3.setRequestV(valueAsString);
                valueAsString = objectMapper.writeValueAsString(agroResponse2);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            agroLogModel3.setResponseV(valueAsString);

            agroClientDAO.saveLog(agroLogModel3);

            System.out.println("LOG-3");


        });
//        }


//        System.out.println(Thread.currentThread().getName());
    }

    private static Transaction getTransaction() throws ParseException {
        Transaction transaction = new Transaction();
        transaction.setDocNum(22);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2023-11-11");
        transaction.setDDate(date);
        transaction.setBankCl("12345");
        transaction.setClient("11111111");
        transaction.setAccCl("12121212121212");
        transaction.setNameCl("Маржон тола сервиc");
        transaction.setBankCo("00253");
        transaction.setAccCo("Str");
        transaction.setNameCo("Str");
        transaction.setPurpose("йил апрель ойи иш хакидан касаба уюшма аъзолик бадали утказилди");
//        transaction.setPurposeCode("Str");
        transaction.setSumma(1000000L);
        transaction.setCurrency("Str");
        transaction.setTypeDoc("11");

        date = dateFormat.parse("2023-05-23");

        transaction.setVDate(date);
        transaction.setPdc("D");
        transaction.setId(33L);
        return transaction;
    }

    ;
}
