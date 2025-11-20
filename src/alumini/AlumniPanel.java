/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package alumini;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author piyush
 */
public class AlumniPanel extends javax.swing.JPanel { // ✅ Fixed Class Name

    /**
     * Creates new form AlumniPanel
     */
    public AlumniPanel() {
        initComponents();
        loadAlumniData(); // Load data when panel opens
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jLabel2.setText("SEARCH ALUMNI:");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(jLabel2))
                .addGap(1, 1, 1))
        );

        jPanel3.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Name", "Email", "Batch", "Dept", "Company", "Designation"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnAdd)
                .addGap(12, 12, 12)
                .addComponent(btnEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelete)
                .addGap(18, 18, 18)
                .addComponent(btnRefresh)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete)
                    .addComponent(btnRefresh))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void loadAlumniData() {
        try (Connection conn = dbconnection.getConnection()) {
            String sql = "SELECT * FROM alumni";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("alumni_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getInt("batch"),
                    rs.getString("department"),
                    rs.getString("company"),
                    rs.getString("designation")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading alumni: " + e.getMessage());
        }
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {                                          
        String searchText = txtSearch.getText().trim();
        if (searchText.isEmpty()) {
             loadAlumniData();
             return;
        }
        try (Connection conn = dbconnection.getConnection()) {
            String sql = "SELECT * FROM alumni WHERE name LIKE ? OR company LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + searchText + "%");
            ps.setString(2, "%" + searchText + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("alumni_id"), rs.getString("name"), rs.getString("email"),
                    rs.getInt("batch"), rs.getString("department"), rs.getString("company"), rs.getString("designation")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage());
        }
    }                                         

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {                                       
        JOptionPane.showMessageDialog(this, "Please use the Registration form to add Alumni properly.");
    }                                      

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // Simple edit logic placeholder
         int row = jTable1.getSelectedRow();
         if(row == -1) return;
         String id = jTable1.getValueAt(row, 0).toString();
         String currentCompany = jTable1.getValueAt(row, 5).toString();
         String newCompany = JOptionPane.showInputDialog(this, "Edit Company:", currentCompany);
         
         if(newCompany != null) {
             try (Connection conn = dbconnection.getConnection()) {
                 String sql = "UPDATE alumni SET company = ? WHERE alumni_id = ?";
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ps.setString(1, newCompany);
                 ps.setString(2, id);
                 ps.executeUpdate();
                 loadAlumniData();
             } catch(Exception e) {
                 JOptionPane.showMessageDialog(this, "Error update: " + e.getMessage());
             }
         }
    }                                       

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int row = jTable1.getSelectedRow();
        if(row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete");
            return;
        }
        String id = jTable1.getValueAt(row, 0).toString();
        try (Connection conn = dbconnection.getConnection()) {
             String sql = "DELETE FROM alumni WHERE alumni_id = ?";
             PreparedStatement ps = conn.prepareStatement(sql);
             ps.setString(1, id);
             ps.executeUpdate();
             loadAlumniData();
        } catch(Exception e) {
             JOptionPane.showMessageDialog(this, "Error deleting: " + e.getMessage());
        }
    }                                         

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {                                           
        loadAlumniData();
    }                                          

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration                   
}