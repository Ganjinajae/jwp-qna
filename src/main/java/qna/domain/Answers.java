package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser, now));
        }
        return deleteHistories;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }
}
