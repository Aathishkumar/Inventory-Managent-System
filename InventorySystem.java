import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InventorySystem {

    static JLabel totalLabel;

    public InventorySystem() {

        JFrame f = new JFrame("Inventory System");

        Color primary = new Color(0,161,155);
        Color bg = new Color(228,221,211);

        f.getContentPane().setBackground(bg);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLayout(new BorderLayout());

        // 🔥 HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(primary);
        header.setPreferredSize(new Dimension(100,70));

        JLabel title = new JLabel("INVENTORY MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JButton back = new JButton("←");
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setBackground(Color.WHITE);

        back.addActionListener(e -> {
            f.dispose();
            new LoginPage();
        });

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);

        // 🔥 MAIN PANEL
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bg);

        // 🔥 CARD
        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(900, 520));
        card.setBackground(Color.WHITE);

        // 🔹 INPUTS
        JLabel l1 = new JLabel("Name");
        l1.setBounds(100,40,100,20);

        JTextField name = new JTextField();
        name.setBounds(100,60,250,35);

        JLabel l2 = new JLabel("Quantity");
        l2.setBounds(380,40,100,20);

        JTextField qty = new JTextField();
        qty.setBounds(380,60,150,35);

        JLabel l3 = new JLabel("Price");
        l3.setBounds(560,40,100,20);

        JTextField price = new JTextField();
        price.setBounds(560,60,150,35);

        // 🔥 INPUT STYLE
        name.setBorder(BorderFactory.createMatteBorder(0,0,2,0,primary));
        qty.setBorder(BorderFactory.createMatteBorder(0,0,2,0,primary));
        price.setBorder(BorderFactory.createMatteBorder(0,0,2,0,primary));

        // 🔥 BUTTONS
        JButton add = new JButton("Add");
        add.setBounds(200,120,120,40);
        add.setBackground(primary);
        add.setForeground(Color.WHITE);

        JButton update = new JButton("Update");
        update.setBounds(350,120,120,40);
        update.setBackground(new Color(52,152,219));
        update.setForeground(Color.WHITE);

        JButton delete = new JButton("Delete");
        delete.setBounds(500,120,120,40);
        delete.setBackground(new Color(231,76,60));
        delete.setForeground(Color.WHITE);

        add.setFocusPainted(false);
        update.setFocusPainted(false);
        delete.setFocusPainted(false);

        // 🔥 TABLE
        JTable table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,200,800,240);

        // 🔥 TABLE STYLE
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.getTableHeader().setBackground(primary);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // 🔥 ZEBRA ROWS
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table,Object value,
                    boolean isSelected,boolean hasFocus,int row,int col){

                Component c = super.getTableCellRendererComponent(
                        table,value,isSelected,hasFocus,row,col);

                if(!isSelected){
                    if(row % 2 == 0)
                        c.setBackground(new Color(245,245,245));
                    else
                        c.setBackground(Color.WHITE);
                }

                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        // 🔥 TOTAL
        totalLabel = new JLabel("Total Value: 0");
        totalLabel.setBounds(50,460,300,30);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // LOAD TABLE
        loadTable(table);

        // 🔥 ADD
        add.addActionListener(e -> {
            try {
                String n = name.getText().trim();
                String q = qty.getText().trim();
                String p = price.getText().trim();

                if(n.isEmpty() || q.isEmpty() || p.isEmpty()){
                    JOptionPane.showMessageDialog(f, "All fields required!");
                    return;
                }

                int quantity = Integer.parseInt(q);
                double priceVal = Double.parseDouble(p);

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/inventory_db",
                        "root",
                        "dbms"
                );

                PreparedStatement pst = con.prepareStatement(
                        "INSERT INTO products(name,quantity,price) VALUES(?,?,?)"
                );

                pst.setString(1, n);
                pst.setInt(2, quantity);
                pst.setDouble(3, priceVal);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(f, "Product Added ✅");

                loadTable(table);

                name.setText("");
                qty.setText("");
                price.setText("");

                con.close();

            } catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(f, "Enter valid numbers!");
            } catch(Exception ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(f, "Database Error!");
            }
        });

        // 🔥 UPDATE
        update.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if(row==-1){ JOptionPane.showMessageDialog(f,"Select row"); return; }

                int id = Integer.parseInt(table.getValueAt(row,0).toString());

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/inventory_db",
                        "root",
                        "dbms"
                );

                PreparedStatement pst = con.prepareStatement(
                        "UPDATE products SET name=?,quantity=?,price=? WHERE id=?"
                );

                pst.setString(1,name.getText());
                pst.setInt(2,Integer.parseInt(qty.getText()));
                pst.setDouble(3,Double.parseDouble(price.getText()));
                pst.setInt(4,id);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(f,"Updated!");
                loadTable(table);

                con.close();

            } catch(Exception ex){
                ex.printStackTrace();
            }
        });

        // 🔥 DELETE
        delete.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();
                if(row==-1){ JOptionPane.showMessageDialog(f,"Select row"); return; }

                int id = Integer.parseInt(table.getValueAt(row,0).toString());

                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/inventory_db",
                        "root",
                        "dbms"
                );

                PreparedStatement pst = con.prepareStatement(
                        "DELETE FROM products WHERE id=?"
                );

                pst.setInt(1,id);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(f,"Deleted!");
                loadTable(table);

                con.close();

            } catch(Exception ex){
                ex.printStackTrace();
            }
        });

        // 🔥 ROW CLICK
        table.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = table.getSelectedRow();
                name.setText(table.getValueAt(row,1).toString());
                qty.setText(table.getValueAt(row,2).toString());
                price.setText(table.getValueAt(row,3).toString());
            }
        });

        // ADD COMPONENTS
        card.add(l1); card.add(name);
        card.add(l2); card.add(qty);
        card.add(l3); card.add(price);
        card.add(add); card.add(update); card.add(delete);
        card.add(sp);
        card.add(totalLabel);

        mainPanel.add(card);

        f.add(header, BorderLayout.NORTH);
        f.add(mainPanel, BorderLayout.CENTER);

        f.setVisible(true);
    }

    // 🔥 LOAD TABLE
    public static void loadTable(JTable table){
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventory_db",
                    "root",
                    "dbms"
            );

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM products");

            java.util.ArrayList<String[]> list = new java.util.ArrayList<>();
            double total = 0;

            while(rs.next()){
                int q = rs.getInt("quantity");
                double p = rs.getDouble("price");
                total += q*p;

                list.add(new String[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("quantity"),
                        rs.getString("price")
                });
            }

            totalLabel.setText("Total Value: " + total);

            String[][] data = new String[list.size()][4];
            for(int i=0;i<list.size();i++) data[i]=list.get(i);

            String[] cols = {"ID","Name","Qty","Price"};
            table.setModel(new DefaultTableModel(data, cols));

            con.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}