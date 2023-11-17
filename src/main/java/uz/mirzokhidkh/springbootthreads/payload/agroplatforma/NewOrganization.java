package uz.mirzokhidkh.springbootthreads.payload.agroplatforma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrganization {

    private int code;

    private String text;

    @JsonProperty("query_id")
    private String queryId;

    //bank MFO
    //Мижозга хизмат курсатувчи банк коди
    @JsonProperty("client_branch")
    private String clientBranch;

    //Мижознинг оферта шартномаси файли
    @JsonProperty("offer_agreement_file_url")
    private String offerAgreementFileUrl;

    //So’rov berilgan sana
    @JsonProperty("query_date")
    private String queryDate;

    @JsonProperty("inn")
    private String inn;

    //	bank HISOB RAQAMI
    @JsonProperty("client")
    private String client;

    //	Ro’yhatdan o’tgan sana
    @JsonProperty("dial_d")
    private String dialD;

    //Мижознинг оферта шартномаси рақами
    @JsonProperty("dial_num")
    private int dialNum;

}