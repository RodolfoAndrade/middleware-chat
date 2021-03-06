package utils;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private MessageHeader header;
	private MessageBody body;

    public Message(MessageHeader messageHeader, MessageBody messageBody) {
        header = messageHeader;
        body = messageBody;
    }

    public Message() {
    }

    public Message(String msg) {
    	header = new MessageHeader("",0,"");
    	body = new MessageBody(msg);
    }

    public MessageBody getBody() {
        return body;
    }

	public MessageHeader getHeader() {
		return header;
	}

	public void setHeader(MessageHeader header) {
		this.header = header;
	}

	public void setBody(MessageBody body) {
		this.body = body;
	}

	@Override
	public String toString() {
		String string = "ip: "+this.header.getIp()+"\n"
				+ "port: "+this.header.getPort()+"\n"
				+ "channel: "+this.header.getChannel()+"\n"
				+ "message: "+this.body.getMessage();
		return string;
	}
	
	
}