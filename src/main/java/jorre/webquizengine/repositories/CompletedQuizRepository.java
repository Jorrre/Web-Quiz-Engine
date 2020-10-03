package jorre.webquizengine.repositories;

import jorre.webquizengine.models.CompletedQuiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompletedQuizRepository extends PagingAndSortingRepository<CompletedQuiz, Long> {

    @Query("select u from CompletedQuiz u where u.completedBy.email = ?1")
    Page<CompletedQuiz> findCompletedQuizBySolvedBy(String email, Pageable pageable);
}
