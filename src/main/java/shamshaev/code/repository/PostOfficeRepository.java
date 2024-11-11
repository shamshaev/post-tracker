package shamshaev.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shamshaev.code.model.PostOffice;

import java.util.Optional;

@Repository
public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {
    Optional<PostOffice> findByPostCode(String postCode);
}
