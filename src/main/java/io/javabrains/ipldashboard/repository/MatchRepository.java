package io.javabrains.ipldashboard.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.javabrains.ipldashboard.model.Match;

@Repository
public interface MatchRepository extends CrudRepository<Match,Long> {

}
