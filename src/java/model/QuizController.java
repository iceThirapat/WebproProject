/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;



/**
 *
 * @author ICE
 */
public class QuizController {
    //userNo
    String subjectNo;
    int pageNo;
    String[] score;

    public QuizController(String subjectNo, int quantityQuestion) {
        this.subjectNo = subjectNo;
        this.pageNo = 0;
        this.score = new String[quantityQuestion];
    }

    public String getSubjectNo() {
        return subjectNo;
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
        this.score[this.pageNo-1] = answerNo;
    }
    
    public void deleteAnswer(){
        this.score[this.pageNo-1] = null;
    }
    
//    public int getOverallScore(){
//        //not support yet.
//    }

    
    
}
