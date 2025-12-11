package BotonesAcciones;

import java.awt.Color;
import table.*;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class TableActionCellRender extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        PanelAccion action =new PanelAccion();
        if (isSelected==false && row%2==0) {
            action.setBackground(Color.white);
        } else {
            action.setBackground(com.getBackground());
        }
        
        action.setBackground(com.getBackground());
        return action;
    }
}
