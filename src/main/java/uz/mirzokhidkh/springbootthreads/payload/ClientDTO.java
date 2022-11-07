package uz.mirzokhidkh.springbootthreads.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    @Column()
    private String name;

    @Column(nullable = false)
    private String address;

    @Column()
    private Integer balance;

    @Column()
    private Integer state;

    @Column()
    @Schema(defaultValue = "N")
    private String active = "N";


}
