package com.fondos.inversion.repository;

import com.fondos.inversion.model.Visitan;
import com.fondos.inversion.model.VisitanId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitanRepository extends JpaRepository<Visitan, VisitanId> {
}
