/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Marku
 */
public class DateUtils {
    public static java.sql.Date convertToSqlDate(java.util.Date utilDate) {
//        Note: java.sql.date don't have time while util.date has. So to convert util.date to sql.date get the milliseconds from util.date and use it as a constructor in sql.Date 
//        Get the milliseconds from utilDate
        Long milliseconds = utilDate.getTime();
  //   Create an instance of java.sql.date with the milliseconds as a constructor
        return new java.sql.Date(milliseconds);
    }
    
    public static Date getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);
        
        return sqlDate;
    }
    
    public static String formatDate(Date date, String pattern) {
        try {
            LocalDate currentDate = date.toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String formattedDate = currentDate.format(formatter);
            return formattedDate;
            
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return null;
    }
    
 
    
    public static Date parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            LocalDate formattedDate = LocalDate.parse(dateString, formatter);
            return Date.valueOf(formattedDate);
            
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    
    
}
