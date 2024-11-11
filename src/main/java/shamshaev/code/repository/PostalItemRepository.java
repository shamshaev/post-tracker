package shamshaev.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shamshaev.code.model.PostalItem;

import java.util.Optional;

@Repository
public interface PostalItemRepository extends JpaRepository<PostalItem, Long> {
    Optional<PostalItem> findByPostalId(String postalId);
}
