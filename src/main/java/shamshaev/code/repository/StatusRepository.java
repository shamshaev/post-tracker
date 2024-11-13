package shamshaev.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shamshaev.code.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
