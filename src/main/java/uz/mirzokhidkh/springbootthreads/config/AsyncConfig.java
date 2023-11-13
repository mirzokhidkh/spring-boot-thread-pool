package uz.mirzokhidkh.springbootthreads.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.AgroResponse;
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.GotNewOrganization;
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.NewOrganization;
import uz.mirzokhidkh.springbootthreads.payload.agroplatforma.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Configuration
//@EnableScheduling
@Component
//@EnableAsync
public class AsyncConfig {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    private static int i = 0;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final WebClient webClient;

    public AsyncConfig(WebClient webClient) {
        this.webClient = webClient;
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
    @Scheduled(cron = "0/5 * * * * MON-FRI") //Every minute, between 09:00 AM and 05:59 PM, Monday through Friday
//    @Scheduled(cron = "${cron.expression}")
    public void scheduleGetNewTransaction() throws InterruptedException {
        Date date = new Date();
        log.info("The time is now {}", dateFormat.format(date));

//        System.out.println("Fixed delay task - " + System.currentTimeMillis() / 1000);
//        System.out.println("Fixed delay task - " + System.currentTimeMillis());
//        System.out.println(new Date());
//    System.out.println("Started : " + ++i);
//    Thread.sleep(3000);
//    System.out.println("Finished : " + i);

        String urlGetNewOrg = "http://localhost:8243/api/company/get-new-organization/";
        String urlGotNewOrg = "http://localhost:8243/api/company/got-new-organization/";

        NewOrganization newOrganization = webClient.get()
                .uri(urlGetNewOrg)
                .header(HttpHeaders.AUTHORIZATION, "Bearer AGRO-TEST-1")
                .retrieve()
                .bodyToMono(NewOrganization.class)
                .block();

        System.out.println(newOrganization);

        int code = newOrganization.getCode();
        String text = newOrganization.getText();
//        if (code != 0 && text == null) {
        if (text == null) {
            int queryId = newOrganization.getQueryId();
            GotNewOrganization gotNewOrganization = new GotNewOrganization(queryId);

            AgroResponse agroResponse1 = webClient.method(HttpMethod.GET)
                    .uri(urlGotNewOrg)
                    .bodyValue(gotNewOrganization)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer AGRO-TEST-2")
                    .retrieve()
                    .bodyToMono(AgroResponse.class)
                    .block();

            System.out.println(agroResponse1);
        }


//        System.out.println(Thread.currentThread().getName());


    }

    //<second> <minute> <hour> <day-of-month> <month> <day-of-week>
    @Scheduled(cron = "0 0/5 * * * MON-FRI") //Every 5 minutes, between 09:00 AM and 05:59 PM, Monday through Friday
//    @Scheduled(cron = "0/10 * * * * MON-FRI") //Every 5 minutes, between 09:00 AM and 05:59 PM, Monday through Friday
    public void scheduleCheckNewTransaction() throws InterruptedException {
        Date date = new Date();
        log.info("The time is now {}", dateFormat.format(date));

        String urlTransaction = "http://localhost:8243/api/company/transaction/";


        Transaction transaction = getTransaction();
        AgroResponse agroResponse2 = webClient.method(HttpMethod.GET)
                .uri(urlTransaction)
                .bodyValue(transaction)
                .header(HttpHeaders.AUTHORIZATION, "Bearer AGRO-TEST-3")
                .retrieve()
                .bodyToMono(AgroResponse.class)
                .block();
        System.out.println(agroResponse2);


//        System.out.println(Thread.currentThread().getName());


    }

    private static Transaction getTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDocNum(22);
        transaction.setDDate("2023-11-11");
        transaction.setBankCl("12345");
        transaction.setClient("11111111");
        transaction.setAccCl("12121212121212");
        transaction.setNameCl("Маржон тола сервиc");
        transaction.setBankCo("00253");
        transaction.setAccCo("Str");
        transaction.setNameCo("Str");
        transaction.setPurpose("йил апрель ойи иш хакидан касаба уюшма аъзолик бадали утказилди");
        transaction.setPurposeCode("Str");
        transaction.setSumma(1000000);
        transaction.setCurrency("Str");
        transaction.setTypeDoc(122);
        transaction.setVDate("2023-05-23");
        transaction.setPdc("D");
        transaction.setId(33);
        return transaction;
    }

    ;
}
