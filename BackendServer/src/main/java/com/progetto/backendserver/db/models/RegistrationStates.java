package com.progetto.backendserver.db.models;

public enum RegistrationStates {
    ADMIN_REGISTERED("ADMIN_REGISTERED"),
    ADMIN_AND_ROLES_REGISTERED("ADMIN_AND_ROLES_REGISTERED"),
    REGISTRATION_COMPLETE("REGISTRATION_COMPLETE");

    RegistrationStates(String registration_complete) {

    }
}
