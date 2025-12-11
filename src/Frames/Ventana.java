/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Frames;

import Clases.SQL;
import ConexionBD.Conexion;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.*;
import scroll.ScrollBarCustomUI;

public class Ventana extends javax.swing.JFrame {
private DefaultTableModel m;
    private Statement stm;
    private boolean vercheck = false;
    public Ventana() {
        initComponents();
        jScrollPane1.getVerticalScrollBar().setUI(new ScrollBarCustomUI());
        jScrollPane1.getHorizontalScrollBar().setUI(new ScrollBarCustomUI());
        Conexion.getConnection();
        this.setLocationRelativeTo(null);
        m = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        String[] nombresColumnas = {"NOMBRE", "EDAD", "SEXO", "ESTADO", "ESPECIE", "VETERINARIO", "HABITAT","ALIMENTACION"};
        m.setColumnIdentifiers(nombresColumnas);
        Tabla.setModel(m);
        llenarTabla();
        checkboxver();
        Tabla.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                int i = Tabla.getSelectedRow();
                if(i >=0){
                    txtNombre.setText(Tabla.getValueAt(i,0).toString());
                    txtEdad.setText(Tabla.getValueAt(i,1).toString());
                    txtSexo.setText(Tabla.getValueAt(i,2).toString());
                    txtEstado.setText(Tabla.getValueAt(i,3).toString());
                    txtEspecie.setText(Tabla.getValueAt(i,4).toString());
                    txtHabitat.setText(Tabla.getValueAt(i,5).toString());
                    txtVeterinario.setText(Tabla.getValueAt(i,6).toString());
                    txtAlimento.setText(Tabla.getValueAt(i,7).toString());
                }
            }
        });
    }

    
    public void llenarTabla() {
    for (int i = m.getRowCount() - 1; i >= 0; i--) {
        m.removeRow(i);
    }

    String sql = SQL.LlenarTabla;

    try (java.sql.Connection con = Conexion.getConnection();
         Statement st = con.createStatement();
         ResultSet r = st.executeQuery(sql)) {

        while (r.next()) {
            Object A[] = new Object[8]; 
            A[0] = r.getString("NOMBRE"); 
            A[1] = r.getInt("EDAD");
            A[2] = r.getString("SEXO");
            A[3] = r.getString("ESTADO");
            A[4] = r.getString("ESPECIE");
            A[5] = r.getString("VETERINARIO");
            A[6] = r.getString("HABITAT");
            A[7] = r.getString("ALIMENTACION");
            m.addRow(A);
        }

        conteo();

    } catch (SQLException ex) {
        System.out.println("Error en llenarTabla: " + ex.getMessage());
    }
    }
    
    public void conteo(){
        int filas = m.getRowCount();
        lblConteo.setText("Total de animales: " + filas);
    }
    
    public void checkboxver(){
        ckNombre.setVisible(vercheck);
        ckSexo.setVisible(vercheck);
        ckEdad.setVisible(vercheck);
        ckEspecie.setVisible(vercheck);
        ckHabitat.setVisible(vercheck);
        ckEstado.setVisible(vercheck);
        ckAlimento.setVisible(vercheck);
        ckVeterinario.setVisible(vercheck);
        vercheck = !vercheck;
        
        if(!vercheck){
            llenarTabla();
        }else{
            filtrar();
        }
    }
    
    public void limpiarCasillas(){
        txtNombre.setText("");
        txtSexo.setText("");
        txtEdad.setText("");
        txtEspecie.setText("");
        txtHabitat.setText("");
        txtEstado.setText("");
        txtAlimento.setText("");
        txtVeterinario.setText("");
        txtFiltrar.setText("");
    } 
    
    private void filtrar() {
    String nombre = (ckNombre.isSelected() && !txtNombre.getText().trim().isEmpty()) ? txtNombre.getText().trim() : null;
    String especie = (ckEspecie.isSelected() && !txtEspecie.getText().trim().isEmpty()) ? txtEspecie.getText().trim() : null;
    String sexo = (ckSexo.isSelected() && !txtSexo.getText().trim().isEmpty()) ? txtSexo.getText().trim() : null;
    String estado = (ckEstado.isSelected() && !txtEstado.getText().trim().isEmpty()) ? txtEstado.getText().trim() : null;
    String edad = (ckEdad.isSelected() && !txtEdad.getText().trim().isEmpty()) ? txtEdad.getText().trim() : null;
    String habitat = (ckHabitat.isSelected() && !txtHabitat.getText().trim().isEmpty()) ? txtHabitat.getText().trim() : null;
    String alimento = (ckAlimento.isSelected() && !txtAlimento.getText().trim().isEmpty()) ? txtAlimento.getText().trim() : null;
    String veterinario = (ckVeterinario.isSelected() && !txtVeterinario.getText().trim().isEmpty()) ? txtVeterinario.getText().trim() : null;
    String general = txtFiltrar.getText().trim().isEmpty() ? null : txtFiltrar.getText().trim();

    String sql = "{call sp_buscar_animales(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    try (java.sql.Connection con = Conexion.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {
        
        DefaultTableModel m = (DefaultTableModel) Tabla.getModel();
        m.setRowCount(0); 

        cs.setString(1, nombre);
        cs.setString(2, edad);
        cs.setString(3, sexo);
        cs.setString(4, estado);
        cs.setString(5, especie);
        cs.setString(6, veterinario);
        cs.setString(7, habitat);
        cs.setString(8, alimento);
        cs.setString(9, general);

        try (ResultSet r = cs.executeQuery()) {
            while (r.next()) {
                Object A[] = new Object[8]; 
                A[0] = r.getString("NOMBRE");
                A[1] = r.getInt("EDAD");
                A[2] = r.getString("SEXO");
                A[3] = r.getString("ESTADO");
                A[4] = r.getString("ESPECIE");
                A[5] = r.getString("VETERINARIO");
                A[6] = r.getString("HABITAD"); 
                A[7] = r.getString("ALIMENTACION");
                m.addRow(A);
            }
        }
        
        conteo(); 

    } catch (SQLException ex) {
        System.out.println("Error al filtrar: " + ex.getMessage());
    }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtFiltrar = new javax.swing.JTextField();
        txtHabitat = new javax.swing.JTextField();
        txtEstado = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtEspecie = new javax.swing.JTextField();
        txtEdad = new javax.swing.JTextField();
        txtAlimento = new javax.swing.JTextField();
        txtVeterinario = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        txtSexo = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        ckVeterinario = new javax.swing.JCheckBox();
        ckNombre = new javax.swing.JCheckBox();
        ckEdad = new javax.swing.JCheckBox();
        ckSexo = new javax.swing.JCheckBox();
        ckEstado = new javax.swing.JCheckBox();
        ckHabitat = new javax.swing.JCheckBox();
        ckEspecie = new javax.swing.JCheckBox();
        ckAlimento = new javax.swing.JCheckBox();
        btnBuscar = new javax.swing.JButton();
        lblConteo = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(190, 219, 185));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEliminar.setBackground(new java.awt.Color(214, 60, 60));
        btnEliminar.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorder(null);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 260, 40));

        jLabel1.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel1.setText("Sexo");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, -1, 30));

        jLabel2.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel2.setText("Edad");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, 30));

        jLabel3.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel3.setText("Especie");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 160, -1, 30));

        jLabel4.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel4.setText("Habitat");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 200, -1, 30));

        Tabla.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        Tabla.setRowHeight(40);
        jScrollPane1.setViewportView(Tabla);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 980, 310));

        jLabel5.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel5.setText("Filtrar");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 400, -1, 40));

        jLabel6.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel6.setText("Alimento");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 160, -1, 30));

        jLabel7.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel7.setText("Nombre");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, 30));

        jLabel8.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel8.setText("Estado");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 240, -1, 30));

        txtFiltrar.setBackground(new java.awt.Color(231, 249, 228));
        txtFiltrar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtFiltrar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFiltrar.setBorder(null);
        txtFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltrarActionPerformed(evt);
            }
        });
        txtFiltrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltrarKeyReleased(evt);
            }
        });
        jPanel1.add(txtFiltrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 400, 260, 40));

        txtHabitat.setBackground(new java.awt.Color(231, 249, 228));
        txtHabitat.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtHabitat.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHabitat.setBorder(null);
        txtHabitat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHabitatActionPerformed(evt);
            }
        });
        txtHabitat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHabitatKeyReleased(evt);
            }
        });
        jPanel1.add(txtHabitat, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 200, 190, 30));

        txtEstado.setBackground(new java.awt.Color(231, 249, 228));
        txtEstado.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtEstado.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEstado.setBorder(null);
        txtEstado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEstadoFocusLost(evt);
            }
        });
        txtEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoActionPerformed(evt);
            }
        });
        txtEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEstadoKeyReleased(evt);
            }
        });
        jPanel1.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 240, 190, 30));

        txtNombre.setBackground(new java.awt.Color(231, 249, 228));
        txtNombre.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtNombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombre.setBorder(null);
        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNombreKeyReleased(evt);
            }
        });
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 190, 30));

        txtEspecie.setBackground(new java.awt.Color(231, 249, 228));
        txtEspecie.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtEspecie.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEspecie.setBorder(null);
        txtEspecie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEspecieActionPerformed(evt);
            }
        });
        txtEspecie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEspecieKeyReleased(evt);
            }
        });
        jPanel1.add(txtEspecie, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 160, 190, 30));

        txtEdad.setBackground(new java.awt.Color(231, 249, 228));
        txtEdad.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtEdad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEdad.setBorder(null);
        txtEdad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEdadActionPerformed(evt);
            }
        });
        txtEdad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEdadKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEdadKeyTyped(evt);
            }
        });
        jPanel1.add(txtEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 200, 190, 30));

        txtAlimento.setBackground(new java.awt.Color(231, 249, 228));
        txtAlimento.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtAlimento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAlimento.setBorder(null);
        txtAlimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlimentoActionPerformed(evt);
            }
        });
        txtAlimento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAlimentoKeyReleased(evt);
            }
        });
        jPanel1.add(txtAlimento, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 160, 190, 30));

        txtVeterinario.setBackground(new java.awt.Color(231, 249, 228));
        txtVeterinario.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtVeterinario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtVeterinario.setBorder(null);
        txtVeterinario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVeterinarioActionPerformed(evt);
            }
        });
        txtVeterinario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtVeterinarioKeyReleased(evt);
            }
        });
        jPanel1.add(txtVeterinario, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 200, 190, 30));

        btnLimpiar.setBackground(new java.awt.Color(160, 160, 160));
        btnLimpiar.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        btnLimpiar.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorder(null);
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 310, 270, 40));

        btnGuardar.setBackground(new java.awt.Color(45, 106, 79));
        btnGuardar.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Insertar");
        btnGuardar.setBorder(null);
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 260, 40));

        btnActualizar.setBackground(new java.awt.Color(233, 196, 106));
        btnActualizar.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("Actualizar");
        btnActualizar.setBorder(null);
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, 270, 40));

        txtSexo.setBackground(new java.awt.Color(231, 249, 228));
        txtSexo.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtSexo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSexo.setBorder(null);
        txtSexo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSexoFocusLost(evt);
            }
        });
        txtSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSexoActionPerformed(evt);
            }
        });
        txtSexo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSexoKeyReleased(evt);
            }
        });
        jPanel1.add(txtSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 190, 30));

        jLabel9.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        jLabel9.setText("Veterinario");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 200, -1, 30));

        ckVeterinario.setMaximumSize(new java.awt.Dimension(25, 25));
        ckVeterinario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckVeterinarioActionPerformed(evt);
            }
        });
        jPanel1.add(ckVeterinario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 200, -1, 30));

        ckNombre.setMaximumSize(new java.awt.Dimension(25, 25));
        ckNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckNombreActionPerformed(evt);
            }
        });
        jPanel1.add(ckNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, -1, 30));

        ckEdad.setMaximumSize(new java.awt.Dimension(25, 25));
        ckEdad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEdadActionPerformed(evt);
            }
        });
        jPanel1.add(ckEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 200, -1, 30));

        ckSexo.setMaximumSize(new java.awt.Dimension(25, 25));
        ckSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckSexoActionPerformed(evt);
            }
        });
        jPanel1.add(ckSexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, -1, 30));

        ckEstado.setMaximumSize(new java.awt.Dimension(25, 25));
        ckEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEstadoActionPerformed(evt);
            }
        });
        jPanel1.add(ckEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 240, -1, 30));

        ckHabitat.setMaximumSize(new java.awt.Dimension(25, 25));
        ckHabitat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckHabitatActionPerformed(evt);
            }
        });
        jPanel1.add(ckHabitat, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 200, -1, 30));

        ckEspecie.setMaximumSize(new java.awt.Dimension(25, 25));
        ckEspecie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckEspecieActionPerformed(evt);
            }
        });
        jPanel1.add(ckEspecie, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 160, -1, 30));

        ckAlimento.setMaximumSize(new java.awt.Dimension(25, 25));
        ckAlimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckAlimentoActionPerformed(evt);
            }
        });
        jPanel1.add(ckAlimento, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 160, -1, 30));

        btnBuscar.setBackground(new java.awt.Color(0, 102, 204));
        btnBuscar.setFont(new java.awt.Font("Consolas", 0, 18)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.setBorder(null);
        btnBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 400, 270, 40));

        lblConteo.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        lblConteo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lblConteo, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 760, 210, 30));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/zoologico.png"))); // NOI18N
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1070, 130));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1072, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    String nombre = txtNombre.getText().trim();
    
    if(nombre.isEmpty()) {
        showMessageDialog(this, "Selecciona un animal de la tabla o escribe su nombre.", "Campo Vacío", WARNING_MESSAGE);
        return;
    }

    int confirmacion = showConfirmDialog(this, 
            "¿Estás seguro de eliminar a: " + nombre + "?\n Se borrará todo su historial.", 
            "Confirmar Eliminación", 
            YES_NO_OPTION, 
            WARNING_MESSAGE);

    if(confirmacion == YES_OPTION){
        
        String sql = "{call sp_eliminar(?)}";
        
        try (java.sql.Connection con = Conexion.getConnection();
             java.sql.CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setString(1, nombre);
            
            cs.execute();
            
            showMessageDialog(this, "Animal eliminado correctamente.");
            
            limpiarCasillas();
            llenarTabla();
            
        } catch (java.sql.SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar:\n" + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltrarActionPerformed
       
    }//GEN-LAST:event_txtFiltrarActionPerformed

    private void txtHabitatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHabitatActionPerformed
        
    }//GEN-LAST:event_txtHabitatActionPerformed

    private void txtEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoActionPerformed
        
    }//GEN-LAST:event_txtEstadoActionPerformed

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtEspecieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEspecieActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEspecieActionPerformed

    private void txtEdadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEdadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdadActionPerformed

    private void txtAlimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlimentoActionPerformed

    private void txtVeterinarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVeterinarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVeterinarioActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCasillas();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
    if (txtNombre.getText().trim().isEmpty() || txtEdad.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, llena al menos el Nombre y la Edad.", "Campos vacíos", WARNING_MESSAGE);
        return;
    }

    int edad;
    try {
        edad = Integer.parseInt(txtEdad.getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "La edad debe ser un número válido.", "Error de Formato", ERROR_MESSAGE);
        return;
    }

    String sql = "{call sp_insertar(?, ?, ?, ?, ?, ?, ?, ?)}";


    try (java.sql.Connection con = Conexion.getConnection();
         java.sql.CallableStatement cs = con.prepareCall(sql)) {

        if (con == null) {
            JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la Base de Datos.", "Error de Conexión", ERROR_MESSAGE);
            return;
        }
        cs.setString(1, txtNombre.getText());
        cs.setInt(2, edad);
        cs.setString(3, txtSexo.getText());
        cs.setString(4, txtEstado.getText());
        cs.setString(5, txtEspecie.getText());     
        cs.setString(6, txtVeterinario.getText()); 
        cs.setString(7, txtHabitat.getText());     
        cs.setString(8, txtAlimento.getText());   

        cs.execute();

        showMessageDialog(this, "Registro guardado exitosamente.");
        
        limpiarCasillas();
        llenarTabla(); 

    } catch (SQLException ex) {
        showMessageDialog(this, "Error de Base de Datos:\n" + ex.getMessage(), "Error SQL",ERROR_MESSAGE);
    } catch (Exception ex) {
        showMessageDialog(this, "Error inesperado:\n" + ex.getMessage(), "Error", ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        int fila = Tabla.getSelectedRow();
        if (fila == -1) {
            showMessageDialog(this, "Selecciona un animal de la tabla para editar.","Ninguna selección",WARNING_MESSAGE);
            return;
        }
        
        if (txtNombre.getText().trim().isEmpty() || 
            txtEdad.getText().trim().isEmpty() ||
            txtSexo.getText().trim().isEmpty() ||
            txtEstado.getText().trim().isEmpty() ||
            txtEspecie.getText().trim().isEmpty() ||
            txtVeterinario.getText().trim().isEmpty() ||
            txtHabitat.getText().trim().isEmpty() ||
            txtAlimento.getText().trim().isEmpty()) {
        
            showMessageDialog(this, "Faltan datos.\nPor favor, asegúrate de llenar TODOS los campos antes de actualizar.", "Campos Vacíos", WARNING_MESSAGE);
            return; 
        }

        String nombreOriginal = Tabla.getValueAt(fila, 0).toString();
        
        String sql = "{call sp_actualizar(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (java.sql.Connection con = Conexion.getConnection();
             java.sql.CallableStatement cs = con.prepareCall(sql)) {

            int edad = Integer.parseInt(txtEdad.getText());

            cs.setString(1, nombreOriginal);       
            cs.setString(2, txtNombre.getText());   
            cs.setInt(3, edad);
            cs.setString(4, txtSexo.getText());
            cs.setString(5, txtEstado.getText());
            cs.setString(6, txtEspecie.getText());      
            cs.setString(7, txtVeterinario.getText());  
            cs.setString(8, txtHabitat.getText());      
            cs.setString(9, txtAlimento.getText());     

            cs.execute();

            showMessageDialog(this, "Registro actualizado correctamente.");
            limpiarCasillas();
            llenarTabla();

        } catch (NumberFormatException nfe) {
            showMessageDialog(this, "La edad debe ser un número.");
        } catch (SQLException ex) {
            showMessageDialog(this, "Error SQL: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSexoActionPerformed
        
    }//GEN-LAST:event_txtSexoActionPerformed

    private void txtFiltrarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltrarKeyReleased
        filtrar();
    }//GEN-LAST:event_txtFiltrarKeyReleased

    private void txtNombreKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyReleased
        if(ckNombre.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtNombreKeyReleased

    private void txtSexoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSexoKeyReleased
        if(ckSexo.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtSexoKeyReleased

    private void txtEstadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoKeyReleased
        if(ckEstado.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtEstadoKeyReleased

    private void txtEspecieKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEspecieKeyReleased
        if(ckEspecie.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtEspecieKeyReleased

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        checkboxver();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void ckNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckNombreActionPerformed
        filtrar();
    }//GEN-LAST:event_ckNombreActionPerformed

    private void ckEdadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEdadActionPerformed
        filtrar();
    }//GEN-LAST:event_ckEdadActionPerformed

    private void ckSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckSexoActionPerformed
        filtrar();
    }//GEN-LAST:event_ckSexoActionPerformed

    private void ckEspecieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEspecieActionPerformed
        filtrar();
    }//GEN-LAST:event_ckEspecieActionPerformed

    private void ckHabitatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckHabitatActionPerformed
        filtrar();
    }//GEN-LAST:event_ckHabitatActionPerformed

    private void ckEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckEstadoActionPerformed
        filtrar();
    }//GEN-LAST:event_ckEstadoActionPerformed

    private void ckAlimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckAlimentoActionPerformed
        filtrar();
    }//GEN-LAST:event_ckAlimentoActionPerformed

    private void ckVeterinarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckVeterinarioActionPerformed
        filtrar();
    }//GEN-LAST:event_ckVeterinarioActionPerformed

    private void txtEdadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadKeyReleased
        if(ckEdad.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtEdadKeyReleased

    private void txtHabitatKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHabitatKeyReleased
        if(ckHabitat.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtHabitatKeyReleased

    private void txtAlimentoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAlimentoKeyReleased
        if(ckAlimento.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtAlimentoKeyReleased

    private void txtVeterinarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVeterinarioKeyReleased
        if(ckVeterinario.isVisible()){
            filtrar();
        }
    }//GEN-LAST:event_txtVeterinarioKeyReleased

    private void txtEstadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEstadoFocusLost
        String texto = txtEstado.getText().trim();
    
        if (!texto.equals("ACTIVO") && !texto.equalsIgnoreCase("BAJA")) {
            txtEstado.setText("");
            showMessageDialog(null,"Datos incorrectos.\nSolo se aceptan 'ACTIVO' o 'BAJA'");
        }
    }//GEN-LAST:event_txtEstadoFocusLost

    private void txtSexoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSexoFocusLost
        String texto = txtSexo.getText().trim();
    
        if (!texto.equals("Macho") && !texto.equalsIgnoreCase("Hembra")) {
            txtSexo.setText("");
            showMessageDialog(null,"Datos incorrectos.\nSolo se aceptan 'Macho' o 'Hembra'");
        }
    }//GEN-LAST:event_txtSexoFocusLost

    private void txtEdadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEdadKeyTyped
        char c = evt.getKeyChar();
        
        if(!Character.isDigit(c)){
            evt.consume();
        }
        
        if(txtEdad.getText().length() >= 3){
            evt.consume();
        }
    }//GEN-LAST:event_txtEdadKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JCheckBox ckAlimento;
    private javax.swing.JCheckBox ckEdad;
    private javax.swing.JCheckBox ckEspecie;
    private javax.swing.JCheckBox ckEstado;
    private javax.swing.JCheckBox ckHabitat;
    private javax.swing.JCheckBox ckNombre;
    private javax.swing.JCheckBox ckSexo;
    private javax.swing.JCheckBox ckVeterinario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblConteo;
    private javax.swing.JTextField txtAlimento;
    private javax.swing.JTextField txtEdad;
    private javax.swing.JTextField txtEspecie;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtFiltrar;
    private javax.swing.JTextField txtHabitat;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSexo;
    private javax.swing.JTextField txtVeterinario;
    // End of variables declaration//GEN-END:variables
}
