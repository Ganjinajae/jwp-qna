## QnA 서비스

* QnA 서비스를 구현하면서 JPA 로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑하는지 파악한다

### 기능 요구 사항

* 질문 데이터를 완전히 삭제하는 것이 아닌 삭제 상태로 변경해야 한다
* 다음과 같은 상황일 때만 삭제가 가능하다
    * 로그인 사용자와 질문한 사람이 같은 경우
    * 답변이 없는 경우
    * 질문자와 답변 글의 모든 답변자가 같은 경우
* 다음과 같은 상황일 때 삭제가 불가능하다
    * 질문자와 답변자가 다른 경우
* 질문을 삭제할 때 답변 모두 삭제 상태가 되어야 한다
* 질문과 답변 삭제 이력에 대한 정보를 `DeleteHistory` 를 활용해 남긴다

### 프로그래밍 요구사항 

* [자바 코드 컨벤션](https://google.github.io/styleguide/javaguide.html)을 지키면서 프로그래밍을 진행한다
* indent depth 가 2 를 넘지 않도록 구현해야 한다
* 3 항 연산자, `else`, `switch` 를 사용하면 안된다
* TDD 로 구현하여 단위 테스트가 존재해야 한다
* 메소드의 길이가 10 라인을 넘기지 않아야 한다
* 일급 컬렉션과 모든 원시 값과 문자열을 포장해야 한다
* 모든 엔티티를 작게 유지해야 한다
* 3개 이상의 인스턴스 변수를 가진 클래스를 사용하지 않는다
* 축약을 하면 안된다

### DDL

```sql
create table answer
(
    id          bigint generated by default as identity,
    contents    clob,
    created_at  timestamp not null,
    deleted     boolean   not null,
    question_id bigint,
    updated_at  timestamp,
    writer_id   bigint,
    primary key (id)
)

create table delete_history
(
    id            bigint generated by default as identity,
    content_id    bigint,
    content_type  varchar(255),
    create_date   timestamp,
    deleted_by_id bigint,
    primary key (id)
)

create table question
(
    id         bigint generated by default as identity,
    contents   clob,
    created_at timestamp    not null,
    deleted    boolean      not null,
    title      varchar(100) not null,
    updated_at timestamp,
    writer_id  bigint,
    primary key (id)
)

create table user
(
    id         bigint generated by default as identity,
    created_at timestamp   not null,
    email      varchar(50),
    name       varchar(20) not null,
    password   varchar(20) not null,
    updated_at timestamp,
    user_id    varchar(20) not null,
    primary key (id)
)

alter table user
    add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)

alter table answer
    add constraint fk_answer_to_question
        foreign key (question_id)
            references question

alter table answer
    add constraint fk_answer_writer
        foreign key (writer_id)
            references user

alter table delete_history
    add constraint fk_delete_history_to_user
        foreign key (deleted_by_id)
            references user

alter table question
    add constraint fk_question_writer
        foreign key (writer_id)
            references user
```
