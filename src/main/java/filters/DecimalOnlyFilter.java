/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package filters;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author Marku
 */
public class DecimalOnlyFilter extends DocumentFilter {
    final String REGEX = "^([1-9][0-9]*\\.?[0-9]*|)$";
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
         StringBuilder newString = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        newString.replace(offset, offset + length, text);
        if (newString.toString().matches(REGEX)) {
            super.replace(fb, offset, length, text, attrs);
        }
        
    }
    
}
