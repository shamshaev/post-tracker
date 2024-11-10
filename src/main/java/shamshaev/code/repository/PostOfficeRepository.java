package shamshaev.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shamshaev.code.model.PostOffice;

import java.util.Optional;

public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {
    Optional<PostOffice> findByPostCode(String name);
}
