package uz.mirzokhidkh.springbootthreads.payload.agroplatforma;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgroResponse {
	private int code;
	private String text;
}