package eu.doppel_helix.netbeans.mantisintegration.issue;

import biz.futureware.mantisconnect.AccountData;
import biz.futureware.mantisconnect.AttachmentData;
import biz.futureware.mantisconnect.IssueData;
import biz.futureware.mantisconnect.IssueNoteData;
import biz.futureware.mantisconnect.ObjectRef;
import biz.futureware.mantisconnect.ProjectData;
import biz.futureware.mantisconnect.RelationshipData;
import eu.doppel_helix.netbeans.mantisintegration.repository.MantisRepository;
import eu.doppel_helix.netbeans.mantisintegration.swing.AttachmentDisplay;
import eu.doppel_helix.netbeans.mantisintegration.swing.ListBackedComboBoxModel;
import eu.doppel_helix.netbeans.mantisintegration.swing.NoteDisplay;
import eu.doppel_helix.netbeans.mantisintegration.swing.RelationshipDisplay;
import eu.doppel_helix.netbeans.mantisintegration.swing.TagDisplay;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.xml.rpc.ServiceException;
import org.netbeans.modules.bugtracking.spi.BugtrackingController;
import org.netbeans.modules.bugtracking.spi.IssueProvider;
import org.netbeans.modules.bugtracking.util.LinkButton;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.windows.WindowManager;

public class MantisIssueController extends BugtrackingController implements PropertyChangeListener, ActionListener {

    private static final Logger logger = Logger.getLogger(MantisIssueController.class.getName());
    private static File lastDirectory;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ListBackedComboBoxModel<ProjectData> projectModel = new ListBackedComboBoxModel<ProjectData>(ProjectData.class);
    private ListBackedComboBoxModel<ObjectRef> viewstatesModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> viewstatesModel2 = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> severitiesModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> reproducibilitiesModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> prioritiesModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> resolutionsModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> statesModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> etasModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<ObjectRef> projectionsModel = new ListBackedComboBoxModel<ObjectRef>(ObjectRef.class);
    private ListBackedComboBoxModel<String> categoriesModel = new ListBackedComboBoxModel<String>(String.class);
    private ListBackedComboBoxModel<AccountData> assignedModel = new ListBackedComboBoxModel<AccountData>(AccountData.class);
    private MantisIssuePanel panel;
    private MantisIssue issue;
    private StateMonitor stateMonitor = new StateMonitor();

    private final SwingWorker updateModel = new SwingWorker() {
        List<ProjectData> projects;
        List<ObjectRef> viewStates;
        List<ObjectRef> severities;
        List<ObjectRef> reproducibilities;
        List<ObjectRef> resolutions;
        List<ObjectRef> priorities;
        List<ObjectRef> states;
        List<ObjectRef> etas;
        List<ObjectRef> projections;
        private Throwable t;

        @Override
        protected Object doInBackground() {
            try {
                MantisRepository mr = issue.getMantisRepository();
                viewStates = Arrays.asList(mr.getViewStates());
                projects = new ArrayList<ProjectData>();
                projects.add(null);
                projects.addAll(Arrays.asList(mr.getProjects()));
                severities = Arrays.asList(mr.getSeverities());
                reproducibilities = Arrays.asList(mr.getReproducibilities());
                resolutions = Arrays.asList(mr.getResolutions());
                priorities = Arrays.asList(mr.getPriorities());
                states = new ArrayList<ObjectRef>(Arrays.asList(mr.getStates()));
                states.add(0, null);
                etas = Arrays.asList(mr.getEtas());
                projections = Arrays.asList(mr.getProjections());
            } catch (ServiceException ex) {
                t = ex;
            } catch (RemoteException ex) {
                t = ex;
            }
            return null;
        }
        
        @Override
        protected void done() {
            if (t != null) {
                NotifyDescriptor nd = new NotifyDescriptor.Exception(t,
                        "Failed to update ");
                DialogDisplayer.getDefault().notifyLater(nd);
            } else {
                projectModel.setBackingList(projects);
                viewstatesModel.setBackingList(viewStates);
                viewstatesModel2.setBackingList(viewStates);
                viewstatesModel2.setSelectedItem(viewStates.get(0));
                severitiesModel.setBackingList(severities);
                reproducibilitiesModel.setBackingList(reproducibilities);
                prioritiesModel.setBackingList(priorities);
                resolutionsModel.setBackingList(resolutions);
                statesModel.setBackingList(states);
                etasModel.setBackingList(etas);
                projectionsModel.setBackingList(projections);
                updateInfo(null);
            }
            issue.setBusy(false);

        }
    };
    
    public MantisIssueController(final MantisIssue issue) {
        this.issue = issue;
        issue.addPropertyChangeListener(this);
        issue.setBusy(true);
        updateModel.execute();
    }

    @Override
    public JComponent getComponent() {
        if (panel == null) {
            panel = new MantisIssuePanel();
            panel.addNoteViewStateComboBox.setModel(viewstatesModel2);
            panel.projectComboBox.setModel(projectModel);
            panel.categoryComboBox.setModel(categoriesModel);
            panel.viewStatusComboBox.setModel(viewstatesModel);
            panel.severityComboBox.setModel(severitiesModel);
            panel.reproducibilityComboBox.setModel(reproducibilitiesModel);
            panel.priorityComboBox.setModel(prioritiesModel);
            panel.assignedToComboBox.setModel(assignedModel);
            panel.resolutionComboBox.setModel(resolutionsModel);
            panel.statusComboBox.setModel(statesModel);
            panel.projectionComboBox.setModel(projectionsModel);
            panel.etaComboBox.setModel(etasModel);
            panel.refreshLinkButton.addActionListener(this);
            panel.openIssueWebbrowserLinkButton.addActionListener(this);
            panel.projectComboBox.addActionListener(this);
            panel.updateIssueButton.setVisible(false);
            panel.addNoteButton.addActionListener(this);
            panel.addIssueButton.addActionListener(this);
            panel.updateIssueButton.addActionListener(this);
            panel.projectComboBox.addActionListener(stateMonitor);
            panel.descriptionEditorPane.getDocument().addDocumentListener(stateMonitor);
            panel.summaryTextField.getDocument().addDocumentListener(stateMonitor);
            panel.waitPanel.setVisible(issue.isBusy());
            updateInfo(null);
        }
        return panel;
    }

    private void updateInputState() {
        panel.updateIssueButton.setVisible(false);
        panel.addIssueButton.setVisible(false);
        panel.notesOuterPanel.setVisible(false);
        panel.statusComboBox.setEnabled(false);
        panel.resolutionComboBox.setEnabled(false);
        panel.headerPanel.setVisible(false);
        panel.relationsLabel.setVisible(false);
        panel.relationsPanel.setVisible(false);
        panel.tagsLabel.setVisible(false);
        panel.tagsPanel.setVisible(false);
        panel.attachmentLabel.setVisible(false);
        panel.attachmentPanel.setVisible(false);
        panel.filler1.setVisible(false);
        if (issue.getId() == null) {
            setUpdateEnabledFields(true);
            panel.addIssueButton.setVisible(true);
            panel.filler1.setVisible(true);
        } else {
            if(issue.canUpdate()) {
                panel.updateIssueButton.setVisible(true);
                panel.statusComboBox.setEnabled(true);
                panel.resolutionComboBox.setEnabled(true);
            }
            setUpdateEnabledFields(issue.canUpdate());
            panel.notesOuterPanel.setVisible(true);
            panel.headerPanel.setVisible(true);
            panel.relationsLabel.setVisible(true);
            panel.relationsPanel.setVisible(true);
            panel.tagsLabel.setVisible(true);
            panel.tagsPanel.setVisible(true);
            panel.attachmentLabel.setVisible(true);
            panel.attachmentPanel.setVisible(true);
        }
    }
    
    private void setUpdateEnabledFields(boolean enabled) {
        panel.projectComboBox.setEnabled(enabled);
        panel.viewStatusComboBox.setEnabled(enabled);
        panel.assignedToComboBox.setEnabled(enabled);
        panel.categoryComboBox.setEnabled(enabled);
        panel.severityComboBox.setEnabled(enabled);
        panel.reproducibilityComboBox.setEnabled(enabled);
        panel.priorityComboBox.setEnabled(enabled);
        panel.projectionComboBox.setEnabled(enabled);
        panel.etaComboBox.setEnabled(enabled);
        panel.platformTextField.setEditable(enabled);
        panel.osTextField.setEditable(enabled);
        panel.osVersionTextField.setEditable(enabled);
        panel.buildTextField.setEditable(enabled);
        panel.summaryTextField.setEditable(enabled);
        panel.descriptionEditorPane.setEditable(enabled);
        panel.stepsToReproduceEditorPane.setEditable(enabled);
        panel.additionalInformationEditorPane.setEditable(enabled);
    }

    private void updateInfo(String property) {
        if (panel != null) {
            if (property == null || "id".equals(property)) {
                updateInputState();
                panel.issueHeader.setText(issue.getDisplayValue());
            }
            if (property == null || "summary".equals(property)) {
                panel.issueHeader.setText(issue.getDisplayValue());
                panel.summaryTextField.setText(issue.getSummary());
            }
            if (property == null || "date_submitted".equals(property)) {
                if (issue.getDate_submitted() == null) {
                    panel.createdValueLabel.setText("-");
                } else {
                    panel.createdValueLabel.setText(dateFormat.format(issue.getDate_submitted().getTime()));
                }
            }
            if (property == null || "last_updated".equals(property)) {
                if (issue.getLast_updated() == null) {
                    panel.updatedValueLabel.setText("-");
                } else {
                    panel.updatedValueLabel.setText(dateFormat.format(issue.getLast_updated().getTime()));
                }
            }
            if (property == null || "reporter".equals(property)) {
                if (issue.getReporter() == null) {
                    panel.reporterValueLabel.setText(" - ");
                } else {
                    String account = issue.getReporter().getName();
                    String name = issue.getReporter().getReal_name();
                    if (name == null) {
                        panel.reporterValueLabel.setText(account);
                    } else {
                        panel.reporterValueLabel.setText(account + " - " + name);
                    }
                }
            }
            if (property == null || "project".equals(property)) {
                ProjectData current = null;
                if (issue.getProject() != null) {
                    BigInteger id = issue.getProject().getId();
                    for (int i = 0; i < projectModel.getSize(); i++) {
                        if (projectModel.getElementAt(i) == null) {
                            continue;
                        } else if (projectModel.getElementAt(i).getId().equals(id)) {
                            current = projectModel.getElementAt(i);
                            break;
                        }
                    }
                }
                panel.projectComboBox.setSelectedItem(current);
            }
            if (property == null || "category".equals(property)) {
                panel.categoryComboBox.setSelectedItem(issue.getCategory());
            }
            if (property == null || "view_state".equals(property)) {
                panel.viewStatusComboBox.setSelectedItem(issue.getView_state());
            }
            if (property == null || "severity".equals(property)) {
                panel.severityComboBox.setSelectedItem(issue.getSeverity());
            }
            if (property == null || "reproducibility".equals(property)) {
                panel.reproducibilityComboBox.setSelectedItem(issue.getReproducibility());
            }
            if (property == null || "priority".equals(property)) {
                panel.priorityComboBox.setSelectedItem(issue.getPriority());
            }
            if (property == null || "handler".equals(property)) {
                AccountData target = issue.getHandler();
                if(! assignedModel.getBackingList().contains(target)) {
                    assignedModel.addElement(target);
                }
                panel.assignedToComboBox.setSelectedItem(target);
            }
            if (property == null || "resolution".equals(property)) {
                panel.resolutionComboBox.setSelectedItem(issue.getResolution());
            }
            if (property == null || "status".equals(property)) {
                panel.statusComboBox.setSelectedItem(issue.getStatus());
            }
            if (property == null || "eta".equals(property)) {
                panel.etaComboBox.setSelectedItem(issue.getEta());
            }
            if (property == null || "projection".equals(property)) {
                panel.projectionComboBox.setSelectedItem(issue.getProjection());
            }
            if (property == null || "description".equals(property)) {
                DefaultCaret caret = (DefaultCaret) panel.descriptionEditorPane.getCaret();
                int oldPolicy = caret.getUpdatePolicy();
                caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
                panel.descriptionEditorPane.setText(issue.getDescription());
                caret.setUpdatePolicy(oldPolicy);
            }
            if (property == null || "additional_information".equals(property)) {
                DefaultCaret caret = (DefaultCaret) panel.additionalInformationEditorPane.getCaret();
                int oldPolicy = caret.getUpdatePolicy();
                caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
                panel.additionalInformationEditorPane.setText(issue.getAdditional_information());
                caret.setUpdatePolicy(oldPolicy);
            }
            if (property == null || "steps_to_reproduce".equals(property)) {
                DefaultCaret caret = (DefaultCaret) panel.stepsToReproduceEditorPane.getCaret();
                int oldPolicy = caret.getUpdatePolicy();
                caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
                panel.stepsToReproduceEditorPane.setText(issue.getSteps_to_reproduce());
                caret.setUpdatePolicy(oldPolicy);
            }
            if (property == null || "platform".equals(property)) {
                panel.platformTextField.setText(issue.getPlatform());
            }
            if (property == null || "os".equals(property)) {
                panel.osTextField.setText(issue.getOs());
            }
            if (property == null || "os_build".equals(property)) {
                panel.osVersionTextField.setText(issue.getOs_build());
            }
            if (property == null || "build".equals(property)) {
                panel.buildTextField.setText(issue.getBuild());
            }
            if (property == null || "relationships".equals(property)) {
                panel.relationsPanel.removeAll();
                if (issue.getRelationships() != null) {
                    for (RelationshipData rd : issue.getRelationships()) {
                        panel.relationsPanel.add(new RelationshipDisplay(issue, rd));
                    }
                }
                LinkButton lb = new LinkButton("Add");
                lb.setActionCommand("addRelationship");
                lb.addActionListener(this);
                panel.relationsPanel.add(lb);
            }
            if (property == null || "tags".equals(property)) {
                panel.tagsPanel.removeAll();
                if (issue.getTags() != null) {
                    for (ObjectRef or : issue.getTags()) {
                        panel.tagsPanel.add(new TagDisplay(issue, or));
                    }
                }
                LinkButton lb = new LinkButton("Add");
                lb.setActionCommand("addTag");
                lb.addActionListener(this);
                panel.tagsPanel.add(lb);
            }
            if (property == null || "attachments".equals(property)) {
                panel.attachmentPanel.removeAll();
                if (issue.getAttachments() != null) {
                    for (AttachmentData ad : issue.getAttachments()) {
                        AttachmentDisplay adisplay = new AttachmentDisplay(issue, ad);
                        adisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
                        panel.attachmentPanel.add(adisplay);
                    }
                }
                LinkButton lb = new LinkButton("Add");
                lb.setActionCommand("addAttachment");
                lb.addActionListener(this);
                panel.attachmentPanel.add(lb);
            }
            if (property == null || "notes".equals(property)) {
                panel.notesPanel.removeAll();
                Dimension fixedDim = new Dimension(1, 5);
                if (issue.getNotes() != null) {
                    boolean first = true;
                    for (IssueNoteData ind : issue.getNotes()) {
                        if (!first) {
                            panel.notesPanel.add(new Box.Filler(fixedDim, fixedDim, fixedDim));
                        }
                        panel.notesPanel.add(new NoteDisplay(ind));
                        if (first) {
                            first = false;
                        }
                    }
                }
            }
            panel.revalidate();
            panel.repaint();
        }
    }

    private IssueData getUpdateData() {
        IssueData updateData = new IssueData();
        updateData.setId(issue.getId());
        updateData.setAdditional_information(panel.additionalInformationEditorPane.getText());
        // Skip attachments
        updateData.setBuild(panel.buildTextField.getText());
        updateData.setCategory((String) panel.categoryComboBox.getSelectedItem());
        // Skip updating submitdate
        updateData.setDescription(panel.descriptionEditorPane.getText());
        updateData.setEta((ObjectRef) panel.etaComboBox.getSelectedItem());
        updateData.setHandler((AccountData) panel.assignedToComboBox.getSelectedItem());
        // Skip notes
        // Skip tags
        updateData.setOs(panel.osTextField.getText());
        updateData.setOs_build(panel.osVersionTextField.getText());
        updateData.setPlatform(panel.platformTextField.getText());
        updateData.setPriority((ObjectRef) panel.priorityComboBox.getSelectedItem());
        ProjectData pd = (ProjectData) panel.projectComboBox.getSelectedItem();
        if (pd == null) {
            updateData.setProject(null);
        } else {
            ObjectRef project = new ObjectRef(pd.getId(), pd.getName());
            updateData.setProject(project);
        }
        updateData.setProjection((ObjectRef) panel.projectionComboBox.getSelectedItem());
        // Skip relationships
        updateData.setReproducibility((ObjectRef) panel.reproducibilityComboBox.getSelectedItem());
        updateData.setResolution((ObjectRef) panel.resolutionComboBox.getSelectedItem());
        updateData.setSeverity((ObjectRef) panel.severityComboBox.getSelectedItem());
        updateData.setStatus((ObjectRef) panel.statusComboBox.getSelectedItem());
        updateData.setSteps_to_reproduce(panel.stepsToReproduceEditorPane.getText());
        updateData.setSummary(panel.summaryTextField.getText());
        updateData.setView_state((ObjectRef) panel.viewStatusComboBox.getSelectedItem());
        // ToDo: the next two need exposure in GUI
        updateData.setTarget_version(issue.getTarget_version());
        updateData.setVersion(issue.getVersion());
        updateData.setSponsorship_total(issue.getSponsorship_total());
        // Should reporter/submitdate/last be updateable?
        updateData.setReporter(issue.getReporter());
        updateData.setDate_submitted(issue.getDate_submitted());
        updateData.setLast_updated(issue.getLast_updated());
        // @todo: implement custom fields
        return updateData;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(MantisIssueController.class.getName());
    }

    @Override
    public boolean isValid() {
        boolean result = true;
        result &= panel.projectComboBox.getSelectedItem() != null;
        result &= (!panel.descriptionEditorPane.getText().trim().isEmpty());
        result &= (!panel.summaryTextField.getText().trim().isEmpty());
        return result;
    }

    @Override
    public void applyChanges() {
        issue.setBusy(true);
        try {
            if (isValid()) {
                if (issue.getId() == null) {
                    MantisRepository mr = issue.getMantisRepository();
                    mr.addIssue(issue, getUpdateData());
                } else {
                    MantisRepository mr = issue.getMantisRepository();
                    mr.updateIssue(issue, getUpdateData());
                }
            }
        } catch (Exception ex) {
            if(ex instanceof RemoteException || ex instanceof ServiceException) {
                NotifyDescriptor nd = new NotifyDescriptor.Exception(ex, 
                        "Failed to create/add issue");
                DialogDisplayer.getDefault().notifyLater(nd);
            } else if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                assert false : "Should never be reached";
            }
        } finally {
            issue.setBusy(false);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if ("busy".equals(propertyName) && panel != null) {
            panel.waitPanel.setVisible((Boolean) evt.getNewValue());
        } else {
            if (IssueProvider.EVENT_ISSUE_REFRESHED.equals(propertyName)) {
                propertyName = null;
            }
            updateInfo(propertyName);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("addIssue".equals(e.getActionCommand())
                || "updateIssue".equals(e.getActionCommand())) {
            issue.getMantisRepository().getRequestProcessor().submit(new Runnable() {
                public void run() {
                    applyChanges();
                }
            });
        } else if ("addNote".equals(e.getActionCommand())) {
            final ObjectRef viewState = (ObjectRef) panel.addNoteViewStateComboBox.getSelectedItem();
            final String comment = panel.addNoteEditorPane.getText();
            panel.addNoteViewStateComboBox.setSelectedIndex(0);
            panel.addNoteEditorPane.setText("");
            issue.getMantisRepository().getRequestProcessor().submit(new Runnable() {
                public void run() {
                    try {
                        issue.addComment(comment, viewState);
                    } catch (Exception ex) {
                        NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                                "Failed to comment to issue");
                        DialogDisplayer.getDefault().notifyLater(nd);
                    }
                }
            });
        } else if ("selectProject".equals(e.getActionCommand())) {
            try {
                List<String> categories = new ArrayList<String>();
                List<AccountData> users = new ArrayList<AccountData>();
                ProjectData p = (ProjectData) panel.projectComboBox.getSelectedItem();
                if (p != null) {
                    MantisRepository mr = issue.getMantisRepository();
                    categories.add(null);
                    categories.addAll(Arrays.asList(mr.getCategories(p.getId())));
                    users.add(null);
                    users.addAll(Arrays.asList(mr.getUsers(p.getId())));
                }
                categoriesModel.setBackingList(categories);
                assignedModel.setBackingList(users);
            } catch (Exception ex) {
                if (ex instanceof RemoteException || ex instanceof ServiceException) {
                    NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                            "Failed to create/add issue");
                    DialogDisplayer.getDefault().notifyLater(nd);
                } else if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                } else {
                    assert false : "Should never be reached";
                }
            }
        } else if ("refreshIssue".equals(e.getActionCommand())) {
            if (issue.getId() != null) {
                issue.getMantisRepository().getRequestProcessor().submit(new Runnable() {
                    public void run() {
                        try {
                            issue.refresh();
                        } catch (Exception ex) {
                            NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                                    "Failed to refresh issue");
                            DialogDisplayer.getDefault().notifyLater(nd);
                        }
                    }
                });
            }
        } else if ("openIssueWebbrowser".equals(e.getActionCommand())) {
            URI uri = issue.getMantisRepository().getIssueUrl(issue);
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if ("addRelationship".equals(e.getActionCommand())) {
            new AddRelationshipDialog((JFrame) WindowManager.getDefault().getMainWindow(), issue).setVisible(true);
        } else if ("addTag".equals(e.getActionCommand())) {
            new AddTagDialog((JFrame) WindowManager.getDefault().getMainWindow(), issue).setVisible(true);
        } else if ("addAttachment".equals(e.getActionCommand())) {
            final JFileChooser fileChooser = new JFileChooser(lastDirectory);
            fileChooser.setDialogTitle("Add attachment");
            int result = fileChooser.showOpenDialog(getComponent());
            if (result == JFileChooser.APPROVE_OPTION) {
                lastDirectory = fileChooser.getCurrentDirectory();
                issue.getMantisRepository().getRequestProcessor().submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            issue.addFile(fileChooser.getSelectedFile(), null);
                        } catch (Exception ex) {
                            NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                                    "Failed to add file to issue");
                            DialogDisplayer.getDefault().notifyLater(nd);
                        }
                    }
                });
            }
        }
    }

    private class StateMonitor implements DocumentListener, ActionListener {

        private void updateButtonState() {
            if (isValid()) {
                panel.addIssueButton.setEnabled(true);
                panel.updateIssueButton.setEnabled(true);
            } else {
                panel.addIssueButton.setEnabled(false);
                panel.updateIssueButton.setEnabled(false);
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateButtonState();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateButtonState();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateButtonState();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateButtonState();
        }
    }
}
