package github.com.LimaDevCod3r.repositories;

import github.com.LimaDevCod3r.models.EmployeeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetails, UUID> {
}
