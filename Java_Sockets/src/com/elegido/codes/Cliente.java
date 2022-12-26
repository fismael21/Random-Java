package com.elegido.codes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    public static void main(String[] args) {
        MarcoCliente marcoCliente = new MarcoCliente("Cliente");
        marcoCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoCliente extends JFrame {
    public MarcoCliente(String titulo){
        setTitle(titulo);
        setSize(280, 350);
        PanelCliente panelCliente = new PanelCliente();
        add(panelCliente);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}

class PanelCliente extends JPanel{

    JTextField campoTexto;
    JButton boton;

    public PanelCliente(){
        JLabel texto = new JLabel("Cliente");
        add(texto);
        campoTexto = new JTextField(22);
        add(campoTexto);
        boton = new JButton("Enviar");
        EnviaTexto enviaTexto = new EnviaTexto();
        boton.addActionListener(enviaTexto);
        add(boton);
    }

    //Clase interna y privada
    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Socket socket = new Socket("192.168.5.128", 5560);
                DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());
                flujoSalida.writeUTF(campoTexto.getText());
                flujoSalida.close();
            } catch(UnknownHostException ex) {
                System.err.println("Error" + ex);
            } catch (IOException ex) {
                //throw new RuntimeException(ex);
                System.err.println("Error: "+ ex);
            }

        }
    }

}