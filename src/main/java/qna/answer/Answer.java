package qna.answer;

import qna.CannotDeleteException;
import qna.action.NullCheckAction;
import qna.domain.DateTimeEntity;
import qna.question.Question;
import qna.user.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer extends DateTimeEntity implements NullCheckAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User user;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "delete", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    public Answer(final User user, final Question question, final String contents) {
        this(null, user, question, contents);
    }

    public Answer(final Long id, final User user, final Question question, final String contents) {
        throwExceptionIsNullObject(user);
        throwExceptionIsNullObject(question);
        this.id = id;
        this.user = user;
        this.question = question;
        this.contents = contents;

        question.addAnswer(this);
    }

    protected Answer() {
    }

    public void throwExceptionNotDeletableUser(final User loginUser) throws CannotDeleteException {
        if (!this.user.equals(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public User getUser() {
        return user;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void deleteAnswer() {
        this.deleted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return deleted == answer.deleted
                && Objects.equals(id, answer.id)
                && Objects.equals(user, answer.user)
                && Objects.equals(contents, answer.contents)
                && Objects.equals(question, answer.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, contents, deleted, question);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + getId() +
                ", user=" + user +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}