package com.example.demo.repository;

import com.example.demo.model.Spectacle;
import com.example.demo.model.SpectacleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpectacleRepository extends JpaRepository<Spectacle, Integer>, SpectacleRepositoryCustom {

	@Query("SELECT s FROM Spectacle s JOIN s.lieu l WHERE " +
			"(:titre IS NULL OR LOWER(s.titre) LIKE LOWER(CONCAT('%', :titre, '%'))) AND " +
			"(:lieu IS NULL OR LOWER(l.nomLieu) LIKE LOWER(CONCAT('%', :lieu, '%'))) AND " +
			"(:ville IS NULL OR LOWER(l.ville) LIKE LOWER(CONCAT('%', :ville, '%'))) AND " +
			"(:dateS IS NULL OR s.dateS = :dateS) AND " +
			"(:hDebut IS NULL OR s.hDebut = :hDebut)")
	List<Spectacle> findSpectaclesByParams(
			@Param("titre") String titre,
			@Param("lieu") String lieu,
			@Param("ville") String ville,
			@Param("dateS") LocalDate dateS,
			@Param("hDebut") Float hDebut
	);
}