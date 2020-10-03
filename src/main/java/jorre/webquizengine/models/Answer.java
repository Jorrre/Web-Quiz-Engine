package jorre.webquizengine.models;

import java.util.ArrayList;
import java.util.List;

public class Answer {

    private List<Integer> answer = new ArrayList<>();

    public Answer(){}

    public Answer(List<Integer> answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer=" + answer +
                '}';
    }

    public List<Integer> getAnswer() {
        return answer;
    }

}
