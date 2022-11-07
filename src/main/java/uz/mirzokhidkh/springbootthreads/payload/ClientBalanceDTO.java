package uz.mirzokhidkh.springbootthreads.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientBalanceDTO {

    @Column()
    private Integer id;

    @Column()
    private Integer balance;

}
