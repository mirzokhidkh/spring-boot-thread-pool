package uz.mirzokhidkh.springbootthreads.payload.agroplatforma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GotNewOrganization {

	@JsonProperty("query_id")
	private String queryId;


}