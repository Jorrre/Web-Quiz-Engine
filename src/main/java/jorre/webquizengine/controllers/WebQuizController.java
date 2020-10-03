package jorre.webquizengine.controllers;

import jorre.webquizengine.models.*;
import jorre.webquizengine.services.QuizService;
import jorre.webquizengine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static jorre.webquizengine.security.SecurityConfig.getCurrentUser;

@RestController
public class WebQuizController {

    private final QuizService quizService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WebQuizController(final QuizService quizService, final UserService userRepository, final PasswordEncoder passwordEncoder) {
        this.quizService = quizService;
        this.userService = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/actuator/shutdown")
    public ResponseEntity<?> noAuth() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/api/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registerNewUser(@Valid @RequestBody final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userService.addUser(user));
    }

    @GetMapping(path = "/api/quizzes")
    public Page<Quiz> getAllQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getAllQuizzes(page);
    }

    @PostMapping(path = "/api/quizzes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Quiz addQuiz(@Valid @RequestBody final Quiz quiz) {
        quiz.setAuthor(getCurrentUser());
        return quizService.addQuiz(quiz);
    }

    @GetMapping(path = "/api/quizzes/completed")
    public Page<CompletedQuiz> getUsersCompletedQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return userService.getAllUsersCompletedQuizzes(page);
    }

    @GetMapping(path = "/api/quizzes/{id}")
    public Quiz getQuizWithId(@PathVariable final long id) {
        return quizService.getQuizById(id);
    }

    @DeleteMapping(path = "/api/quizzes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable final long id) {
        quizService.deleteQuizById(id);
    }

    @PostMapping(path = "/api/quizzes/{id}/solve", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response solveQuiz(@PathVariable final long id, @RequestBody final Answer answer) {
        Quiz quiz = getQuizWithId(id);
        boolean isCorrect = List.copyOf(quiz.getAnswer()).equals(answer.getAnswer());
        if (isCorrect)
            userService.addCompletedQuiz(quiz);
        return new Response(isCorrect);
    }

}
