package jorre.webquizengine.services;


import jorre.webquizengine.models.Quiz;
import jorre.webquizengine.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static jorre.webquizengine.security.SecurityConfig.getCurrentUser;

@Service
public class QuizService {


    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Page<Quiz> getAllQuizzes(int pageNo) {
        System.out.println("Getting all quizzes from the database...");
        return quizRepository.findAll(
                PageRequest.of(pageNo, 10, Sort.by("id"))
        );
    }

    public Quiz addQuiz(Quiz quiz) {
        System.out.println("Adding new quiz to database...");
        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(long id) {
        System.out.printf("Getting quiz with id [%d] from the database%n", id);
        return quizRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Quiz with id [%d] could not be found.", id)));
    }

    public void deleteQuizById(long id) {
        System.out.printf("Deleting quiz with id [%d] from the database%n", id);
        if (! getCurrentUser().getEmail().equals(getQuizById(id).getAuthor().getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the author of this quiz.");
        quizRepository.deleteById(id);
    }
}
