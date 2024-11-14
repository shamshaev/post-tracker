package shamshaev.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shamshaev.code.model.TrackStatus;

public interface TrackStatusRepository extends JpaRepository<TrackStatus, Long> {
}
