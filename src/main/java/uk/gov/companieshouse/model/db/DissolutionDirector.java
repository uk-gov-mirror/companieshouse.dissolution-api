package uk.gov.companieshouse.model.db;

public class DissolutionDirector {

    private String name;
    private String email;
    private String onBehalfName;
    private DirectorApproval approval;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOnBehalfName() {
        return onBehalfName;
    }

    public void setOnBehalfName(String onBehalfName) {
        this.onBehalfName = onBehalfName;
    }

    public boolean hasDirectorApproval() {
        return approval != null;
    }

    public DirectorApproval getDirectorApproval() {
        return approval;
    }

    public void setDirectorApproval(DirectorApproval approval) {
        this.approval = approval;
    }
}
