package crac.admin.daos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import crac.admin.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByNameStartsWithIgnoreCase(String name);
	/*
	@Query("SELECT max(t.id) FROM CracUser t")
	Long getMaxId();
	*/
}
