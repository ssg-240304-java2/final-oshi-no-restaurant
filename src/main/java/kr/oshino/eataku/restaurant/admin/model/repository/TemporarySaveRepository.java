package kr.oshino.eataku.restaurant.admin.model.repository;

import kr.oshino.eataku.restaurant.admin.entity.TemporarySave;
import kr.oshino.eataku.restaurant.admin.model.dto.TemporarySaveDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporarySaveRepository extends JpaRepository<TemporarySave, Long> {

    TemporarySave save(TemporarySave temporarySave);

    TemporarySave findByAccount(String account);
}
