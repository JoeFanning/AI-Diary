package com.example.joesp.aidiary;

/**
 * News class for database
 * Created by joesp on 21/11/2017.
 */

public class News {

    int id;
    int date;
    private String displayDate;
    private String articleTitle1;
    private String articleTitle2;
    private String articleTitle3;
    private String articleTitle4;
    private String articleTitle5;
    private String articleDescription1;
    private String articleDescription2;
    private String articleDescription3;
    private String articleDescription4;
    private String articleDescription5;
    private String articleImage1;
    private String articleImage2;
    private String articleImage3;
    private String articleImage4;
    private String articleImage5;

    public News() {
    }

    public News(int id, int date, String displayDate, String articleTitle1, String articleTitle2, String articleTitle3,
                String articleTitle4, String articleTitle5, String articleDescription1, String articleDescription2,
                String articleDescription3, String articleDescription4, String articleDescription5, String articleImage1,
                String articleImage2, String articleImage3, String articleImage4, String articleImage5) {

        this.id = id;
        this.date = date;
        this.displayDate = displayDate;
        this.articleTitle1 = articleTitle1;
        this.articleTitle2 = articleTitle2;
        this.articleTitle3 = articleTitle3;
        this.articleTitle4 = articleTitle4;
        this.articleTitle5 = articleTitle5;
        this.articleDescription1 = articleDescription1;
        this.articleDescription2 = articleDescription2;
        this.articleDescription3 = articleDescription3;
        this.articleDescription4 = articleDescription4;
        this.articleDescription5 = articleDescription5;
        this.articleImage1 = articleImage1;
        this.articleImage2 = articleImage2;
        this.articleImage3 = articleImage3;
        this.articleImage4 = articleImage4;
        this.articleImage5 = articleImage5;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", date=" + date +
                ", displayDate='" + displayDate + '\'' +
                ", articleTitle1='" + articleTitle1 + '\'' +
                ", articleTitle2='" + articleTitle2 + '\'' +
                ", articleTitle3='" + articleTitle3 + '\'' +
                ", articleTitle4='" + articleTitle4 + '\'' +
                ", articleTitle5='" + articleTitle5 + '\'' +
                ", articleDescription1='" + articleDescription1 + '\'' +
                ", articleDescription2='" + articleDescription2 + '\'' +
                ", articleDescription3='" + articleDescription3 + '\'' +
                ", articleDescription4='" + articleDescription4 + '\'' +
                ", articleDescription5='" + articleDescription5 + '\'' +
                ", articleImage1='" + articleImage1 + '\'' +
                ", articleImage2='" + articleImage2 + '\'' +
                ", articleImage3='" + articleImage3 + '\'' +
                ", articleImage4='" + articleImage4 + '\'' +
                ", articleImage5='" + articleImage5 + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    String getArticleTitle1() {
        return articleTitle1;
    }

    void setArticleTitle1(String articleTitle1) {
        this.articleTitle1 = articleTitle1;
    }

    String getArticleTitle2() {
        return articleTitle2;
    }

    void setArticleTitle2(String articleTitle2) {
        this.articleTitle2 = articleTitle2;
    }

    String getArticleTitle3() {
        return articleTitle3;
    }

    void setArticleTitle3(String articleTitle3) {
        this.articleTitle3 = articleTitle3;
    }

    String getArticleTitle4() {
        return articleTitle4;
    }

    void setArticleTitle4(String articleTitle4) {
        this.articleTitle4 = articleTitle4;
    }

    String getArticleTitle5() {
        return articleTitle5;
    }

    void setArticleTitle5(String articleTitle5) {
        this.articleTitle5 = articleTitle5;
    }

    String getArticleDescription1() {
        return articleDescription1;
    }

    void setArticleDescription1(String articleDescription1) {
        this.articleDescription1 = articleDescription1;
    }

    String getArticleDescription2() {
        return articleDescription2;
    }

    void setArticleDescription2(String articleDescription2) {
        this.articleDescription2 = articleDescription2;
    }

    String getArticleDescription3() {
        return articleDescription3;
    }

    void setArticleDescription3(String articleDescription3) {
        this.articleDescription3 = articleDescription3;
    }

    String getArticleDescription4() {
        return articleDescription4;
    }

    void setArticleDescription4(String articleDescription4) {
        this.articleDescription4 = articleDescription4;
    }

    String getArticleDescription5() {
        return articleDescription5;
    }

    void setArticleDescription5(String articleDescription5) {
        this.articleDescription5 = articleDescription5;
    }

    String getArticleImage1() {
        return articleImage1;
    }

    void setArticleImage1(String articleImage1) {
        this.articleImage1 = articleImage1;
    }

    String getArticleImage2() {
        return articleImage2;
    }

    void setArticleImage2(String articleImage2) {
        this.articleImage2 = articleImage2;
    }

    String getArticleImage3() {
        return articleImage3;
    }

    void setArticleImage3(String articleImage3) {
        this.articleImage3 = articleImage3;
    }

    String getArticleImage4() {
        return articleImage4;
    }

    void setArticleImage4(String articleImage4) {
        this.articleImage4 = articleImage4;
    }

    String getArticleImage5() {
        return articleImage5;
    }

    void setArticleImage5(String articleImage5) {
        this.articleImage5 = articleImage5;
    }

}
