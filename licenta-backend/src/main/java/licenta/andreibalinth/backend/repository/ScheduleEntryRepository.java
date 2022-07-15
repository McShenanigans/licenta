package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.ScheduleEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntryEntity, Long> {
    List<ScheduleEntryEntity> findAllByUser_Id(Long userId);
}
