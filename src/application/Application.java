package application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import distribution.Callback;
import distribution.Invoker;
import utils.Message;

public class Application extends JFrame implements MouseListener, FocusListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static ChatClient chat;
	private int width = 800;
	private int height = 480;
	private static JTextArea chatArea, messageArea;
	private static JList<String> channelList, subscriberList;
	private static Vector<String> channelVector, subscriberVector;
	
	// Constructor to setup GUI components and event handlers
	public Application() {
		setLayout(new BorderLayout(5, 5));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setPreferredSize(new Dimension((int)(width*0.7), (int)(height*0.8)));
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setPreferredSize(new Dimension((int)(width*0.3), (int)(height*0.8)));
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setPreferredSize(new Dimension((int)(width), (int)(height*0.2)));
		
		//chat
        chatArea = new JTextArea();
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        JScrollPane areaScrollText = new JScrollPane(chatArea);
        areaScrollText.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollText.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Chat"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		areaScrollText.getBorder()));
		
		//channel
        channelVector = new Vector<String>();
        channelList = new JList<String>(channelVector);
        channelList.addFocusListener(this);
        JScrollPane areaScrollChannel = new JScrollPane(channelList);
        areaScrollChannel.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //new topic button
        JButton newTopic = new JButton("New Topic");
        newTopic.addMouseListener(this);
        
        //create border channels
        JPanel borderChannel = new JPanel(new BorderLayout());
        borderChannel.add(areaScrollChannel, BorderLayout.CENTER);
        borderChannel.add(newTopic, BorderLayout.SOUTH);
        borderChannel.setBorder(
    		    BorderFactory.createCompoundBorder(
    			        BorderFactory.createCompoundBorder(
    			                        BorderFactory.createTitledBorder("Topics"),
    			                        BorderFactory.createEmptyBorder(5,5,5,5)),
    			        				borderChannel.getBorder()));
		
		//subscriber
        subscriberVector = new Vector<String>();
        subscriberList = new JList<String>(subscriberVector);
        subscriberList.setEnabled(false);
        JScrollPane areaScrollSubscriber = new JScrollPane(subscriberList);
        areaScrollSubscriber.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //new subscriber button
//        JButton newClient = new JButton("New Subscriber");
//        newClient.addMouseListener(this);
        
        //create border subscribers
        JPanel borderSubscriber = new JPanel(new BorderLayout());
        borderSubscriber.add(areaScrollSubscriber, BorderLayout.CENTER);
//        borderSubscriber.add(newClient, BorderLayout.SOUTH);
        borderSubscriber.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Subscribers"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		        				borderSubscriber.getBorder()));
        
		//message
        messageArea = new JTextArea();
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane areaScrollMessage = new JScrollPane(messageArea);
        areaScrollMessage.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
		//send button
        JButton send = new JButton("Send");
        send.addMouseListener(this);
        
        //create border message
        JPanel borderMessage = new JPanel(new BorderLayout());
        borderMessage.add(areaScrollMessage, BorderLayout.CENTER);
        borderMessage.add(send, BorderLayout.EAST);
        borderMessage.setBorder(
		    BorderFactory.createCompoundBorder(
		        BorderFactory.createCompoundBorder(
		                        BorderFactory.createTitledBorder("Message"),
		                        BorderFactory.createEmptyBorder(5,5,5,5)),
		areaScrollMessage.getBorder()));

		//add to panels
		leftPanel.add(areaScrollText);
		rightPanel.add(borderChannel);
		rightPanel.add(borderSubscriber);
		bottomPanel.add(borderMessage);
		
		//add panels to application
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		add(bottomPanel, BorderLayout.SOUTH);
		
		//basic configurations
	    setTitle("Chat");
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    pack();
	    setLocationRelativeTo(null);
	    setVisible(true);
	}
	
	public static void main(String[] args) {
		chat = new ChatClient(new Callback() {
			
			@Override
			public void onReceive(String msg) {
				// TODO Auto-generated method stub
//				byte[] receivedMsg;

//				receivedMsg = crhr.receive();
//				if (receivedMsg != null) {
//					try {
//						Message rcvdMsg = marshaller.unmarshall(receivedMsg);
//						System.out.println(port+" recebeu msg: "+rcvdMsg.getBody().getMessage());
//					} catch (ClassNotFoundException | IOException
//							| InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
				System.out.println("recebeu: "+msg);
				if(msg.indexOf(':') != -1 && msg.substring(0,msg.indexOf(':')).equals("topics")){
					String string = msg.substring(msg.indexOf(':')+1);
					string = string.replace("[", "");
					string = string.replace("]", "");
					string = string.replace(" ", "");
					String[] topics = string.split(",");
					channelVector.clear();
					for(String topic : topics) {
						channelVector.add(topic);
					}
					channelList.setListData(channelVector);
					channelList.setSelectedIndex(0);
					chat.getSubscribers(channelVector.firstElement());
				}
				else if(msg.indexOf(':') != -1 && msg.substring(0,msg.indexOf(':')).equals("subscribers")){
						String string = msg.substring(msg.indexOf(':')+1);
						string = string.replace("[", "");
						string = string.replace("]", "");
						string = string.replace(" ", "");
						String[] subscribers = string.split(",");
						subscriberVector.clear();
						for(String subscriber : subscribers) {
							subscriberVector.add(subscriber);
						}
						subscriberList.setListData(subscriberVector);
				}
				else {
					chatArea.setText(chatArea.getText()+msg+"\n");
				}
			}

			@Override
			public void onReceive() {
				// TODO Auto-generated method stub
				
			}
		});
		new Application();
		chat.new NewSubscriber();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton) e.getSource();
		switch (button.getText()) {
		case "New Subscriber":
			//quando cria novo subscriber o outro � sobrescrito
			chat.new NewSubscriber();
			break;
		case "New Topic":
			chat.new NewTopic();
			break;
		case "Send":
			chat.send(messageArea.getText());
			messageArea.setText("");
			break;
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		JList jList = (JList) e.getSource();
		String selected = channelVector.get(jList.getSelectedIndex());
		if(chat.getSubscribers(selected)){
			chatArea.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}
}
