package edu.Ciesla;

import edu.Ciesla.models.Answer;
import edu.Ciesla.models.Survey;
import edu.Ciesla.models.User;
import edu.Ciesla.services.AnswerService;
import edu.Ciesla.services.NotFounException;
import edu.Ciesla.services.SurveyService;
import edu.Ciesla.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

@RestController
public class MappController {
    private ObjectMapper objectMapper;

    private UserService   userService;
    private AnswerService answerService;
    private SurveyService surveyService;
    MappController(){
        objectMapper = new ObjectMapper();
        userService = new UserService();
        answerService = new AnswerService();
        surveyService = new SurveyService();
    }
// 1
    @RequestMapping(value = "/api/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestParam String name){
        return new ResponseEntity<>(userService.addUser(name), HttpStatus.ACCEPTED);
    }
// 2
    @RequestMapping(value = "/api/survey", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createsurvey(@RequestParam int userID, @RequestParam String title,@RequestParam String question){

        try {
            User author = this.userService.getUser(userID);
            Survey returnVale = surveyService.addSurvey(new Survey(author, title, question));
            ObjectWriter w = objectMapper.writer();
            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.ACCEPTED);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFounException e) {
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.NOT_FOUND);
        }
        catch (NullPointerException e){
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.OK);
        }
    }
// 3
    @RequestMapping(value = "/api/survey/user/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSurveysOfUser(@PathVariable int userID){
        try{
            User author = this.userService.getUser(userID);
            ArrayList<Survey> returnVale= surveyService.getSurveysOf(author);
            ObjectWriter w = objectMapper.writer();
            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.ACCEPTED);
        }
        catch(NotFounException e){
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.NOT_FOUND);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
// 4
    @RequestMapping(value = "/api/survey/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllSurveys(){
        ArrayList<Survey> returnVale= surveyService.getAllSurveys();
        ObjectWriter w = objectMapper.writer();
        try {
            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.ACCEPTED);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
// 5
    @RequestMapping(value = "/api/survey/{ID}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteSurvey(@PathVariable int ID){
        try{
            Survey s = surveyService.getSurvey(ID);
            answerService.removeAnswersOf(s);
            surveyService.removeSurvey(s);
            return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
        }
        catch (NoSuchFieldError | NotFounException e){
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.NOT_FOUND);
        }
    }
// 6
    @RequestMapping(value = "/api/answer/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createAnswer(@RequestParam int userID, @RequestParam int surveyID, @RequestParam int mark){
        try {
            User author = this.userService.getUser(userID);
            Survey question = this.surveyService.getSurvey(surveyID);
            Answer returnVale = answerService.putAnswer(new Answer(author, question, mark));
            ObjectWriter w = objectMapper.writer();
            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.ACCEPTED);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFounException e) {
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.NOT_FOUND);
        }
        catch (IndexOutOfBoundsException e){
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.OK);
        }
    }
// 7
    @RequestMapping(value = "/api/answer/{surveyID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAnswers(@PathVariable int surveyID){
        try {
            Survey target = surveyService.getSurvey(surveyID);
            ArrayList<Answer> returnVale= answerService.getAnswersOf(target);
            ObjectWriter w = objectMapper.writer();
            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.ACCEPTED);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFounException e) {
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.NOT_FOUND);
        }
    }
// 8
    @RequestMapping(value = "/api/answer/{surveyID}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> modifyAnswersOf(@PathVariable int surveyID,@RequestParam int mark){
        try {
            Survey target = surveyService.getSurvey(surveyID);
            ArrayList<Answer> returnVale = answerService.setAnswers(target, mark);
            ObjectWriter w = objectMapper.writer();
            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.ACCEPTED);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFounException e) {
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.NOT_FOUND);
        }catch (IndexOutOfBoundsException e){
            return new ResponseEntity<>(e.getMessage()+"", HttpStatus.OK);
        }
    }
// 9
    @RequestMapping(value = "/api/stats/user/{userID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getUserStats(@PathVariable int userID) {
        try {
            TreeMap<String, Object> returnVale = new TreeMap<>();
            User target = userService.getUser(userID);
            returnVale.put("Stats fo user:", target);
            returnVale.put("number of surveys created:", surveyService.getNumberOfSurveys(target));
            returnVale.put("total number of answers created:", answerService.getNumberOfAnswers(target));
            returnVale.put("total number of answers created for specific survey (survey_id -> number of answers):", answerService.getStatsOf(target));
            returnVale.put("mean of ratio for answers at specific surveys (survey_id -> mean):", answerService.getMeanOf(target));
            ObjectWriter w = objectMapper.writer();
            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.ACCEPTED);
        }catch (NotFounException e) {
            return new ResponseEntity<>(e.getMessage() + "", HttpStatus.NOT_FOUND);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//10
    @RequestMapping(value = "/api/stats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSysStats() {
        try {
            ObjectWriter w = objectMapper.writer();
            HashMap<String, Object> returnVale = new HashMap<>();
            returnVale.put("Users - created surveys", surveyService.getAuthorRanking());
            returnVale.put("Surveys - typed answers", answerService.getAnswersRanking());
            returnVale.put("Surveys per user", (double)surveyService.getNumberOfSurveys(null)/(double)userService.getNumberofUsers());
            returnVale.put("Answers per survey", answerService.getNumberOfAnswers(null)/(double)surveyService.getNumberOfSurveys(null));
            returnVale.put("Number of Surveys: " , surveyService.getNumberOfSurveys(null));

            return new ResponseEntity<>(w.writeValueAsString(returnVale), HttpStatus.OK);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("JsonProcessingException has appeared.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}














































