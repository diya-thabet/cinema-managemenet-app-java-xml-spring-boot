package com.example.demo.repository;

import com.example.demo.model.Crenaux;
import com.example.demo.model.Spectacle;
import com.example.demo.model.SpectacleDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpectacleRepositoryCustom {
	List<Spectacle> findSpectacles();


	SpectacleDetails findSpectacleDetailsById( Integer id);
	Crenaux getCrenaux(Integer idSpec, Integer idCrenaux);
}
