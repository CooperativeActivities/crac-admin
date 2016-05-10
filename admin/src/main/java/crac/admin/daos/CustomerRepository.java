package crac.admin.daos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import crac.admin.models.CracUser;

public interface CustomerRepository extends JpaRepository<CracUser, Long> {

	List<CracUser> findByLastNameStartsWithIgnoreCase(String lastName);
	/*
	@Query("SELECT max(t.id) FROM CracUser t")
	Long getMaxId();
	*/
}
