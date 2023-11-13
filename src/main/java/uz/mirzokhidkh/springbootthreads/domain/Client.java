package uz.mirzokhidkh.springbootthreads.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
//import org.hibernate.Hibernate;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Entity
//@Table(name = "clients_info")
public class Client {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    private Integer balance;

    private Integer state;

    private String active;

//    @UpdateTimestamp
    @JsonProperty("last_modified")
//    @Column(name = "last_modified")
    private Timestamp lastModified;



//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Client client = (Client) o;
//        return id != null && Objects.equals(id, client.id);
//    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
