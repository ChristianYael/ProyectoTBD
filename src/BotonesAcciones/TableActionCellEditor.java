/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BotonesAcciones;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Tadeo Nuñez
 */
public class TableActionCellEditor extends DefaultCellEditor{
    private TableActionEvent event;
    public TableActionCellEditor(TableActionEvent event) {
        super(new JCheckBox());
        this.event=event;
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PanelAccion action=new PanelAccion();
        action.iniEvent(event, row);
        action.setBackground(table.getSelectionBackground());
        return action;
    }
}
