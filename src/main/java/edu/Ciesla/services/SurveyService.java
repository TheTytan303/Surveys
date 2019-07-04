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
import java.util.Optional;

@Service
public class SurveyService {


    public Survey addSurvey(Survey s){
        Transaction tx;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try{
            tx = session.beginTransaction();
            session.save(s);
            tx.commit();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            session.close();
        }
        return s;
    }
    public Survey getSurvey(int ID) throws NotFounException{
        ArrayList<Survey> returnVale = this.getCommands("SELECT * FROM `surveys` WHERE survey_id=" + ID);
        if(returnVale.size()==0){
            throw new NotFounException("Survey with that ID does not exists");
        }
        return returnVale.get(0);
    }
    public ArrayList<Survey> getSurveysOf(User author){
        return this.getCommands("SELECT * FROM `surveys` WHERE user=" + author.getUser_id());
    }
    public ArrayList<Survey> getAllSurveys(){
        return this.getCommands("SELECT * FROM `surveys`");
    }
    public void removeSurvey(Survey s)throws NoSuchFieldError{
        String sql= "DELETE FROM `surveys` WHERE `survey_id`=" +s.getSurvey_id();
        this.commands(sql);
    }
    public int getNumberOfSurveys(User author){
        String sql;
        if(author==null){
            sql = "SELECT COUNT( s.survey_id) as 'survey_id' FROM surveys s";
        }
        else{
            sql = "SELECT COUNT(s.survey_id) as 'survey_id'  FROM surveys s where s.user= "+author.getUser_id()+" GROUP BY s.user";
        }
        Transaction tx;
        int returnVale=-1;
        Session session = HibernateConfig.getSessionFactory().openSession();
        List l;
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            //query.addEntity(Survey.class);
            query.addScalar("survey_id");
            tx.commit();
            l = query.list();
            returnVale = ((BigInteger)l.get(0)).intValue();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
            return returnVale;
        }
    }
    public HashMap<String, String> getAuthorRanking(){
        String sql = "SELECT user, COUNT(survey_id) as 'surveys' FROM `surveys`GROUP BY user";
        Transaction tx;
        HashMap<String, String> returnVale = new HashMap<>();
        Session session = HibernateConfig.getSessionFactory().openSession();
        List l;
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            //query.addEntity(Survey.class);
            query.addScalar("user");
            query.addScalar("surveys");
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




    private ArrayList<Survey> getCommands(String sql){
        Transaction tx;
        ArrayList<Survey> returnVale = new ArrayList<>();
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(Survey.class);
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
