/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.ws.Holder;
import jdk.nashorn.internal.ir.BreakNode;
import modelo.Medicamento;
import modelo.modelMedicamento;
import vistas.vistaMedicamentos;

/**
 *
 * @author Abel Gomez
 */
public class controladorMedicamento {

    private modelMedicamento modelo;
    private vistaMedicamentos vista;

    public controladorMedicamento(modelMedicamento modelo, vistaMedicamentos vista) {
        this.modelo = modelo;
        this.vista = vista;

        vista.setVisible(true);
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            FlatIntelliJLaf.setup();
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    public void inciacontrol() throws IOException {
        cargardatos();

        vista.getBtnNuevoPedido().addActionListener(l -> abrirDialogo(1));
        vista.getBtnGuardarMedicamento().addActionListener(l -> optenerdatosMedicina());
        vista.getBtnatras().addActionListener(l -> botonatras());

    }

    private void abrirDialogo(int ope) {
        String titulo;
        if (ope == 1) {

            titulo = "Crear Pedido.";

            vista.getjDialogMedicamento().setName("C");
            vista.getjDialogMedicamento().setVisible(true);
            vista.getjDialogMedicamento().setTitle(titulo);

        }

        vista.getjDialogMedicamento().setSize(600, 600);
        vista.getjDialogMedicamento().setLocationRelativeTo(vista);
    }

    private void optenerdatosMedicina() {

        if (vista.getjDialogMedicamento().getName().contentEquals("C")) {

            String nombre = vista.getTxtNombre1().getText();//1

            String tipo = vista.getJcmbBxTipo1().getSelectedItem().toString();//2
            // int cantidad = (int) vista.getjSpinnerCantidad1().getValue();//3
            String cantidad = (String) vista.getjSpinnerCantidad1().getValue().toString();
            String selecteddistribuidor = null;
            if (vista.getjRadioButtonCEMEFAR1().isSelected()) {

                selecteddistribuidor = vista.getjRadioButtonCEMEFAR1().getText();

            }
            
            if (vista.getjRadioButtonCOFARMA1().isSelected()) {

                selecteddistribuidor = vista.getjRadioButtonCOFARMA1().getText();

            }
            if (vista.getjRadioButtonEMPSEPHAR1().isSelected()) {

                selecteddistribuidor = vista.getjRadioButtonEMPSEPHAR1().getText();

            }

            String sucursal = "";

            if (vista.getjCheckBoxPrincipal1().isSelected()) {
                sucursal += vista.getjCheckBoxPrincipal1().getText() + " ";
            }

            if (vista.getjCheckBoxSECUNDARIA1().isSelected()) {
                sucursal += vista.getjCheckBoxSECUNDARIA1().getText() + " ";
            }

            if (vista.getjCheckBoxPrincipal1().isSelected() && vista.getjCheckBoxSECUNDARIA1().isSelected()) {
                sucursal = sucursal.trim();
            }

            if (validarMedicina(nombre, tipo, cantidad, selecteddistribuidor, sucursal) == true) {

                modelMedicamento medicamento = new modelMedicamento();
                medicamento.setNombre(nombre);
                medicamento.setTipo(tipo);
                medicamento.setCantidad(Integer.parseInt(cantidad));
                medicamento.setDistribuidor(selecteddistribuidor);
                medicamento.setSucursal(sucursal);

                crearPedido(medicamento);
//
//                if (medicamento.setMediamento()) {
//
//                    cargardatos();
//                } else {
//                    JOptionPane.showMessageDialog(vista, "Error , datos No guardados");
//
//                }

            }

        }

    }

    private boolean validarMedicina(String nombre, String tipo, String cantidad, String distribuidor, String sucursal) {

        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El campo Nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int tiponumero = vista.getJcmbBxTipo1().getSelectedIndex();
        if (tipo.trim().isEmpty() || tiponumero == 0) {
            JOptionPane.showMessageDialog(vista, "El campo Tipo es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
      
        if (Integer.parseInt(cantidad) <= 0) {

            JOptionPane.showMessageDialog(vista, "La cantidad debe ser mayor a cero", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (distribuidor == null) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar un distribuidor", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (sucursal.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Debe seleccionar al menos una sucursal", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void crearPedido(modelMedicamento medicamento) {

        JFrame resumenPedido = new JFrame("Pedido al Distribuidor " + medicamento.getDistribuidor());
        resumenPedido.setLayout(new GridLayout(3, 1));//cuadrícula con 3 filas y 1 columna

        JLabel pedidoLabel = new JLabel(medicamento.getCantidad() + " unidades del " + medicamento.getTipo() + " " + medicamento.getNombre());
        String[] partes = medicamento.getSucursal().split(" ");
        String secundaria = "situada en Calle Alcazabilla n.3.";
        String principal = "situada en Calle de la Rosa n.28";
        String sucursal = "";
        if (partes.length == 1) {

            if (partes[0].equalsIgnoreCase("PRINCIPAL")) {
                sucursal = "PRINCIPAL  " + principal;

            } else {

                sucursal = "SECUNDARIA " + secundaria;

            }
        } else {
            String parte1 = partes[0];
            String parte2 = partes[1];

            if (parte1.equalsIgnoreCase("PRINCIPAL") && parte2.equalsIgnoreCase("SECUNDARIA")) {
                sucursal = parte1 + principal + parte2 + secundaria;
            } else if (parte1.equalsIgnoreCase("PRINCIPAL")) {
                sucursal = parte1 + principal;
            } else if (parte2.equalsIgnoreCase("SECUNDARIA")) {
                sucursal = parte2 + secundaria;
            } else if (parte1.equalsIgnoreCase("SECUNDARIA")) {
                sucursal = " SECUNDARIA " + secundaria;
            } else if (parte2.equalsIgnoreCase("PRINCIPAL")) {
                sucursal = "PRINCIPAL " + principal;
            } else {
                // codigo a ejecutar si ninguna de las condiciones anteriores es verdadera
            }
        }

        JLabel farmaciaLabel = new JLabel("Para la Farmacia situada en " + sucursal);
        JButton cancelarButton = new JButton("Cancelar");
        JButton enviarButton = new JButton("Enviar Pedido");

        resumenPedido.add(pedidoLabel);
        resumenPedido.add(farmaciaLabel);
        resumenPedido.add(cancelarButton);
        resumenPedido.add(enviarButton);

        cancelarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resumenPedido.dispose();
                vista.setVisible(true);
                vista.getjDialogMedicamento().setVisible(false);

            }
        });
        
        

        enviarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Pedido Enviado");
                vista.setVisible(true);
                vista.getjDialogMedicamento().setVisible(false);
                resumenPedido.dispose();
                //guardar base 
                if (medicamento.setMediamento()) {

                    cargardatos();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error , datos No guardados");

                }
            }
        });

        // Mostrar la ventana
        resumenPedido.pack();
        resumenPedido.setLocationRelativeTo(null);
        resumenPedido.setVisible(true);

    }

    private void cargardatos() {

        DefaultTableModel estructuraTabla;
        estructuraTabla = (DefaultTableModel) vista.getTablaMedicamemntos().getModel();

        estructuraTabla.setNumRows(0);

        List<Medicamento> listap = modelo.getMedicamentos();

        Holder<Integer> i = new Holder<>(0);
        listap.stream().forEach(pe -> {
            estructuraTabla.addRow(new Object[6]);

            vista.getTablaMedicamemntos().setValueAt(pe.getId(), i.value, 0);
            vista.getTablaMedicamemntos().setValueAt(pe.getNombre(), i.value, 1);
            vista.getTablaMedicamemntos().setValueAt(pe.getTipo(), i.value, 2);
            vista.getTablaMedicamemntos().setValueAt(pe.getCantidad(), i.value, 3);
            vista.getTablaMedicamemntos().setValueAt(pe.getDistribuidor(), i.value, 4);
            vista.getTablaMedicamemntos().setValueAt(pe.getSucursal(), i.value, 5);

            i.value++;

        });

    }

    public void botonatras() {

        vista.getjDialogMedicamento().setVisible(false);
        //limpiardatos();

    }

}
