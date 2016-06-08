package application;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialCom {
	
	private OutputStream out;
	private InputStream in;
	private SerialPort serialPort;
	private CommPort commPort;
	private boolean isConnected;
	private CommPortIdentifier portIdentifier;
	
	public SerialCom() {
		super();
		isConnected = false;
	}
	
	void connect(String portName) {
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
			}
			else {
				commPort = portIdentifier.open(this.getClass().getName(),2000);
			    if (commPort instanceof SerialPort) {
			    	serialPort = (SerialPort) commPort;
			        serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			        out = serialPort.getOutputStream();	  
			        in = serialPort.getInputStream();
			        isConnected = true;
			    }
			    else{
			        System.out.println("Error: Only serial ports can be handled");
			    }
			} 
		}
		catch (Exception e){
			System.err.println("Exception during serial communication: " + e);
		}
	}
	
	void disconnect() {
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		serialPort.close();
		commPort.close();
		isConnected = false;
	}
	
	boolean isConnected(){
		return isConnected;
	}
	
	int read() {
		int readValue = -1;
		try {
			readValue = in.read();
		} catch (IOException e) {
			System.err.println("Error reading from COM");
			e.printStackTrace();
		}
		return readValue;
	}
	
	void write(String data) {
		byte byteData[] = data.getBytes();
		try {
			out.write(byteData);
		} catch (IOException e) {
			System.err.println("Error writing to COM");
			e.printStackTrace();
		}
	}
	
	ObservableList<String> listPorts() {
		ObservableList<String> comList = FXCollections.observableArrayList();
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()){
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            //System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );
            comList.add(portIdentifier.getName());       
        }    
        return comList;
    }
    
    String getPortTypeName (int portType) {
        switch (portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}
