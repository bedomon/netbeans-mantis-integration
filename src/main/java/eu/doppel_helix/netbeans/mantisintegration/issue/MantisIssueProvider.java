
package eu.doppel_helix.netbeans.mantisintegration.issue;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import org.netbeans.modules.bugtracking.spi.IssueController;
import org.netbeans.modules.bugtracking.spi.IssueProvider;
import org.netbeans.modules.bugtracking.spi.IssueStatusProvider;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

public class MantisIssueProvider implements IssueProvider<MantisIssue> {

    @Override
    public String getDisplayName(MantisIssue data) {
        return data.getDisplayValue();
    }

    @Override
    public String getTooltip(MantisIssue data) {
        return getDisplayName(data);
    }

    @Override
    public String getID(MantisIssue data) {
        return data.getIdAsString();
    }

    @Override
    public String getSummary(MantisIssue data) {
        return data.getSummary();
    }

    @Override
    public boolean isNew(MantisIssue data) {
        return data.getId() == null;
    }

    @Override
    public boolean isFinished(MantisIssue data) {
        return data.isFinished();
    }

    @Override
    public boolean refresh(MantisIssue data) {
        try {
            return data.refresh();
        } catch (Exception ex) {
            NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                    "Failed to refresh issue");
            DialogDisplayer.getDefault().notifyLater(nd);
            return false;
        }
    }

    @Override
    public void addComment(MantisIssue data, String comment, boolean closeAsFixed) {
        try {
            data.addComment(comment, closeAsFixed);
        } catch (Exception ex) {
            NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                    "Failed to add comment to issue");
            DialogDisplayer.getDefault().notifyLater(nd);
        }
    }

    @Override
    public IssueController getController(MantisIssue data) {
        return data.getController();
    }

    @Override
    public void removePropertyChangeListener(MantisIssue data, PropertyChangeListener listener) {
        data.removePropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(MantisIssue data, PropertyChangeListener listener) {
        data.addPropertyChangeListener(listener);
    }

    @Override
    public Collection<String> getSubtasks(MantisIssue data) {
        return data.getSubtasks();
    }

    @Override
    public void attachFile(MantisIssue data, File file, String description, boolean isPatch) {
        try {
            data.attachFile(file, description);
        } catch (Exception ex) {
            NotifyDescriptor nd = new NotifyDescriptor.Exception(ex,
                    "Failed to add patch to issue");
            DialogDisplayer.getDefault().notifyLater(nd);
        }
    }
}