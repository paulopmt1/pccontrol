/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pccontrol;

import java.awt.Color;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author Paulo
 */
public class JanelaJFrame extends JFrame{
    JLabel statusLabel;
    

    public void create() throws Exception{
        this.createWindow();
    }

    public void initLoading() throws Exception {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                
                try {
                    
                    
                } catch (Exception e) {
                    String msg = "Falha ao renderizar: " + e.getMessage();
                    System.out.println(msg);
                    statusLabel.setText(msg);
                }
            }
        };
        
        Timer timer = new Timer();
        Integer checkTime = 10;
        
        timer.schedule(task, 0, 1000 * checkTime);
        
    }

    private void createWindow() throws IOException {
        JPanel topPanel = new JPanel();
        topPanel.setVisible(true);
        topPanel.setBackground(Color.white);
        topPanel.setSize(650, 50);
        topPanel.setLocation(0, 0);
        
        JPanel textPanel = new JPanel();
        textPanel.setVisible(true);
        textPanel.setBackground(Color.white);
        textPanel.setSize(650, 100);
        textPanel.setLocation(0, 50);
        
        statusLabel = new JLabel("Carregando Projetos...");
        textPanel.add(statusLabel);
        
        JPanel logoPanel = new JPanel();
        logoPanel.setVisible(true);
        logoPanel.setBackground(Color.white);
        logoPanel.setSize(650, 200);
        logoPanel.setLocation(0, 150);
        this.getContentPane().setLayout(null); 
        
        this.add(topPanel);
        this.add(logoPanel);
        this.add(textPanel);
        
        this.setLocation(350, 250);
        setResizable(false);
        this.setTitle("Sincronizador de arquivos - auryn");
        this.setSize(650, 340);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.white);
    }
}
