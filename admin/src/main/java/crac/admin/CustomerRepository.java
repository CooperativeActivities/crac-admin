package crac.admin;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CracUser, Long> {

	List<CracUser> findByLastNameStartsWithIgnoreCase(String lastName);
}
