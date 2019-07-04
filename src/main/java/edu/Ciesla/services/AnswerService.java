package edu.Ciesla.services;

import edu.Ciesla.models.Answer;
import edu.Ciesla.models.Survey;
import edu.Ciesla.models.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AnswerService {
    public Answer putAnswer(Answer a){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try{
            tx = session.beginTransaction();
            session.save(a);
            tx.commit();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }finally {
            session.close();
        }
        return a;
    }
    public ArrayList<Answer> getAnswersOf(Survey s){
        String sql = "SELECT * FROM `answers` WHERE survey=" + s.getSurvey_id();
        return this.getCommands(sql);
    }
    public ArrayList<Answer> setAnswers(Survey target, int mark)throws IndexOutOfBoundsException{
        String sql = "UPDATE `answers` SET rating = "+mark+" WHERE survey ="+target.getSurvey_id();
        this.commands(sql);
        sql = "SELECT * FROM `answers` WHERE survey=" + target.getSurvey_id();
        return this.getCommands(sql);
    }
    public void removeAnswersOf(Survey target)throws IndexOutOfBoundsException{
        String sql= "DELETE FROM `answers` WHERE `survey` =" +target.getSurvey_id();
        this.commands(sql);
    }
    public HashMap<String, String> getMeanOf(User user){
        String sql = "SELECT answers.survey, AVG(rating) as 'mean' FROM `answers` where answers.user= "+user.getUser_id()+" GROUP BY answers.survey";
        HashMap<String, String> returnVale = new HashMap<>();
        ArrayList l;
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.addScalar("survey");
            query.addScalar("mean");
            tx.commit();
            l =(ArrayList) query.getResultList();
            for(Object o: l){
                Object[] row =(Object[]) o;
                returnVale.put(row[0].toString(), row[1].toString());
            }
            System.err.println("");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
        return returnVale;
    }
    public ArrayList<Answer> getAnswers(){
        return this.getCommands("SELECT * FROM `answers`");
    }
    public int getNumberOfAnswers(User author){
        String sql = "SELECT COUNT(answers.answer_id) as 'answer_id' FROM `answers`";
        if(author!=null){
            sql = "SELECT COUNT(answer_id) as 'answer_id' FROM `answers` where answers.user= "+author.getUser_id()+" GROUP BY answers.user";
        }
        Transaction tx;
        int returnVale=-1;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            tx.commit();
            returnVale = ((BigInteger) query.list().get(0)).intValue();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
            return returnVale;
        }
    }
    public HashMap<String, String> getStatsOf(User user){
        String sql = "SELECT answers.survey, COUNT(answer_id) as 'answer_number' FROM `answers` where answers.user= "+user.getUser_id()+" GROUP BY answers.survey";
        HashMap<String, String> returnVale = new HashMap<>();
        ArrayList l;
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            //query.addEntity(Answer.class);
            query.addScalar("survey");
            query.addScalar("answer_number");

            tx.commit();

            l =(ArrayList) query.getResultList();
            for(Object o: l){
                Object[] row =(Object[]) o;
                returnVale.put(row[0].toString(), row[1].toString());
            }
            System.err.println("");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }

        return returnVale;
    }
    public HashMap<String, String> getAnswersRanking(){
        String sql = "SELECT survey, COUNT(answer_id)  as 'answers' FROM `answers`GROUP BY answers.survey";
        Transaction tx;
        HashMap<String, String> returnVale = new HashMap<>();
        Session session = HibernateConfig.getSessionFactory().openSession();
        List l;
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.addScalar("survey");
            query.addScalar("answers");
            tx.commit();
            l =query.getResultList();
            for(Object o: l){
                Object[] row =(Object[]) o;
                returnVale.put(row[0].toString(), row[1].toString());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
        return returnVale;
    }



    private ArrayList<Answer> getCommands(String sql){
        Transaction tx;
        ArrayList<Answer> returnVale = new ArrayList<>();
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(Answer.class);
            returnVale.addAll(query.list());
            tx.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
        return returnVale;
    }
    private void commands(String sql){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
    }
}
