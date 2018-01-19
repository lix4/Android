package edu.rosehulman.lix4.finalexamlix4;

import com.google.firebase.database.Exclude;

import java.util.Map;
import java.util.Objects;

/**
 * Created by boutell on 8/14/17.
 */
public class Riddle {
    public String key;
    public String question;
    public String answer;
    public String extra; // option for Dr. B to use
    public long numLikes;
    public String owner;
    public Map<String, Boolean> recipients;

    public Riddle() {

    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(long numLikes) {
        this.numLikes = numLikes;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Map<String, Boolean> getRecipients() {
        return recipients;
    }

    public void setRecipients(Map<String, Boolean> recipients) {
        this.recipients = recipients;
    }

    public void setValues(Riddle riddle) {
        setOwner(riddle.getOwner());
        setAnswer(riddle.getAnswer());
        setQuestion(riddle.getQuestion());
        setRecipients(riddle.getRecipients());
        setNumLikes(riddle.getNumLikes());
        setExtra(riddle.getExtra());
    }

    public String processRecipients() {
        Object[] currentRecipients = recipients.keySet().toArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < currentRecipients.length; i++) {
            builder.append(currentRecipients[i]);
            if (i != currentRecipients.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
