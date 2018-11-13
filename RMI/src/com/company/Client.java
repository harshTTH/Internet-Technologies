package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private DataInputStream in = null;      //Input Stream for Server Response
    private BufferedReader reader = null;   //Buffered Reader for user input
    private DataOutputStream output = null; //Output Stream of socket for server
    private Socket s;

    /*
    * Client Class constructor
    * address - server ip
    * port - server port
    */
    public Client(String address,int port) throws IOException{
        s = new Socket(address,port);       //connection to socket
        reader = new BufferedReader(new InputStreamReader(System.in));
        output = new DataOutputStream(s.getOutputStream());
        in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        System.out.println("Client Connection Successful");
    }

    /*
    sendMessage method for taking user Input
    and sending data to server
    (writing to Socket's Output Stream)
    */
    private void sendMessage() throws IOException {
        Scanner in = new Scanner(System.in);
        String className,methodName,typeVal;
        int paramCount;

        //Taking User Input
        System.out.println("Enter Remote Class Name");
        className = in.nextLine();
        this.output.writeUTF(className);

        System.out.println("Enter Remote Method Name");
        methodName = in.nextLine();
        this.output.writeUTF(methodName);

        System.out.println("How many parameters method needs ?");
        paramCount = in.nextInt();
        this.output.writeInt(paramCount);

        if(paramCount != 0){
            System.out.println("Enter Parameter type and value as type1/value1,type2/value2");
            Scanner inn = new Scanner(System.in);
            typeVal = inn.nextLine();
            this.output.writeUTF(typeVal);
        }
    }

    public static void main(String[] args) {
        String address;
        int port;
        Client c= null;
        //User Input
        Scanner in = new Scanner(System.in);
        System.out.println("Enter IP Address of the Server");
        address = in.nextLine();
        System.out.println("Enter Port no of server application");
        port = in.nextInt();

        try {
            c = new Client(address,port);
            c.sendMessage();

            //Reading Server Response
            switch(c.in.readUTF()){
                case "java.lang.String": {
                    System.out.println("Server Response : " + c.in.readUTF());
                    break;
                }
                case "int":{
                    System.out.println("Server Response : " + c.in.readInt());
                    break;
                }
                case "float": {
                    System.out.println("Server Response : " + c.in.readFloat());
                    break;
                }
                case "double":{
                    System.out.println("Server Response : " + c.in.readDouble());
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to Connect to IP - "+address);
        }

        //Closing Connections after server response

        try {
            c.s.close();
            c.reader.close();
            c.output.close();
            c.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
