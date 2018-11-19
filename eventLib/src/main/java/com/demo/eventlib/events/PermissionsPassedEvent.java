package com.demo.eventlib.events;


public class PermissionsPassedEvent {
    private int requestCode;
    private String[]permissions;
    private int[] grantResults;

    public PermissionsPassedEvent(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        this.permissions = permissions;
        this.grantResults = grantResults;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public int[] getGrantResults() {
        return grantResults;
    }

    public void setGrantResults(int[] grantResults) {
        this.grantResults = grantResults;
    }
}
