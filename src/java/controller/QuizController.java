/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import model.Question;
import model.Subjects;



/**
 *
 * @author ICE
 * @param <T>
 */
public class QuizController {
    int userNo;
    Subjects subject;
    int pageNo;
    String[] answerNo;
    HashMap<Integer,Question> allQuestion = new HashMap();
 
    public QuizController(int userNo, Subjects subject,int quantityQuestion) {
        this.userNo = userNo;
        this.subject = subject;
        answerNo = new String[quantityQuestion];
        this.pageNo = 1;
    }

    public void setSubject(Subjects subject) {
        this.subject = subject;
    }

    public Subjects getSubject() {
        return subject;
    }

    public int goNextPage() {
        this.pageNo++;
        return this.pageNo;
    }

    public int goPreviosPage() {
        this.pageNo--;
        return this.pageNo;
    }

    public int goSpecificPage(int specificPage){
        this.pageNo = specificPage;
        return this.pageNo;
    }

    public void addAnswer(String answerNo){
        this.answerNo[pageNo-1] = answerNo;
    }
    
    public void deleteAnswer(){
        this.answerNo[pageNo-1] = null;
    }

    public HashMap<Integer, Question> getAllQuestion() {
        return allQuestion;
    }

    public int getPageNo() {
        return pageNo;
    }

    public String getAnswerNo(int index) {
        return answerNo[index];
    }

    public String[] getAllAnswer() {
        return answerNo;
    }

    public int getUserNo() {
        return userNo;
    }
    
    
    
    
    
    
    




    
    
}
