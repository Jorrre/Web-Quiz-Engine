package jorre.webquizengine.services;

import jorre.webquizengine.models.CompletedQuiz;
import jorre.webquizengine.models.Quiz;
import jorre.webquizengine.models.User;
import jorre.webquizengine.repositories.CompletedQuizRepository;
import jorre.webquizengine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static jorre.webquizengine.security.SecurityConfig.getCurrentUser;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final CompletedQuizRepository completedQuizRepository;

    @Autowired
    public UserService(UserRepository userRepository, CompletedQuizRepository completedQuizRepository) {
        this.userRepository = userRepository;
        this.completedQuizRepository = completedQuizRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.printf("Getting user with username %s...%n", username);
        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Couldn't find user %s", username)));
    }

    public User addUser(User user) {
        System.out.println("Adding new user to the database...");
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User already in database");
        return userRepository.save(user);
    }

    public Page<CompletedQuiz> getAllUsersCompletedQuizzes(int pageNo) {
        System.out.println("Getting the logged in users completed quizzes...");
        return completedQuizRepository.findCompletedQuizBySolvedBy(getCurrentUser().getEmail(),
                PageRequest.of(pageNo, 10, Sort.by("completedAt").descending()));
    }

    public void addCompletedQuiz(Quiz quiz) {
        System.out.println("Adding new quiz to the database...");
        CompletedQuiz completedQuiz = new CompletedQuiz(quiz, LocalDateTime.now(), getCurrentUser());
        completedQuizRepository.save(completedQuiz);
    }

}
