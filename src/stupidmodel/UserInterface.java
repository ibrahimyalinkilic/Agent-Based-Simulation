package stupidmodel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.util.concurrent.ThreadLocalRandom;
import com.jidesoft.icons.JideIconsFactory.Cursor;


public class UserInterface {
static int x;
static int y;
static int recoverability_rate;
	
	//Create cell function
	public static void createCell(int x, int y, int recoverability_rate) throws IOException{
        int y_temp = y;
        int x_temp = x;
        double randomNum = 0.0;
        File file = new File("Stupid_Cell.data");
        
        if (!file.exists()) //file not found
        {
        	return;
        }

        FileWriter fileWriter = new FileWriter(file, false);
        BufferedWriter bWriter = new BufferedWriter(fileWriter);
        
        for(int i = 0; i <= x_temp; i++)
        {
        	for(int j = 0; j <= y_temp; j++)
        	{
        		if (recoverability_rate == 4)
                {
                	 randomNum = 0.02;
                }
                else if(recoverability_rate == 3)
                {
                	 randomNum = 0.01;
                }
                else if(recoverability_rate == 2)
                {
                	 randomNum = 0.009;
                }
                else if(recoverability_rate == 1)
                {
                	 randomNum = 0.008;
                }
        		bWriter.write(x+" "+y+" "+ randomNum+"\n");
        		y--;
        	}
        	x--;
        	y = y_temp;
        }
        bWriter.close();
    }
	

	//Calculate Cell Count Function
   public static void calculateCellCount(int device_number, String topology, int user_count, int location_count) throws IOException
   {
	   double total_count = 0;
	   
	   switch (topology) {
	   
       case "LAN": 
    	   total_count = (device_number * 0.4) + (user_count * 0.6);
    	   total_count += total_count * 0.1;
    	   total_count += total_count * location_count;
           break;

       case "MAN" :
    	   total_count = (device_number * 0.4) + (user_count * 0.6);
    	   total_count += total_count * 0.3;
    	   total_count += total_count * location_count;
           break;

       case "WAN" :
    	   total_count = (device_number * 0.4) + (user_count * 0.6);
    	   total_count += total_count * 0.5;
    	   total_count += total_count * location_count;
           break;

       default :
           System.out.println("Wrong value!");
           break;
       }
	   
	   if (total_count < 150)
	   {
		   x = 50;
		   y = 22;
	   }
	   else if (total_count < 256400)
	   {
		   x = 100;
		   y = 44;
	   }
	   else if(total_count < 513000)
	   {
		   x = 150;
		   y = 66;
	   }
	   else if(total_count < 1032000)
	   {
		   x = 200;
		   y = 88;
	   }
	   else if(total_count >= 1032000)
	   {
		   x = 250;
		   y = 112;
	   }
	   else
	   {
		   //Wrong values
		   x = 1;
		   y = 1;
	   }  
   }
   
   public static void calculateRecoverRate(String security_degree, String backed_up, int system_engineer_count)
   {
	   double total_count = 0;
	   switch (security_degree) {
	   
       case "Insufficient":
    	   if (backed_up.equals("YES"))
    	   {
    		   total_count = 500;
    		   total_count += system_engineer_count * 0.5;
    	   }
    	   else
    	   {
    		   total_count = 200;
    		   total_count += system_engineer_count * 0.5;
    	   }
           break;

       case "Decent" :
    	   if (backed_up.equals("YES"))
    	   {
    		   total_count = 1000;
    		   total_count += system_engineer_count * 0.5;
    	   }
    	   else
    	   {
    		   total_count = 700;
    		   total_count += system_engineer_count * 0.5;
    	   }
           break;

       case "High Level" :
    	   if (backed_up.equals("YES"))
    	   {
    		   total_count = 1500;
    		   total_count = 1500 + system_engineer_count * 0.5;
    	   }
    	   else
    	   {
    		   total_count = 1000;
    		   total_count = total_count + system_engineer_count * 0.5;
    	   }
           break;

       default :
           System.out.println("Wrong value!");
           break;
       }
	   
	   if (total_count < 250)
	   {
		   recoverability_rate = 1;
	   }
	   else if(total_count < 900)
	   {
		   recoverability_rate = 2;
	   }
	   else if(total_count < 2000)
	   {
		   recoverability_rate = 3;
	   }
	   else if(total_count >= 2000)
	   {
		   recoverability_rate = 4;
	   }
	   else //Wrong value
	   {
		   recoverability_rate = -1;
	   }
   }
    
	//main function
    public static void main(String[] args) {
    	            
        //UI
        JFrame frame = new JFrame("Security Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(880, 800);
        frame.setLocationRelativeTo(null);
        
        //background img
        JLabel background;
        ImageIcon img = new ImageIcon("bir.jpg");
        background = new JLabel("",img,JLabel.CENTER);
        
        //set icon
        ImageIcon icon = new ImageIcon("icon.png");
        frame.setIconImage(icon.getImage());
             
        //Panele componentlerin eklenmesi
        JPanel panel = new JPanel();
        JLabel label0 = new JLabel("SECURITY SYSTEM PARAMETERS");
       
        JLabel label = new JLabel("Total End Device Number:");
        JTextField tf = new JTextField(10); // 10 karakterlik text area
        
        String[] topologyTypes = { "LAN", "MAN", "WAN"};
        JLabel label2 = new JLabel("Topology Size:");
        JComboBox topology_types = new JComboBox(topologyTypes);
        topology_types.setSelectedIndex(1);
        
        String[] security_protocol_degrees_list = { "Insufficient", "Decent",  "High Level"};
        JLabel label3 = new JLabel("Security Protocol Level:");
        JComboBox security_protocol_degrees = new JComboBox(security_protocol_degrees_list);
        security_protocol_degrees.setSelectedIndex(1);
           
        JLabel label4 = new JLabel("Total Network Users:");
        JTextField tf4 = new JTextField(10);
        
        JLabel label5 = new JLabel("Number of Different Locations:");
        JTextField tf5 = new JTextField(10);
        
        String[] yes_no_list = { "YES", "NO"};
        JLabel label6 = new JLabel("Is your data backed up?");
        JComboBox back_up = new JComboBox(yes_no_list);
        back_up.setSelectedIndex(1);
        
        
        JLabel label7 = new JLabel("Number of System Engineers:");
        JTextField tf7 = new JTextField(10);
        
        JButton enter = new JButton("Enter");
        
        enter.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e){  
            int device_number = Integer.parseInt(tf.getText());
            String topology = (String)topology_types.getSelectedItem();
            String security_degree = (String)security_protocol_degrees.getSelectedItem();
            int user_count = Integer.parseInt(tf4.getText());
            int location_count = Integer.parseInt(tf5.getText());
            String is_backed_up = (String)back_up.getSelectedItem();
            int system_engineer_count = Integer.parseInt(tf7.getText());
            try {
				calculateCellCount(device_number, topology, user_count, location_count);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            calculateRecoverRate(security_degree, is_backed_up, system_engineer_count);
            try {
				createCell(x, y, recoverability_rate);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            frame.dispose();
            
        }  
        });  
        // Componentlerin Flow Layout'a eklenmesi
        panel.setLayout(null);
       	panel.setBackground(Color.LIGHT_GRAY);
        
       	panel.add(background);
       	background.setSize(920, 800);
       	
       	background.add(label0);
       	label0.setLocation(215,20);
       	label0.setSize(400,100);
       	label0.setFont(new Font("Arial Bold", Font.BOLD, 22));
       	label0.setForeground(new java.awt.Color(51, 153, 255));
       	
       	
        background.add(label); 
        label.setLocation(200,150);
        label.setSize(200,30);
        label.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(tf);
        tf.setLocation(450,150);
        tf.setSize(150,30);
        tf.setHorizontalAlignment(JTextField.CENTER);
        tf.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(label2);
        label2.setLocation(200,400);
        label2.setSize(200,30);
        label2.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(topology_types);  
        topology_types.setLocation(450,400);
        topology_types.setSize(150,30);
        topology_types.setBackground(Color.white);
        topology_types.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(label3);
        label3.setLocation(200,350);
        label3.setSize(200,30);
        label3.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(security_protocol_degrees);
        security_protocol_degrees.setLocation(450,350);
        security_protocol_degrees.setSize(150,30);
        security_protocol_degrees.setBackground(Color.white);
        security_protocol_degrees.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(label4);
        label4.setLocation(200,300);
        label4.setSize(200,30);
        label4.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(tf4);
        tf4.setLocation(450,300);
        tf4.setSize(150,30);
        tf4.setHorizontalAlignment(JTextField.CENTER);
        tf4.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(label5);
        label5.setLocation(200,250);
        label5.setSize(230,30);
        label5.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(tf5);
        tf5.setLocation(450,250);
        tf5.setSize(150,30);
        tf5.setHorizontalAlignment(JTextField.CENTER);
        tf5.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(label6);
        label6.setLocation(200,450);
        label6.setSize(200,30);
        label6.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(back_up);
        back_up.setLocation(450,450);
        back_up.setSize(150,30);
        back_up.setBackground(Color.white);
        back_up.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(label7);
        label7.setLocation(200,200);
        label7.setSize(230,30);
        label7.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(tf7);
        tf7.setLocation(450,200);
        tf7.setSize(150,30);
        tf7.setHorizontalAlignment(JTextField.CENTER);
        tf7.setFont(new Font("Arial Bold", Font.BOLD, 14));
        
        background.add(enter);
        enter.setLocation(300,550);
        enter.setSize(200,50);
        
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
    }
}
