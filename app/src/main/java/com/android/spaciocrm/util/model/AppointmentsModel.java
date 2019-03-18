package com.android.spaciocrm.util.model;

/**
 * Created by freshfuturesmy on 23/01/18.
 */

public class AppointmentsModel {

    private String EVENTS_TITLE;
    private String DATE_OF_APPOINTMENT;
    private String CONTACT_NAME;
    private String TIME_OF_APPOINTMENT;
    private String AGENDA_OF_APPOINTMENT;
    private String OUTCOME_OF_MEETING;
    private String APPOINTMENT_ADDRESS;
    private String APPOINTMENT_CITY;
    private String APPOINTMENT_STATE;
    private String APPOINTMENT_COUNTRY;
    private String SEND_REMINDER_BY;

    public AppointmentsModel(String appointment, String timeLeft, String agenda) {
        EVENTS_TITLE = appointment;
        DATE_OF_APPOINTMENT = timeLeft;
        AGENDA_OF_APPOINTMENT = agenda;
    }

    public AppointmentsModel() {

    }

    public String getAppointmentTitle() {
        return EVENTS_TITLE;
    }

    public String getContactName() {
        return CONTACT_NAME;
    }

    public String getAppointmentDate() {
        return DATE_OF_APPOINTMENT;
    }

    public String getAppointmentTime() {
        return TIME_OF_APPOINTMENT;
    }


    public String getAgenda() {
        return AGENDA_OF_APPOINTMENT;
    }

    public String getOutcome() {
        return OUTCOME_OF_MEETING;
    }

    public String getAppointmentAddress() {
        return APPOINTMENT_ADDRESS;
    }


    public String getAppointmentCity() {
        return APPOINTMENT_CITY;
    }

    public String getAppointmentState() {
        return APPOINTMENT_STATE;
    }

    public String getAppointmentCountry() {
        return APPOINTMENT_COUNTRY;
    }

    public String getSentBy() {
        return SEND_REMINDER_BY;
    }

}
