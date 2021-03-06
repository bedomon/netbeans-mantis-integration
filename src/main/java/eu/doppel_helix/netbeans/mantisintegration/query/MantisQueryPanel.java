package eu.doppel_helix.netbeans.mantisintegration.query;

import biz.futureware.mantisconnect.ObjectRef;
import eu.doppel_helix.netbeans.mantisintegration.Mantis;
import eu.doppel_helix.netbeans.mantisintegration.swing.AccountDataListCellRenderer;
import eu.doppel_helix.netbeans.mantisintegration.swing.FilterDataListCellRenderer;
import eu.doppel_helix.netbeans.mantisintegration.swing.FullSizeLayout;
import eu.doppel_helix.netbeans.mantisintegration.swing.NoopListener;
import eu.doppel_helix.netbeans.mantisintegration.swing.ObjectRefListCellRenderer;
import eu.doppel_helix.netbeans.mantisintegration.swing.ProjectListCellRenderer;
import eu.doppel_helix.netbeans.mantisintegration.swing.StringNullSaveListCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.math.BigInteger;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;

public class MantisQueryPanel extends javax.swing.JPanel {

    private final Map<BigInteger, Color> colorMap = Mantis.getInstance().getStatusColorMap();
    // Swallow Mouseevents
    private static final NoopListener noopListener = new NoopListener();
    private QueryListModel queryListModel = new QueryListModel();
    JPanel waitPanel;

    public QueryListModel getQueryListModel() {
        return queryListModel;
    }
    
    public MantisQueryPanel() {
        initComponents();
        waitPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
                Rectangle r = getBounds();
                g2.fillRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
                super.paintComponent(g);
            }

            @Override
            public void setVisible(boolean aFlag) {
                super.setVisible(aFlag);
                requestFocus();
            }
        };
        JLabel label = new JLabel("Busy");
        label.setFont(label.getFont().deriveFont(label.getFont().getStyle() & java.awt.Font.BOLD,
                AffineTransform.getScaleInstance(4, 4)));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        waitPanel.add(label);
        waitPanel.setFocusable(true);
        waitPanel.setOpaque(false);
        waitPanel.setVisible(false);
        // Swallow Mouse + Keyboard events
        waitPanel.addMouseListener(noopListener);
        waitPanel.addKeyListener(noopListener);
        innerQuery.add(waitPanel, JLayeredPane.MODAL_LAYER);
        innerQuery.setLayout(new FullSizeLayout());
        
        DefaultTableColumnModelExt  tcm = new DefaultTableColumnModelExt();
        
        TableColumnExt tce;
        
        tce = new TableColumnExt(0);
        tce.setTitle("ID");
        tce.setToolTipText("Identifier");
        tce.setMinWidth(0);
        tce.setPreferredWidth(40);
        tce.setMaxWidth(40);
        tcm.addColumn(tce);
        
        tce = new TableColumnExt(1);
        tce.setTitle("#");
        tce.setToolTipText("Note count");
        tce.setMinWidth(0);
        tce.setPreferredWidth(40);
        tce.setMaxWidth(40);
        tcm.addColumn(tce);
        
        tce = new TableColumnExt(2);
        tce.setTitle("Category");
        tce.setToolTipText("Category");
        tce.setMinWidth(0);
        tce.setPreferredWidth(80);
        tce.setMaxWidth(80);
        tcm.addColumn(tce);
        
        tce = new TableColumnExt(3);
        tce.setTitle("Severity");
        tce.setToolTipText("Severity");
        tce.setCellRenderer(new MantisObjectRefCellRenderer());
        tce.setMinWidth(0);
        tce.setPreferredWidth(80);
        tce.setMaxWidth(80);
        tcm.addColumn(tce);
        
        tce = new TableColumnExt(4);
        tce.setTitle("Priority");
        tce.setToolTipText("Priority");
        tce.setCellRenderer(new MantisObjectRefCellRenderer());
        tce.setMinWidth(0);
        tce.setPreferredWidth(80);
        tce.setMaxWidth(80);
        tcm.addColumn(tce);
        
        tce = new TableColumnExt(5);
        tce.setTitle("Status");
        tce.setToolTipText("Status");
        tce.setCellRenderer(new MantisObjectRefCellRenderer());
        tce.setHighlighters(new MantisStatusHighlighter());
        tce.setMinWidth(0);
        tce.setPreferredWidth(80);
        tce.setMaxWidth(80);
        tcm.addColumn(tce);
        
        tce = new TableColumnExt(6);
        tce.setTitle("Updated");
        tce.setToolTipText("Updated");
        tce.setCellRenderer(new MantisCalendarCellRenderer());
        tce.setMinWidth(0);
        tce.setPreferredWidth(80);
        tce.setMaxWidth(80);
        tcm.addColumn(tce);
        
        tce = new TableColumnExt(7);
        tce.setTitle("Summary");
        tce.setToolTipText("Summary");
        tce.setPrototypeValue("Ein doch recht langer Text als Prototyp sollte genug Platz sichern!");
        tce.setPreferredWidth(250);
        tcm.addColumn(tce);

        issueTable.getTableHeader().setReorderingAllowed(false);
        issueTable.setColumnModel(tcm);
        issueTable.setColumnControlVisible(true);
        issueTable.doLayout();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        gotoIssuePanel = new javax.swing.JPanel();
        gotoIssueLabel = new javax.swing.JLabel();
        gotoIssueTextField = new javax.swing.JFormattedTextField();
        gotoIssueButton = new javax.swing.JButton();
        innerQuery = new javax.swing.JLayeredPane();
        mainPanel = new javax.swing.JPanel();
        filterPanel = new javax.swing.JPanel();
        reporterLabel = new javax.swing.JLabel();
        reporterComboBox = new javax.swing.JComboBox();
        assignedToLabel = new javax.swing.JLabel();
        assignedToComboBox = new javax.swing.JComboBox();
        categoryLabel = new javax.swing.JLabel();
        categoryComboBox = new javax.swing.JComboBox();
        severityLabel = new javax.swing.JLabel();
        severityComboBox = new javax.swing.JComboBox();
        resolutionLabel = new javax.swing.JLabel();
        resolutionComboBox = new javax.swing.JComboBox();
        statusLabel = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox();
        priorityLabel = new javax.swing.JLabel();
        priorityComboBox = new javax.swing.JComboBox();
        viewStatusLabel = new javax.swing.JLabel();
        viewStatusComboBox = new javax.swing.JComboBox();
        lastUpdateAfterLabel = new javax.swing.JLabel();
        lastUpdateAfterDatePicker = new org.jdesktop.swingx.JXDatePicker();
        lastUpdateBeforeLabel = new javax.swing.JLabel();
        lastUpdateBeforeDatePicker = new org.jdesktop.swingx.JXDatePicker();
        summaryLabel = new javax.swing.JLabel();
        summaryTextField = new javax.swing.JTextField();
        matchTypeLabel = new javax.swing.JLabel();
        matchTypeComboBox = new javax.swing.JComboBox();
        projectLabel = new javax.swing.JLabel();
        projectComboBox = new javax.swing.JComboBox();
        filterComboBox = new javax.swing.JComboBox();
        filterLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        headerButtonsPanel = new javax.swing.JPanel();
        deleteQueryLinkButton = new org.jdesktop.swingx.JXHyperlink();
        buttonsPanel = new javax.swing.JPanel();
        saveQueryButton = new javax.swing.JButton();
        executeQueryButton = new javax.swing.JButton();
        issueTablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        issueTable = new org.jdesktop.swingx.JXTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        gotoIssuePanel.setLayout(flowLayout1);

        org.openide.awt.Mnemonics.setLocalizedText(gotoIssueLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.gotoIssueLabel.text")); // NOI18N
        gotoIssuePanel.add(gotoIssueLabel);

        gotoIssueTextField.setColumns(7);
        gotoIssueTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        gotoIssueTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gotoIssueTextField.setText(org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.gotoIssueTextField.text")); // NOI18N
        gotoIssueTextField.setMinimumSize(new java.awt.Dimension(500, 19));
        gotoIssuePanel.add(gotoIssueTextField);

        org.openide.awt.Mnemonics.setLocalizedText(gotoIssueButton, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.gotoIssueButton.text")); // NOI18N
        gotoIssuePanel.add(gotoIssueButton);

        add(gotoIssuePanel, java.awt.BorderLayout.NORTH);

        mainPanel.setBackground(javax.swing.UIManager.getDefaults().getColor("TextArea.background"));
        mainPanel.setLayout(new java.awt.GridBagLayout());

        filterPanel.setBackground(new java.awt.Color(255, 255, 255));
        filterPanel.setLayout(new java.awt.GridBagLayout());

        reporterLabel.setFont(reporterLabel.getFont().deriveFont(reporterLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(reporterLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.reporterLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(reporterLabel, gridBagConstraints);

        reporterComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        reporterComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        reporterComboBox.setPrototypeDisplayValue("XXXXXXXX");
        reporterComboBox.setRenderer(new AccountDataListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(reporterComboBox, gridBagConstraints);

        assignedToLabel.setFont(assignedToLabel.getFont().deriveFont(assignedToLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(assignedToLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.assignedToLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(assignedToLabel, gridBagConstraints);

        assignedToComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        assignedToComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        assignedToComboBox.setPrototypeDisplayValue("XXXXXXXX");
        assignedToComboBox.setRenderer(new AccountDataListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(assignedToComboBox, gridBagConstraints);

        categoryLabel.setFont(categoryLabel.getFont().deriveFont(categoryLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(categoryLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.categoryLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(categoryLabel, gridBagConstraints);

        categoryComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        categoryComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        categoryComboBox.setPrototypeDisplayValue("XXXXXXXX");
        categoryComboBox.setRenderer(new StringNullSaveListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(categoryComboBox, gridBagConstraints);

        severityLabel.setFont(severityLabel.getFont().deriveFont(severityLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(severityLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.severityLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(severityLabel, gridBagConstraints);

        severityComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        severityComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        severityComboBox.setPrototypeDisplayValue("XXXXXXXX");
        severityComboBox.setRenderer(new ObjectRefListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(severityComboBox, gridBagConstraints);

        resolutionLabel.setFont(resolutionLabel.getFont().deriveFont(resolutionLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(resolutionLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.resolutionLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(resolutionLabel, gridBagConstraints);

        resolutionComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        resolutionComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        resolutionComboBox.setPrototypeDisplayValue("XXXXXXXX");
        resolutionComboBox.setRenderer(new ObjectRefListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(resolutionComboBox, gridBagConstraints);

        statusLabel.setFont(statusLabel.getFont().deriveFont(statusLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(statusLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.statusLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(statusLabel, gridBagConstraints);

        statusComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        statusComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        statusComboBox.setPrototypeDisplayValue("XXXXXXXX");
        statusComboBox.setRenderer(new eu.doppel_helix.netbeans.mantisintegration.swing.StatusListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(statusComboBox, gridBagConstraints);
        statusComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object value = statusComboBox.getSelectedItem();

                Color color = null;

                if(value instanceof ObjectRef) {
                    ObjectRef or = (ObjectRef)value;
                    BigInteger level = or.getId();
                    color = colorMap.get(level);
                }

                if(color == null) {
                    color = Color.WHITE;
                }

                statusComboBox.setBackground(color);
            }
        });

        priorityLabel.setFont(priorityLabel.getFont().deriveFont(priorityLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(priorityLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.priorityLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(priorityLabel, gridBagConstraints);

        priorityComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        priorityComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        priorityComboBox.setPrototypeDisplayValue("XXXXXXXX");
        priorityComboBox.setRenderer(new ObjectRefListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(priorityComboBox, gridBagConstraints);

        viewStatusLabel.setFont(viewStatusLabel.getFont().deriveFont(viewStatusLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(viewStatusLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.viewStatusLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(viewStatusLabel, gridBagConstraints);

        viewStatusComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        viewStatusComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        viewStatusComboBox.setPrototypeDisplayValue("XXXXXXXX");
        viewStatusComboBox.setRenderer(new ObjectRefListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(viewStatusComboBox, gridBagConstraints);

        lastUpdateAfterLabel.setFont(lastUpdateAfterLabel.getFont().deriveFont(lastUpdateAfterLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(lastUpdateAfterLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.lastUpdateAfterLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(lastUpdateAfterLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(lastUpdateAfterDatePicker, gridBagConstraints);

        lastUpdateBeforeLabel.setFont(lastUpdateBeforeLabel.getFont().deriveFont(lastUpdateBeforeLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(lastUpdateBeforeLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.lastUpdateBeforeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(lastUpdateBeforeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(lastUpdateBeforeDatePicker, gridBagConstraints);

        summaryLabel.setFont(summaryLabel.getFont().deriveFont(summaryLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(summaryLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.summaryLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(summaryLabel, gridBagConstraints);

        summaryTextField.setText(org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.summaryTextField.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(summaryTextField, gridBagConstraints);

        matchTypeLabel.setFont(matchTypeLabel.getFont().deriveFont(matchTypeLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(matchTypeLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.matchTypeLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(matchTypeLabel, gridBagConstraints);

        matchTypeComboBox.setFont(matchTypeComboBox.getFont().deriveFont(matchTypeComboBox.getFont().getStyle() & ~java.awt.Font.BOLD));
        matchTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "All conditions", "Any conditions" }));
        matchTypeComboBox.setMinimumSize(new java.awt.Dimension(118, 23));
        matchTypeComboBox.setPreferredSize(new java.awt.Dimension(118, 23));
        matchTypeComboBox.setPrototypeDisplayValue("XXXXXXXX");
        matchTypeComboBox.setRenderer(new ObjectRefListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(matchTypeComboBox, gridBagConstraints);

        projectLabel.setFont(projectLabel.getFont().deriveFont(projectLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(projectLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.projectLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(projectLabel, gridBagConstraints);

        projectComboBox.setActionCommand(org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.projectComboBox.actionCommand")); // NOI18N
        projectComboBox.setMinimumSize(new java.awt.Dimension(100, 24));
        projectComboBox.setPreferredSize(new java.awt.Dimension(200, 24));
        projectComboBox.setPrototypeDisplayValue("XXXXXXXX");
        projectComboBox.setRenderer(new ProjectListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(projectComboBox, gridBagConstraints);

        filterComboBox.setActionCommand(org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.filterComboBox.actionCommand")); // NOI18N
        filterComboBox.setMinimumSize(new java.awt.Dimension(100, 24));
        filterComboBox.setPreferredSize(new java.awt.Dimension(200, 24));
        filterComboBox.setPrototypeDisplayValue("XXXXXXXX");
        filterComboBox.setRenderer(new FilterDataListCellRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(filterComboBox, gridBagConstraints);

        filterLabel.setFont(filterLabel.getFont().deriveFont(filterLabel.getFont().getStyle() & ~java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(filterLabel, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.filterLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(filterLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(jSeparator1, gridBagConstraints);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.jLabel1.text")); // NOI18N
        jLabel1.setAlignmentY(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.jLabel2.text")); // NOI18N
        jLabel2.setAlignmentY(0.0F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        filterPanel.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        mainPanel.add(filterPanel, gridBagConstraints);

        headerButtonsPanel.setAlignmentX(1.0F);
        headerButtonsPanel.setOpaque(false);

        deleteQueryLinkButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        org.openide.awt.Mnemonics.setLocalizedText(deleteQueryLinkButton, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.deleteQueryLinkButton.text")); // NOI18N
        deleteQueryLinkButton.setActionCommand(org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.deleteQueryLinkButton.actionCommand")); // NOI18N
        deleteQueryLinkButton.setFont(deleteQueryLinkButton.getFont().deriveFont(deleteQueryLinkButton.getFont().getStyle() & ~java.awt.Font.BOLD));
        headerButtonsPanel.add(deleteQueryLinkButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        mainPanel.add(headerButtonsPanel, gridBagConstraints);

        buttonsPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        buttonsPanel.setOpaque(false);
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT);
        flowLayout3.setAlignOnBaseline(true);
        buttonsPanel.setLayout(flowLayout3);

        org.openide.awt.Mnemonics.setLocalizedText(saveQueryButton, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.saveQueryButton.text")); // NOI18N
        saveQueryButton.setActionCommand(org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.saveQueryButton.actionCommand")); // NOI18N
        buttonsPanel.add(saveQueryButton);

        org.openide.awt.Mnemonics.setLocalizedText(executeQueryButton, org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.executeQueryButton.text")); // NOI18N
        executeQueryButton.setActionCommand(org.openide.util.NbBundle.getMessage(MantisQueryPanel.class, "MantisQueryPanel.executeQueryButton.actionCommand")); // NOI18N
        buttonsPanel.add(executeQueryButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        mainPanel.add(buttonsPanel, gridBagConstraints);

        issueTablePanel.setMinimumSize(new java.awt.Dimension(20, 20));
        issueTablePanel.setOpaque(false);
        issueTablePanel.setPreferredSize(new java.awt.Dimension(20, 20));
        issueTablePanel.setLayout(new java.awt.BorderLayout());

        issueTable.setAutoCreateColumnsFromModel(false);
        issueTable.setModel(queryListModel);
        jScrollPane1.setViewportView(issueTable);

        issueTablePanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(issueTablePanel, gridBagConstraints);

        innerQuery.add(mainPanel);
        mainPanel.setBounds(0, 0, 913, 294);

        add(innerQuery, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JComboBox assignedToComboBox;
    javax.swing.JLabel assignedToLabel;
    javax.swing.JPanel buttonsPanel;
    javax.swing.JComboBox categoryComboBox;
    javax.swing.JLabel categoryLabel;
    org.jdesktop.swingx.JXHyperlink deleteQueryLinkButton;
    javax.swing.JButton executeQueryButton;
    javax.swing.JComboBox filterComboBox;
    javax.swing.JLabel filterLabel;
    javax.swing.JPanel filterPanel;
    javax.swing.JButton gotoIssueButton;
    javax.swing.JLabel gotoIssueLabel;
    javax.swing.JPanel gotoIssuePanel;
    javax.swing.JFormattedTextField gotoIssueTextField;
    javax.swing.JPanel headerButtonsPanel;
    javax.swing.JLayeredPane innerQuery;
    org.jdesktop.swingx.JXTable issueTable;
    javax.swing.JPanel issueTablePanel;
    javax.swing.JLabel jLabel1;
    javax.swing.JLabel jLabel2;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JSeparator jSeparator1;
    org.jdesktop.swingx.JXDatePicker lastUpdateAfterDatePicker;
    javax.swing.JLabel lastUpdateAfterLabel;
    org.jdesktop.swingx.JXDatePicker lastUpdateBeforeDatePicker;
    javax.swing.JLabel lastUpdateBeforeLabel;
    javax.swing.JPanel mainPanel;
    javax.swing.JComboBox matchTypeComboBox;
    javax.swing.JLabel matchTypeLabel;
    javax.swing.JComboBox priorityComboBox;
    javax.swing.JLabel priorityLabel;
    javax.swing.JComboBox projectComboBox;
    javax.swing.JLabel projectLabel;
    javax.swing.JComboBox reporterComboBox;
    javax.swing.JLabel reporterLabel;
    javax.swing.JComboBox resolutionComboBox;
    javax.swing.JLabel resolutionLabel;
    javax.swing.JButton saveQueryButton;
    javax.swing.JComboBox severityComboBox;
    javax.swing.JLabel severityLabel;
    javax.swing.JComboBox statusComboBox;
    javax.swing.JLabel statusLabel;
    javax.swing.JLabel summaryLabel;
    javax.swing.JTextField summaryTextField;
    javax.swing.JComboBox viewStatusComboBox;
    javax.swing.JLabel viewStatusLabel;
    // End of variables declaration//GEN-END:variables

}
