package com.js.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.js.Utility.AES;
import com.js.Utility.OTPservice;
import com.js.dao.ChatDao;
import com.js.dao.Dao;
import com.js.dao.PersonalDetailsDao;
import com.js.entity.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@org.springframework.stereotype.Controller
@CrossOrigin
public class Controller implements ErrorController {

    @Autowired
    private Dao dao;

    @Autowired
    private PersonalDetailsDao personalDetailsDao;

    @Autowired
    private ChatDao chatDao;

    final private String key = "123456789qwertya";
    final private int interval=(60*10);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send/message")
    public String useSocketCommunication(String message) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> messageConverted;
        ChatDetails chatDetails = new ChatDetails();

        try {
            messageConverted = mapper.readValue(message, Map.class);
        } catch (IOException e) {
            messageConverted = null;
        }

        if(messageConverted!=null){
            if(messageConverted.containsKey("toId") && messageConverted.get("toId")!=null && !messageConverted.get("toId").equals("")){
                this.simpMessagingTemplate.convertAndSend("/chat/"+messageConverted.get("toId"),messageConverted);
                this.simpMessagingTemplate.convertAndSend("/chat/"+messageConverted.get("fromId"),message);
                chatDetails.setFromId(messageConverted.get("fromId"));
                chatDetails.setToId(messageConverted.get("toId"));
                chatDetails.setMsg(messageConverted.get("message"));
                chatDetails.setTime(new Date());
                chatDao.save(chatDetails);
            }else{
                this.simpMessagingTemplate.convertAndSend("/chat",messageConverted);
            }
        }
        return mapper.writeValueAsString(messageConverted);
    }

    @RequestMapping(value = "/getChat", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String getChat(@RequestParam String username) throws JsonProcessingException {
        List<ChatDetails> chatDetailsFrom = chatDao.findAllByFromId(username);
        List<ChatDetails> chatDetailsTo = chatDao.findAllByToId(username);
        List<Map<String, String>> response = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        if (chatDetailsFrom!=null){
            for (ChatDetails details : chatDetailsFrom){
                Map<String, String> map = new HashMap<>();
                map.put("fromId", details.getFromId());
                map.put("toId", details.getToId());
                map.put("message", details.getMsg());
                map.put("time", String.valueOf(details.getTime()));
                response.add(map);
            }
        }

        if (chatDetailsTo!=null){
            for (ChatDetails details : chatDetailsTo){
                Map<String, String> map = new HashMap<>();
                map.put("fromId", details.getFromId());
                map.put("toId", details.getToId());
                map.put("message", details.getMsg());
                map.put("time", String.valueOf(details.getTime()));
                response.add(map);
            }
        }

        Collections.sort(response, new Comparator<Map<String, String>>() {
            public int compare(final Map<String, String> o1, final Map<String, String> o2) {
                return o1.get("time").compareTo(o2.get("time"));
            }
        });

        result.put("Chat", response);
        return mapper.writeValueAsString(result);
    }

    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity sms(@RequestParam String toEmail) throws JsonProcessingException {

        //final String user = "greatshubhamsrivastava@gmail.com";
        final String user = "shubhamsrivastavawebsite@gmail.com";
        //final String pass = "jxlbgrhgjtkkstxz";
        final String pass = "iywtstducedvytiw";
        String otpValue;

        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        Properties prop= new Properties();
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.port","587");
        prop.put("mail.debug", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.fallback", false);

        Session session = Session.getInstance(prop, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(user,pass);
            }
        });
        try
        {
            OTPservice otp = new OTPservice();
            otpValue = otp.generateOTP(interval, key, toEmail);

            if (otpValue!=null) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("shubhamsrivastavawebsite@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setHeader("name", "Shubham Srivastava");
                message.setSubject("OTP to verify email id.");
                message.setContent("<html><head></head><body>Hello,<br/><br/>Hope you are doing fine.<br/><br/>Please enter the following otp to verify your email id: <br /><b>" + otpValue + "</b><br /><br /> Thank you!<br/><br/>Regards,<br/>Shubham Srivastava<br/>Contact: +91-9918036800<br/>Email Id: shubham.srstv@gmail.com </body></html>", "text/html");

                /*MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(msg);
                Multipart multipart=new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart=new MimeBodyPart();
                javax.activation.DataSource source =new FileDataSource(attachment_path);
                messageBodyPart.setDataHandler(new DataHandler(source));
                // messageBodyPart.setFileName(name_attach.getText());//not able to set file name
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);*/

                Transport.send(message);
                response.put("status", "Success");
                return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
            }
            else {
                return new ResponseEntity(ExceptionHandling("101", "Error in otp generation"), HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception e)
        {
            return new ResponseEntity(ExceptionHandling("101", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/forget", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> forget(@RequestParam String emailId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if(dao.findByEMail(emailId)!=null){
            ResponseEntity smsResult = sms(emailId);
            if (smsResult.getStatusCodeValue() == 400){
                JSONObject object = new ObjectMapper().readValue(smsResult.getBody().toString(), JSONObject.class);
                return new ResponseEntity(ExceptionHandling("101", object.getString("message")), HttpStatus.BAD_REQUEST);
            }
            else{
                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity(ExceptionHandling("101", "Email Id is incorrect"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> Registration(@RequestParam String name, String email, String gender, String username, String password, long contactNumber) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        UserDetails details = new UserDetails();
        UserDetails result;
        AES aes = new AES();

        if (name.isEmpty() || email.isEmpty() || gender.isEmpty() || username.isEmpty() || password.isEmpty() || contactNumber==0){
            return new ResponseEntity(ExceptionHandling("101", "Value cannot be blank"), HttpStatus.BAD_REQUEST);
        }

        result = dao.findByEMail(email);
        if (result!=null){
            return new ResponseEntity(ExceptionHandling("101", "Email Id already registered with "+result.getUserName()), HttpStatus.BAD_REQUEST);
        }
        result = null;

        try {
            details.setName(name);
            details.seteMail(email);
            details.setGender(gender);
            details.setUserName(username);
            details.setPassword(aes.encrypt(password));
            details.setContactNumber(contactNumber);
            if (username.equals("shubham_srivastava") && email.equals("shubham.srstv@gmail.com")){
                details.setRole("Admin");
            }
            if (!dao.findById(username).isPresent()) {
                ResponseEntity smsResult = sms(email);
                if (smsResult.getStatusCodeValue() == 400){
                    JSONObject object = new ObjectMapper().readValue(smsResult.getBody().toString(), JSONObject.class);
                    return new ResponseEntity(ExceptionHandling("101", object.getString("message")), HttpStatus.BAD_REQUEST);
                }
                else {
                    result = dao.save(details);
                }
            }
            if (result!=null) {
                response.put("username", result.getUserName());
                response.put("name", result.getName());
                return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
            }
            else{
                return new ResponseEntity(ExceptionHandling("101", "Username already exist"), HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception ex){
            return new ResponseEntity(ExceptionHandling("101", "Exception happened"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public void deleteUser(@RequestParam String username){
        dao.deleteById(username);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> login(@RequestParam String username, String password) throws JsonProcessingException {

        AES aes = new AES();

        if (username.isEmpty() || password.isEmpty()){
            return new ResponseEntity(ExceptionHandling("101", "Value cannot be blank"), HttpStatus.BAD_REQUEST);
        }

        if (dao.existsById(username)){
            UserDetails details = dao.findById(username).get();
            if (details.getPassword().equals(aes.encrypt(password))){
                if (details.getIsVerified().equals("N")){
                    return new ResponseEntity(ExceptionHandling("101", "Please verify your email."), HttpStatus.BAD_REQUEST);
                }
                else{
                    Map<String, Object> response = new HashMap<>();
                    ObjectMapper mapper = new ObjectMapper();
                    response.put("username", details.getUserName());
                    response.put("name", details.getName());
                    response.put("contactNumber", details.getContactNumber());
                    response.put("email", details.geteMail());
                    response.put("role", details.getRole());
                    return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
                }
            }
            else {
                return new ResponseEntity(ExceptionHandling("101", "Invalid password"), HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity(ExceptionHandling("101", "Invalid username"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> Verify(@RequestParam String otpResponse, String email) throws JsonProcessingException {
        UserDetails details = new UserDetails();
        ObjectMapper mapper = new ObjectMapper();
        String otpValue=null;

        details = dao.findByEMail(email);
        if (details!=null){
            OTPservice otp = new OTPservice();

            otpValue = otp.generateOTP(interval, key, email);
            if (otpValue.equals(otpResponse)){
                details.setIsVerified("Y");
                dao.save(details);
                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
            }
            else {
                return new ResponseEntity(ExceptionHandling("101", "Invalid Otp"), HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity(ExceptionHandling("101", "Invalid email"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/forgetverify", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> forgetverify(@RequestParam String otpResponse, String email, String password) throws JsonProcessingException {
        UserDetails details = new UserDetails();
        ObjectMapper mapper = new ObjectMapper();
        String otpValue=null;
        AES aes = new AES();

        if (otpResponse.isEmpty() || password.isEmpty()){
            return new ResponseEntity(ExceptionHandling("101", "Value cannot be blank"), HttpStatus.BAD_REQUEST);
        }

        details = dao.findByEMail(email);
        if (details!=null){
            OTPservice otp = new OTPservice();

            otpValue = otp.generateOTP(interval, key, email);
            if (otpValue.equals(otpResponse)){
                details.setPassword(aes.encrypt(password));
                details.setIsVerified("Y");
                dao.save(details);
                Map<String, String> response = new HashMap<>();
                response.put("status", "Success");
                return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
            }
            else {
                return new ResponseEntity(ExceptionHandling("101", "Invalid Otp"), HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity(ExceptionHandling("101", "Invalid email"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/addDetails", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> addDetails(@RequestParam String role, @RequestParam String completeDetails, MultipartFile resumeFile) throws IOException, InvocationTargetException, IllegalAccessException {

        ObjectMapper mapper = new ObjectMapper();
        if (role.equalsIgnoreCase("Admin")){
            JSONObject jsonObject = new ObjectMapper().readValue(completeDetails, JSONObject.class);
            PersonalDetails completeDetailJsonObj = new Gson().fromJson(mapper.writeValueAsString(jsonObject), PersonalDetails.class);

            JSONArray educationDetailsJsonArray = jsonObject.getJSONArray("educations");
            JSONArray technicalSkillsJsonArray = jsonObject.getJSONArray("technicalSkills");
            JSONArray workDetailsJsonArray = jsonObject.getJSONArray("workDetails");

            List<Education> educationList = new ArrayList<>();
            for (Object obj : educationDetailsJsonArray){
                JSONObject object = (JSONObject) obj;
                Education beanEducation = new ObjectMapper().readValue(mapper.writeValueAsString(object), Education.class);
                beanEducation.setPersonalDetailsWithEducation(completeDetailJsonObj);
                educationList.add(beanEducation);
            }
            completeDetailJsonObj.setEducations(educationList);

            List<TechnicalSkills> skillsList = new ArrayList<>();
            for (Object obj : technicalSkillsJsonArray){
                JSONObject object = (JSONObject) obj;
                TechnicalSkills beanTechnical = new ObjectMapper().readValue(mapper.writeValueAsString(object), TechnicalSkills.class);
                beanTechnical.setPersonalDetailsWithSkills(completeDetailJsonObj);
                skillsList.add(beanTechnical);
            }
            completeDetailJsonObj.setTechnicalSkills(skillsList);

            List<WorkDetails> workDetailsList = new ArrayList<>();
            for (Object obj : workDetailsJsonArray){
                JSONObject object = (JSONObject) obj;
                WorkDetails beanWork = new ObjectMapper().readValue(mapper.writeValueAsString(object), WorkDetails.class);
                beanWork.setPersonalDetailsWithWork(completeDetailJsonObj);
                workDetailsList.add(beanWork);
            }
            completeDetailJsonObj.setWorkDetails(workDetailsList);
            if (resumeFile!=null) {
                completeDetailJsonObj.setResumeFile(resumeFile.getBytes());
            }

            if (personalDetailsDao.existsById(completeDetailJsonObj.getUsername())){
                personalDetailsDao.deleteById(completeDetailJsonObj.getUsername());
            }
            personalDetailsDao.save(completeDetailJsonObj);

            Map<String, String> response = new HashMap<>();
            response.put("status", "Success");
            return new ResponseEntity(mapper.writeValueAsString(response), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(ExceptionHandling("101", "You are not Admin"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/downloadpdf", method = RequestMethod.POST, produces = "application/pdf")
    @ResponseBody
    public byte[] downloadpdf(){
        byte[] file = personalDetailsDao.findById("shubham_srivastava").get().getResumeFile();
        return file;
    }

    @RequestMapping(value = "/getDetails", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getDetails(@RequestParam String username) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        PersonalDetails personalDetails = new PersonalDetails();

        if (dao.existsById(username)) {
            if (personalDetailsDao.existsById("shubham_srivastava")) {
                personalDetails = personalDetailsDao.findById("shubham_srivastava").get();
                return new ResponseEntity(mapper.writeValueAsString(personalDetails), HttpStatus.OK);
            } else {
                return new ResponseEntity(mapper.writeValueAsString(personalDetails), HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity(ExceptionHandling("101", "Invalid username"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getTable", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getTable(@RequestParam String role) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        if (role.equalsIgnoreCase("Admin")){
            Iterable<UserDetails> userDetails = dao.findAll();
            Map<String, Object> map = new HashMap<>();
            map.put("register", userDetails);
            return new ResponseEntity(mapper.writeValueAsString(map), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(ExceptionHandling("101", "You are not Admin"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping({"/error"})
    public String error(){
        return "forward:/error.html";
    }

    private String ExceptionHandling(String code, String message) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        response.put("code", code);
        response.put("message", message);
        return mapper.writeValueAsString(response);
    }

    @RequestMapping({"/registration", "/login", "/home"})
    public String home() {
        return "forward:/index.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
