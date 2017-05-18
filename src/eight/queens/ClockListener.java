/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eight.queens;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.text.Text;

/**
 *
 * @author Saad
 */
public class ClockListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {

        EightQueens.c.add(Calendar.SECOND, 1);       //add second every time
        Date newtime = EightQueens.c.getTime();

        EightQueens.time.setText("Time : "+EightQueens.dateFormat.format(newtime));

    }

}
