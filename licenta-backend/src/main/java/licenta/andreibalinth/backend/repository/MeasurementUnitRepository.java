package licenta.andreibalinth.backend.repository;

import licenta.andreibalinth.backend.entities.MeasurementUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnitEntity, Long> {
}
