package com.elegido.codes;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        VentanaServidor ventanaServidor = new VentanaServidor("Servidor");
        ventanaServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class VentanaServidor extends JFrame implements Runnable{
    JTextArea areaTexto;

    public VentanaServidor(String titulo){
        setTitle(titulo);
        setSize(280, 350);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        areaTexto = new JTextArea();
        panel.add(areaTexto, BorderLayout.CENTER);
        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);

        //Hilo para que el servidor esté siempre a la escucha
        Thread hiloPrincipal = new Thread(this);
        hiloPrincipal.start();
    }

    @Override
    public void run() {
        //Socket de servidor
        try {
            ServerSocket servidor = new ServerSocket(5560);

            while(true){
                Socket socket = servidor.accept();

                DataInputStream flujoEntrada = new DataInputStream(socket.getInputStream());
                String mensaje = flujoEntrada.readUTF();

                areaTexto.append(mensaje + "\n");
                socket.close();
            }

        } catch (IOException e) {
            System.err.println("Error: " + e);
            //throw new RuntimeException(e);
        }

    }
}
