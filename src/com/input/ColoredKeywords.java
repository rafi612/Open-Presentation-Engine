package com.input;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ColoredKeywords extends DefaultStyledDocument
{
	private static final long serialVersionUID = 1L;
	final StyleContext cont = StyleContext.getDefaultStyleContext();
    final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
    final AttributeSet attrorange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
    final AttributeSet attrgreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
    final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    public void insertString (int offset, String str, AttributeSet a) throws BadLocationException 
    {
        super.insertString(offset, str, a);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offset);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offset + str.length());
        int wordL = before;
        int wordR = before;
        
        while (wordR <= after) {
            if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                if (text.substring(wordL, wordR).matches("(\\W)*(import|as|class)"))
                    setCharacterAttributes(wordL, wordR - wordL, attr, false);
                else if (text.substring(wordL, wordR).matches("(\\W)*(True|False)"))
                    setCharacterAttributes(wordL, wordR - wordL, attrorange, false);
                else if (text.substring(wordL, wordR).matches("(\\W)*(#)"))
                    setCharacterAttributes(wordL, wordR - wordL, attrgreen, false);
                else
                    setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                wordL = wordR;
            }
            wordR++;
        }
    }

    public void remove (int offs, int len) throws BadLocationException {
        super.remove(offs, len);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offs);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offs);

        if (text.substring(before, after).matches("(\\W)*(import|as|class)")) 
            setCharacterAttributes(before, after - before, attr, false);
        else if (text.substring(before, after).matches("(\\W)*(True|False)")) 
            setCharacterAttributes(before, after - before, attrorange, false);
        else if (text.substring(before, after).matches("(\\W)*(#)"))
            setCharacterAttributes(before, after - before, attrgreen, false);
        else 
        {
            setCharacterAttributes(before, after - before, attrBlack, false);
        }
    }
    
    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

}
