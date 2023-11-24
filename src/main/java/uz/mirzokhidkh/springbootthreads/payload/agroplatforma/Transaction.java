package uz.mirzokhidkh.springbootthreads.payload.agroplatforma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction{

	//To’lovchi yuridik shaxs hisob raqami
	@JsonProperty("acc_cl")
	private String accCl;

	//Korrespondent nomi
	@JsonProperty("name_co")
	private String nameCo;

	//Mijoz banki MFO
	@JsonProperty("bank_cl")
	private String bankCl;

	//To’lov maqsadi
	@JsonProperty("purpose")
	private String purpose;

	//To’lov xujjati raqami
	@JsonProperty("doc_num")
	private int docNum;


	//To’lov xujjati turi
	@JsonProperty("type_doc")
	private String typeDoc;

	//To’lov xujjati sanasi
	@JsonProperty("d_date")
	private Date dDate;

	//Korrespondent bank MFO
	@JsonProperty("bank_co")
	private String bankCo;

	//Korrespondent hisob raqami
	@JsonProperty("acc_co")
	private String accCo;

	//Debit,Kredit
	@JsonProperty("pdc")
	private String pdc;

	//To’lov o’tkazilgan sana
	@JsonProperty("v_date")
	private Date vDate;

	//To’lov summasi
	@JsonProperty("summa")
	private Long summa;

	//Mijoz unikal bank kodi
	@JsonProperty("client")
	private String client;

	//To’lov maqsadi kodi
//	@JsonProperty("purpose_code")
//	private String purposeCode;

	//To’lov valyuta kodi
	@JsonProperty("currency")
	private String currency;

	//to’lov idsi
	@JsonProperty("id")
	private Long id;

	//To’lovchi yuridik shaxs nomi
	@JsonProperty("name_cl")
	private String nameCl;

}