package edu.Ciesla.services;

import edu.Ciesla.models.User;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public User addUser(String name){
       // String SQL = "INSERT INTO `users` (`user_id`, `nick`) VALUES (NULL, '"+name+"');";
        User returnvale = new User(name);
        Session session = HibernateConfig.getSessionFactory().openSession();
        Transaction tx;
        try{
            tx = session.beginTransaction();
            session.save(returnvale);
            tx.commit();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }finally {
            session.close();
        }
        return returnvale;
    }
    public User getUser(int ID) throws NotFounException {
        String sql = "SELECT * FROM `users` WHERE user_id=" + ID;
        ArrayList<User> result = this.getCommands(sql);
        if (result.size() == 0) {
            throw new NotFounException("user: '" + ID + "' does not exists in DataBase");
        } else {
            return result.get(0);
        }
    }
    public ArrayList<User> getUsers() throws NotFounException {
        String sql = "SELECT * FROM `users` ";
        ArrayList<User> returnVale = this.getCommands(sql);

        if (returnVale.size() == 0) {
            throw new NotFounException("Any user does not exists in DataBase");
        } else {
            return returnVale;
        }
    }
    public int getNumberofUsers(){
        String sql = "SELECT COUNT(user_id) FROM `users`";
        Transaction tx;
        int returnVale=-1;
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.addScalar("COUNT(user_id)");
            tx.commit();
            List l = query.list();
            returnVale = ((BigInteger)l.get(0)).intValue();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
            return returnVale;
        }
    }


    //public HashMap<String, String> getStatsOf(User target){
    //
    //    return null;
    //}



    private ArrayList<User> getCommands(String sql){

        Transaction tx;
        ArrayList<User> returnVale = new ArrayList<>();
        Session session = HibernateConfig.getSessionFactory().openSession();
        try {
            tx = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(sql);
            query.addEntity(User.class);
            returnVale.addAll(query.list());
            tx.commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            session.close();
        }
        return returnVale;
    }
}

