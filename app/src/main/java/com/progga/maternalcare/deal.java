package com.progga.maternalcare;



public class deal {
    private  String question,key,answers,uid,querymark;

    public deal() {
    }

    public deal(String question, String key, String answers, String uid, String querymark) {
        this.question = question;
        this.key = key;
        this.answers = answers;
        this.uid = uid;
        this.querymark = querymark;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuerymark() {
        return querymark;
    }

    public void setQuerymark(String querymark) {
        this.querymark = querymark;
    }
}

