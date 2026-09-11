import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class UserInterface {
    JFrame frame;

    public UserInterface() {
        System.out.print("Attempting to create user interface...");
        this.frame = new JFrame("User Interface");

        frame.setLayout(new GridLayout(2, 2));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
