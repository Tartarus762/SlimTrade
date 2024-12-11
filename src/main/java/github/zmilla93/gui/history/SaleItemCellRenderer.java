package github.zmilla93.gui.history;

import github.zmilla93.core.data.SaleItemWrapper;
import github.zmilla93.gui.components.CurrencyLabelFactory;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Renders a list of SaleItems.
 */
public class SaleItemCellRenderer extends JLabel implements TableCellRenderer {

    public SaleItemCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll();
        CurrencyLabelFactory.applyItemToComponent(this, ((SaleItemWrapper) value).items);
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }

}
