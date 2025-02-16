package com.davidperezmillan.ms_black.infrastructure.bbdd.repositories;

import com.davidperezmillan.ms_black.infrastructure.bbdd.models.Show;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {

    // find top n shows that are not deleted
    List<Show> findByDeletedFalse(Pageable pageable);

    // find top n shows that are not deleted where tags contains the given tag
    List<Show> findByDeletedFalseAndTagsContaining(String tag, Pageable pageable);

    List<Show> findByDeletedFalse();

    Optional<Show> findByShowId(String showId);

    @Query(value = "SELECT * FROM `show` ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Show> findRandomShow();


    // find shows by title or tags containing the given keyword
    @Query("SELECT s FROM Show s WHERE s.deleted = false AND (s.title LIKE %:keyword% OR s.tags LIKE %:keyword%)")
    List<Show> findByTitleOrTagsContaining(String keyword, Pageable pageable);// find shows by title or tags containing the given keyword

}