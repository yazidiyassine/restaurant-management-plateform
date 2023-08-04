package com.rms.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String text, List<String> list){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("yyy@mailinator.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if (list != null && !list.isEmpty())
            message.setCc(getCcArray(list));
        javaMailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList){
        String[] cc = new String[ccList.size()];
        for(int i=0; i<ccList.size(); i++){
            cc[i] = ccList.get(i);
        }
        return cc;
    }
}
