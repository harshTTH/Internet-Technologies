package com.company;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket ss = null;     //ServerSocket object for Listening to user specified port
    private Socket s = null;            //Socket instance of existing socket connection form client
    private DataInputStream in = null;  //input stream for reading client request
    private DataOutputStream out = null;//Output stream for sending response to client

    /*
    Server Class Constructor
    port - port on which server needs to start listening
     */
    public Server(int port){
        try {
            ss = new ServerSocket(port);
            System.out.println("Server Started Successfully");

            s = ss.accept();
            System.out.println("Client Connection Accepted");

            in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            out = new DataOutputStream(s.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object[] getParamTypes(String[][] typeVals,int paramCount) throws IOException {
        int k = 0;

        Class<?>[] paramType = null;
        Object[] param = null;
        if(paramCount != 0){
            paramType = new Class[paramCount];
            param = new Object[paramCount];

            System.out.println(paramCount);
            while(k < paramCount){
                String[] typeVal = typeVals[k];

                switch(typeVal[0]){
                    case "Integer":
                    case "int":
                        paramType[k] = int.class;
                        param[k] = Integer.parseInt(typeVal[1]);
                        break;
                    case "String":
                        paramType[k] = String.class;
                        param[k] = typeVal[1];
                        break;
                    case "double":
                        paramType[k] = Double.class;
                        param[k] = typeVal[1];
                        break;
                    case "float":
                        paramType[k] = Float.class;
                        param[k] = typeVal[1];
                        break;
                }
                k++;
            }
        }
        return new Object[]{paramType,param};
    }
    /*
    runRemoteMethod method for parsing client request
    msg - Client's message
     */
    public void runRemoteMethod(String className, String methodName, String[][]typeVal,int paramCount) throws IOException {

        try {
            //Reflection API usage

            Class<?> c = Class.forName(className);
            Object obj = c.getDeclaredConstructor().newInstance();
            Class<?>[] paramTypes = null;
            Object[] params = null;

            if(paramCount != 0){
                Object arr[] = getParamTypes(typeVal,paramCount);
                paramTypes = (Class<?>[]) arr[0];
                params = (Object[]) arr[1];
            }
            Method method = obj.getClass().getMethod(methodName,paramTypes);
            out.writeUTF(method.getReturnType().getName());
            String returnType = method.getReturnType().getName();

            switch(returnType){
                case "java.lang.String": {
                    String val1 = (String) method.invoke(obj, params);
                    out.writeUTF(val1);
                    break;
                }
                case "" +
                        "int":{
                    int val2 = (int) method.invoke(obj,params);
                    out.writeInt(val2);
                    break;
                }
                case "float": {
                    float val3 = (float) method.invoke(obj,params);
                    out.writeFloat(val3);
                    break;
                }
                case "double":{
                    double val4 = (double) method.invoke(obj,params);
                    out.writeDouble(val4);
                    break;
                }
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            out.writeUTF("No such Method Exists " + methodName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            out.writeUTF("No such Class Exists" + className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        int port;
        String className,methodName;
        int paramCount;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter port on which server needs to run");
        port = in.nextInt();

        Server server = new Server(port);
        try {
            className = server.in.readUTF();      //reading Client Message
            methodName = server.in.readUTF();
            paramCount = server.in.readInt();

            if(paramCount != 0){
                String[] temp = server.in.readUTF().split(",");

                String[][] typeVal = new String[2][temp.length];

                int k = 0;
                if(temp.length == paramCount){
                    while(k < temp.length){
                        typeVal[k] = temp[k].split("/");
                        k++;
                    }
                    server.runRemoteMethod(className,methodName,typeVal,temp.length);
                }else{
                    System.out.println("Total "+paramCount+" parameters needed and "+temp.length+" Arguments supplied");
                }
            }else{
                server.runRemoteMethod(className,methodName,null,paramCount);
            }

            server.in.close();
            server.out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


