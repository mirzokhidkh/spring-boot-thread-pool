//package uz.mirzokhidkh.springbootthreads.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//import uz.mirzokhidkh.springbootthreads.domain.Client;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface Client2Repository extends JpaRepository<Client,Integer> {
////public interface ClientRepository extends CrudRepository<Client,Integer> {
//
////    @Query(value = "select * from clients_info", nativeQuery = true)
//    @Query(value = "select * from clients_info_v", nativeQuery = true)
//    List<Client> getAllClientsa();
//
//    @Query(value = "select t.* from clients_info_v t FETCH FIRST 100000 ROWS ONLY", nativeQuery = true)
//    List<Client> getAllClients();
//
//    List<Client> findAll();
//
//    Optional<Client> findById(Integer id);
//
//    @Query(value = "select t.id from clients_info_v t", nativeQuery = true)
//    List<Integer> findAllIntegers();
//
//}
